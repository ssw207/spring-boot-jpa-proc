package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.MemberRepositoryOld;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * JPA는 트렌젝션 안에서 동작해야하므로 트렌젝션 어노테이션이 필수적이다.
 * @Transactional은 Spring에서 제공하는걸 추천
 * readOnly = true인 경우 JPA가 조회하는곳에는 성능 최적화를 해준다.
 */
@Service
@Transactional(readOnly = true) // @Transactiona 어노테이션이 없는경우 적용됨
@RequiredArgsConstructor // final이 있는 필드만 생성자로 생성해준다.
public class MemberService {

    /**
     * 컴파일 시점에 주입되지 않으면 컴파일 에러가 나기때문에 final을 입력
     */
    private final MemberRepository memberRepository;

    /**
     * 생성자 주입방식
     * 스프링컨테이너가 실행될때 주입된다.
     * 가장권장하는 방식 (필드주입은 테스트시 mock객체가 주입하기 힘듬, setter주입 런타임에 변경도리수 있음)
     * @Autowired 어노테이션이 없어서 스프링이 자동으로 주입해줌 (생성자가 하나인경우)

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
     */

    /**
     * 회원가입
     */
    @Transactional(readOnly = false) // 쓰기인 경우에는 따로 체크를해준다.
    public Long join(Member member) {
        validateDuplicateMemeber(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMemeber(Member member) {
        //문제가 있으면 에러를 터트린다.
        //멀티쓰레드 환경에서 동일한 이름으로 저장요청이 들어온 경우 검중을 통과하기 때문에 DB레벨에서 제약조건을 추가하는게 좋다.
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    public Member findOne(Long memberId) {
        return memberRepository.findById(memberId).get();
    }

    @Transactional
    public Long update(Long id, String name) {
        //영속화
        Member member = memberRepository.findById(id).get();
        //더티체킹
        member.setName(name);

        //AOP종료시점 commit -> flush() 변경점 동기화 -> SQL날림

        //member를 반환하면 영속후에 조회를함 (영속, 쿼리를 같이함)
        return member.getId();
    }
}

