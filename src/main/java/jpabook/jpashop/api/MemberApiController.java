package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {

        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemeberV2 (
            @PathVariable("id") Long id,
            @RequestBody @Valid UpdateMemberRequest request) {

        /*
            성능상 크리티컬하지 않다면 커맨드(수정,삭제,생성등)와 쿼리(조회)를 분리하면 유지보수정이 좋아짐. (단일책임원칙)
        */
        memberService.update(id, request.getName()); // 커맨드
        Member findMember = memberService.findOne(id); // 쿼리
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());
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

    @Data
    static class UpdateMemberRequest {
        private String name;
    }

    @Data
    @AllArgsConstructor
    static class UpdateMemberResponse {
        private Long id;
        private String name;
    }
}
