package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /*
    v1 버전 문제점
    1)엔티티의 정보가 모두 노출됨 - 원치않는 orders 정보도 도출된다
      ->  사용시 원하지 않는 필드는 제외하고 표현가능
      -> but 엔티티에 화면관련 로직이 들어옴
    2)엔티티가 바뀌면 api스팩이 바뀜 
    3)컬랙션 리턴시 스팩 확장이 어려움 
      - 리턴시 array이나 데이터와 별개로 갯수를 보내줘야하는경우 대응불가
     */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1 () {
        return memberService.findMembers();
    }

    /*
    v2 버전 장점
    - 엔티티가 변해도 api 스팩이 변하지 않는다
    - api 스팩이변해도 유연하게 대응가능
     */
    @GetMapping("/api/v2/members")
    public Result memberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> collect = findMembers.stream()
                .map(m -> new MemberDto(m.getName()))
                .collect(Collectors.toList());
        return new Result(collect);
    }

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

    @Data
    @AllArgsConstructor
    static class Result<T> {
        private T data;
    }

    @Data
    @AllArgsConstructor
    static class MemberDto {
        private String name;
    }
}
