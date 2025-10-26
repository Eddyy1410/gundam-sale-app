package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.configuration.component.CustomeJwtDecoder;
import com.huyntd.superapp.gundam_shop.configuration.util.JwtAuthenticationEntryPoint;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

    static final String[] PUBLIC_POST_ENDPOINTS = {
            "/user/register",
            "/auth/log-in", "/auth/introspect", "auth/google-android", "auth/logout",
            "/api/products"
    };

    static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    final CustomeJwtDecoder customeJwtDecoder;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                                .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                                .requestMatchers("/api/products/**").permitAll()
                                .requestMatchers("api/conversations/chat/test").permitAll()
                                .requestMatchers("api/notifications/**").permitAll()
                                // Nếu bạn dùng /ws-native
                                .requestMatchers("/ws-native/**").permitAll()
                                .anyRequest().authenticated())
                // .oauth2Login chỉ dành cho stateful API (cookie, session)
                //      |--- LƯU Ý: khái niệm statefull/ stateless mô tả cách SERVER xử lý trạng thái (state) của các phiên làm việc, không phải client
                //      |--- Tránh nhầm lẫn với sessionManager phía client
                //      |--- session này là phía backend lưu trong dtb để quản lý state của 1 client
                // ko có ý nghĩa khi dùng với stateless API (REST api, SPA - Single Page Application web)
                // --> nên disable tránh các lỗi tiềm ẩn
                .oauth2Login(oauth2 -> oauth2.disable())
                //Xử lý bearer token
                //chỗ này phía BE của mình sẽ đóng vai trò là resource server để xử lý token gửi về
                //nên sẽ dùng oauth2ResourceServer
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(
                                jwtConfigurer -> jwtConfigurer
                                        .decoder(customeJwtDecoder)
                                        // Tùy chỉnh để chuyển mặc định "SCOPE_****" thành "ROLE_****"
                                        .jwtAuthenticationConverter(jwtAuthenticationConverter()))
                                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                )
        ;

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthoritiesClaimName("role");
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

//    @Bean
//    JwtDecoder jwtDecoder() {
//        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS256");
//
//        return NimbusJwtDecoder
//                .withSecretKey(secretKeySpec)
//                .macAlgorithm(MacAlgorithm.HS256)
//                .build();
//    }

}
