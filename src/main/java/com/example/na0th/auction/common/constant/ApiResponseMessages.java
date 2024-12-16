package com.example.na0th.auction.common.constant;

public class ApiResponseMessages {
    /**
     *TODO
     *  NOT FOUND 같은 BAD CASE는 예외 메세지는 Response 메세지가 아니니까 분리해야 함
     */

    //AUCTION
    public static final String AUCTION_CREATED_SUCCESS = "경매를 성공적으로 생성했습니다.";
    public static final String AUCTION_NOT_FOUND = "해당하는 경매가 존재하지 않습니다.";
    public static final String AUCTION_RETRIEVED_SUCCESS = "경매를 성공적으로 조회했습니다";
    public static final String AUCTION_UPDATED_SUCCESS = "경매 정보를 성공적으로 업데이트 했습니다.";
    public static final String AUCTION_DELETED_SUCCESS = "경매를 성공적으로 삭제했습니다.";


    //USER
    public static final String USER_CREATED_SUCCESS = "유저를 성공적으로 생성했습니다.";
    public static final String USER_NOT_FOUND = "해당하는 유저가 존재하지 않습니다.";
    public static final String USER_UPDATED_SUCCESS = "유저 정보를 성공적으로 업데이트 했습니다.";
    public static final String USER_DELETED_SUCCESS = "유저를 성공적으로 삭제했습니다.";

    //PRODUCT
    public static final String PRODUCT_CREATED_SUCCESS = "상품을 성공적으로 생성했습니다.";
    public static final String PRODUCT_UPDATED_SUCCESS = "상품 정보를 성공적으로 업데이트 했습니다.";
    public static final String PRODUCT_NOT_FOUND = "해당하는 상품이 존재하지 않습니다.";
    public static final String PRODUCT_DELETED_SUCCESS = "상품을 성공적으로 삭제했습니다.";
}