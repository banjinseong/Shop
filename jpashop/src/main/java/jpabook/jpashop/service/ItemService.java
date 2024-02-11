package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Clothes;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.ClothesDTO;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(ClothesDTO dto){
        Clothes item = (Clothes) dto.toEntity();
        itemRepository.save(item);
    }

    @Transactional
    public void updateItem(Long id, ClothesDTO dto){
        Item item = itemRepository.findById(id).get();
        item.setName(dto.getName());
        item.setPrice(dto.getPrice());
        item.setStockQuantity(dto.getStockQuantity());
        item.setModified_date(LocalDateTime.now());
    }


    public ClothesDTO findOne(Long itemId){
        Item id = itemRepository.findById(itemId).get();
        ClothesDTO dto = new ClothesDTO();
        dto.setName(id.getName());
        dto.setPrice(id.getPrice());
        dto.setStockQuantity(id.getStockQuantity());
        return dto;
    }

    public List<Item> findAll(){
        return itemRepository.findAll();
    }
}
