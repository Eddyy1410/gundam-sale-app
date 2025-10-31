package com.huyntd.superapp.gundam_shop.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.AuthenticationRequest;
import com.huyntd.superapp.gundam_shop.dto.request.ChangePasswordRequest;
import com.huyntd.superapp.gundam_shop.dto.request.GoogleTokenRequest;
import com.huyntd.superapp.gundam_shop.dto.request.IntrospectRequest;
import com.huyntd.superapp.gundam_shop.dto.request.LogoutRequest;
import com.huyntd.superapp.gundam_shop.dto.request.ResetPasswordRequest;
import com.huyntd.superapp.gundam_shop.dto.request.VerifyResetCodeRequest;
import com.huyntd.superapp.gundam_shop.dto.response.AuthenticationResponse;
import com.huyntd.superapp.gundam_shop.dto.response.IntrospectResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.service.authentication.AuthenticationService;
import com.huyntd.superapp.gundam_shop.service.authentication.PasswordResetService;
import com.huyntd.superapp.gundam_shop.service.user.UserService;
import com.nimbusds.jose.JOSEException;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
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

    PasswordResetService passwordResetService;

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

    @PostMapping("/logout")
    ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws ParseException, JOSEException {
        return ApiResponse.<Void>builder()
                .result(authenticationService.logout(request))
                .build();
    }

    @PostMapping("/forgot-password")
    ApiResponse<Void> forgotPassword(@RequestParam @NotBlank(message = "BLANK_EMAIL") @Email(message = "INVALID_EMAIL") String email) {
        // Do not reveal whether the email exists
        passwordResetService.requestReset(email);
        return ApiResponse.<Void>builder().result(null).build();
    }

    @PostMapping("/reset-password")
    ApiResponse<String> resetPassword(@RequestBody ResetPasswordRequest request) {
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        passwordResetService.resetPassword(request.getEmail(), request.getCode(), request.getNewPassword());
        return ApiResponse.<String>builder().result("Password reset successful").build();
    }

    @PostMapping("/verify-reset-code")
    ApiResponse<Void> verifyResetCode(@RequestBody VerifyResetCodeRequest request) {
        passwordResetService.verifyCode(request.getEmail(), request.getCode());
        return ApiResponse.<Void>builder().result(null).build();
    }

    @PostMapping("/change-password")
    ApiResponse<Void> changePassword(@RequestBody ChangePasswordRequest request) {
        // Validate confirmation
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new AppException(ErrorCode.PASSWORD_MISMATCH);
        }

        // Prevent user from reusing the same password
        if (request.getNewPassword().equals(request.getCurrentPassword())) {
            throw new AppException(ErrorCode.NEW_PASSWORD_SAME_AS_CURRENT);
        }

        userService.changePassword(request.getCurrentPassword(), request.getNewPassword());

        return ApiResponse.<Void>builder().result(null).build();
    }
}
