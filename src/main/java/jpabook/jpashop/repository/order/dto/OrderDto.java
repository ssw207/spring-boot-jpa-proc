package jpabook.jpashop.repository.order.dto;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Getter
public class OrderDto {
    private Long orderId;
    private String name;
    private LocalDateTime orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public OrderDto(Order order) {
        orderId = order.getId();
        name = order.getMember().getName();
        orderStatus = order.getStatus();
        address = order.getDelivery().getAddress();
        orderDate = order.getOrderDate();
        //order.getOrderItems().stream().forEach(o -> o.getItem().getName());
        /**
         * 내부에 엔티티도 Dto로 변환해서 리턴해야함
         * 엔티티를 노출하면 엔티티 변셩시 api스팩이 변경되는 이슈발생
         */
        orderItems = order.getOrderItems().stream().map(OrderItemDto::new).collect(toList());
    }
}