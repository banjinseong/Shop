package jpabook.jpashop.controller;


import jakarta.validation.Valid;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.MemberDTO;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 세션 띄어서 admin권한 확인 하기
 */
@Controller
@RequiredArgsConstructor
@RequestMapping("member/*")
public class MemberController {

    private final MemberService memberService;

    /**
     * 회원가입 페이지 진입
     */
    @GetMapping("join")
    public String joinGet(Model model){
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/join";
    }

    /**
     *회원가입시
     */
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

    /**
     *회원 목록 페이지(관리자만 들어가게끔)
     */
    @GetMapping("/list")
    public String memberList(Model model){
        List<Member> members = memberService.findMembers();
        model.addAttribute("members", members);
        return "member/list";
    }

    /**
     *로그인 페이지 진입(로그인 post는 시큐리티가 처리, 로그아웃도)
     */
    @GetMapping("/login")
    public String loginPage(Model model) {
        model.addAttribute("memberDTO", new MemberDTO());
        return "member/login";
    }
    /**
     * 내 정보 페이지 진입
     */
    @GetMapping("myPage")
    public String getMyPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                            Model model){
        String email = principalDetails.getUsername();
        MemberDTO memberDTO = memberService.findOne(email);
        Long memberId = memberService.getId(email);
        model.addAttribute("memberDTO", memberDTO);
        model.addAttribute("memberId", memberId);
        return "member/myPage";
    }

    @PostMapping("/myPage/edit")
    public String postMyPage(@ModelAttribute("memberDTO") MemberDTO dto){
        memberService.updateMember(dto);
        return "redirect:/member/myPage";
    }
    /**
     * 관리자 페이지 진입
     */
    @GetMapping("adminPage")
    public String getAdminPage(){
        return "member/adminPage";
    }
}
