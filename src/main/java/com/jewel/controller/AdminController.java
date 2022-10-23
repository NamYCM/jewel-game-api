package com.jewel.controller;

import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.gson.JsonObject;
import com.jewel.entity.user.Admin;
import com.jewel.service.MailService;
import com.jewel.service.UserService;
import com.jewel.util.ResponseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private UserService userService;
    @Autowired
    private MailService mailService;

    @PutMapping("/send-verify-gmail")
    public ResponseEntity<String> SendVerifyGmail (HttpServletRequest request) throws MessagingException, FirebaseAuthException {
        FirebaseToken firebaseToken = (FirebaseToken)request.getAttribute("user");
        String email = firebaseToken.getEmail();
        String uid = firebaseToken.getUid();

        int code = mailService.SendVerifyCodeMail(email);
        userService.AddVerifyCodeInAdmin(uid, code);
        return ResponseUtil.Response(email, 200, null);
    }

    @PostMapping("sign-up")
    public ResponseEntity<String> SignUp (@RequestBody Admin admin, HttpServletRequest request) throws FirebaseAuthException, IOException {
        //Check null/empty data
        if (admin.getUsername() == null || admin.getUsername().isBlank())
        {
            return ResponseUtil.Response(admin.getUsername(), 401, "lossing username in body");
        }

        if (admin.getPassword() == null || admin.getPassword().isBlank())
        {
            return ResponseUtil.Response(admin.getUsername(), 401, "lossing password in body");
        }

        FirebaseToken firebaseToken = (FirebaseToken)request.getAttribute("user");
        String email = firebaseToken.getEmail();
        String uid = firebaseToken.getUid();

        if (!userService.VerifyCodeInAdmin(uid, admin.getCode())) {
            return ResponseUtil.Response(email, 400, "invalid code");
        }

        JsonObject jsonObject = userService.CreateAdmin(admin.getUsername(), admin.getPassword());

        if (jsonObject.get("error") != null) {
            return ResponseUtil.Response(admin.getUsername(), jsonObject.get("error").getAsJsonObject().get("code").getAsInt(), jsonObject.get("error").getAsJsonObject().get("message").getAsString());
        }

        return ResponseUtil.Response(email, 200, null);
    }
}
