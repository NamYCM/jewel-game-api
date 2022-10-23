package com.jewel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jewel.entity.levelMap.LevelData;
import com.jewel.entity.levelMap.LevelMap;
import com.jewel.service.LevelMapService;
import com.jewel.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/levelMap")
public class LevelMapController {
    @Autowired
    LevelMapService levelMapService;

    @PostMapping("/add-map")
    public ResponseEntity<String> AddMap (@RequestBody LevelMap levelMap) throws ExecutionException, InterruptedException {
        if (levelMap == null) {
            return ResponseUtil.Response("add map", 401, "missing level map data in body");
        }

        int newLevelNumber = levelMapService.GetMapAmount() + 1;

        if (newLevelNumber == 1) {
            levelMapService.InitVersionField();
        }

        levelMapService.AddMap(newLevelNumber, levelMap);

        return ResponseUtil.Response("Add map " + String.valueOf(newLevelNumber), 200, null);
    }

    @DeleteMapping("/delete-map/{levelNumber}")
    public ResponseEntity<String> DeleteMap (@PathVariable("levelNumber") int levelNumber) throws ExecutionException, InterruptedException {
        levelMapService.DeleteMap(levelNumber);
        return ResponseUtil.Response("delete map " + levelNumber, 200, null);
    }

    @PutMapping("/update-map")
    public ResponseEntity<String> UpdateMap (@RequestBody HashMap<Integer, LevelMap>  levelData) {
        if (levelData == null) {
            return ResponseUtil.Response("update map", 401, "missing level map data in body");
        }

        levelMapService.UpdateMap(levelData);
        return ResponseUtil.Response("update map " + levelData, 200, "");
    }

    @GetMapping("/get-all-map")
    public ResponseEntity<String> GetAllMap () throws ExecutionException, InterruptedException, JsonProcessingException {
        LevelData levelMap = levelMapService.GetAllMap();
        ObjectMapper objectMapper = new ObjectMapper();

        return ResponseUtil.Response("all map", 200, objectMapper.writeValueAsString(levelMap));
    }

    @GetMapping("/get-current-version-of-map")
    public ResponseEntity<String> GetCurrentVersion () throws ExecutionException, InterruptedException, JsonProcessingException {
        int version = levelMapService.GetCurrentVersionOfMap();
        HashMap<String, Integer> map = new HashMap<>();
        map.put("version", version);
        ObjectMapper objectMapper = new ObjectMapper();
        return ResponseUtil.Response("all map", 200, objectMapper.writeValueAsString(map));
    }
}
