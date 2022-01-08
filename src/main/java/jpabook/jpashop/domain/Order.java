package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders") // order by 예약어때문에 관례로 orders사용
@Getter @Setter
public class Order {

    @Id @GeneratedValue
    @Column(name = "order_id") // 테이블_id 형식
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) //여러개의 주문가 하나의 회원을 가진다
    @JoinColumn(name = "member_id") // join을 어떤 컬럼으로 할거냐? fk 컬럼명이됨
    private Member member;

    /*
    mappedBy: 연관관계의 주인은 orderItem의 order 필드
    cascade : 영속성 컨텍스트에 등록할때 order만 등록해도 orderItems의 객체가 같이 등록된다.
              cascade 옵션이 없다면 persist(orderItemA).. 형태로 각각 영속화해야함
              CascadeType.ALL 인경우 delete할때도 같이 지움
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL) 
    private List<OrderItem> orderItems = new ArrayList<>();

    /*
    cascade : Order 엔티티가 영속화할때 Delivery 엔티티도 자동으로 영속화한다.
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 1:1인경우 fk는 어디에 둬도 상관없으나 많이 사용하는쪽에 추가하는게 좋다.
    @JoinColumn(name = "delivery_id") // join을 어떤 컬럼으로 할거냐? fk 컬럼명이됨
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간, 로컬데이트 타임을쓰면 자바8에서 자동으로 읽기때문에 @ 필요없다

    private OrderStatus status; // 주문상태 [ORDER, CANCEL]

    /**
     * 연관관계 편의 매서드 (양방향일때 사용)
     * Order 엔티티에 Member 엔티티를 추가할때
     * Member 엔티티에 Order 엔티티를 자동으로 추가한다
     *
     * 연관관계 매소드가 없는경우 아래처럼 개발자가 수동을로 넣어줘야하기 때문에 실수할수 있다.
     *
     * Member member = new Memeber();
     * Order order = new Order();
     *
     * member.geOrder().add(order);
     * order.setMember(member);
     *
     */
    public void setMember(Member member) {
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrderItem orderItem) {
        orderItems.add(orderItem);
        orderItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    //==생성 매서드==//
    public static Order createOrder(Member memebr, Delivery delivery, OrderItem... orderitems) {
        Order order = new Order();
        order.setMember(memebr);
        order.setDelivery(delivery);
        for (OrderItem orderItem : orderitems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    //==비즈니스 로직==//
    /**
     * 주문취소
     */
    public void cancel() {
        if (delivery.getStatus() == DeliveryStatus.COMP) {
            throw new IllegalStateException("이미 배송완료된 상품은 취소가 불가능합니다.");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrderItem orderItem : orderItems) {
            orderItem.cancel();
        }
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice() {
        /*
        int totalPrice =0;
        for (OrderItem orderItem : orderItems) {
            totalPrice += orderItem.getTotalPrice();
        }
        return totalPrice;
        */

        return orderItems.stream().mapToInt(OrderItem::getTotalPrice).sum();
    }

}
