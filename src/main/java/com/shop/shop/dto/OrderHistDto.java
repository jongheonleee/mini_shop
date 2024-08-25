package com.shop.shop.dto;

import com.shop.shop.constant.OrderStatus;
import com.shop.shop.entity.Order;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderHistDto {

    public OrderHistDto(Order order) {
        this.orderId = order.getId();
        this.orderDate = order.getOrderDate()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        this.orderStatus = order.getOrderStatus();
    }

    private Long orderId;
    private String orderDate;
    private OrderStatus orderStatus;
    private List<OrderItemDto> orderItemDtoList = new ArrayList<>();

    public void addOrderItemDto(OrderItemDto orderItemDto) {
        orderItemDtoList.add(orderItemDto);
    }
}
