package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /*
    api v1
    문제1 : 프레젠테이션 검증로직이 엔티티에 들어가있다.
    문제2 : 엔티티를 바뀌면 api스팩이 바뀐다. -> api 스팩을 위한 별도의 dto가 필요하다. (매우큰 장애요인)
    문제3 : 회원가입 방식은 여러개일수 있음 -> 엔티티 하나로 대응불가
     */
    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /*
    v2
    - 엔티티가 변해도 api스팩이 변하지 않는다.
    - CreateMemberRequest를 보면 api스팩 확인가능
     */
    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @Data
    static class CreateMemberResponse {
        private Long id;

        public CreateMemberResponse(Long id) {
            this.id = id;
        }
    }

    @Data
    static class CreateMemberRequest {
        private String name;
    }
}
