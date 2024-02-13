package jpabook.jpashop.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
public class OrderItemDTOList {
    private List<OrderItemDTO> items;

    public OrderItemDTOList(){
        items = new ArrayList<>();
    }
}
