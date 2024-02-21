package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.boot.autoconfigure.elasticsearch.ElasticsearchConnectionDetails;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
//@NoArgsConstructor(access = AccessLevel.PROTECTED) 임의로 생성 못하게 막기
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long id;

    private String email;

    private String password;

    @Embedded
    private Address address;

    @Enumerated(EnumType.STRING)
    private UserRole userRole;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>();

    @OneToOne(mappedBy = "member")
    private Basket baskets;

    protected Member(){}
    @Builder
    public Member(String email, String password, Address address){
        this.email = email;
        this.password = password;
        this.userRole = UserRole.ROLE_USER;
        this.address = address;
    }
}
