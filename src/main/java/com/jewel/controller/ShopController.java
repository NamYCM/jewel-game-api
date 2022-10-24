package com.jewel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jewel.entity.levelMap.LevelMap;
import com.jewel.entity.shop.Shop;
import com.jewel.service.ShopService;
import com.jewel.util.JsonUtil;
import com.jewel.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/shop")
public class ShopController {
    @Autowired
    ShopService shopService;

    @GetMapping("/all-items")
    public ResponseEntity<String> GetAllItems () throws ExecutionException, InterruptedException, JsonProcessingException {
        Shop shop = shopService.GetAllItems();
        ObjectMapper objectMapper = JsonUtil.GetObjectMapper();

        return ResponseUtil.Response("all map", 200, objectMapper.writeValueAsString(shop));
    }
}
