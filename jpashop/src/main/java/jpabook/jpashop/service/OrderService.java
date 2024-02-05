package jpabook.jpashop.service;

import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Long order(Long memberId, List<OrderItemDTO> orderItemDTOS){
        //엔티티 조회
        Optional<Member> optionalMember = memberRepository.findById(memberId);

        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            // 아이디가 memberId에 해당하는 회원을 찾았을 때 수행할 작업

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
        } else {
            return 1l;
            // 아이디가 memberId에 해당하는 회원을 찾지 못했을 때 수행할 작업
            // 예를 들어, 예외를 발생시키거나 메시지를 출력하고 컨트롤러로 이동하는 등의 작업을 수행할 수 있습니다.
        }
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

//    public List<Order> findOrders(OrderSearch orderSearch){
//        return orderRepository.find
//    }
}
