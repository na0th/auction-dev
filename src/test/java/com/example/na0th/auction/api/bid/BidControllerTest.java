package com.example.na0th.auction.api.bid;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureMockMvc
class BidControllerTest {
    /**
     * STOMP는 Slice 테스트로는 테스트가 효율적이지 않은 것 같다.
     * 1) MockMvc가 STOMP를 지원하지 않음.
     * 2) 실제 WebSocket 서버를 실행하지 않아 SimpleMessageTemplate 가 구동되지 않음
     * 비동기 지원 X
     * 통합 테스트로 전환해야 할 것 같다.
     */
}