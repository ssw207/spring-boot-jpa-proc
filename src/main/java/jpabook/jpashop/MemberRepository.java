package jpabook.jpashop;

import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class MemberRepository {

    @PersistenceContext // spring-boot-starter-jpa를 통해 의존성이 추가됨
    private EntityManager em;

    public Long save(Member member) {
        em.persist(member); // 커맨드성이기 때문에 리턴값으로 사용하지 않음
        return  member.getId(); // id만 반환하는 이유 => 커맨드와 쿼리를 분리하라는 원칙
    }

    public Member find(Long id) {
        return em.find(Member.class, id);
    }
}
