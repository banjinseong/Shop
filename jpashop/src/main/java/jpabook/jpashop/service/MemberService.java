package jpabook.jpashop.service;

import jpabook.jpashop.domain.Basket;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.MemberDTO;
import jpabook.jpashop.repository.BasketRepository;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final BasketRepository basketRepository;

    @Transactional
    public Long join(MemberDTO dto){
        Member member = dto.toEntity(passwordEncoder);
        validateDuplicateMember(member);
        Basket basket = Basket.createBasket(member);
        basketRepository.save(basket);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByEmail(member.getEmail()).ifPresent(member1 ->{
               throw new IllegalStateException("이미 존재하는 이메일입니다.");
        });
    }


    public List<Member> findMembers(){
        return memberRepository.findAll();
    }



}
