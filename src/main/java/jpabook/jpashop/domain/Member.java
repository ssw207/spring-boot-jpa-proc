package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Member {

    @Id @GeneratedValue
    @Column(name = "member_id") // 컬럼명지정
    private Long id;

    @NotEmpty
    private String name;

    @Embedded // 엔티티가 추가된걸 표시함
    private Address address;

    /*
    - 하나의 회원은 여러개의 상품들을 가질수 있다.
    - order의 member애 맵핑된 정보. 
    - 읽기전용이며 mebmer가 연관관계 주인이다. why 테이블 관게에서 fk는 order에 존재함
    - member정보를 바꿨는데 order의 fk가 변경되는것보다 order의 정보를바꾸니까 order의 fk가 바뀌는게 자연스러움
    - 컬랙션은 변수선언광
     */
    @OneToMany(mappedBy = "member") // 
    private List<Order> orders = new ArrayList<>();
}
