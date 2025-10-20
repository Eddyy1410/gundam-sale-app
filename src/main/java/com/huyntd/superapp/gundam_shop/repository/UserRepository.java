package com.huyntd.superapp.gundam_shop.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.huyntd.superapp.gundam_shop.model.User;

// save(entity)
// findById(id) return Optional<Entity>
// existsById(id) return boolean
// deleteById(id)
// delete(entity)

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
