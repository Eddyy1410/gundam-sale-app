package com.huyntd.superapp.gundam_shop.service.password;

import com.huyntd.superapp.gundam_shop.dto.request.ForgotPasswordRequest;
import com.huyntd.superapp.gundam_shop.dto.request.ResetPasswordRequest;

public interface PasswordResetService {
    // returns generated token (or could send email)
    String createResetToken(ForgotPasswordRequest request);

    void resetPassword(ResetPasswordRequest request);
}
