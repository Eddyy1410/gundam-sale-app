package com.huyntd.superapp.gundam_shop.dto.wsResponse;

import com.huyntd.superapp.gundam_shop.dto.enums.CountType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountResponse implements Serializable {

    private Integer count;
    private CountType type; // Ví dụ: UNREAD_MESSAGE, ORDER_BADGE, CART_BADGE

}
