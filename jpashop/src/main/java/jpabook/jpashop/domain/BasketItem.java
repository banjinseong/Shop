package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class BasketItem {
    @Id
    @GeneratedValue
    @Column(name = "basket_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    private int count;

    public static BasketItem createBasketItem(Basket basket, Item item, int count){
        BasketItem basketItem = new BasketItem();
        basketItem.setBasket(basket);
        basketItem.item = item;
        basketItem.count = count;
        return basketItem;
    }

    public void addCount(int count){
        this.count += count;
    }

}
