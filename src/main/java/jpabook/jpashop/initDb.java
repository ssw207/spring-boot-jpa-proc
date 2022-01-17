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
            Member member = createMember("서울", "1", "11111", zipcode);
            em.persist(member);

            Book book1 = createBook("JPA BOOK", 1000, 1000);
            em.persist(book1);

            Book book2 = createBook("JPA BOOK", 1000, 1000);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 10000, 1);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);

            Delivery delivery = createDelivery(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Member createMember(String name, String city, String streat, String zipcode) {
            Member member = new Member();
            member.setName("userA");
            member.setAddress(new Address(city,streat,zipcode));
            return member;
        }

        public void dbInit2() {
            Member member = createMember("userB", "진주", "11111", "11111");
            em.persist(member);

            Book book1 = createBook("SPRING BOOK", 20000, 200);
            em.persist(book1);

            Book book2 = createBook("SPRING BOOK2", 40000, 300);
            em.persist(book2);

            OrderItem orderItem1 = OrderItem.createOrderItem(book1, 20000, 3);
            OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);

            Delivery delivery = createDelivery(member.getAddress());
            Order order = Order.createOrder(member, delivery, orderItem1, orderItem2);
            em.persist(order);
        }

        private Delivery createDelivery(Address address1) {
            Delivery delivery = new Delivery();
            delivery.setAddress(address1);
            return delivery;
        }

        private Book createBook(String jpa_book, int stockQuantity, int price) {
            Book book1 = new Book();
            book1.setName(jpa_book);
            book1.setStockQuantity(stockQuantity);
            book1.setPrice(price);
            return book1;
        }
    }
}
