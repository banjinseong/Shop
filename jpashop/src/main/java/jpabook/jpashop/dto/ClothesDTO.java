package jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ClothesDTO {

    private Long id;

    private String name;
    private int price;
    private int stockQuantity;

    private String size;
    private String wear;
}
