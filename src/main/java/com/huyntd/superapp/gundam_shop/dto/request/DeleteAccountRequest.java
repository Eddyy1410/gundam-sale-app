package com.huyntd.superapp.gundam_shop.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class DeleteAccountRequest {

    @NotBlank(message = "BLANK_PHONE")
    @Size(max = 30)
    private String phone;

}
