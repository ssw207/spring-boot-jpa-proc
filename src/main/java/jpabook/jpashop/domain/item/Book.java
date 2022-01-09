package jpabook.jpashop.domain.item;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("B") // Item의 dtype 컬럼값 결정
@Getter @Setter
public class Book extends Item {

    private String author;
    private String isbn;

}
