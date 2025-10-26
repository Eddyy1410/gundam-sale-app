package com.huyntd.superapp.gundam_shop.util;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Small stateless reset-token utility. Token format: base64url(payload).base64url(signature)
 * payload = JSON {"uid":<int>,"p":"passwordHash","exp":<epochSeconds>}
 * signature = HMAC_SHA256(secret, payload)
 */
public class ResetTokenUtil {

    private static final String HMAC_ALGO = "HmacSHA256";
    private final byte[] secretBytes;
    private final long expirationSeconds;
    private final ObjectMapper mapper = new ObjectMapper();

    public ResetTokenUtil(String secret, long expirationSeconds) {
        this.secretBytes = secret.getBytes(StandardCharsets.UTF_8);
        this.expirationSeconds = expirationSeconds;
    }

    public String generate(int userId, String email, String passwordHash, String nonce) {
        try {
            long exp = Instant.now().getEpochSecond() + expirationSeconds;
            Payload p = new Payload(userId, email, passwordHash, nonce, exp);
            String payloadJson = mapper.writeValueAsString(p);
            String payloadB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
            byte[] sig;
            try {
                sig = hmac(payloadJson.getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                throw new RuntimeException("Failed to sign token", ex);
            }
            String sigB64 = Base64.getUrlEncoder().withoutPadding().encodeToString(sig);
            return payloadB64 + "." + sigB64;
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to build reset token", e);
        }
    }

    public Payload verify(String token) {
        try {
            String[] parts = token.split("\\.");
            if (parts.length != 2) throw new IllegalArgumentException("Invalid token format");
            String payloadB64 = parts[0];
            String sigB64 = parts[1];

            byte[] payloadBytes = Base64.getUrlDecoder().decode(payloadB64);
            String payloadJson = new String(payloadBytes, StandardCharsets.UTF_8);

            byte[] expected;
            try {
                expected = hmac(payloadJson.getBytes(StandardCharsets.UTF_8));
            } catch (Exception ex) {
                throw new IllegalArgumentException("Failed to verify token", ex);
            }
            byte[] provided = Base64.getUrlDecoder().decode(sigB64);
            if (!java.security.MessageDigest.isEqual(expected, provided)) {
                throw new IllegalArgumentException("Invalid token signature");
            }

            Payload p = mapper.readValue(payloadJson, Payload.class);
            long now = Instant.now().getEpochSecond();
            if (p.exp < now) throw new IllegalArgumentException("Token expired");
            return p;
        } catch (Exception ex) {
            throw new IllegalArgumentException("Invalid or expired token", ex);
        }
    }

    private byte[] hmac(byte[] msg) throws Exception {
        Mac mac = Mac.getInstance(HMAC_ALGO);
        SecretKeySpec keySpec = new SecretKeySpec(secretBytes, HMAC_ALGO);
        mac.init(keySpec);
        return mac.doFinal(msg);
    }

    public static class Payload {
    public int uid;
    public String e; // email
    public String p; // password hash
    public String n; // nonce
    public long exp;

        // default ctor for Jackson
        public Payload() {}

        public Payload(int uid, String e, String p, String n, long exp) {
            this.uid = uid;
            this.e = e;
            this.p = p;
            this.n = n;
            this.exp = exp;
        }
    }
}
