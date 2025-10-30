package com.huyntd.superapp.gundam_shop.service.product;


import com.huyntd.superapp.gundam_shop.dto.request.ProductCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.ProductResponse;
import com.huyntd.superapp.gundam_shop.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

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

    /**
     * Lấy danh sách sản phẩm sắp hết hàng.
     * @param threshold ngưỡng số lượng tối thiểu, ví dụ 5
     * @return danh sách sản phẩm
     */
    List<ProductResponse> getLowStockProducts(int threshold);

    long countLowStockProducts(int threshold);
}
