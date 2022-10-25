package com.jewel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.FieldValue;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.SetOptions;
import com.google.firebase.cloud.FirestoreClient;
import com.jewel.entity.levelMap.LevelData;
import com.jewel.entity.levelMap.LevelMap;
import com.jewel.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

@Service
public class LevelMapService {
    private static final String COLLECTION_NAME = "levels";

    public void IncreaseVersion () {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document("version").update("version", FieldValue.increment(1));
    }

    public LevelMap GetMap (int levelNumber) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

       return objectMapper.convertValue(dbFirestore.collection(COLLECTION_NAME).document(String.valueOf(levelNumber)).get().get().getData(), LevelMap.class);
    }

    public LevelData GetAllMap () throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        List<QueryDocumentSnapshot> levelMaps = dbFirestore.collection(COLLECTION_NAME).get().get().getDocuments();

        HashMap<Integer, LevelMap> levelMapHashMap = new HashMap<>();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        levelMaps.forEach(levelMapSnapshot -> {
            if (levelMapSnapshot.getId().compareTo("version") != 0)
            {
                LevelMap levelMap = objectMapper.convertValue(levelMapSnapshot.getData(), LevelMap.class);

                levelMapHashMap.put(Integer.parseInt(levelMapSnapshot.getId()), levelMap);
            }
        });
        
        LevelData levelData = new LevelData();
        levelData.setMaps(levelMapHashMap);
        return levelData;
//        HashMap<Integer, Integer> a = new HashMap<>()
    }

    public int GetCurrentVersionOfMap () throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        return dbFirestore.collection(COLLECTION_NAME).document("version").get().get().get("version", Integer.class);
    }

    public int GetMapAmount () throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        // minor 1 because in levels collection has version field
        int amount = dbFirestore.collection(COLLECTION_NAME).get().get().getDocuments().size() - 1;
        return amount < 0 ? 0 : amount;
    }

    public void InitVersionField () {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Integer> map = new HashMap<>();
        map.put("version", 1);
        dbFirestore.collection(COLLECTION_NAME).document("version").set(map);
    }

    public void AddMap (int levelNumber, LevelMap levelMap) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        dbFirestore.collection(COLLECTION_NAME).document(String.valueOf(levelNumber)).set(levelMap);
    }

    public void DeleteMap (int levelNumberDelete) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        int mapAmount = GetMapAmount();

        for (int levelNumber = levelNumberDelete; levelNumber < mapAmount + 1; levelNumber++) {
            if (levelNumber == mapAmount) {
                dbFirestore.collection(COLLECTION_NAME).document(String.valueOf(levelNumber)).delete();
            } else {
                dbFirestore.collection(COLLECTION_NAME).document(String.valueOf(levelNumber)).set(GetMap(levelNumber + 1));
            }
        }
    }

    public void  UpdateMap (HashMap<Integer, LevelMap> levelData) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        levelData.forEach((levelNumber, levelMap) -> {
            dbFirestore.collection(COLLECTION_NAME).document(String.valueOf(levelNumber)).set(levelMap);
        });
    }

    public void Update2To1 () throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        LevelMap levelMap = GetMap(2);
        HashMap<Integer, LevelMap> levelMapHashMap = new HashMap<>();
        levelMapHashMap.put(1, levelMap);
        UpdateMap(levelMapHashMap);
    }
}
