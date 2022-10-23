package com.jewel.util;

import org.springframework.http.ResponseEntity;

public class ResponseUtil {
    public static <T> ResponseEntity<T> Response (String username, int status, T body) {
        System.out.println(username + " " + (Object)body);
        return ResponseEntity.status(status).body(body);
    }
}
