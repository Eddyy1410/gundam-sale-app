package com.huyntd.superapp.gundam_shop.service.category.impl;

import com.huyntd.superapp.gundam_shop.dto.request.CategoryCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CategoryResponse;
import com.huyntd.superapp.gundam_shop.mapper.CategoryMapper;
import com.huyntd.superapp.gundam_shop.model.Category;
import com.huyntd.superapp.gundam_shop.repository.CategoryRepository;
import com.huyntd.superapp.gundam_shop.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final CategoryMapper mapper;

    @Override
    public Page<CategoryResponse> getAll(Pageable pageable) {
        return categoryRepository.findAll(pageable).map(mapper::toDTO);
    }

    @Override
    public Page<CategoryResponse> searchByName(String keyword, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCase(keyword, pageable).map(mapper::toDTO);
    }

    @Override
    public CategoryResponse create(CategoryCreateRequest request) {
        Category category = new Category();
        category.setName(request.getName());
        return mapper.toDTO(categoryRepository.save(category));
    }

    @Override
    public CategoryResponse update(int id, CategoryCreateRequest request) {
        Category existing = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        existing.setName(request.getName());
        return mapper.toDTO(categoryRepository.save(existing));
    }

    @Override
    public void delete(int id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public CategoryResponse getById(int id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return mapper.toDTO(category);
    }
}
