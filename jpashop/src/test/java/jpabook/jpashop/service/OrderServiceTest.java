package jpabook.jpashop.service;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.*;
import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.repository.OrderRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
@Transactional
public class OrderServiceTest {

    @Autowired
    EntityManager em;
    @Autowired OrderService orderService;
    List<OrderItemDTO> orderItemDTOS = new ArrayList<>();
    @Autowired
    OrderRepository orderRepository;

    @Test
    public void 상품주문() throws Exception {
        //given
        Member member = createMember2();

        Item item1 = createItem();

        Item item2 = new Clothes();
        item2.setName("옷2");
        item2.setPrice(20000);
        item2.setStockQuantity(20);
        em.persist(item2);


        orderItemDTOS.add(new OrderItemDTO(item1.getId(),item1.getPrice(), 2));
        orderItemDTOS.add(new OrderItemDTO(item2.getId(),item2.getPrice(), 4));
        //when
        Long orderId = orderService.order(member.getId(), orderItemDTOS);
        //then

        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("상품 주문시 상태는 ORDER", OrderStatus.ORDER, getOrder.getStatus());
        assertEquals("주문한 상품 종류 수가 정확해야 한다.", 2, getOrder.getOrdersItems().size());
        assertEquals("주문 가격은 얼마이다", 100000, getOrder.getTotalPrice());
        assertEquals("재고가 줄어야만 한다 아이템 1", 8, item1.getStockQuantity());
        assertEquals("재고가 줄어야만 한다 아이템 2", 16, item2.getStockQuantity());
    }




    @Test
    public void 주문취소() throws Exception {
        //given
        Member member = createMember2();
        Item item = createItem();

        orderItemDTOS.add(new OrderItemDTO(item.getId(),item.getPrice(),2));
        Long orderId = orderService.order(member.getId(), orderItemDTOS);
        //when
        orderService.cancelOrder(orderId);

        //then
        Order getOrder = orderRepository.findOne(orderId);

        assertEquals("주문 취소시 상태는 cancel이다", OrderStatus.CANCEL, getOrder.getStatus());
        assertEquals("재고는 그대로여야한다.", 10, item.getStockQuantity());

    }


    @Test(expected = NotEnoughStockException.class)
    public void 상품주문_재고수량초과() throws Exception {
        //given
        Member member = createMember2();
        System.out.println(member.getId());
        Item item = createItem();
        orderItemDTOS.add(new OrderItemDTO(item.getId(),item.getPrice(), 11));
        //when
        Long orderId = orderService.order(member.getId(), orderItemDTOS);
        //then
        fail("재고 ㅜ량 부족 예외가 터져야한다");
    }

    private Item createItem() {
        Item item1 = new Clothes();
        item1.setName("옷1");
        item1.setPrice(10000);
        item1.setStockQuantity(10);
        em.persist(item1);
        return item1;
    }

    private Member createMember2() {
        Member member = new Member();
        member.setEmail("ps123");
        member.setAddress(new Address("서울", "강가", "123-124"));
        em.persist(member);
        return member;
    }

}