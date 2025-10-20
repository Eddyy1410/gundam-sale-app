package com.huyntd.superapp.gundam_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import com.huyntd.superapp.gundam_shop.model.PasswordResetToken;

import jakarta.persistence.LockModeType;

public interface PasswordResetTokenRepository extends JpaRepository<PasswordResetToken, Integer> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PasswordResetToken> findByToken(String token);

    Optional<PasswordResetToken> findTopByUserOrderByIdDesc(com.huyntd.superapp.gundam_shop.model.User user);
    Optional<PasswordResetToken> findTopByUserIdOrderByIdDesc(int userId);
    java.util.List<PasswordResetToken> findByUserId(int userId);
}
