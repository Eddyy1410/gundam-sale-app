package com.huyntd.superapp.gundam_shop.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
    private int id;
    private String name;
    private String briefDescription;
    private String fullDescription;
    private String technicalSpecification;
    private BigDecimal price;
    private int quantity;
    private int categoryId;
    private String categoryName;
    private List<String> imageUrls;
}
