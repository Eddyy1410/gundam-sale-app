package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResetCodeRequest {

    @NotBlank(message = "BLANK_CODE")
    private String code;

    @NotBlank(message = "BLANK_EMAIL")
    @Email(message = "INVALID_EMAIL")
    private String email;
}
