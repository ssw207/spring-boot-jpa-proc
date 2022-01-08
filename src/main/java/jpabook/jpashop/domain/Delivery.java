package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
public class Delivery {
    @Id @GeneratedValue
    @Column(name = "delivery_id")
    private Long id;

    @OneToOne(mappedBy = "delivery") // 1:1 관계는 join 컬럼 설정하지 않아도됨
    private Order order;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING) // ORDINAL은 순서대로 숫자로 들어감(디폴트). 중간에 순서가 바뀌면 데이터가 꼬이므로 절대 쓰면안됨
    private DeliveryStatus status; //READY, COMP
}
