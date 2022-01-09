package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Book;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.persistence.EntityManager;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ItemUpdateTest {
    @Autowired
    EntityManager em;
    
    @Test
    public void 테스트() throws Exception {
        Book book = em.find(Book.class, 1L);

        //TX
        book.setName("이름");

        //변경감지 == 더티체킹
        //TX commit
    }
}
