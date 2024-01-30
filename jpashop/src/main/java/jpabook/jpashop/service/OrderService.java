package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ItemRepository itemRepository;

    /**
     * 주문
     */
    @Transactional
    public Long order(Long memberId, List<OrderItemDTO> orderItemDTOS){
        //엔티티 조회
        Member member = memberRepository.findOne(memberId);

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        List<OrdersItem> ordersItems = new ArrayList<>();
        for(OrderItemDTO orderItemDTO : orderItemDTOS){
            Item item = itemRepository.findOne(orderItemDTO.getItemId());
            int price = orderItemDTO.getPrice();
            int count = orderItemDTO.getCount();

            OrdersItem ordersItem = OrdersItem.createOrdersItem(item, price, count);
            ordersItems.add(ordersItem);
        }

        //주문 생성
        Order order = Order.createOrder(member, delivery, ordersItems);

        //주문 저장
        orderRepository.save(order);
        return order.getId();
    }


    /**
     * 취소
     */

    @Transactional
    public void cancelOrder(Long orderId){
        //주문 엔티티 조회
        Order order = orderRepository.findOne(orderId);
        // 주문 취소
        order.cancel();

    }

    /**
     * 검색
     */
    /*
    public List<Order> findOrders(OrderSearch orderSearch){
        return orderSearch.findAll(orderSearch)
    }*/
}
