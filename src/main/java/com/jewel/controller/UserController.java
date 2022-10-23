package com.jewel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jewel.entity.user.LevelData;
import com.jewel.entity.PieceType;
import com.jewel.entity.shop.ShopItem;
import com.jewel.entity.user.User;
import com.jewel.service.ShopService;
import com.jewel.service.UserService;
import com.jewel.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    ShopService shopService;

    @GetMapping("/{username}")
    public ResponseEntity<String> GetUser (@PathVariable("username") String username) throws ExecutionException, InterruptedException, JsonProcessingException {
        User user = userService.GetUser(username);
        ObjectMapper objectMapper = new ObjectMapper();

        return ResponseUtil.Response(username, 200, objectMapper.writeValueAsString(user));
    }

    @PutMapping("/update")
    public ResponseEntity<String> Update (@RequestBody User user) throws ExecutionException, InterruptedException, JsonProcessingException {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        User databaseUser = userService.GetUser(user.getUsername());
        if (databaseUser == null)
        {
            return ResponseUtil.Response(user.getUsername(), 401, "username is not exists");
        }
        user.setPassword(databaseUser.getPassword());

        userService.UpdateUser(user);
        user = userService.GetUser(user.getUsername());
        ObjectMapper objectMapper = new ObjectMapper();

        return ResponseUtil.Response(user.getUsername(), 200, objectMapper.writeValueAsString(user));
    }

    @PutMapping("update-level-data/{levelNumber}")
    public ResponseEntity<String> UpdateLevelData (@RequestBody User user, @PathVariable("levelNumber") int levelNumber) {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        LevelData newLevelData = user.getLevelDatas().get(String.valueOf(levelNumber));
        if (newLevelData == null) {
            return ResponseUtil.Response(user.getUsername(), 401, "missing data of level " + levelNumber + " in body");
        }

        userService.UpdateLevelData(user.getUsername(), levelNumber, newLevelData);

        return ResponseUtil.Response(user.getUsername(), 200, null);
    }

    @PutMapping("update-current-level")
    public ResponseEntity<String> UpdateCurrentLevel (@RequestBody User user) {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        userService.UpdateCurrentLevel(user.getUsername(), user.getCurrentLevel());

        return ResponseUtil.Response(user.getUsername(), 200, null);
    }

    @PutMapping("/increte-money/{increment}")
    public ResponseEntity<String> IncreteMoney (@RequestBody User user, @PathVariable("increment") int increment) throws ExecutionException, InterruptedException {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        userService.IncreteMoney(user.getUsername(), increment);

        return ResponseUtil.Response(user.getUsername(), 200, String.valueOf(userService.GetUser(user.getUsername()).getMoney()));
    }

    @PutMapping("/buy-item/{type}")
    public ResponseEntity<String> BuyItem (@RequestBody User user, @PathVariable("type") PieceType type) throws ExecutionException, InterruptedException, JsonProcessingException {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        User databaseUser = userService.GetUser(user.getUsername());
        if (databaseUser == null)
        {
            return ResponseUtil.Response(user.getUsername(), 401, "username is not exists");
        }

        ShopItem item = shopService.GetItem(type);
        userService.BuyItem(user.getUsername(), item.getPrice(), type);

        return ResponseUtil.Response(user.getUsername(), 200, null);
    }

    @PutMapping("/use-item/{type}")
    public ResponseEntity<String> UseItem (@RequestBody User user, @PathVariable("type") String type) throws ExecutionException, InterruptedException, JsonProcessingException {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        User databaseUser = userService.GetUser(user.getUsername());
        if (databaseUser == null)
        {
            return ResponseUtil.Response(user.getUsername(), 401, "username is not exists");
        }

        if (!userService.UseItem(user.getUsername(), type)) {
            return ResponseUtil.Response(user.getUsername(), 401, "don't have item " + type + " to use");
        }

        return ResponseUtil.Response(user.getUsername(), 200, null);
    }
}
