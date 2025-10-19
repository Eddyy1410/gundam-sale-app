package com.huyntd.superapp.gundam_shop.service.Authentication.Implementation;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.huyntd.superapp.gundam_shop.configuration.GoogleTokenVerifier;
import com.huyntd.superapp.gundam_shop.dto.request.*;
import com.huyntd.superapp.gundam_shop.dto.response.AuthenticationResponse;
import com.huyntd.superapp.gundam_shop.dto.response.IntrospectResponse;
import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.Authentication.AuthenticationService;
import com.huyntd.superapp.gundam_shop.service.User.UserService;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class AuthenticationServiceImplement implements AuthenticationService {

    UserRepository userRepository;

    //@NonFinal không cần cái này vì đã khai báo là static (đảm bảo thuộc về class không thuộc về object)
    //Spring tạo và quản lý các bean (object), không quản lý vòng đời của các lớp
    // ---> không thể tiêm phụ thuộc các attribute được cho là static
    //protected static final String SIGNER_KEY = "ubopm2vag1cty33fjsvf86eue7x8lsl6";
    @NonFinal
    @Value("${jwt.signerKey}")
    protected String SIGNER_KEY;

    GoogleTokenVerifier googleTokenVerifier;

    UserService userService;

    private String generateToken(String email) {
        var user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS256);

        // build payload
        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(email)
                .issuer("huyntd.com") // chỗ này là người chịu trách nhiệm cái gì đó, nói chung là để domain project vào
                .issueTime(new Date())
                .expirationTime(new Date(
                        Instant.now().plus(1, ChronoUnit.HOURS).toEpochMilli()
                ))
                // đặt tên claim là "scope" nó sẽ được MẶC ĐỊNH mapping "SCOPE_****" để thành scope dùng cho authorization
                // Có thể thay đổi mặc định "SCOPPE_****" này thành "ROLE_***" tùy chỉnh bằng cách
                // Setup jwtAuthenticationConverter ở SecurityConfig
                .claim("role", user.getRole())
                .claim("id", user.getId())
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes()));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Cannot create JWT ---> ", e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {

        //Chắc là do findByEmail set trả về là Optional<User> nên khai báo bằng "var" cho an toàn
        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);
        boolean authenticated = passwordEncoder.matches(request.getPassword(), user.getPasswordHash());

        if (!authenticated) throw new AppException(ErrorCode.UNAUTHENTICATED);

        var token = generateToken(request.getEmail());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();

    }

    @Override
    public IntrospectResponse introspect(IntrospectRequest request)
            throws JOSEException, ParseException {
        var token = request.getToken();

        //JWSVerifier = Json Web Signature Verifier
        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes());

        // "giải mã" chuỗi token thô thành một đối tượng có cấu trúc (SignedJWT).
        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expiryTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        // Chỗ này mới thật sự là verify sign của token và sử dụng JWSVerifier đã khai báo phía trên
        // 1. lấy (header + payload) từ signedJWT
        // 2. dùng SIGNER_KEY đã truyền vào MACVerifier để tạo ra verifier phía trên
        // từ (1) và (2) tạo ra 1 sign mới
        // 3. dùng sign mới vừa tạo so sánh với sign của token gửi đến
        // ==> Nếu 2 sign giống nhau thì "hợp lệ" else "không hợp lệ"
        var verified = signedJWT.verify(verifier);

        return IntrospectResponse.builder()
                .valid(verified && expiryTime.after(new Date())) //new Date() lấy thời gian hiện tại
                .build();
    }

    @Override
    public AuthenticationResponse processGoogleToken(GoogleTokenRequest request) throws GeneralSecurityException, IOException {
        // user sau khi đăng nhập Google thành công gửi xuống server "id-token"
        // GoogleIdToken.Payload là class chứa "bản dịch" sang tiếng Java của phần Payload (phần nội dung) bên trong chuỗi JWT đó
        GoogleIdToken.Payload payload = googleTokenVerifier.verify(request.getIdToken());
        userService.createOAuth2(UserOAuth2RegisterRequest.builder()
                .email(payload.getEmail())
                .fullName((String) payload.get("name"))
                .build());

        var token = generateToken(payload.getEmail());

        return AuthenticationResponse.builder()
                .token(token)
                .authenticated(true)
                .build();
    }

}
