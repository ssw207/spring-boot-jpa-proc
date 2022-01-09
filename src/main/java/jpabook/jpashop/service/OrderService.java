package jpabook.jpashop.service;

import jpabook.jpashop.domain.Delivery;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, Long itemId, int count) {
        //엔티티조회
        Member member = memberRepository.findOne(memberId);
        Item item = itemRepository.findOne(itemId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());

        //주문 상품생성
        OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);

        //주문 생성
        Order order = Order.createOrder(member, delivery, orderItem);
        
        /**
         * 주문 저장
         * delivery,orderItem에 cascade옵셔이 있기때문에 order가 영속성 컨테이너에 등록될때 delivery, orderItem도 같에 등록된다.
         * 트레젝션이 커밋되는 시점에 flush()가 실행되면서 Order, Delivery, OrderItem에 insert 쿼리가 생성됨
         */
        orderRepository.save(order);
        
        return order.getId();
    }

    /**
     * 주문 취소
     */
    @Transactional
    public void cancelOrder(Long orderId) {
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);

        /**
         * 주문 취소
         * Entity안에 데이터만 바꾸면 JPA가 더티체킹을 하면서 변경된 내용을 DB에 업데이트 처리한다.
         * order : 주문상태 cancel로 변경
         * orderItem : stockQuantity 가 변경
         */
        order.cancel();
    }

    /**
     * 검색
     */
}
