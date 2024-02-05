package jpabook.jpashop.dto;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
public class ClothesDTO {

    private String name;
    private int price;
    private int stockQuantity;

    private String size;
    private String wear;

    public Clothes toEntity(){
        return Clothes.builder()
                .name(name)
                .price(price)
                .stockQuantity(stockQuantity)
                .size(size)
                .wear(wear)
                .build();
    }
}
