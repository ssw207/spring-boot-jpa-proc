package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/members/new")
    public String createForm(Model model) {
        //validateion 문제로 빈 껍데기라도 들고감
       model.addAttribute("memberForm", new MemberForm());
       return "members/createMemberForm";
    }

    @PostMapping("/members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        Address address = new Address(form.getCity(), form.getStreet(), form.getZipcode());

        //MemberForm에 에러가 있으면
        if (result.hasErrors()) {
            return "members/createMemberForm";
        }

        Member member = new Member();
        member.setName(form.getName());
        member.setAddress(address);

        memberService.join(member);
        return "redirect:/"; //첫페이지로 리다이렉트
    }

    @GetMapping("/members")
    public String list(Model model) {

        /**
         * API를 만들때는 절대 entity를 넘기면 안됨
         * 1.API는 스팩인데 엔티티에 필드가 추가되면 API 스팩이 변하게됨
         * 2.민감성 데이터가 노출됨
         */
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "members/memberList";
    }
}
