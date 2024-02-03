package jpabook.jpashop.controller;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.service.ItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Clothes clothes = new Clothes();
        clothes.setName(dto.getName());
        clothes.setPrice(dto.getPrice());
        clothes.setStockQuantity(dto.getStockQuantity());
        clothes.setSize(dto.getSize());
        clothes.setWear(dto.getWear());

        itemService.saveItem(clothes);
        return "redirect:/item/list";

    }

    @GetMapping("list")
    public String itemList(Model model){
        List<Item> items = itemService.findAll();

        model.addAttribute("items", items);
        return "item/list";
    }
}
