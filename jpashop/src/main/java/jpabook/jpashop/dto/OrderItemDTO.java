package jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class OrderItemDTO {
    private Long itemId;
    private String name;
    private int price;
    private int count;



    private List<OrderItemDTO> orderItemList;
}
