package jpabook.jpashop.domain.item;

import jakarta.persistence.Entity;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Clothes extends Item{

    private String size;
    private String wear;

    protected Clothes(){}
    @Builder
    public Clothes(String name, int price, int stockQuantity, String size, String wear) {
        super(name, price, stockQuantity);
        this.size = size;
        this.wear = wear;

    }
}
