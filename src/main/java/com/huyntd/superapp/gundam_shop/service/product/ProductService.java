package com.huyntd.superapp.gundam_shop.service.product;


import com.huyntd.superapp.gundam_shop.dto.request.ProductCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.ProductResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
public interface ProductService {
    /**
     * Lấy tất cả sản phẩm (phân trang)
     */
    Page<ProductResponse> getAll(Pageable pageable);

    /**
     * Tìm kiếm sản phẩm theo tên (phân trang)
     */
    Page<ProductResponse> searchByName(String keyword, Pageable pageable);

    /**
     * Lấy danh sách sản phẩm theo category (phân trang)
     */
    Page<ProductResponse> getByCategory(int categoryId, Pageable pageable);

    /**
     * Tạo mới sản phẩm
     */
    ProductResponse create(ProductCreateRequest request);

    /**
     * Cập nhật sản phẩm
     */
    ProductResponse update(int id, ProductCreateRequest request);

    /**
     * Xóa sản phẩm theo id
     */
    void delete(int id);

    /**
     * Lấy chi tiết sản phẩm theo id
     */
    ProductResponse getById(int id);

    /**
     * Upload ảnh cho sản phẩm
     */
    void uploadProductImage(int productId, MultipartFile file) throws IOException;
}
