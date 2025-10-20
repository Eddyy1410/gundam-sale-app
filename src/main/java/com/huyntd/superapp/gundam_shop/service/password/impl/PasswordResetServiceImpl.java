package com.huyntd.superapp.gundam_shop.service.password.impl;

import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.huyntd.superapp.gundam_shop.dto.request.ForgotPasswordRequest;
import com.huyntd.superapp.gundam_shop.dto.request.ResetPasswordRequest;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.model.PasswordResetToken;
import com.huyntd.superapp.gundam_shop.model.User;
import com.huyntd.superapp.gundam_shop.repository.PasswordResetTokenRepository;
import com.huyntd.superapp.gundam_shop.repository.UserRepository;
import com.huyntd.superapp.gundam_shop.service.email.EmailService;
import com.huyntd.superapp.gundam_shop.service.password.PasswordResetService;

import jakarta.transaction.Transactional;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    private final UserRepository userRepository;

    private final PasswordResetTokenRepository tokenRepository;

    private final PasswordEncoder passwordEncoder;

    private final EmailService emailService;

    private final String frontendResetBaseUrl;

    public PasswordResetServiceImpl(UserRepository userRepository,
                                    PasswordResetTokenRepository tokenRepository,
                                    PasswordEncoder passwordEncoder,
                                    EmailService emailService,
                                    @Value("${gundamshop.frontend.reset-base-url:http://localhost:3000/reset-password}") String frontendResetBaseUrl) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
        this.frontendResetBaseUrl = frontendResetBaseUrl;
    }

    @Override
    @Transactional
    public String createResetToken(ForgotPasswordRequest request) {
        if (request.getEmail() == null || request.getEmail().isBlank())
            throw new AppException(ErrorCode.BLANK_EMAIL);

        // Do not reveal whether an email is registered. If user not found, return silently.
        Optional<User> maybeUser = userRepository.findByEmail(request.getEmail());
        if (maybeUser.isEmpty()) {
            // Security: don't leak existence of email. Return null (controller will still send generic message).
            return null;
        }

    User user = maybeUser.get();

    final int expiryMinutes = 5;

        // Check latest token for this user and reuse if still valid (not used, not expired)
        Optional<PasswordResetToken> latestOptional = tokenRepository.findTopByUserIdOrderByIdDesc(user.getId());
        if (latestOptional.isPresent()) {
            PasswordResetToken latest = latestOptional.get();
            Date now = new Date();
            if (!latest.isUsed() && latest.getExpiryDate() != null && latest.getExpiryDate().after(now)) {
                // Reuse existing unexpired token
                return latest.getToken();
            }

            // If we reach here, latest is either used or expired. Update the existing row instead of inserting
            // generate a unique token string
            String newToken = UUID.randomUUID().toString();
            while (tokenRepository.findByToken(newToken).isPresent()) {
                newToken = UUID.randomUUID().toString();
            }

            Date expiry = calculateExpiryDate(expiryMinutes); // minutes
            latest.setToken(newToken);
            latest.setExpiryDate(expiry);
            latest.setUsed(false);

            try {
                tokenRepository.save(latest);
            } catch (org.springframework.dao.DataIntegrityViolationException e) {
                // Very rare race: regenerate token and retry once
                String retryToken = UUID.randomUUID().toString();
                while (tokenRepository.findByToken(retryToken).isPresent()) {
                    retryToken = UUID.randomUUID().toString();
                }
                latest.setToken(retryToken);
                tokenRepository.save(latest);
                newToken = retryToken;
            }

            // send reset email for updated token (token + expiry + instruction)
            String subject = "Reset your password";
            String body = String.format("Your password reset token: %s\n\nThis token is valid for %d minutes.\n\nUse this token on the reset page (paste it into the form) to set a new password. If you didn't request this, ignore this email.", newToken, expiryMinutes);
            try {
                emailService.sendSimpleMessage(user.getEmail(), subject, body);
            } catch (Exception e) {
                System.out.println("Failed to send reset email: " + e.getMessage());
            }

            return newToken;
        }

    // create a new token and ensure its string is unique
    String token = UUID.randomUUID().toString();
    Date expiry = calculateExpiryDate(expiryMinutes); // minutes

        // ensure generated token string does not already exist (very unlikely) to avoid unique constraint
        while (tokenRepository.findByToken(token).isPresent()) {
            token = UUID.randomUUID().toString();
        }

        PasswordResetToken prt = PasswordResetToken.builder()
                .token(token)
                .expiryDate(expiry)
                .used(false)
                .user(user)
                .build();

        try {
            tokenRepository.save(prt);
        } catch (org.springframework.dao.DataIntegrityViolationException e) {
            // Possible race or unique constraint on user_id: another transaction inserted/updated a token for this user.
            // Re-query latest token for this user and update that row instead of inserting.
            Optional<PasswordResetToken> conflictLatest = tokenRepository.findTopByUserIdOrderByIdDesc(user.getId());
            if (conflictLatest.isPresent()) {
                PasswordResetToken existing = conflictLatest.get();
                String newToken = UUID.randomUUID().toString();
                while (tokenRepository.findByToken(newToken).isPresent()) {
                    newToken = UUID.randomUUID().toString();
                }
                existing.setToken(newToken);
                existing.setExpiryDate(calculateExpiryDate(5));
                existing.setUsed(false);
                tokenRepository.save(existing);
                token = newToken;
            } else {
                // If still not found, try regenerating token and saving again (very unlikely)
                String newToken = UUID.randomUUID().toString();
                while (tokenRepository.findByToken(newToken).isPresent()) {
                    newToken = UUID.randomUUID().toString();
                }
                prt.setToken(newToken);
                tokenRepository.save(prt);
                token = newToken;
            }
        }

        // send reset email. Don't let email failures surface to the API response. Include token, expiry, instruction.
        String subject = "Reset your password";
        String body = String.format("Your password reset token: %s\n\nThis token is valid for %d minutes.\n\nUse this token on the reset page (paste it into the form) to set a new password. If you didn't request this, ignore this email.", token, expiryMinutes);
        try {
            emailService.sendSimpleMessage(user.getEmail(), subject, body);
        } catch (Exception e) {
            // Log and continue — we don't want to reveal email config issues to callers
            System.out.println("Failed to send reset email: " + e.getMessage());
        }

        return token;
    }

    @Override
    @Transactional
    public void resetPassword(ResetPasswordRequest request) {
        if (request == null || request.getToken() == null || request.getToken().isBlank()) {
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        if (request.getNewPassword() == null || request.getNewPassword().isBlank()) {
            throw new AppException(ErrorCode.BLANK_PASSWORD);
        }

        Optional<PasswordResetToken> optionalToken;
        try {
            optionalToken = tokenRepository.findByToken(request.getToken());
        } catch (Exception e) {
            // repository may throw when given invalid input; normalize to INVALID_TOKEN
            throw new AppException(ErrorCode.INVALID_TOKEN);
        }

        PasswordResetToken token = optionalToken.orElseThrow(() -> new AppException(ErrorCode.INVALID_TOKEN));

        if (token.isUsed()) throw new AppException(ErrorCode.INVALID_TOKEN);

        if (token.getExpiryDate().before(new Date())) throw new AppException(ErrorCode.INVALID_TOKEN);

        User user = token.getUser();
        user.setPasswordHash(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        token.setUsed(true);
        tokenRepository.save(token);
    }

    private Date calculateExpiryDate(int expiryMinutes) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Date());
        cal.add(Calendar.MINUTE, expiryMinutes);
        return cal.getTime();
    }
}
