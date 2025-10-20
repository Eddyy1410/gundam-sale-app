package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

// save(entity)
// findById(id) return Optional<Entity>
// existsById(id) return boolean
// deleteById(id)
// delete(entity)

public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);
}
