package com.jewel.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.cloud.FirestoreClient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.jewel.entity.user.LevelData;
import com.jewel.entity.PieceType;
import com.jewel.entity.user.User;
import com.jewel.util.JsonUtil;
import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private static final String COLLECTION_NAME = "users";

    public User GetUser (String username) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();
        DocumentSnapshot userSnapshot = dbFirestore.collection(COLLECTION_NAME).document(username).get().get();
        User user = objectMapper.convertValue(userSnapshot.getData(), User.class);
        if (user != null) user.setUsername(username);
        return user;
    }

    public void UpdateUser (User user) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        dbFirestore.collection(COLLECTION_NAME).document(user.getUsername()).update(objectMapper.convertValue(user, Map.class));
    }

    public void UpdateLevelData (String username, int levelNumber, LevelData levelData) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, LevelData> map = new HashMap<>();

        map.put(String.valueOf(levelNumber), levelData);
        HashMap<String, Map<String, LevelData>> test = new HashMap<>();
        test.put("levelDatas", map);
        dbFirestore.collection(COLLECTION_NAME).document(username).set(test, SetOptions.merge());
    }

    public void UpdateCurrentLevel(String username, int currentLevel) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        Map<String, Object> map = new HashMap<>();

        map.put("currentLevel", currentLevel);
        dbFirestore.collection(COLLECTION_NAME).document(username).update(map);
    }

    public void IncreteMoney (String username, int increment) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        User user = GetUser(username);
        Map<String, Object> map = new HashMap<>();

        if (user == null) throw new NullPointerException("user " + username + " is not exists");

        int newMoney = user.getMoney() + increment;
        newMoney = Math.max(0, newMoney);

        map.put("money", newMoney);

        dbFirestore.collection(COLLECTION_NAME).document(username).update(map);
    }

    public void BuyItem (String username, int price, PieceType itemType) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        User user = GetUser(username);
        Map<String, Object> map = new HashMap<>();

        if (user == null) throw new NullPointerException("user " + username + " is not exists");

        int newMoney = user.getMoney() - price;
        newMoney = Math.max(0, newMoney);
        int newItemAmount = user.getSpecialPiecesAmount().get(itemType.name()) + 1;
        user.getSpecialPiecesAmount().put(itemType.name(), newItemAmount);

        map.put("money", newMoney);
        map.put("specialPiecesAmount", user.getSpecialPiecesAmount());

        dbFirestore.collection(COLLECTION_NAME).document(username).update(map);
    }

    public boolean UseItem (String username, String itemType) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        User user = GetUser(username);
        Map<String, Object> map = new HashMap<>();

        if (user == null) throw new NullPointerException("user " + username + " is not exists");

        int newItemAmount = user.getSpecialPiecesAmount().get(itemType) - 1;
        if (newItemAmount < 0) return false;

        user.getSpecialPiecesAmount().put(itemType, newItemAmount);
        map.put("specialPiecesAmount", user.getSpecialPiecesAmount());

        dbFirestore.collection(COLLECTION_NAME).document(username).update(map);

        return true;
    }

    public boolean IsExist (String username) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ApiFuture<DocumentSnapshot> userSnapshot = dbFirestore.collection(COLLECTION_NAME).document(username).get();
        return userSnapshot.get().exists();
    }

    public boolean IsCorrectPassword (String username, String password) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        DocumentSnapshot userSnapshot = dbFirestore.collection(COLLECTION_NAME).document(username).get().get();

        if (!userSnapshot.exists()) return false;

        return new BCryptPasswordEncoder().matches(password, userSnapshot.getString("password"));
    }

    public JsonObject CreateToken (String username) throws FirebaseAuthException, IOException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userRole", true);
        String token = FirebaseAuth.getInstance().createCustomToken(username, claims);

        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://identitytoolkit.googleapis.com/v1/accounts:signInWithCustomToken?key=AIzaSyC8FE3pSDKPpreuE31QBD5ZQEmbXEYEtG4");
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("token", token));
        params.add(new BasicNameValuePair("returnSecureToken", "true"));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        HttpEntity entity = httpclient.execute(httppost).getEntity();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(entity.getContent()));
        JsonObject rootObj = root.getAsJsonObject();

        return rootObj;
    }

    public JsonObject CreateAdminToken (String username, String password) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://identitytoolkit.googleapis.com/v1/accounts:signInWithPassword?key=AIzaSyC8FE3pSDKPpreuE31QBD5ZQEmbXEYEtG4");
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("returnSecureToken", "true"));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        HttpEntity entity = httpclient.execute(httppost).getEntity();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(entity.getContent()));
        JsonObject rootObj = root.getAsJsonObject();

        return  rootObj;
    }

    public User CreateUser (String username, String password) {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        User newUser = new User(username, new BCryptPasswordEncoder().encode(password));

        dbFirestore.collection("levels").listDocuments().forEach(levelRef -> {
            try {
                int levelNumber = Integer.parseInt(levelRef.getId());
                newUser.getLevelDatas().put(levelRef.getId(), new LevelData(levelNumber, 0, levelNumber == 1 ? 2 : 0));
            } catch (NumberFormatException nfe) {
                // it's on version field
            }
        });
        dbFirestore.collection(COLLECTION_NAME).document(username).create(newUser);
        return newUser;
    }

    public JsonObject CreateAdmin (String username, String password) throws FirebaseAuthException, IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://identitytoolkit.googleapis.com/v1/accounts:signUp?key=AIzaSyC8FE3pSDKPpreuE31QBD5ZQEmbXEYEtG4");
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("email", username));
        params.add(new BasicNameValuePair("password", password));
        params.add(new BasicNameValuePair("returnSecureToken", "true"));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        HttpEntity entity = httpclient.execute(httppost).getEntity();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(entity.getContent()));
        JsonObject rootObj = root.getAsJsonObject();

        if (rootObj.get("error") == null) {
            Map<String, Object> claims = new HashMap<>();
            claims.put("adminRole", true);
            FirebaseAuth.getInstance().setCustomUserClaims(rootObj.get("localId").getAsString(), claims);
        }

        return rootObj;
    }

    public JsonObject ResetPasswordAdmin (String email) throws IOException {
        HttpClient httpclient = HttpClients.createDefault();
        HttpPost httppost = new HttpPost("https://identitytoolkit.googleapis.com/v1/accounts:sendOobCode?key=AIzaSyC8FE3pSDKPpreuE31QBD5ZQEmbXEYEtG4");
        List<NameValuePair> params = new ArrayList<NameValuePair>(1);
        params.add(new BasicNameValuePair("requestType", "PASSWORD_RESET"));
        params.add(new BasicNameValuePair("email", email));
        httppost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
        HttpEntity entity = httpclient.execute(httppost).getEntity();

        JsonParser jp = new JsonParser();
        JsonElement root = jp.parse(new InputStreamReader(entity.getContent()));
        JsonObject rootObj = root.getAsJsonObject();

        return  rootObj;
    }

    public void AddVerifyCodeInAdmin (String uid, int code) throws FirebaseAuthException {
        Map<String, Object> claims = new HashMap<>();
        claims.put("adminRole", true);
        claims.put("verifyCode", code);

        FirebaseAuth.getInstance().setCustomUserClaims(uid, claims);
    }

    public boolean VerifyCodeInAdmin (String uid, double code) throws FirebaseAuthException {
        return Double.parseDouble(FirebaseAuth.getInstance().getUser(uid).getCustomClaims().get("verifyCode").toString()) == code;
    }

    public boolean AddLevelMapToAllUsers (int levelNumber) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        Iterator<DocumentReference> userRefs = dbFirestore.collection(COLLECTION_NAME).listDocuments().iterator();

        while (userRefs.hasNext()) {
            DocumentReference userRef = userRefs.next();
            Map<String, LevelData> levelData = new HashMap<>();
            User user = objectMapper.convertValue(userRef.get().get().getData(), User.class);

            if (user.getLevelDatas().get(String.valueOf(levelNumber)) != null)
            {
                return false;
            }

            if (levelNumber == 1) {
                //level always able to open when it is the first level
                levelData.put(String.valueOf(levelNumber), new LevelData(levelNumber, 0, 2));
            } else {
                //check previous level is lock or not
                if (user.getLevelDatas().get(String.valueOf(levelNumber - 1)).getState() == 0) {
                    levelData.put(String.valueOf(levelNumber), new LevelData(levelNumber, 0, 0));
                } else {
                    levelData.put(String.valueOf(levelNumber), new LevelData(levelNumber, 0, 2));
                }
            }

            HashMap<String, Map<String, LevelData>> newLevelData = new HashMap<>();
            newLevelData.put("levelDatas", levelData);

            userRef.set(newLevelData, SetOptions.merge());
        }

        return true;
    }

    public boolean DeleteLevelMapInAllUsers (int levelNumber) throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        Iterator<DocumentReference> userRefs = dbFirestore.collection(COLLECTION_NAME).listDocuments().iterator();

        while (userRefs.hasNext()) {
            DocumentReference userRef = userRefs.next();
            Map<String, FieldValue> levelData = new HashMap<>();
            User user = objectMapper.convertValue(userRef.get().get().getData(), User.class);

            if (user.getLevelDatas().get(String.valueOf(levelNumber)) == null)
            {
                return false;
            }
            levelData.put(String.valueOf(levelNumber), FieldValue.delete());

            HashMap<String, Map<String, FieldValue>> newLevelData = new HashMap<>();
            newLevelData.put("levelDatas", levelData);

            userRef.set(newLevelData, SetOptions.merge());
        }

        return true;
    }
}
