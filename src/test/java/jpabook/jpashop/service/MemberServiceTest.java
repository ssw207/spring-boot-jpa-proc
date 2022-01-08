package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class) //Spring 을 사용해 테스트하기위함
@SpringBootTest //Spring 을 사용해 테스트하기위함
@Transactional //롤백처리를 위함
public class MemberServiceTest {

    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;
    @Autowired EntityManager em;

    @Test
    //@Rollback(false)
    public void 회원가입() throws Exception {
        //given
        Member member = new Member();
        member.setName("kim");

        //when
        Long savedId = memberService.join(member); //트렌젝션이 커밋될때 insert 쿼리가 나가므로 @Rollback 어노테이션이 없으면 insert쿼리가 안나감

        //then
        //em.flush(); // 영속성 컨테이너에서 강제로 flush()를 하면 쿼리가 날라감.

        assertEquals(member, memberRepository.findOne(savedId)); // member와 조회한게 같은가? pk 같을때 같은 영속성 컨테이너에서하는 하나로 관리하기때문에 같다
    }

    /**
     * @Test(expected = IllegalStateException.class) 어노테이션은 아래의 소스를 간략화 해준다.
     *
     *         try {
     *             memberService.join(member1);
     *             memberService.join(member2);
     *         } catch (IllegalStateException e) {
     *             //에러가 발생시 리턴되면서 테스트가 성공
     *             return;
     *         }
     *
     */
    @Test(expected = IllegalStateException.class)
    public void 중복_회원_예외() throws Exception {
        //given
        Member member1 = new Member();
        member1.setName("kim");

        Member member2 = new Member();
        member2.setName("kim");

        //when
        memberService.join(member1);
        memberService.join(member2);

        //then
        fail("예외가 발생해야 한다."); // 여기라일이 실행되면 테스트가 실패함
    }
}