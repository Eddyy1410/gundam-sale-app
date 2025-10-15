package com.huyntd.superapp.gundam_shop.configuration;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Component
@FieldDefaults(level = AccessLevel.PRIVATE)
public class GoogleTokenVerifier {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    String GOOGLE_CLIENT_ID;

    final NetHttpTransport transport = new NetHttpTransport();
    final GsonFactory jsonFactory = new GsonFactory();

    /**
     * Xác thực một chuỗi id_token với server của Google.
     * @param idTokenString Chuỗi token nhận được từ client.
     * @return Đối tượng Payload chứa thông tin người dùng nếu token hợp lệ.
     * @throws GeneralSecurityException Nếu token không hợp lệ (sai chữ ký, hết hạn...).
     * @throws IOException Nếu có lỗi mạng khi giao tiếp với Google.
     */
    public GoogleIdToken.Payload verify(String idTokenString) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, jsonFactory)
                .setAudience(Collections.singletonList(GOOGLE_CLIENT_ID))
                .build();

        System.out.println(idTokenString);

        GoogleIdToken idToken = verifier.verify(idTokenString);

        if (idToken != null) {
            return idToken.getPayload();
        } else {
            throw new GeneralSecurityException("Invalid ID token");
        }
    }

}
