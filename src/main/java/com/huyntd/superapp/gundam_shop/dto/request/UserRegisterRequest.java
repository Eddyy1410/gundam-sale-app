package com.huyntd.superapp.gundam_shop.dto.request;

import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data // Getter, Setter, RequiredConstructor, ToString, EqualsAndHashCode
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterRequest {

    @NotBlank(message = "email must not be a blank")
    String email;

    @NotBlank(message = "password must not be a blank")
    String password;

    String fullName;

    String phone;
}
