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

    @ManyToOne //여러개의 주문가 하나의 회원을 가진다
    @JoinColumn(name = "member_id") // join을 어떤 컬럼으로 할거냐? fk 컬럼명이됨
    private Member member;

    @OneToMany(mappedBy = "order") // 연관관계의 주인은 order
    private List<OderItem> orderItems = new ArrayList<>();

    @OneToOne // 1:1인경우 fk는 어디에 둬도 상관없으나 많이 사용하는쪽에 추가하는게 좋다.
    @JoinColumn(name = "delivery_id") // join을 어떤 컬럼으로 할거냐? fk 컬럼명이됨
    private Delivery delivery;

    private LocalDateTime orderDate; //주문시간, 로컬데이트 타임을쓰면 자바8에서 자동으로 읽기때문에 @ 필요없다

    private OrderStatus status; // 주문상태 [ORDER, CANCEL]
}
