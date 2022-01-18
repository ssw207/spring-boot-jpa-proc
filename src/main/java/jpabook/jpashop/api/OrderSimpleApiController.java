package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;

/**
 * xToOne(ManyToOne, OneToOne 성능최적화)
 * Order
 * Order -> Member
 * Order -> Deliver
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;

    @GetMapping("/api/v1/simple-orders")
    public List<Order> orderV1() {
        List<Order> all = orderRepository.findAll();
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<SimpleOrderDto> orderV2() {
        /**
         * 분석
         * 1)Order조회 -> 쿼리1, 결과 2행
         * 2)Order조회후 루프를 돌면서 Memeber, Deleivery LAZY로딩으로 조회 (2행 * 2(회원,배송))
         * -> 총5회 쿼리 실행됨
         *
         * EAGER 관계로 바꿔도 최초에 Order조회 jpql실행후 각 연관관계를 따로따로 조회해 최적화되지 않음
         */
        return orderRepository.findAll().stream()
                .map(SimpleOrderDto::new)
                .collect(toList());
    }

    @Data
    static class SimpleOrderDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate;
        private OrderStatus orderStatus;
        private Address address;

        public SimpleOrderDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
            orderDate = order.getOrderDate();
        }
    }
}
