package jpabook.jpashop.domain;

import jakarta.persistence.*;
import jpabook.jpashop.domain.item.Item;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OrdersItem {
    @Id
    @GeneratedValue
    @Column(name = "order_item_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    private int orderPrice;
    private int count;

    /**
     * 생성 메서드
     */
    public static OrdersItem createOrdersItem(Item item, int order_price, int count){
        OrdersItem ordersItem = new OrdersItem();
        ordersItem.setItem(item);
        ordersItem.setOrderPrice(order_price);
        ordersItem.setCount(count);

        item.removeCount(count);
        return ordersItem;
    }

    /**
     * 주문 취소
     */
    public void cancel() {
        getItem().addCount(count);
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        return getOrderPrice() * getCount();
    }
}
