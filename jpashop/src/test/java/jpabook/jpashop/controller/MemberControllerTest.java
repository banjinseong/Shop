package jpabook.jpashop.controller;

import jakarta.transaction.Transactional;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.ItemImage;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.dto.MemberDTO;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.service.MemberService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.multipart.MultipartFile;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class MemberControllerTest {

    @Autowired
    public MemberService memberService;

    @Autowired
    public MemberRepository memberRepository;
    @Test
    public void 멤버정보업데이트() throws Exception {
        //given
        Member member = new Member("12", "12", new Address("12", "12", "12"));
        memberRepository.save(member);
        MemberDTO dto = new MemberDTO();
        dto.setCity("15");
        dto.setStreet("15");
        dto.setZipcode("15");
        //when
        memberService.updateMember(dto);
        //then
        Assert.assertEquals(member.getAddress().getCity(),"12");
    }

}