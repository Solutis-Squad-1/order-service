package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.OrderProduct;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {

}
