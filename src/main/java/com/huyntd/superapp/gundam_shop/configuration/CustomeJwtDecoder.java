package com.huyntd.superapp.gundam_shop.configuration;

import com.huyntd.superapp.gundam_shop.dto.request.IntrospectRequest;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.service.authentication.AuthenticationService;
import com.nimbusds.jose.JOSEException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.text.ParseException;
import java.util.Objects;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CustomeJwtDecoder implements JwtDecoder {
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

    final AuthenticationService authenticationService;

    NimbusJwtDecoder nimbusJwtDecoder = null;

    @Override
    public Jwt decode(String token) throws JwtException {

        try {
            var response = authenticationService.introspect(IntrospectRequest.builder()
                            .token(token)
                    .build());

            if (!response.isValid())
                throw new AppException(ErrorCode.INVALID_KEY);

        } catch (JOSEException | ParseException e) {
            throw new JwtException(e.getMessage());
        }

        if (Objects.isNull(nimbusJwtDecoder)) {
            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(),"HS256");
            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS256)
                    .build();
        }

        System.out.println(nimbusJwtDecoder);

        return nimbusJwtDecoder.decode(token);
    }
}
