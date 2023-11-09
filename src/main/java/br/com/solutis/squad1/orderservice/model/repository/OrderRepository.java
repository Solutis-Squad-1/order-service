package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query(
            "SELECT oi FROM OrderItem oi WHERE oi.order IN :orders AND oi.deleted = false"
    )
    List<OrderItem> findOrderItemsFromOrders(@Param("orders") List<Order> orders);

    @Query(
            "SELECT oi FROM OrderItem oi WHERE oi.order = :order AND oi.deleted = false"
    )
    List<OrderItem> findOrderItemsFromOrder(@Param("order") Order order);

    @Query(
            "SELECT o FROM Order o WHERE o.userId = :id AND o.canceled = false"
    )
    Page<Order> findOrdersByUserAndAndCanceledFalse(@Param("id") Long id, Pageable pageable);

    @Query(
            "SELECT o FROM Order o WHERE o.canceled = false"
    )
    Page<Order> findAllAndAndCanceledFalse(Pageable pageable);


    @Modifying
    @Query(
            "UPDATE OrderItem oi SET oi.deleted = true, oi.deletedAt = CURRENT_TIMESTAMP WHERE oi.order.id = :id"
    )
    void deleteAllOrderItemsFromOrderId(@Param("id") Long id);

    Optional<Order> findByIdAndCanceledFalse(Long id);
}
