package com.huyntd.superapp.gundam_shop.mapper;

import com.huyntd.superapp.gundam_shop.dto.response.CategoryResponse;
import com.huyntd.superapp.gundam_shop.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryMapper INSTANCE = Mappers.getMapper(CategoryMapper.class);

    CategoryResponse toDTO(Category category);

    Category toEntity(CategoryResponse dto);
}
