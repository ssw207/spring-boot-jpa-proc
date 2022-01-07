package jpabook.jpashop;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringRunner.class) // Junit에게 SpringBoot 관련 테스트를 한다고 알려줌
@SpringBootTest
public class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;

    @Test
    /*
    - EntityManager는 Transaction 안에서만 동작하므로 이 어노테이션이 없다면 동작하지 않는다.
    - 테스트케이스에에 Transactional 어노테이션이 있다면 테스트가 종료된뒤에 롤백처리되기 때문에 DB에 저장되지 않는다.
     */
    @Transactional
    @Rollback(false) //@Transactional어노테이션이 있지만 DB에 저장하고 싶은경우 @Rollback(false)를 추가하면 DB에 저장된다
    public void testMember() throws Exception {
        //given
        Member member = new Member();
        member.setUsername("memberA");

        //when
        Long saveId = memberRepository.save(member);
        Member findMember = memberRepository.find(saveId);

        //then
        Assertions.assertThat(findMember.getId()).isEqualTo(member.getId());
        Assertions.assertThat(findMember.getUsername()).isEqualTo(member.getUsername());

        /**
         * 같은 트렌젝션안에서 저장,조회시 영속성 컨테이너 가 같음
         * 같은 영속성 컨테이너에서 ID값이 같으면 같은 Entity로 식별됨
         */
        Assertions.assertThat(findMember).isEqualTo(member); //저장, 조회한 객체가 같은지 검증
    }
}