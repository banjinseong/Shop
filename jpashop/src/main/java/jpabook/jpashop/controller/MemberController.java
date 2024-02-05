package jpabook.jpashop.controller;


import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.MemberDTO;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

/**
 * 세션 띄어서 admin권한 확인 하기
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("member/*")
public class MemberController {

    private final MemberService memberService;

    @GetMapping("join")
    public String joinGet(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/join";
    }

    @PostMapping("join")
    public String joinPost(Model model, @Valid MemberDTO dto, BindingResult result){


        try {
            if(result.hasErrors()){
                return "member/join";
            }

            memberService.join(dto);

            return "redirect:/";
        } catch (IllegalStateException e) {
            model.addAttribute("errorMessage", e.getMessage()); // 예외 메시지를 모델에 추가
            return "member/join"; // 회원가입 폼 페이지로 이동
        }

    }

    @GetMapping("/list")
    public String memberList(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "member/list";
    }

    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/login";
    }
}
