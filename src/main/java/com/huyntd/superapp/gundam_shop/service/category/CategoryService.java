package com.huyntd.superapp.gundam_shop.service.category;
import com.huyntd.superapp.gundam_shop.dto.request.CategoryCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.CategoryResponse;
import com.huyntd.superapp.gundam_shop.model.Category;
import com.huyntd.superapp.gundam_shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface CategoryService {
    /**
     * Lấy tất cả category (phân trang)
     */
    Page<CategoryResponse> getAll(Pageable pageable);

    /**
     * Tìm kiếm category theo tên (phân trang)
     */
    Page<CategoryResponse> searchByName(String keyword, Pageable pageable);

    /**
     * Tạo mới category
     */
    CategoryResponse create(CategoryCreateRequest request);

    /**
     * Cập nhật category
     */
    CategoryResponse update(int id, CategoryCreateRequest request);

    /**
     * Xóa category theo id
     */
    void delete(int id);

    /**
     * Lấy thông tin category theo id
     */
    CategoryResponse getById(int id);
}
