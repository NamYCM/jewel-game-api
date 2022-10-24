package com.jewel.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.jewel.entity.PieceType;
import com.jewel.entity.shop.Shop;
import com.jewel.entity.shop.ShopItem;
import com.jewel.util.JsonUtil;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutionException;

@Service
public class ShopService {
    private static final String COLLECTION_NAME = "shop";

    public ShopItem GetItem (PieceType type) throws ExecutionException, InterruptedException, JsonProcessingException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        DocumentSnapshot shopItems = dbFirestore.collection(COLLECTION_NAME).document("items").get().get();
        Shop shop = objectMapper.convertValue(shopItems.getData(), Shop.class);
        return shop.getShopItems().get(type.name());
    }

    public Shop GetAllItems () throws ExecutionException, InterruptedException {
        Firestore dbFirestore = FirestoreClient.getFirestore();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        DocumentSnapshot shopItems = dbFirestore.collection(COLLECTION_NAME).document("items").get().get();
        Shop shop = objectMapper.convertValue(shopItems.getData(), Shop.class);
        return shop;
    }
}
