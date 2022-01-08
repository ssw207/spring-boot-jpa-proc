package jpabook.jpashop.domain;

import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
public class Category {

    @Id @GeneratedValue
    private Long id;

    private String name;

    @ManyToMany
    @JoinTable(name = "category_item", // 1:M , M:1로 풀어내는 중간 테이블 필요
            joinColumns = @JoinColumn(name = "category_id"),
            inverseJoinColumns = @JoinColumn(name = "item_id")) //category_item 테이블에 item에 들어가는 컬럼
    private List<Item> items = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parent;

    @OneToMany(mappedBy = "parent") // parent 객체와 self 조인
    private List<Category> child = new ArrayList<>();

    /**
     * 연관관게 편의 메서드 (양방할일때 사용)
     *
     * child와 parent는 양방향 관계이므로 child에 Category가 추가되면 parent에도 동일하게 추가되야한다.
     *
     */
    public void addChildCategory(Category child) {
        this.child.add(child);
        child.setParent(this);
    }
}