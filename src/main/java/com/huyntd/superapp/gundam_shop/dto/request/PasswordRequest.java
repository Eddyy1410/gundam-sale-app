package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PasswordRequest {
    @NotBlank
    String currentPassword;

    @NotBlank
    String newPassword;
}
