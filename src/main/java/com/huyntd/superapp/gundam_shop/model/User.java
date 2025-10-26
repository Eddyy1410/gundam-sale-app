package com.huyntd.superapp.gundam_shop.model;

import com.huyntd.superapp.gundam_shop.model.enums.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "[user]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @Column(name = "email", nullable = false, unique = true)
    String email;

    @Column(name = "password_hash")
    String passwordHash;

    @Column(name = "full_name")
    String fullName;

    @Column(name = "phone")
    String phone;

    @Column(name = "role")
    @Enumerated(EnumType.STRING)
    UserRole role;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    Date createdAt;

    @LastModifiedDate
    @Column(name = "updated_at")
    Date updatedAt;

    @Column(name = "fcm_token")
    private String fcmToken;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    Cart cart;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Order> orders = new ArrayList<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    List<Notification> notifications = new ArrayList<>();

    @OneToOne(mappedBy = "customer")
    Conversation customerConversation;

    @OneToMany(mappedBy = "staff", fetch = FetchType.LAZY)
    List<Conversation> staffConversations = new ArrayList<>();

}

