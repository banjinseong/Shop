package jpabook.jpashop.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class MemberDTO {

    @NotEmpty(message = "이메일을 입력해주세요.")
    private String email;

    private String password;
    private String city;
    private String street;
    private String zipcode;
}
