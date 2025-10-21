package com.huyntd.superapp.gundam_shop.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "[message]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Message {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @ManyToOne
    @JoinColumn(name = "conservation_id", nullable = false)
    Conversation conversation;

    @ManyToOne
    @JoinColumn(name = "sender_id", nullable = false)
    User sender;

    @Column(name = "content", nullable = false, length = 200)
    String content;

    @CreatedDate
    @Column(name = "sent_at", nullable = false, updatable = false)
    Date sentAt;

}
