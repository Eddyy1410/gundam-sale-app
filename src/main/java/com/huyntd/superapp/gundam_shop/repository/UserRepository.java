package com.huyntd.superapp.gundam_shop.repository;

import com.huyntd.superapp.gundam_shop.dto.response.UserResponse;
import com.huyntd.superapp.gundam_shop.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

// save(entity)
// findById(id) return Optional<Entity>
// existsById(id) return boolean
// deleteById(id)
// delete(entity)
// List<T> findAll()
// List<T> findAllById(Iterable<ID> ids) .... List<User> users = userRepository.findAllById(userIds);
// List<T> findAll(Sort sort) .... List<User> sortedUsers = userRepository.findAll(Sort.by(Sort.Direction.ASC, "username"));
// Page<T> findAll(Pageable pageable)

public interface UserRepository extends JpaRepository<User, Integer> {
    boolean existsByEmail(String email);
    Optional<User> findByEmail(String email);

    @Query("SELECT new com.huyntd.superapp.gundam_shop.dto.response.UserResponse(u.id, u.email, u.fullName, u.phone, u.role, u.createdAt) FROM User u")
    List<UserResponse> findAllUsersResponse();

    @Query("SELECT u FROM User u WHERE u.role = :role")
    List<User> findByRole(@Param("role") String role);

    @Query("""
    SELECT u
    FROM User u
    LEFT JOIN u.staffConversations c
    GROUP BY u
    ORDER BY COUNT(c) ASC
    """)
    List<User> findUsersOrderByConversationCountAsc();
}
