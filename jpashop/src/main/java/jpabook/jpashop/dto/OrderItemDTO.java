package jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class OrderItemDTO {
    private Long itemId;
    private int price;
    private int count;


    public OrderItemDTO(Long itemId, int price, int count){
        this.itemId = itemId;
        this.price = price;
        this.count = count;
    }
}
