package com.huyntd.superapp.gundam_shop.controller;
import com.huyntd.superapp.gundam_shop.dto.ApiResponse;

import com.huyntd.superapp.gundam_shop.dto.request.CategoryCreateRequest;

import com.huyntd.superapp.gundam_shop.dto.response.CategoryResponse;
import com.huyntd.superapp.gundam_shop.service.category.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Danh sách category có phân trang
     */
    @GetMapping
    public ApiResponse<Page<CategoryResponse>> getAll(Pageable pageable) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .result(categoryService.getAll(pageable))
                .build();
    }

    /**
     * Tìm kiếm category theo tên
     */
    @GetMapping("/search")
    public ApiResponse<Page<CategoryResponse>> search(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return ApiResponse.<Page<CategoryResponse>>builder()
                .result(categoryService.searchByName(keyword, pageable))
                .build();
    }

    /**
     * Lấy chi tiết category theo ID
     */
    @GetMapping("/{id}")
    public ApiResponse<CategoryResponse> getById(@PathVariable int id) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.getById(id))
                .build();
    }

    /**
     * Tạo mới category
     */
    @PostMapping
    public ApiResponse<CategoryResponse> create(@RequestBody CategoryCreateRequest request) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.create(request))
                .message("Category created successfully")
                .build();
    }

    /**
     * Cập nhật category
     */
    @PutMapping("/{id}")
    public ApiResponse<CategoryResponse> update(
            @PathVariable int id,
            @RequestBody CategoryCreateRequest request
    ) {
        return ApiResponse.<CategoryResponse>builder()
                .result(categoryService.update(id, request))
                .message("Category updated successfully")
                .build();
    }

    /**
     * Xóa category
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        categoryService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Category deleted successfully")
                .build();
    }
}
