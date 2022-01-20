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

    /**
     * 페치조인 사용하는경우 = 쿼리1
     * dao 로직 변경으로만 쿼리튜닝이 가능하다
     * 단 컬랙션 패치조인시 페이징이 불가능하다. (메모리에 올린뒤 메모리에서 페이징 처리시도함)
     * -> 1: N 조인시 Order의 갯수가 뻥튀기 되기 때문에 OrderItem이 아니라 OrderItem 갯수 기준으로 페이징 처리됨
     * 컬랙션 패치조인은 1개만 사용가능함. 부정확하게 조회될수 있음
     */
    @GetMapping("/api/v3/orders")
    public List<OrderDto> orderV3() {
        /**
         * order가 2 orderItem이 4인경우 join을하면 order가 복제되어 2 * 2 = 4개가 출력됨
         * distinct 명령어 사용시 order가 같은 id값이면 Order 엔티티만 중복을 줄여줌 (SQL distinct와 다르다)
         *      select distinc o from Order o ...
         */
        List<OrderDto> collect = orderRepository.findAllWithItem().stream().map(OrderDto::new).collect(toList());
        for (OrderDto orderDto : collect) {
            /*
                //disticnt 사용X
                jpabook.jpashop.api.OrderApiController$OrderDto@19cbed57 id: 4
                jpabook.jpashop.api.OrderApiController$OrderDto@1a6b2ed8 id: 4
                jpabook.jpashop.api.OrderApiController$OrderDto@39beca58 id: 11
                jpabook.jpashop.api.OrderApiController$OrderDto@4c917c6e id: 11

                //distinct 사용시
                jpabook.jpashop.api.OrderApiController$OrderDto@61cce2a3 id: 4
                jpabook.jpashop.api.OrderApiController$OrderDto@10926dc4 id: 11
             */
            System.out.println(orderDto.toString()+ " id: " +orderDto.getOrderId());
        }
        return collect;
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