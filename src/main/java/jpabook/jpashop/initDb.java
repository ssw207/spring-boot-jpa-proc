package jpabook.jpashop;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Book;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

/**
 * 총 주문2개
 *
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class initDb {

    private final InitService initService;

    @PostConstruct // Bean이 다 실행된 뒤에 호출됨
    public void init() {
        initService.dbInit1();
    }


    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {

        private final EntityManager em;

        public void dbInit1() {
            Member member =new Member();
            member.setName("userA");
            member.setAddress(new Address("서울","1","11111"));
            em.persist(member);

            Book book1 = new Book();
            book1.setName("JPA BOOK");
            book1.setStockQuantity(1000);
            book1.setPrice(1000);
            em.persist(book1);

            Book book2 = new Book();
            book2.setName("JPA BOOK");
            book2.setStockQuantity(1000);
            book2.setPrice(1000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = new Delivery();
            delivery.setAddress(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }
    }
}
