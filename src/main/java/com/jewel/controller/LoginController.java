package com.jewel.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.gson.JsonObject;
import com.jewel.entity.user.User;
import com.jewel.service.UserService;
import com.jewel.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/login")
public class LoginController {
    @Autowired
    private UserService userService;

    @GetMapping("/sign-in-admin/{username}-{password}")
    public ResponseEntity<String> SignInAdmin (@PathVariable("username") String username, @PathVariable("password") String password) throws IOException {
        //Check null/empty data
        if (username == null || username.isBlank())
        {
            return ResponseUtil.Response(username, 401, "lossing username in body");
        }

        if (password == null || password.isBlank())
        {
            return ResponseUtil.Response(username, 401, "lossing password in body");
        }

        JsonObject result = userService.CreateAdminToken(username, password);

        if (result.get("error") != null) {
            return ResponseUtil.Response(username, result.get("error").getAsJsonObject().get("code").getAsInt(), result.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setToken(result.get("idToken").getAsString());
            ObjectMapper objectMapper = new ObjectMapper();
            return ResponseUtil.Response(username, 200, objectMapper.writeValueAsString(user));
        }
    }

    @GetMapping("/sign-in/{username}-{password}")
    public ResponseEntity<String> SignIn (@PathVariable("username") String username, @PathVariable("password") String password) throws ExecutionException, InterruptedException, IOException, FirebaseAuthException {
        //Check null/empty data
        if (username == null || username.isBlank())
        {
            return ResponseUtil.Response(username, 401, "lossing username in body");
        }

        if (password == null || password.isBlank())
        {
            return ResponseUtil.Response(username, 401, "lossing password in body");
        }

        User user = userService.GetUser(username);

        if (user == null)
        {
            return ResponseUtil.Response(username, 401, "username is not exists");
        }

        if (!userService.IsCorrectPassword(username, password))
        {
            return ResponseUtil.Response(username, 401, "password is not correct");
        }

        String token;
        JsonObject jsonObject = userService.CreateToken(username);

        if (jsonObject.get("error") != null) {
            return ResponseUtil.Response(username, jsonObject.get("error").getAsJsonObject().get("code").getAsInt(), jsonObject.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            token = jsonObject.get("idToken").getAsString();
        }

        user.setToken(token);

        ObjectMapper objectMapper = new ObjectMapper();

        return ResponseUtil.Response(username, 200, objectMapper.writeValueAsString(user));
    }

    @PostMapping("/sign-up")
    public ResponseEntity<String> SignUp (@RequestBody User user) throws ExecutionException, InterruptedException, JsonProcessingException {
        //Check null/empty data
        if (user.getUsername() == null || user.getUsername().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing username in body");
        }

        if (user.getPassword() == null || user.getPassword().isBlank())
        {
            return ResponseUtil.Response(user.getUsername(), 401, "lossing password in body");
        }

        if (userService.IsExist(user.getUsername())) {
            return ResponseUtil.Response(user.getUsername(), 401, "username is already exists");
        }

        user = userService.CreateUser(user.getUsername(), user.getPassword());
        ObjectMapper objectMapper = new ObjectMapper();

        return  ResponseUtil.Response(user.getUsername(), 200, objectMapper.writeValueAsString(user));
    }

    @PutMapping("/reset-password-admin/{email}")
    public ResponseEntity<String> ResetPasswordAdmin (@PathVariable("email") String email) throws IOException {
        //Check null/empty data
        if (email == null || email.isBlank())
        {
            return ResponseUtil.Response(email, 401, "lossing email in body");
        }

        JsonObject jsonObject = userService.ResetPasswordAdmin(email);

        if (jsonObject.get("error") != null) {
            return ResponseUtil.Response(email, jsonObject.get("error").getAsJsonObject().get("code").getAsInt(), jsonObject.get("error").getAsJsonObject().get("message").getAsString());
        } else {
            return ResponseUtil.Response(email, 200, null);
        }
    }
}
