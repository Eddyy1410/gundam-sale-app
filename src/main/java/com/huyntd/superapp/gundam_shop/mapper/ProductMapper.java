package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.request.ProductCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.ProductResponse;
import com.huyntd.superapp.gundam_shop.model.Product;
import com.huyntd.superapp.gundam_shop.model.ProductImage;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.id")
    @Mapping(target = "categoryName", source = "category.name")
    @Mapping(target = "imageUrls", ignore = true) // Xử lý ở AfterMapping
    ProductResponse toDTO(Product product);

    @InheritInverseConfiguration(name = "toDTO")
    Product toEntity(ProductResponse dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "category", ignore = true) // set trong service
    Product toEntity(ProductCreateRequest request);

    @AfterMapping
    default void mapImageUrls(@MappingTarget ProductResponse response, Product product) {
        if (product.getProductImages() != null) {
            List<String> urls = product.getProductImages()
                    .stream()
                    .map(ProductImage::getImageUrl)
                    .collect(Collectors.toList());
            response.setImageUrls(urls);
        }
    }
}
