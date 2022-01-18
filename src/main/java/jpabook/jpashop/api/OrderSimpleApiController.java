package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
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
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

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

    @GetMapping("/api/v3/simple-orders")
    public List<SimpleOrderDto> orderV3() {
        /**
         * 분석
         * 1)fetch join 으로 order, delivery, member를 join해 하나의 쿼리로 리턴
         * 2)LAZY를 무시하고 프록시가 아니라 엔티티 객체를 세팅함
         * 3)Order를 손대지 않고 내부를 손대 성능튜닝함 범용성이 좋음
         */
        return orderRepository.findAllWithMemberDelivery().stream()
                .map(SimpleOrderDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        /**
         * 분석
         * 1)entity나 embedable만 리턴가능함
         * 2)엔티티를 넘기면 엔티티으 식별자만 넘어감, value타입은 값처럼 동작해서 넘길수있음
         * 3)엔티티의 모든 컬럼이 아니라 필요한 컬럼만 넘김 (join은 페치조인과 동일함)
         * 4)화면에는 최적화 됐지만 재사용성X OrderSimpleQueryDto를 쓸때만 사용가능
         * 5)DTO로 조회했기때문에 변경불가능         
         * 6)에플리케이션 네트웍용량을 줄일수 있으나 미비함
         * 7)API스팩을 레파지토리가 의존하고 있으므로 논리적으로 계층분리가 되어있지않음
         */
        return orderSimpleQueryRepository.findOrderDtos();
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
