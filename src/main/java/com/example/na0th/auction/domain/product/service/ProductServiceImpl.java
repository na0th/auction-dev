package com.example.na0th.auction.domain.product.service;

import com.example.na0th.auction.domain.product.dto.request.ProductRequest;
import com.example.na0th.auction.domain.product.dto.response.ProductResponse;
import com.example.na0th.auction.domain.product.exception.ProductImageUploadException;
import com.example.na0th.auction.domain.product.exception.ProductNotFoundException;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.model.ProductImage;
import com.example.na0th.auction.domain.product.repository.ProductImageRepository;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private static final String UPLOADS_DIR = "src/main/resources/static/uploads/thumbnails/";
    private final ProductRepository productRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional
    public ProductResponse create(ProductRequest.Create request, List<MultipartFile> images) {
        //image 업로드 안 하는 경우에는 NPE??
        Product product = Product.create(
                request.getName(),
                request.getDescription(),
                ProductCategory.valueOf(request.getProductCategory())
        );
        Product newProduct = productRepository.save(product);

        List<String> imageUrls = uploadImages(images);
        List<ProductImage> productImages = imageUrls.stream()
                .map(imageUrl -> ProductImage.create(imageUrl, product))
                .toList();
        //saveAll해도 persist마다 쿼리 1개씩 날라가서 의미는 없다..
        List<ProductImage> newProductImages = productImageRepository.saveAll(productImages);

        return ProductResponse.of(newProduct);
    }

    public ProductResponse getById(Long productId) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found" + productId));
        return ProductResponse.of(foundProduct);
    }

    public ProductResponse update(Long productId, ProductRequest.Update request) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found" + productId));

        foundProduct.update(
                request.getName(),
                request.getDescription(),
                request.getProductCategory()
        );

        Product updatedProduct = productRepository.save(foundProduct);

        return ProductResponse.of(updatedProduct);
    }

    public void delete(Long productId) {
        Product foundProduct = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found" + productId));

        productRepository.deleteById(productId);
    }

    public List<String> uploadImages(List<MultipartFile> images) {
        return images.stream()
                .map(this::generateImageUrl)
                .collect(Collectors.toList());
    }

    public String generateImageUrl(MultipartFile image) {
        String fileName = generateFileName(image);
        Path filePath = Paths.get(UPLOADS_DIR, fileName);
        saveFile(filePath, image);
        return "/uploads/thumbnails/" + fileName;
    }

    private String generateFileName(MultipartFile image) {
        return UUID.randomUUID().toString().replace("-", "") + "_" + image.getOriginalFilename();
    }

    private void saveFile(Path filePath, MultipartFile image) {
        try {
            Files.createDirectories(filePath.getParent());
            Files.copy(image.getInputStream(), filePath);
        } catch (IOException e) {
            throw new ProductImageUploadException("Failed to upload image: " + image.getOriginalFilename(), e);
        }
    }
}
