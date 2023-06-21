package com.jewel.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.*;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.jewel.service.LevelMapService;
import com.jewel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.annotation.Nullable;
import javax.annotation.PostConstruct;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@Service
public class FirebaseInitialization {
    @Autowired
    LevelMapService levelMapService;
    @Autowired
    UserService userService;

    @PostConstruct
    public void initialization () {
        try {
            ClassPathResource cp = new ClassPathResource("serviceAccountKey.json");
            FileInputStream serviceAccount = new FileInputStream(cp.getFile());

            FirebaseOptions options = FirebaseOptions.builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setDatabaseUrl("https://testapi-d3e3a-default-rtdb.firebaseio.com")
                    .build();
            FirebaseApp.initializeApp(options);

            initTriggerLevelMap();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void onAddLevelMmap (QueryDocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        if (documentSnapshot.getId().compareTo("version") == 0)
        {
            return;
        }

        if (userService.AddLevelMapToAllUsers(Integer.parseInt(documentSnapshot.getId())))
        {
            levelMapService.IncreaseVersion();
        }
    }

    private void onDeleteLevelMap (QueryDocumentSnapshot documentSnapshot) throws ExecutionException, InterruptedException {
        if (documentSnapshot.getId().compareTo("version") == 0)
        {
            return;
        }

        if (userService.DeleteLevelMapInAllUsers(Integer.parseInt(documentSnapshot.getId())))
        {
            levelMapService.IncreaseVersion();
        }
    }

    private void onUpdateLevelMap (QueryDocumentSnapshot documentSnapshot) {
        if (documentSnapshot.getId().compareTo("version") == 0)
        {
            return;
        }
        levelMapService.IncreaseVersion();
    }

    private void initTriggerLevelMap () {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection("levels").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirestoreException error) {
                if (error != null) {
                    System.err.println("Listen failed: " + error);
                    return;
                }

                for (DocumentChange dc : value.getDocumentChanges()) {
                    switch (dc.getType()) {
                        case ADDED:
                            try {
                                onAddLevelMmap(dc.getDocument());
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                            break;
                        case MODIFIED:
                            onUpdateLevelMap(dc.getDocument());
                            break;
                        case REMOVED:
                            try {
                                onDeleteLevelMap(dc.getDocument());
                            } catch (Exception e) {
                                System.err.println(e.getMessage());
                                throw new RuntimeException(e);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        });
    }
}
