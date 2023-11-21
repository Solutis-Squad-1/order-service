package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * This interface represents a repository for the OrderItem entity.
 * It extends JpaRepository which provides JPA related methods such as save(), findOne(), findAll(), count(), delete() etc.
 * @see org.springframework.data.jpa.repository.JpaRepository
 */
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
