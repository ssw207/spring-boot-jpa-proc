package jpabook.jpashop.repository.order.simplequery;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;


/**
 * 레파지토리는 순수한 엔티티를 조회하는데사용해야하므로 화면과 의존성을 가지지 않기 위해 분리
 *
 */
@Repository
@RequiredArgsConstructor
public class OrderSimpleQueryRepository {

    private EntityManager em;

    public List<OrderSimpleQueryDto> findOrderDtos() {
        return em.createQuery(
                "select new jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto(o.id, m.name, o.orderDate, o.status, d.address)" +
                        " from Order o" +
                        " join o.member m " +
                        " join o.delivery d", OrderSimpleQueryDto.class
        ).getResultList();
    }
}

