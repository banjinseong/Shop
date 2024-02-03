package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("order/*")
public class OrderController {

    private final OrderService orderService;
    private final MemberService memberService;
    private final ItemService itemService;

    @GetMapping("orderForm")
    public String createForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findAll();

        model.addAttribute("members", members);
        model.addAttribute("items",items);

        return "order/orderForm";
    }

    /**
     * 나중에 폼을 바꿔서 받는 변수를 달리할거니 일단 더러워
     * 수량 오류 터지는것도 try catch써서 잡기
     */
    @PostMapping("orderForm")
    public String order(@RequestParam("memberId") long meberId,
                        @RequestParam("itemId") Long itemId,
                        @RequestParam("count") int count)
    {
        Item item = itemService.findOne(itemId);
        List<OrderItemDTO> orderItemDTO = new ArrayList<>();
        orderItemDTO.add(new OrderItemDTO(itemId,item.getPrice(),count));

        orderService.order(meberId, orderItemDTO);
        return "redirect:/order/list";
    }

//    @GetMapping("list")
//    public String getList(@ModelAttribute("orderSearch")OrderSearch orderSearch, Model model){
//
//    }

}
