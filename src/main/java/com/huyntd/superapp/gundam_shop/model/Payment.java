package com.huyntd.superapp.gundam_shop.model;

import com.huyntd.superapp.gundam_shop.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "[payment]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    int id;

    @OneToOne
    @JoinColumn(name = "order_id", unique = true, nullable = false)
    Order order;

    @Column(name = "amount", nullable = false, precision = 18, scale = 2)
    BigDecimal amount;

    @CreatedDate
    @Column(name = "payment_date", updatable = false)
    LocalDateTime paymentDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    PaymentStatus status;

}
