package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
/**
 * 주문검색 쿼리메소드 사용해보기
 */
public class OrderRepository {

    private final EntityManager em;

    public void save(Order order){
        em.persist(order);
    }

    public Order findOne(Long id){
        return em.find(Order.class, id);
    }



    public List<Order> findAll(OrderSearch orderSearch){
        return em.createQuery("select o from Order o join o.member m" +
                        "where o.status = :status" +
                        "and m.email like :email", Order.class)
                .setParameter("status", orderSearch.getOrderStatus())
                .setParameter("email", orderSearch.getMemberEmail())
                .setMaxResults(1000) // 최대 1000건
                .getResultList();
    }

}
