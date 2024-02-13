package jpabook.jpashop.repository;

import jakarta.persistence.EntityManager;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
/**
 * 주문검색 쿼리메소드 사용해보기
 */
public interface OrderRepository extends JpaRepository<Order, Long> {


    @Query("SELECT o FROM Order o JOIN FETCH o.member WHERE (:email IS NULL OR o.member.email LIKE %:email%) AND (:status IS NULL OR o.status = :status)")
    List<Order> findByEmailAndStatus(@Param("email") String email, @Param("status") OrderStatus status);
}
