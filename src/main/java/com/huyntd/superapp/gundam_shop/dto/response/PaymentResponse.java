package com.huyntd.superapp.gundam_shop.dto.response;

import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaymentResponse {
    int id;
    int orderId;
    double amount;
    LocalDateTime paymentDate;
    String status;
}
