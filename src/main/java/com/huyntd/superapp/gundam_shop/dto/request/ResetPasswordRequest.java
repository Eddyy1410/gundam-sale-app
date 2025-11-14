package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ResetPasswordRequest {

    @NotBlank(message = "BLANK_EMAIL")
    @Email(message = "INVALID_EMAIL")
    private String email;

    @NotBlank(message = "UNAUTHENTICATED")
    @Pattern(regexp = "\\d{6}", message = "INVALID_CODE")
    private String code;

    @NotBlank(message = "BLANK_PASSWORD")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Za-z]).{8,}", message = "WEAK_PASSWORD")
    private String newPassword;

    @NotBlank(message = "BLANK_PASSWORD")
    private String confirmNewPassword;

}
