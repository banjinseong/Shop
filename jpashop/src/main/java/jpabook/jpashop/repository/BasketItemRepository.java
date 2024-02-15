package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Basket;
import jpabook.jpashop.domain.BasketItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BasketItemRepository extends JpaRepository<BasketItem, Long> {

    void deleteByBasketIdAndItemId(Long basketId, Long itemId);

    BasketItem findByBasketIdAndItemId(Long basketId, Long itemId);

    List<BasketItem> findByBasket(Basket basket);
}
