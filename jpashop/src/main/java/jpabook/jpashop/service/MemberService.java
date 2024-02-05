package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.MemberDTO;
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

    @Transactional
    public Long join(MemberDTO dto){
        validateDuplicateMember(dto.toEntity(passwordEncoder));
        memberRepository.save(dto.toEntity(passwordEncoder));
        return dto.toEntity(passwordEncoder).getId();
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
