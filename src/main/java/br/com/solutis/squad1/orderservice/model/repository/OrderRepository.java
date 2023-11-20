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

    /**
     * Finds order items associated with a list of orders that are not deleted.
     *
     * @param orders List of orders
     * @return List of non-deleted order items
     */
    @Query(
            "SELECT oi FROM OrderItem oi WHERE oi.order IN :orders AND oi.deleted = false"
    )
    List<OrderItem> findOrderItemsFromOrders(@Param("orders") List<Order> orders);

    /**
     * Finds order items associated with a specific order that are not deleted.
     *
     * @param order The order
     * @return List of non-deleted order items
     */
    @Query(
            "SELECT oi FROM OrderItem oi WHERE oi.order = :order AND oi.deleted = false"
    )
    List<OrderItem> findOrderItemsFromOrder(@Param("order") Order order);

    /**
     * Finds orders associated with a user that are not canceled.
     *
     * @param id       User ID
     * @param pageable Pageable information
     * @return Page of non-canceled orders
     */
    @Query(
            "SELECT o FROM Order o WHERE o.userId = :id AND o.canceled = false ORDER BY o.createdAt ASC"
    )
    Page<Order> findOrdersByUserAndCanceledFalse(@Param("id") Long id, Pageable pageable);

    /**
     * Finds all orders that are not canceled.
     *
     * @param pageable Pageable information
     * @return Page of non-canceled orders
     */
    @Query(
            "SELECT o FROM Order o WHERE o.canceled = false ORDER BY o.createdAt ASC"
    )
    Page<Order> findAllAndCanceledFalse(Pageable pageable);

    /**
     * Deletes all order items associated with a specific order by setting them as deleted.
     *
     * @param id Order ID
     */
    @Modifying
    @Query(
            "UPDATE OrderItem oi SET oi.deleted = true, oi.deletedAt = CURRENT_TIMESTAMP WHERE oi.order.id = :id"
    )
    void deleteAllOrderItemsFromOrderId(@Param("id") Long id);

    /**
     * Finds an order by ID that is not canceled.
     *
     * @param id Order ID
     * @return Optional of the non-canceled order
     */
    Optional<Order> findByIdAndCanceledFalse(Long id);
}
