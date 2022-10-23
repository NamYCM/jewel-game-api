package com.jewel.service;

import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

@Service
public class MailService {
    public int SendVerifyCodeMail (String email) throws MessagingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("jewelgameproject@gmail.com", "srrlymuhxtmxydgf");
            }
        });

        Message msg = new MimeMessage(session);
        msg.setFrom(new InternetAddress("jewelgameproject@gmail.com", false));

        int code = (int) Math.floor(Math.random() * (999999 - 100000 + 1) + 100000);

        msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(email));
        msg.setSubject("Verify Code");
        msg.setContent("<b>Your code to create a new admin account is: " + code + "</b>", "text/html");
        msg.setSentDate(new Date());

        Transport.send(msg);

        return code;
    }
}
