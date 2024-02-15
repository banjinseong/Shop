package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("item/*")
public class ItemController {

    private final ItemService itemService;

    @GetMapping("new")
    public String itemGet(Model model){
        model.addAttribute("dto", new ClothesDTO());
        return "item/new";
    }

    @PostMapping("new")
    public String itemPost(ClothesDTO dto){

        itemService.saveItem(dto);
        return "redirect:/item/list";

    }

    /**
     * 서비스 가서 dto로 반환하게 하자
     */
    @GetMapping(value = "{itemId}/edit")
    public String updateItemForm(@PathVariable("itemId") Long itemId, Model model) {
        ClothesDTO form = itemService.findOne(itemId);

        model.addAttribute("form", form);
        model.addAttribute("id", itemId);
        return "item/update";
    }
    /**
     * 상품 수정
     */
    @PostMapping(value = "{itemId}/edit")
    public String updateItem(@PathVariable("itemId") Long itemId, @ModelAttribute("form") ClothesDTO form) {
        itemService.updateItem(itemId, form);

        return "redirect:/item/list";
    }

    @GetMapping("list")
    public String itemList(Model model){
        List<Item> items = itemService.findAll();

        model.addAttribute("items", items);
        return "item/list";
    }

    @GetMapping(value = "{itemId}/order")
    public String getOrder(@PathVariable("itemId") Long itemId, Model model){
        ClothesDTO form = itemService.findOne(itemId);

        model.addAttribute("form", form);
        model.addAttribute("itemId", itemId);
        return "item/order";
    }

    @GetMapping(value = "{itemId}/view")
    public String getView(@PathVariable("itemId") Long itemId, Model model){
        ClothesDTO form = itemService.findOne(itemId);

        model.addAttribute("form", form);
        model.addAttribute("itemId", itemId);
        return "item/view";
    }
}
