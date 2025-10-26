package com.huyntd.superapp.gundam_shop.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.huyntd.superapp.gundam_shop.dto.request.FirebaseProperties;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.nio.charset.StandardCharsets;

@Configuration
@RequiredArgsConstructor
public class FirebaseConfig {

    private final FirebaseProperties firebaseProperties;



    @PostConstruct
    public void init() {
        try {
            JsonObject json = new JsonObject();
            json.addProperty("type", firebaseProperties.getType());
            json.addProperty("project_id", firebaseProperties.getProjectId());
            json.addProperty("private_key_id", firebaseProperties.getPrivateKeyId());
            json.addProperty("private_key", firebaseProperties.getPrivateKey().replace("\\n", "\n"));
            json.addProperty("client_email", firebaseProperties.getClientEmail());
            json.addProperty("client_id", firebaseProperties.getClientId());
            json.addProperty("auth_uri", firebaseProperties.getAuthUri());
            json.addProperty("token_uri", firebaseProperties.getTokenUri());
            json.addProperty("auth_provider_x509_cert_url", firebaseProperties.getAuthProviderX509CertUrl());
            json.addProperty("client_x509_cert_url", firebaseProperties.getClientX509CertUrl());
            json.addProperty("universe_domain", firebaseProperties.getUniverseDomain());
//            FileInputStream serviceAccount = new FileInputStream("serviceAccountKey.json");
            ByteArrayInputStream serviceAccount = new ByteArrayInputStream(
                    new Gson().toJson(json).getBytes(StandardCharsets.UTF_8)
            );
            FirebaseOptions options = new FirebaseOptions.Builder()
                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                    .setProjectId(firebaseProperties.getProjectId())
                    .build();
            FirebaseApp.initializeApp(options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
