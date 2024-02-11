package jpabook.jpashop.controller;


import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
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
    public String order(PrincipalDetails principalDetails,
                        @ModelAttribute("items") OrderItemDTO orderItems)
    {
        String email = principalDetails.getUsername();// 회원의 이메일을 가져온다.
        System.out.println(email);
        List<OrderItemDTO> dto = orderItems.getOrderItemList();
        orderService.order(email, dto);
        return "redirect:/order/list";
    }

//    @GetMapping("list")
//    public String getList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
//
//    }

}
