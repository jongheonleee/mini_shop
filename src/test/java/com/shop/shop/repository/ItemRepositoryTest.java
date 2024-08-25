package com.shop.shop.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.entity.Item;
import com.shop.shop.entity.QItem;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.TestPropertySource;
import org.thymeleaf.util.StringUtils;


@SpringBootTest
@TestPropertySource(locations="classpath:application-test.properties")
class ItemRepositoryTest {

    @Autowired
    ItemRepository itemRepository;

    @PersistenceContext
    EntityManager em;

    @BeforeEach
    public void beforeEach() {
        createItemList2();
    }


    @Test
    @DisplayName("상품 저장")
    public void createItemTest() {
        Item item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
//        item.setRegTiem(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        Item savedItem = itemRepository.save(item);
        System.out.println("after : " + savedItem);
    }

    @Test
    @DisplayName("상품명 조회 테스트")
    public void findByItemNmTest() {
        List<Item> selected = itemRepository.findByItemNm("테스트 상품1");
        selected.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품명, 상품상세설명 or 테스트")
    public void findByItemNmOrItemDetailTest() {
        List<Item> itemList = itemRepository.findByItemNmOrItemDetail("테스트 상품1", "테스트 상품 상세 설명5");
        itemList.forEach(System.out::print);
    }

    @Test
    @DisplayName("가격 LessThan 테스트")
    public void findByPriceLessThanTest() {
        List<Item> itemList = itemRepository.findByPriceLessThan(10_005);
        itemList.forEach(System.out::print);
    }

    @Test
    @DisplayName("가격 내림차순 조회 테스트")
    public void findByPriceLessThanOrderByPriceDesc() {
        List<Item> itemList = itemRepository.findByPriceLessThanOrderByPriceDesc(10_005);
        itemList.forEach(System.out::print);
    }

    @Test
    @DisplayName("@Query를 이용한 상품 조회 테스트")
    public void findByItemDetailTest() {
        List<Item> itemList = itemRepository.findByItemDetail("테스트 상품 상세 설명");
        itemList.forEach(System.out::print);
    }

    @Test
    @DisplayName("Querydsl 조회 테스트1")
    public void queryDslTest() {
        JPAQueryFactory queryFactory = new JPAQueryFactory(em);
        QItem qItem = QItem.item;

        JPAQuery<Item> query = queryFactory.selectFrom(qItem)
                .where(qItem.itemSellStatus.eq(ItemSellStatus.SELL))
                .where(qItem.itemDetail.like("%" + "테스트 상품 상세 설명" + "%"))
                .orderBy(qItem.price.desc());

        List<Item> itemList = query.fetch();
        itemList.forEach(System.out::println);
    }

    @Test
    @DisplayName("상품 Querydsl 조회 테스트2")
    public void queryDslTest2() {
        BooleanBuilder booleanBuilder = new BooleanBuilder();
        QItem item = QItem.item;

        String itemDetail = "테스트 상품 상세 설명";
        int price = 10_003;
        String itemSellStat = "SELL";

        booleanBuilder.and(item.itemDetail.like("%" + itemDetail + "%"));
        booleanBuilder.and(item.price.gt(price));

        if (StringUtils.equals(itemSellStat, ItemSellStatus.SELL)) {
            booleanBuilder.and(item.itemSellStatus.eq(ItemSellStatus.SELL));
        }

        PageRequest pageable = PageRequest.of(0, 5);
        Page<Item> itemPagingResult = itemRepository.findAll(booleanBuilder, pageable);
        System.out.println("total elements : " + itemPagingResult.getTotalElements());

        List<Item> resultItemList = itemPagingResult.getContent();
        resultItemList.forEach(System.out::println);
    }


    private void createItemList() {
        for (int i=0; i<10; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명");
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100 + i);
//            item.setRegTiem(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }
    }

    private void createItemList2() {
        for (int i=0; i<=5; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SELL);
            item.setStockNumber(100);
//            item.setRegTiem(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }

        for (int i=6; i<=10; i++) {
            Item item = new Item();

            item.setItemNm("테스트 상품" + i);
            item.setPrice(10000 + i);
            item.setItemDetail("테스트 상품 상세 설명" + i);
            item.setItemSellStatus(ItemSellStatus.SOLD_OUT);
            item.setStockNumber(0);
//            item.setRegTiem(LocalDateTime.now());
            item.setUpdateTime(LocalDateTime.now());

            itemRepository.save(item);
        }
    }
}