package com.huyntd.superapp.gundam_shop.service.firebase;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    public String sendNotification(String targetToken, String title, String body) {
        try {
            Message message = Message.builder()
                    .setToken(targetToken)
                    .setNotification( Notification.builder()
                            .setTitle(title)
                            .setBody(body)
                            .build())
                    .build();

            return FirebaseMessaging.getInstance().send(message);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
            return "Error sending FCM message: " + e.getMessage();
        }
    }
}

