package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.PaymentRequest;
import com.huyntd.superapp.gundam_shop.dto.response.PaymentResponse;
import com.huyntd.superapp.gundam_shop.model.Payment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PaymentMapper {
    @Mapping(target = "orderId", source = "order.id")
    PaymentResponse toPaymentResponse(Payment payment);
    @Mapping(target = "order.id", source = "orderId")
    Payment toPayment(PaymentRequest paymentRequest);
}
