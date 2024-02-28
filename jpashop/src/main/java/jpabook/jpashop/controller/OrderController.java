package jpabook.jpashop.controller;


import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.dto.OrderItemDTOList;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.BasketService;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("order/*")
public class OrderController {

    private final OrderService orderService;
    private final BasketService basketService;

    /**
     *아이템 주문페이지에서 주문 확인 페이지로 넘어옴
     */
    @PostMapping("orderInsert")
    public String createForm(Model model,@ModelAttribute OrderItemDTO orderItems){

        model.addAttribute("items",orderItems);
        return "order/orderForm";
    }

    /**
     * 나중에 폼을 바꿔서 받는 변수를 달리할거니 일단 더러워
     * 수량 오류 터지는것도 try catch써서 잡기
     */
    @PostMapping("orderForm")
    public String order(@AuthenticationPrincipal PrincipalDetails principalDetails,
                        @ModelAttribute("items") OrderItemDTOList orderItems,
                        RedirectAttributes redirectAttributes)
    {
        String email = principalDetails.getUsername();// 회원의 이메일을 가져온다.
        List<OrderItemDTO> dto = orderItems.getItems();

        //주문시 장바구니 아이템 삭제
        for(OrderItemDTO itemDTO : dto){
            basketService.delete(email, itemDTO.getItemId());
        }
        Long orderId = orderService.order(email, dto);
        return "redirect:/order/success?id=" + orderId;
    }


    /**
     * 주문 목록 보여주기
     * 관리자,유저 전용으로 나누어야할듯?(유저 전용 하나 만들어보자)
     */
    @GetMapping("orderList")
    public String getList(@ModelAttribute("orderSearch")OrderSearch orderSearchModel,
                          Model model){
        List<Order> orders = orderService.findAllByString(orderSearchModel);
        model.addAttribute("orders", orders);
        return "order/list";
    }

    /**
     * 개인 주문 목록 보여주기
     */
    @GetMapping("/{memberId}/orders")
    public String memberOrderList(@PathVariable("memberId") Long memberId,
                                  Model model){
        List<Order> orders = orderService.findByMemberId(memberId);
        model.addAttribute("orders", orders);
        return "order/myList";
    }

    /**
     * 주문 취소
     */
    @PostMapping(value = "/{orderId}/UserCancel")
    public String cancelUserOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/order/" + orderId + "/orders";
    }

    /**
     * 관리자 주문 취소
     */
    @PostMapping(value = "/{orderId}/AdminCancel")
    public String cancelAdminOrder(@PathVariable("orderId") Long orderId) {
        orderService.cancelOrder(orderId);
        return "redirect:/order/orderList";
    }

    /**
     * 배송 완료 변환
     */
    @PostMapping(value = "/{orderId}/complete")
    public String comOrder(@PathVariable("orderId") Long orderId) {
        orderService.completeOrder(orderId);
        return "redirect:/order/orderList";
    }

    /**
     * 주문완료 페이지
     */
    @GetMapping("/order/success")
    public String success(@RequestParam("id") Long id,
                          Model model) {
        Order order = orderService.findById(id);
        model.addAttribute("order", order);
        return "order/success";
    }

}
