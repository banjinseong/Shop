package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.dto.OrderItemDTOList;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("order/*")
public class OrderController {

    private final OrderService orderService;
    private final ItemService itemService;

    /**
     * 아이템 정보를 가변적으로 받아야하는데... 어떻게 받아오지
     * dto로 받아와서 dto로 보여줄거
     * post로 받아오자
     */
    @PostMapping("orderInsert")
    public String createForm(Model model,@ModelAttribute OrderItemDTO orderItems){

        model.addAttribute("items",orderItems);
        return "order/orderForm";
    }

    /**
     * 나중에 폼을 바꿔서 받는 변수를 달리할거니 일단 더러워
     * 수량 오류 터지는것도 try catch써서 잡기
     * 오더 서비스 long으로 받아서 url에 번호 넣고 주문 내역 보여주는 페이지로 이동하자
     */
    @PostMapping("orderForm")
    public String order(@AuthenticationPrincipal PrincipalDetails principalDetails,
                        @ModelAttribute("items") OrderItemDTOList orderItems)
    {
        String email = principalDetails.getUsername();// 회원의 이메일을 가져온다.
        List<OrderItemDTO> dto = orderItems.getItems();
        orderService.order(email, dto);
        return "redirect:/";
    }

    @GetMapping("orderList")
    public String getList(@ModelAttribute("orderSearch")OrderSearch orderSearchModel,
                          Model model){
        List<Order> orders = orderService.findAllByString(orderSearchModel);
        model.addAttribute("orders", orders);
        return "order/list";
    }

    @PostMapping(value = "/{orderId}/cancel")
    public String cancelOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/order/orderList";
    }

}
