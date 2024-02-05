package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@Slf4j
@RequiredArgsConstructor
public class HomeController {

    private final MemberRepository memberRepository;

    @RequestMapping("/")
    public String home(Model model, Authentication auth){
        log.info("home controller");

        if(auth != null){
            Optional<Member> member = memberRepository.findByEmail(auth.getName());
            if (member != null) {
                model.addAttribute("nickname", member.get().getEmail());
            }
        }
        return "home";
    }
}
