package com.example.na0th.auction.domain.auction.repository.custom;

import com.example.na0th.auction.domain.auction.model.Auction;
import com.example.na0th.auction.domain.auction.model.QAuction;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
//
//@SpringBootTest
//@Transactional
////삭제 예정.. 테스트 시간이 너무 오래 걸림
//class AuctionRepositoryCustomImplTest {
//
//    @Autowired
//    EntityManager em;
//
//    @Test
//    void queryDSL_작동_테스트() {
//        //given
//        Auction auction1 = new Auction();
//        Auction auction2 = new Auction();
//        em.persist(auction1);
//        em.persist(auction2);
//        JPAQueryFactory query = new JPAQueryFactory(em);
//        QAuction qAuction = QAuction.auction; //Querydsl Q타입 동작 확인
//        //when
//        List<Auction> result = query
//                .selectFrom(qAuction)
//                .fetch();
//        //then
//        Assertions.assertThat(result.get(0)).isEqualTo(auction1);
//        Assertions.assertThat(result.get(1)).isEqualTo(auction2);
//    }
//
//
//}