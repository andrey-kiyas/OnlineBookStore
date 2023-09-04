package com.onlinebookstore.repository;

import com.onlinebookstore.model.Order;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Order o SET o.status = :status WHERE o.id = :orderId")
    void updateOrderByStatus(Long orderId, Order.Status status);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems"
            + " LEFT JOIN FETCH o.user u WHERE u.id = :userId")
    List<Order> findAllOrders(long userId);

    @EntityGraph(attributePaths = "orderItems")
    Optional<Order> findById(Long id);
}
