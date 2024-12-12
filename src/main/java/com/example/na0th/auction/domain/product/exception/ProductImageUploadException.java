package com.example.na0th.auction.domain.product.exception;

public class ProductImageUploadException extends RuntimeException {
    public ProductImageUploadException(String message) {
        super(message);
    }

    //예외를 바꿔서 던졌으므로 추적을 위해 cause 추가
    public ProductImageUploadException(String message, Throwable cause) {
        super(message, cause);
    }

}
