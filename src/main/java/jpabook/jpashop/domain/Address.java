package jpabook.jpashop.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

@Embeddable // 어디에 내장될수 있다
@Getter // 값타입은 변경되선 안되므로 Setter는 추가하지 않는다.
public class Address {
    private String city;
    private String street;
    private String zipcode;

    /**
     * JPA구현 라이브러리가 객체를 생성할때 리플랙션,프록시를 사용할수 있도록 지원해 줘야하는데 기본생성자가 없으면 사용이 불가능하다.
     * JPA스팩상 기본생성자는 public 또는 protected로 설정해야한다
     * public 보다는 protected가 안전하므로 protected로 설정한다.
     */
    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
