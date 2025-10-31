package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class ChangePasswordRequest {

    @NotBlank(message = "BLANK_PASSWORD")
    private String currentPassword;

    @NotBlank(message = "BLANK_PASSWORD")
    @Pattern(regexp = "(?=.*[0-9])(?=.*[A-Za-z]).{8,}", message = "WEAK_PASSWORD")
    private String newPassword;

    @NotBlank(message = "BLANK_PASSWORD")
    private String confirmNewPassword;

}
