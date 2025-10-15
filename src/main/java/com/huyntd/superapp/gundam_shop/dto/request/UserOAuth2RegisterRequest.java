package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserOAuth2RegisterRequest {

    @NotBlank(message = "BLANK_EMAIL")
    String email;

    String fullName;

    String phone;

}
