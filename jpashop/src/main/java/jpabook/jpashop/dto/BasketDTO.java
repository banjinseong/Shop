package jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class BasketDTO {
    private Long itemId;
    private String name;
    private int count;
}
