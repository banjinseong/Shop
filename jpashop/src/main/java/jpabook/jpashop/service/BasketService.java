package jpabook.jpashop.service;

import jpabook.jpashop.domain.Basket;
import jpabook.jpashop.domain.BasketItem;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.dto.BasketDTO;
import jpabook.jpashop.repository.BasketItemRepository;
import jpabook.jpashop.repository.BasketRepository;
import jpabook.jpashop.repository.ItemRepository;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BasketService {

    private final MemberRepository memberRepository;
    private final BasketRepository basketRepository;
    private final BasketItemRepository basketItemRepository;
    private final ItemRepository itemRepository;

    @Transactional
    public Long takeIt(String email, BasketDTO basketDTO){

        Optional<Member> optionalMember = memberRepository.findByEmail(email);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            Item item = itemRepository.findById(basketDTO.getItemId()).get();

            Basket basket = basketRepository.findByMember(member).get();

            BasketItem basketItem = basketItemRepository.findByBasketIdAndItemId(basket.getId(), item.getId());

            if(basketItem != null){
                basketItem.addCount(basketDTO.getCount());
                return basketItem.getId();
            }else{
                basketItem = BasketItem.createBasketItem(basket, item, basketDTO.getCount());
                basketItemRepository.save(basketItem);
                return basketItem.getId();
            }


        }
        else{//아이디 없을때
            return 1L;
        }
    }


    public List<BasketItem> findBasket(String email){
        Member member = memberRepository.findByEmail(email).get();
        Basket basket = basketRepository.findByMember(member).get();
        List<BasketItem> basketItemList = basketItemRepository.findByBasket(basket);
        return basketItemList;
    }

    @Transactional
    public void delete(String email, Long itemId){
        Member member = memberRepository.findByEmail(email).get();
        basketItemRepository.deleteByBasketIdAndItemId(member.getBaskets().getId(), itemId);
    }
}
