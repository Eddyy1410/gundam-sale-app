package com.huyntd.superapp.gundam_shop.service.payment;

import com.huyntd.superapp.gundam_shop.dto.request.CreateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.dto.request.UpdateOrderRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.dto.response.PaymentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PaymentService {
    Page<PaymentResponse> getPaymentsByStatus (Pageable pageable, String status);
    Page<PaymentResponse> getPaymentByOrderId(int orderId, Pageable pageable);
    PaymentResponse get (int paymentId);
    PaymentResponse createPayment (PaymentRequest request);
    PaymentResponse updatePayment (int id, int orderId, String status);
}
