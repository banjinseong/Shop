package jpabook.jpashop.dto;

import jakarta.validation.constraints.NotEmpty;
import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter @Setter
public class MemberDTO {

    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    private String password;

    private String city;
    private String street;
    private String zipcode;


    public Member toEntity(PasswordEncoder passwordEncoder){
        return Member.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .address(new Address(city,street,zipcode))
                .build();
    }
}
