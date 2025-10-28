package com.huyntd.superapp.gundam_shop.service.payment.impl;

import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.dto.response.OrderResponse;
import com.huyntd.superapp.gundam_shop.dto.response.PaymentResponse;
import com.huyntd.superapp.gundam_shop.exception.AppException;
import com.huyntd.superapp.gundam_shop.exception.ErrorCode;
import com.huyntd.superapp.gundam_shop.mapper.PaymentMapper;
import com.huyntd.superapp.gundam_shop.model.Order;
import com.huyntd.superapp.gundam_shop.model.Payment;
import com.huyntd.superapp.gundam_shop.model.enums.OrderStatus;
import com.huyntd.superapp.gundam_shop.model.enums.PaymentStatus;
import com.huyntd.superapp.gundam_shop.repository.OrderRepository;
import com.huyntd.superapp.gundam_shop.repository.PaymentRepository;
import com.huyntd.superapp.gundam_shop.service.payment.PaymentService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Service
public class PaymentServiceImpl implements PaymentService {

    PaymentRepository paymentRepository;
    OrderRepository orderRepository;

    PaymentMapper paymentMapper;

    @Override
    public Page<PaymentResponse> getPaymentsByStatus(Pageable pageable, String status) {
        PaymentStatus paymentStatus;

        try {
            paymentStatus = PaymentStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode.INVALID_KEY);
        }
        Page<Payment> paymentPage;

        paymentPage = paymentRepository.findAllByStatus(paymentStatus, pageable)
                    .orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentPage.map(paymentMapper::toPaymentResponse);
    }

    @Override
    public Page<PaymentResponse> getPaymentByOrderId(int orderId, Pageable pageable) {
        Page<Payment> paymentPage = paymentRepository.findAllByOrderId(orderId, pageable)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        return paymentPage.map(paymentMapper::toPaymentResponse);
    }

    @Override
    public PaymentResponse get(int paymentId) {
        var payment = paymentRepository.findById(paymentId).
                orElseThrow(() -> new RuntimeException("Payment not found"));
        return paymentMapper.toPaymentResponse(payment);
    }

    @Override
    public PaymentResponse createPayment(PaymentRequest request) {

        var payment = paymentMapper.toPayment(request);
        payment.setPaymentDate(LocalDateTime.now());
        payment.setStatus(PaymentStatus.PENDING);
        var paymentResponse = paymentRepository.save(payment);
        return paymentMapper.toPaymentResponse(paymentResponse);

    }

    @Override
    public PaymentResponse updatePayment(int id, int orderId, String status) {
        var order = orderRepository.findById(orderId).orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(OrderStatus.PROCESSING);
        orderRepository.save(order);

        var payment = paymentRepository.findById(id).
                orElseThrow(() -> new RuntimeException("Payment not found"));
        payment.setStatus(PaymentStatus.valueOf(status.toUpperCase()));
        var paymentResponse = paymentRepository.save(payment);
        return paymentMapper.toPaymentResponse(paymentResponse);
    }
}
