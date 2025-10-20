package com.huyntd.superapp.gundam_shop.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.AuthenticationRequest;
import com.huyntd.superapp.gundam_shop.dto.request.GoogleTokenRequest;
import com.huyntd.superapp.gundam_shop.dto.request.IntrospectRequest;
import com.huyntd.superapp.gundam_shop.dto.response.AuthenticationResponse;
import com.huyntd.superapp.gundam_shop.dto.response.IntrospectResponse;
import com.huyntd.superapp.gundam_shop.service.authentication.AuthenticationService;
import com.huyntd.superapp.gundam_shop.service.user.UserService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    AuthenticationService authenticationService;

    UserService userService;

    com.huyntd.superapp.gundam_shop.service.password.PasswordResetService passwordResetService;

    @PostMapping("/log-in")
    ApiResponse<AuthenticationResponse> logIn(@RequestBody @Valid AuthenticationRequest request) {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws ParseException, JOSEException {
        return ApiResponse.<IntrospectResponse>builder()
                .result(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/google-android")
    ApiResponse<AuthenticationResponse> googleLogin(@RequestBody GoogleTokenRequest request) throws GeneralSecurityException, IOException {
        return ApiResponse.<AuthenticationResponse>builder()
                .result(authenticationService.processGoogleToken(request))
                .build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<String> forgotPassword(@RequestBody @Valid com.huyntd.superapp.gundam_shop.dto.request.ForgotPasswordRequest request) {
        passwordResetService.createResetToken(request);
        return ApiResponse.<String>builder().result("If the email exists, password reset instructions will be sent.").build();
    }

    @PostMapping("/reset-password")
    ApiResponse<String> resetPassword(@RequestBody @Valid com.huyntd.superapp.gundam_shop.dto.request.ResetPasswordRequest request) {
        passwordResetService.resetPassword(request);
        return ApiResponse.<String>builder().result("OK").build();
    }

}
