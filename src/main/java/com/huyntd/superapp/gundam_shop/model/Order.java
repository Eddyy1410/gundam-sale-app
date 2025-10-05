package com.huyntd.superapp.gundam_shop.model;

import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentMethod;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "[order]")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {

    @Id
    @GeneratedValue
    @Column(name = "id")
    int id;

    @OneToOne
    @JoinColumn(name = "cart_id", nullable = false)
    Cart cart;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    OrderStatus status;

    @Column(name = "billing_address", length = 100)
    String billingAddress;

    @CreatedDate
    @Column(name = "order_date", updatable = false)
    LocalDateTime OrderDate;

    @OneToOne(mappedBy = "order")
    Payment payment;

}
