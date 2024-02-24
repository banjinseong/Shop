package jpabook.jpashop.controller;

import jpabook.jpashop.domain.BasketItem;
import jpabook.jpashop.domain.PrincipalDetails;
import jpabook.jpashop.dto.BasketDTO;
import jpabook.jpashop.dto.OrderItemDTO;
import jpabook.jpashop.dto.OrderItemDTOList;
import jpabook.jpashop.service.BasketService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("basket/*")
public class BasketController {

    private final BasketService basketService;

    /**
     * 장바구니 페이지 들어가기
     */
    @GetMapping("baskets")
    public String basketPage(@AuthenticationPrincipal PrincipalDetails principalDetails,
                             Model model){
        String email = principalDetails.getUsername();
        List<BasketItem> basket = basketService.findBasket(email);
        model.addAttribute("baskets", basket);

        return "basket/baskets";
    }

    /**
     *장바구니에서 주문할때
     */
    @PostMapping("basketOrder")
    public String createForm(Model model,
                             @ModelAttribute("items") OrderItemDTOList orderItems){

        model.addAttribute("items",orderItems);
        return "order/orderForm";
    }


    /**
     * 장바구니 아이템 추가(itemid 뺄까..?)
     * 아이템 view로 다시 이동해도 ㄱㅊ을듯
     *
     */
    @PostMapping("/{itemId}/takeIt")
    public String addCartItem(@PathVariable("itemId") Long itemId,
                              @AuthenticationPrincipal PrincipalDetails principalDetails,
                              BasketDTO basketDTO) {
        String email = principalDetails.getUsername();
        basketService.takeIt(email, basketDTO);
        return "redirect:/basket/baskets";
    }

    @GetMapping("/{itemId}/delete")
    public String deleteCartItem(@PathVariable("itemId") Long itemId,
                              @AuthenticationPrincipal PrincipalDetails principalDetails) {
        String email = principalDetails.getUsername();
        basketService.delete(email, itemId);
        return "redirect:/basket/baskets";
    }


}
