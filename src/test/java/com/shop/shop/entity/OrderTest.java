package com.shop.shop.entity;

import static org.junit.jupiter.api.Assertions.*;

import com.shop.shop.constant.ItemSellStatus;
import com.shop.shop.constant.OrderStatus;
import com.shop.shop.repository.ItemRepository;
import com.shop.shop.repository.MemberRepository;
import com.shop.shop.repository.OrderItemRepository;
import com.shop.shop.repository.OrderRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.PersistenceContext;
import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class OrderTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private OrderItemRepository orderItemRepository;

    @PersistenceContext
    private EntityManager em;
    @Autowired
    private MemberRepository memberRepository;


    public Item createItem() {
        var item = new Item();

        item.setItemNm("테스트 상품");
        item.setPrice(10000);
        item.setItemDetail("테스트 상품 상세 설명");
        item.setItemSellStatus(ItemSellStatus.SELL);
        item.setStockNumber(100);
//        item.setRegTiem(LocalDateTime.now());
        item.setUpdateTime(LocalDateTime.now());

        return item;
    }

    public Order createOrder() {
        var order = new Order();

        for (int i=0; i<3; i++) {
            var item = createItem();
            itemRepository.save(item);

            var orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1_000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        var member = new Member();
        memberRepository.save(member);

        order.setMember(member);
        orderRepository.save(order);
        return order;
    }

    @ParameterizedTest(name = "{index} ==> 테스트 데이터 개수 ''{0}''")
    @ValueSource(ints = {1, 10, 15, 20})
    @DisplayName("영속성 전이 테스트")
    public void cascadeTest(int count) {
        var order = new Order();

        for (int i=0; i<count; i++) {
            var item = createItem();
            itemRepository.save(item);

            var orderItem = new OrderItem();
            orderItem.setItem(item);
            orderItem.setCount(10);
            orderItem.setOrderPrice(1_000);
            orderItem.setOrder(order);
            order.getOrderItems().add(orderItem);
        }

        orderRepository.saveAndFlush(order);
        em.clear();

        orderRepository.findById(order.getId())
                .orElseThrow(EntityNotFoundException::new);
        assertEquals(count, order.getOrderItems().size());
    }

    @Test
    @DisplayName("고아객체 제거 테스트")
    public void orphanRemovalTest() {
        var order = createOrder();
        order.getOrderItems().remove(0);
        em.flush();
    }
    
    @Test
    @DisplayName("지연 로딩 테스트")
    public void lazyLoadingTest() {
        var order = createOrder();
        Long orderItemId = order.getOrderItems().get(0).getId();
        
        em.flush();
        em.clear();

        OrderItem orderItem = orderItemRepository.findById(orderItemId)
                .orElseThrow(EntityNotFoundException::new);

        System.out.println("orderItem.getOrder().getClass() = " + orderItem.getOrder().getClass());
        System.out.println("=====================================");
        orderItem.getOrder().getOrderDate();
        System.out.println("=====================================");
    }


}