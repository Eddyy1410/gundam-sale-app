package com.huyntd.superapp.gundam_shop.service.product.impl;

import com.huyntd.superapp.gundam_shop.dto.request.ProductCreateRequest;
import com.huyntd.superapp.gundam_shop.dto.response.ProductResponse;
import com.huyntd.superapp.gundam_shop.mapper.ProductMapper;
import com.huyntd.superapp.gundam_shop.model.Category;
import com.huyntd.superapp.gundam_shop.model.Product;
import com.huyntd.superapp.gundam_shop.model.ProductImage;
import com.huyntd.superapp.gundam_shop.repository.CategoryRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductImageRepository;
import com.huyntd.superapp.gundam_shop.repository.ProductRepository;
import com.huyntd.superapp.gundam_shop.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductImageRepository productImageRepository;
    private final ProductMapper mapper;

    @Value("${upload.path:uploads}")
    private String uploadPath;

    @Override
    public Page<ProductResponse> getAll(Pageable pageable) {
//        return productRepository.findAll(pageable).map(mapper::toDTO);
        Page<Product> products = productRepository.findAll(pageable);
        products.forEach(p -> log.info("Product entity: id={}, name={}, images={}",
                p.getId(), p.getName(), p.getProductImages() != null ? p.getProductImages().size() : 0));
        return products.map(mapper::toDTO);
    }

    @Override
    public Page<ProductResponse> searchByName(String keyword, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCase(keyword, pageable).map(mapper::toDTO);
    }

    @Override
    public Page<ProductResponse> getByCategory(int categoryId, Pageable pageable) {
        return productRepository.findByCategory_Id(categoryId, pageable).map(mapper::toDTO);
    }

    @Override
    public ProductResponse create(ProductCreateRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));
        Product product = mapper.toEntity(request);
        product.setCategory(category);
        return mapper.toDTO(productRepository.save(product));
    }

    @Override
    public ProductResponse update(int id, ProductCreateRequest request) {
        Product existing = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        existing.setName(request.getName());
        existing.setBriefDescription(request.getBriefDescription());
        existing.setFullDescription(request.getFullDescription());
        existing.setTechnicalSpecification(request.getTechnicalSpecification());
        existing.setPrice(request.getPrice());
        existing.setQuantity(request.getQuantity());
        existing.setCategory(category);

        return mapper.toDTO(productRepository.save(existing));
    }

    @Override
    public void delete(int id) {
        productRepository.deleteById(id);
    }

    @Override
    public ProductResponse getById(int id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found"));
        return mapper.toDTO(product);
    }

    @Override
    public void uploadProductImage(int productId, MultipartFile file) throws IOException {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Path uploadDir = Paths.get(uploadPath).toAbsolutePath().normalize();
        Files.createDirectories(uploadDir);
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();

        Path filePath = uploadDir.resolve(fileName);
        file.transferTo(filePath.toFile());

        ProductImage image = ProductImage.builder()
                .product(product)
                .imageUrl("/uploads/" + fileName)
                .build();
        productImageRepository.save(image);
    }
}
