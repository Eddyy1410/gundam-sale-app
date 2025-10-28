package com.huyntd.superapp.gundam_shop.dto.request;

import lombok.Data;
import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Getter
@Configuration
@ConfigurationProperties(prefix = "firebase")
public class FirebaseProperties {
    private String type;
    private String projectId;
    private String privateKeyId;
    private String privateKey;
    private String clientEmail;
    private String clientId;
    private String authUri;
    private String tokenUri;
    private String authProviderX509CertUrl;
    private String clientX509CertUrl;
    private String universeDomain;
}
