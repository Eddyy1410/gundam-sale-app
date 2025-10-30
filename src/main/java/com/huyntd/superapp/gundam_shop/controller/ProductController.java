package com.huyntd.superapp.gundam_shop.controller;

import com.huyntd.superapp.gundam_shop.dto.ApiResponse;
import com.huyntd.superapp.gundam_shop.dto.request.ProductCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.ProductResponse;
import com.huyntd.superapp.gundam_shop.model.Product;
import com.huyntd.superapp.gundam_shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor

public class ProductController {
    private final ProductService productService;

    /**
     * Danh sách sản phẩm có phân trang
     */
    @GetMapping
    public ApiResponse<Page<ProductResponse>> getAll(Pageable pageable) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.getAll(pageable))
                .build();
    }

    /**
     * Tìm kiếm sản phẩm theo tên
     */
    @GetMapping("/search")
    public ApiResponse<Page<ProductResponse>> search(
            @RequestParam String keyword,
            Pageable pageable
    ) {
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.searchByName(keyword, pageable))
                .build();
    }

    /**
     * Lọc sản phẩm theo Category
     */
    @GetMapping("/category/{categoryId}")
    public ApiResponse<Page<ProductResponse>> getByCategory(
            @PathVariable int categoryId,
            Pageable pageable
    ) {
        System.out.println("đang call api này");
        return ApiResponse.<Page<ProductResponse>>builder()
                .result(productService.getByCategory(categoryId, pageable))
                .build();
    }

    /**
     * Lấy chi tiết sản phẩm
     */
    @GetMapping("/{id}")
    public ApiResponse<ProductResponse> getById(@PathVariable int id) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.getById(id))
                .build();
    }

    /**
     * Tạo mới sản phẩm
     */
    @PostMapping
    public ApiResponse<ProductResponse> create(@RequestBody ProductCreateRequest request) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.create(request))
                .message("Product created successfully")
                .build();
    }

    /**
     * Cập nhật sản phẩm
     */
    @PutMapping("/{id}")
    public ApiResponse<ProductResponse> update(
            @PathVariable int id,
            @RequestBody ProductCreateRequest request
    ) {
        return ApiResponse.<ProductResponse>builder()
                .result(productService.update(id, request))
                .message("Product updated successfully")
                .build();
    }

    /**
     * Xóa sản phẩm
     */
    @DeleteMapping("/{id}")
    public ApiResponse<Void> delete(@PathVariable int id) {
        productService.delete(id);
        return ApiResponse.<Void>builder()
                .message("Product deleted successfully")
                .build();
    }

    /**
     * Upload hình ảnh cho sản phẩm
     */
    @PostMapping(
            value = "/{id}/upload-image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ApiResponse<Void> uploadImage(
            @PathVariable int id,
            @RequestParam("file") MultipartFile file
    ) throws IOException {
        productService.uploadProductImage(id, file);
        return ApiResponse.<Void>builder()
                .message("Image uploaded successfully")
                .build();
    }

    /**
     * Lấy danh sách sản phẩm sắp hết hàng
     */
    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStock(@RequestParam(defaultValue = "5") int threshold) {
        return productService.getLowStockProducts(threshold);
    }

    /**
     * Đếm số lượng sản phẩm sắp hết hàng
     */
    @GetMapping("/low-stock-count")
    public long getLowStockCount(@RequestParam(defaultValue = "5") int threshold) {
        return productService.countLowStockProducts(threshold);
    }
}
