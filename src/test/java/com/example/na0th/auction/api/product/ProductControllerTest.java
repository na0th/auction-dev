package com.example.na0th.auction.api.product;

import com.example.na0th.auction.common.config.JpaAuditingConfig;
import com.example.na0th.auction.common.config.SecurityConfig;
import com.example.na0th.auction.common.constant.ApiResponseMessages;
import com.example.na0th.auction.config.TestAuthConfig;
import com.example.na0th.auction.domain.product.dto.request.ProductRequest;
import com.example.na0th.auction.domain.product.dto.response.ProductResponse;
import com.example.na0th.auction.domain.product.exception.ProductNotFoundException;
import com.example.na0th.auction.domain.product.repository.ProductRepository;
import com.example.na0th.auction.domain.product.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static com.example.na0th.auction.common.constant.ApiResponseMessages.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ProductController.class, excludeAutoConfiguration = JpaAuditingConfig.class)
@Import({TestAuthConfig.class})
class ProductControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private ProductService productService;

    private MockMultipartFile image;
    private MockMultipartFile request;

    @BeforeEach
    void setUp() {
        //인증 인가 생략을 위한 인증 객체 생성
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken("testUser", null,
                        //권한 부여 없음
                        List.of()));
        image = new MockMultipartFile("images", "image.jpg", "image/jpeg", "image/png".getBytes());
        request = new MockMultipartFile("productRequest", "request.json", "application/json", "{\"productId\":1}".getBytes());
    }

    @Nested
    class 등록 {
        @Test
        @DisplayName("상품 등록을 성공한다")
        void 상품을_등록한다() throws Exception {
            // given
            ProductResponse createdProduct = new ProductResponse(1L, "productName", "description", "productCategory");
            when(productService.create(any(ProductRequest.Create.class), any(List.class))).thenReturn(createdProduct);

            // when & then
            mockMvc.perform(multipart("/api/v1/products")
                            .file(image)
                            .file(request)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.message").value(PRODUCT_CREATED_SUCCESS))
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.productId").value(1L));
        }

        @Test
        @DisplayName("상품 등록을 실패한다")
        void 상품_등록을_실패한다() throws Exception {
            // given
            when(productService.create(any(ProductRequest.Create.class), any(List.class))).thenThrow(new IllegalArgumentException("argument can not be null"));

            // when & then
            mockMvc.perform(multipart("/api/v1/products")
                            .file(image)
                            .file(request)
                            .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("argument can not be null"));
        }

    }

    @Nested
    class 조회 {
        @Test
        @DisplayName("상품 조회를 성공한다")
        void 상품을_조회한다() throws Exception {
            // given
            ProductResponse foundProduct = new ProductResponse(1L, "productName", "description", "productCategory");
            when(productService.getById(any(Long.class))).thenReturn(foundProduct);
            // when & then
            mockMvc.perform(get("/api/v1/products/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.productId").value(1L))
                    .andExpect(jsonPath("$.data.productName").value("productName"))
                    .andExpect(jsonPath("$.data.description").value("description"))
                    .andExpect(jsonPath("$.data.productCategory").value("productCategory"));

        }

        @Test
        @DisplayName("존재하지 않는 상품 조회는 실패한다")
        void 존재하지_않는_상품을_조회하면_예외가_발생한다() throws Exception {
            // given
            when(productService.getById(any(Long.class))).thenThrow(ProductNotFoundException.class);
            // when & then
            mockMvc.perform(get("/api/v1/products/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
        }
    }

    @Nested
    class 수정 {
        @Test
        @DisplayName("상품 수정을 성공한다")
        void 상품을_수정한다() throws Exception {
            // given
            ProductRequest.Update request = new ProductRequest.Update("update : productName", "update : description", "update : productCategory");
            ProductResponse updatedProduct = new ProductResponse(1L, "update : productName", "update : description", "update : productCategory");
            when(productService.update(any(Long.class), any(ProductRequest.Update.class))).thenReturn(updatedProduct);

            // when & then
            mockMvc.perform(put("/api/v1/products/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.data.productId").value(1L))
                    .andExpect(jsonPath("$.data.productName").value("update : productName"))
                    .andExpect(jsonPath("$.data.description").value("update : description"))
                    .andExpect(jsonPath("$.data.productCategory").value("update : productCategory"));
        }

        @Test
        @DisplayName("존재하지 않는 상품 수정은 실패한다")
        void 존재하지_않는_상품을_수정하면_예외가_발생한다() throws Exception {
            // given
            ProductRequest.Update request = new ProductRequest.Update("update : productName", "update : description", "update : productCategory");
            when(productService.update(any(Long.class), any(ProductRequest.Update.class))).thenThrow(ProductNotFoundException.class);

            // when & then
            mockMvc.perform(put("/api/v1/products/{id}", 1L)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
        }

    }

    @Nested
    class 삭제 {
        @Test
        @DisplayName("상품 삭제를 성공한다")
        void 상품을_삭제한다() throws Exception {
            // given
            doNothing().when(productService).delete(any(Long.class));
            // when & then
            mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value(PRODUCT_DELETED_SUCCESS));
        }

        @Test
        @DisplayName("존재하지 않는 상품 삭제는 실패한다")
        void 존재하지_않는_상품을_삭제하면_예외가_발생한다() throws Exception {
            // given
            doThrow(ProductNotFoundException.class).when(productService).delete(any(Long.class));
            // when & then
            mockMvc.perform(delete("/api/v1/products/{id}", 1L))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(PRODUCT_NOT_FOUND));
        }
    }

}