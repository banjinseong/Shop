package jpabook.jpashop.domain.item;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Category;
import jpabook.jpashop.exception.NotEnoughStockException;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
public abstract class Item {
    @Id
    @GeneratedValue
    @Column(name = "item_id")
    private Long id;

    private String name; //상품명
    private int stockQuantity; //상품수량
    private int price; //상품가격

    private LocalDateTime created_date; //등록날짜
    private LocalDateTime modified_date; //수정날짜

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    protected Item(){}

    public Item(String name, int price, int stockQuantity){
        this.name = name;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.created_date = LocalDateTime.now();
    }

    public void addCount(int quantity){
        this.stockQuantity += quantity;
    }

    public void removeCount(int quantity){
        int restCount = this.stockQuantity - quantity;
        if(restCount < 0){
            throw new NotEnoughStockException("재고 부족");
        }
        this.stockQuantity -= quantity;
    }
}
