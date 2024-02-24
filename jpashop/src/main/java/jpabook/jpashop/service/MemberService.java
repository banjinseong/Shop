package jpabook.jpashop.service;

import jpabook.jpashop.domain.Address;
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

    @Transactional
    public void updateMember(MemberDTO dto){
        Member member = memberRepository.findByEmail(dto.getEmail()).get();
        member.setAddress(new Address(dto.getCity(), dto.getStreet(), dto.getZipcode()));
    }


    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    public MemberDTO findOne(String email){
        Member member = memberRepository.findByEmail(email).get();
        MemberDTO memberDTO = new MemberDTO();
        memberDTO.setEmail(member.getEmail());
        memberDTO.setCity(member.getAddress().getCity());
        memberDTO.setStreet(member.getAddress().getStreet());
        memberDTO.setZipcode(member.getAddress().getZipcode());
        return memberDTO;
    }

    public Long getId(String email) {
        Member member = memberRepository.findByEmail(email).get();

        return member.getId();
    }
}
