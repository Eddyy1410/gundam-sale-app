package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.InvalidatedToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvalidatedTokenRepository extends JpaRepository<InvalidatedToken, String> {
}
