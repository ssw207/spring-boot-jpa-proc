package jpabook.jpashop.controller;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberForm {
    /**
     * Controller의 커맨트객체로 MemberForm 을 받을때 @Valid MemberForm 형태로 입력시
     * 아래 @NotEmpty가 동작해서 입력값이 없는지 체크하고 없으면 에러를 리턴한다.
     */
    @NotEmpty(message = "회원 이름은 필수 입니다.")
    private String name;

    private String city;
    private String street;
    private String zipcode;
}
