package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[notification]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Notification {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Column(name = "message")
    String message;

    @Column(name = "is_read", nullable = false)
    Boolean isRead = false;

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    LocalDateTime CreatedAt;

}
