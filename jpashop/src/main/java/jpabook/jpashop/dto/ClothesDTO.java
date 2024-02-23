package jpabook.jpashop.dto;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.domain.item.ItemImage;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter @Getter
public class ClothesDTO {

    private String name;
    private int price;
    private int stockQuantity;

    private String size;
    private String wear;

    private List<ItemImage> images;

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
