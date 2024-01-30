package jpabook.jpashop.domain.item;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Clothes extends Item{

    private String size;
    private String wear;
}
