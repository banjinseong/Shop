package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "orders")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order {
    @Id
    @GeneratedValue
    @Column(name = "order_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING)
    private OrderStatus status; //주문상태 [ORDER, CANCEL]

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrdersItem> ordersItems = new ArrayList<>();

    /**
     * 연관관계 메서드
     */
    public void setMember(Member member){
        this.member = member;
        member.getOrders().add(this);
    }

    public void addOrderItem(OrdersItem ordersItem){
        ordersItems.add(ordersItem);
        ordersItem.setOrder(this);
    }

    public void setDelivery(Delivery delivery){
        this.delivery = delivery;
        delivery.setOrder(this);
    }

    /**
     * 생성 메서드
     */
    public static Order createOrder(Member member, Delivery delivery,  List<OrdersItem> ordersItems){
        Order order = new Order();
        order.setMember(member);
        order.setDelivery(delivery);
        for (OrdersItem orderItem: ordersItems) {
            order.addOrderItem(orderItem);
        }
        order.setStatus(OrderStatus.ORDER);
        order.setOrderDate(LocalDateTime.now());
        return order;
    }

    /**
     * 주문 취소
     */
    public void cancel(){
        if(delivery.getStatus() == DeliveryStatus.COMP){
            throw new IllegalStateException("이미 배송이 완료된 상품은 취소가 불가능합니다");
        }

        this.setStatus(OrderStatus.CANCEL);
        for (OrdersItem ordersItem : ordersItems){
            ordersItem.cancel();
        }

    }

    /**
     * 배송 완료 변환
     */
    public void complete() {
        this.delivery.setStatus(DeliveryStatus.COMP);
        System.out.println("****"+this.delivery.getStatus());
    }

    /**
     * 전체 주문 가격 조회
     */
    public int getTotalPrice(){
        int totalPrice = 0;
        for(OrdersItem ordersItem : ordersItems){
            totalPrice += ordersItem.getTotalPrice();
        }
        return totalPrice;
    }


}
