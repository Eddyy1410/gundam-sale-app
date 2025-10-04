package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
}
