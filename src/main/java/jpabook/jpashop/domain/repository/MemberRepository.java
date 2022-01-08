package jpabook.jpashop.domain.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * @Repository 어노테이션은 내부에 @Component 어노테이션을 가지고 있는데
 * SpringBoot는 @SpringBootApplication 어노테이션이 있는 클래스의 패키지 하위경로의 @Component 어노테이션이 있는 클래스를 스캔해
 * SpringBean으로 등록한다.
 */
@Repository
public class MemberRepository {

    @PersistenceContext
    private EntityManager em; //Spring이 EntityManager을 만들어 주입

    public void save(Member member) {
        /**
         * persist시 영속성 컨텍스트에 입력한 객체를 넣고
         * 트렉젝션이 종료되는 시점에 DB에 반영한다.
         */
        em.persist(member);
    }

    public Member findOne (Long id) {
        /**
         * PK로 단건조회
         */
        return em.find(Member.class, id);
    }

    public List<Member> findAll () {
        //JPQL에서 from은 테이블이 아니라 엔티티
        return em.createQuery("select m from Member m", Member.class) // ...(JPQL, 반환타입)
                .getResultList(); // 맴버를 리스트로 변환
    }

    public List<Member> findByName(String name) {
        /**
         * JQPL의 파라미터는 ":변수명"으로 입력하고
         * setParameter("변수명",값) 으로 값을 전달한다.
         */
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name) // JQPL 파라미터 "name"으로 name변수를 전달
                .getResultList(); //결과를 List로 변환
    }
}

