package com.huyntd.superapp.gundam_shop.dto.request;

import java.math.BigDecimal;
import lombok.*;
import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {
    private String name;
    private String briefDescription;
    private String fullDescription;
    private String technicalSpecification;
    private BigDecimal price;
    private int quantity;
    private int categoryId;
}
