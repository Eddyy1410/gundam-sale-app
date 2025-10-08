package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import javax.crypto.spec.SecretKeySpec;

@Configuration
@EnableWebSecurity
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SecurityConfig {

    static final String[] PUBLIC_POST_ENDPOINTS = {
            "/user/",
            "/auth/log-in", "/auth/introspect"
    };

    static final String[] SWAGGER_ENDPOINTS = {
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };


    // ***********@Value khá nguy hiểm, cần chú ý********
    // static SIGNER_KEY được tạo ra trước. Nó được JVM gán giá trị null ban đầu.
    // Spring DI (@Value) bỏ qua nó. Nó không biết cách (và cũng không được thiết kế để) thay đổi giá trị của một trường static.
    // jwtDecoder() được gọi sau. Nó lấy ra static SIGNER_KEY vẫn đang mang giá trị null từ bước 1.
    // Lỗi xảy ra: null.getBytes() -> NullPointerException.

    // tương tự static thì @Value cũng tương tác lỗi với final
    // đây là LỖI BIÊN DỊCH không phải LÕI KHI CHẠY
    // Trình biên dịch Java nhìn vào code này. Nó không biết @Value là gì. Đó là một annotation của Spring, không phải của Java.
    // Nó thấy một trường final không được gán giá trị ở dòng khai báo.
    // Nó kiểm tra xem có constructor nào gán giá trị cho nó không. Không có.
    // Kết luận của Compiler: "Lời hứa đã bị phá vỡ. Biến final này không được khởi tạo. Tôi không thể biên dịch code này."
    // Kết quả: Bạn nhận được lỗi "variable SIGNER_KEY not initialized". Lỗi này xảy ra trước cả khi ứng dụng của bạn có cơ hội chạy.
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .authorizeHttpRequests(requests ->
                        requests.requestMatchers(HttpMethod.POST, PUBLIC_POST_ENDPOINTS).permitAll()
                                .requestMatchers(SWAGGER_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.GET, "/user/")
                                    //.hasAuthority("SCOPE_ADMIN")
                                    .hasRole(UserRole.ADMIN.name())
                                .anyRequest().authenticated())
                //Xử lý bearer token
                //chỗ này phía BE của mình sẽ đóng vai trò là resource server để xử lý token gửi về
                //nên sẽ dùng oauth2ResourceServer
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(jwtConfigurer -> jwtConfigurer
                                .decoder(jwtDecoder())
                                // Tùy chỉnh để chuyển mặc định "SCOPE_****" thành "ROLE_****"
                                .jwtAuthenticationConverter(jwtAuthenticationConverter())
                        ))
        ;

        return httpSecurity.build();
    }

    @Bean
    JwtAuthenticationConverter jwtAuthenticationConverter() {
        JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
        jwtGrantedAuthoritiesConverter.setAuthorityPrefix("ROLE_");

        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);

        return jwtAuthenticationConverter;
    }

    @Bean
    JwtDecoder jwtDecoder() {
        SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(), "HS256");

        return NimbusJwtDecoder
                .withSecretKey(secretKeySpec)
                .macAlgorithm(MacAlgorithm.HS256)
                .build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }

}
