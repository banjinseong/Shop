package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.aspectj.weaver.ast.Or;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * 회원 못찾았을때 오류 처리해야함
 */
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
    public Long order(String email, List<OrderItemDTO> orderItemDTOS){
        //엔티티 조회
        Member member = memberRepository.findByEmail(email).get();

        //배송정보 생성
        Delivery delivery = new Delivery();
        delivery.setAddress(member.getAddress());
        delivery.setStatus(DeliveryStatus.READY);

        //주문상품 생성
        List<OrdersItem> ordersItems = new ArrayList<>();
        for(OrderItemDTO orderItemDTO : orderItemDTOS){
            Item item = itemRepository.findById(orderItemDTO.getItemId()).get();
            int count = orderItemDTO.getCount();
            int price = orderItemDTO.getPrice();

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
        Optional<Order> order = orderRepository.findById(orderId);
        // 주문 취소
        order.get().cancel();

    }

    /**
     * 검색
     */

    public List<Order> findAllByString(OrderSearch orderSearch) {
        return orderRepository.findByEmailAndStatus(orderSearch.getMemberEmail(),orderSearch.getOrderStatus());
    }

    public Order findById(Long id){
        return orderRepository.findById(id).get();
    }

    public List<Order> findByMemberId(Long memberId) {
        return orderRepository.findByMemberId(memberId);
    }
}
