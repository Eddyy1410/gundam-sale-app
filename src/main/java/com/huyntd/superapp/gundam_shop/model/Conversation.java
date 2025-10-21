package com.huyntd.superapp.gundam_shop.model;

import com.huyntd.superapp.gundam_shop.model.enums.ConversationStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "[conservation]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Conversation {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @OneToOne()
    @JoinColumn(name = "customer_id", nullable = false, unique = true)
    User customer;

    // 1 Conversation chỉ có thể được xử lý bởi 1 staff, và 1 staff sẽ xử lý nhiều Conversation
    @ManyToOne
    @JoinColumn(name = "staff_id", nullable = false)
    User staff;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    ConversationStatus status;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Message> messages;

}
