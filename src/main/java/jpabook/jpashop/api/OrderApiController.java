package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;

    /**
     * 엔티티 노출로 무한루프
     */
    @GetMapping("/api/v1/orders")
    public List<Order> orderList() {
        List<Order> all = orderRepository.findAll();
        
        for (Order order : all) {
            //강제초기화
            order.getDelivery().getAddress();
            order.getMember().getName();

            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.stream().forEach(o -> o.getItem().getName());
        }

        return all;
    }

    @GetMapping("/api/v2/orders")
    public List<OrderDto> orderV2() {
        /**
         * orders -> 2개조회
         *   -> order에서 delivery 조회1
         *               member 조회1
         *               orderitem 조회2
         *                     -> item 조회1
         *
         */
        List<Order> orders = orderRepository.findAll();
        return orders.stream()
                .map(OrderDto::new)
                .collect(toList());
    }

    @Getter
    static class OrderDto {
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

    @Getter
    static class OrderItemDto {

        private String itemName;
        private int orderPrice;
        private int count;

        OrderItemDto(OrderItem orderItem) {
            itemName = orderItem.getItem().getName();;
            orderPrice = orderItem.getOrderPrice();
            count = orderItem.getCount();
        }
    }
}
