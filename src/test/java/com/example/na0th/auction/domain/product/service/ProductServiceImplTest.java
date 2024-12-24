package com.example.na0th.auction.domain.product.service;

import com.example.na0th.auction.domain.product.dto.request.ProductRequest;
import com.example.na0th.auction.domain.product.dto.response.ProductResponse;
import com.example.na0th.auction.domain.product.exception.ProductNotFoundException;
import com.example.na0th.auction.domain.product.model.Product;
import com.example.na0th.auction.domain.product.model.ProductCategory;
import com.example.na0th.auction.domain.product.model.ProductImage;
import com.example.na0th.auction.domain.product.repository.ProductImageRepository;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.user.model.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {
    @Mock
    ProductRepository productRepository;
    @Mock
    ProductImageRepository productImageRepository;
    @Spy
    @InjectMocks
    ProductServiceImpl productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = Product.builder()
                .id(1L)
                .name("name")
                .description("description")
                .productCategory(ProductCategory.DSLR)
                .build();
    }

    @Nested
    class 상품_생성 {
        //이미지 업로드는 클래스 혹은 메서드를 분리해서 Mock 하는 것으로 리팩터링 고려하기
        @Test
        @DisplayName("상품 이미지를 포함한 상품 생성")
        void 상품_이미지가_있는_상품을_생성한다() {
            // given
            ProductRequest.Create request = new ProductRequest.Create("name1", "description1", "DSLR");
            Product createdProduct = Product.builder()
                    .id(1L)
                    .name("name1")
                    .description("description1")
                    .productCategory(ProductCategory.DSLR)
                    .build();
            when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

            MockMultipartFile image1 = new MockMultipartFile("images", "image1.jpg", "image/jpeg", "image1Content".getBytes());
            MockMultipartFile image2 = new MockMultipartFile("images", "image2.jpg", "image/jpeg", "image2Content".getBytes());
            List<MultipartFile> images = List.of(image1, image2);

            List<String> imageUrls = List.of("imageUrl1", "imageUrl2");
            when(productService.uploadImages(images)).thenReturn(imageUrls);

            List<ProductImage> productImages = imageUrls.stream()
                    .map(imageUrl -> ProductImage.create(imageUrl, product))
                    .toList();
            when(productImageRepository.saveAll(anyList())).thenReturn(productImages);

            // when
            ProductResponse productResponse = productService.create(request, images);
            // then
            assertThat(productResponse).isNotNull(); // 응답이 null이 아님을 검증
            assertThat(productResponse.getProductName()).isEqualTo("name1");
            assertThat(productResponse.getDescription()).isEqualTo("description1");
            assertThat(productResponse.getProductCategory()).isEqualTo(ProductCategory.DSLR.getDisplayName());
        }

        @Test
        @DisplayName("상품 이미지를 포함하지 않은 상품 생성")
        void 상품_이미지가_없는_상품을_생성한다() {
            // given
            ProductRequest.Create request = new ProductRequest.Create("name1", "description1", "DSLR");
            Product createdProduct = Product.builder()
                    .id(1L)
                    .name("name1")
                    .description("description1")
                    .productCategory(ProductCategory.DSLR)
                    .build();
            when(productRepository.save(any(Product.class))).thenReturn(createdProduct);

            List<MultipartFile> images = Collections.emptyList(); //이미지 없음
            List<String> imageUrls = Collections.emptyList();

            when(productService.uploadImages(images)).thenReturn(imageUrls);
            when(productImageRepository.saveAll(anyList())).thenReturn(Collections.emptyList());

            // when
            ProductResponse productResponse = productService.create(request, images);

            // then
            assertThat(productResponse).isNotNull(); // 응답이 null이 아님을 검증
            assertThat(productResponse.getProductName()).isEqualTo("name1");
            assertThat(productResponse.getDescription()).isEqualTo("description1");
            assertThat(productResponse.getProductCategory()).isEqualTo(ProductCategory.DSLR.getDisplayName());
            verify(productRepository, times(1)).save(any(Product.class));
            verify(productImageRepository, times(1)).saveAll(Collections.emptyList());
        }
    }

    @Nested
    class 상품_조회 {
        @Test
        void 상품을_상품ID로_조회한다() {
            // given
            Product product = Product.builder()
                    .id(1L)
                    .name("name1")
                    .description("description1")
                    .productCategory(ProductCategory.DSLR)
                    .build();
            when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));
            // when
            ProductResponse productResponse = productService.getById(1L);
            // then
            assertThat(productResponse)
                    .extracting("productId", "productName", "description", "productCategory")
                    .containsExactlyInAnyOrder(1L, "name1", "description1", ProductCategory.DSLR.getDisplayName());
        }

        @Test
        @DisplayName("찾으려는 상품 ID가 존재하지 않으면 예외가 발생한다")
        void 없는_상품을_상품ID로_조회하면_예외가_발생한다() {
            // given
            when(productRepository.findById(anyLong())).thenReturn(Optional.empty());
            // when & then
            Assertions.assertThatThrownBy(() -> productService.getById(2L)).isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    class 상품_수정 {
        @Test
        @DisplayName("상품 ID가 존재하면 정상적으로 수정한다")
        void 상품을_수정한다() {
            // given
            ProductRequest.Update request = new ProductRequest.Update("updated:name1", "updated:description1", "TV");
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            when(productRepository.save(any(Product.class))).thenReturn(product);

            // when
            ProductResponse productResponse = productService.update(1L, request);

            // then
            assertThat(productResponse)
                    .extracting("productName", "description", "productCategory")
                    .containsExactlyInAnyOrder("updated:name1", "updated:description1", ProductCategory.TV.getDisplayName());
        }

        @Test
        @DisplayName("상품 ID가 존재하지 않으면 수정 실패한다.")
        void 상품_ID가_없으면_상품_수정을_실패한다() {
            // given
            ProductRequest.Update request = new ProductRequest.Update("updated:name1", "updated:description1", "TV");
            when(productRepository.findById(999L)).thenReturn(Optional.empty());
            // when & then
            Assertions.assertThatThrownBy(() -> productService.update(999L, request)).isInstanceOf(ProductNotFoundException.class);
        }
    }

    @Nested
    class 상품_삭제 {
        @Test
        @DisplayName("상품 ID가 존재하면 정상적으로 삭제한다 ")
        void 상품을_삭제한다() {
            // given
            when(productRepository.findById(1L)).thenReturn(Optional.of(product));
            // when
            productService.delete(1L);
            // then
            verify(productRepository, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("상품 ID가 존재하지 않으면 삭제 실패한다.")
        void 상품_ID가_없으면_상품_삭제를_실패한다() {
            // given
            when(productRepository.findById(999L)).thenReturn(Optional.empty());
            // when & then
            Assertions.assertThatThrownBy(() -> productService.delete(999L)).isInstanceOf(ProductNotFoundException.class);
        }
    }

}