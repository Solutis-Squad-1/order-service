package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class OrderProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public List<Product> findProductsByOrderId(Long id) {
        String jpql = "SELECT op.product FROM OrderProduct op WHERE op.order.id = :id";

        return em.createQuery(jpql, Product.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Map<Long, Integer> findQuantitiesByOrderId(Long id){
        String jpql = "SELECT op.product.id, op.quantity FROM Order o JOIN o.items op WHERE o.id = :id";

        List<Object[]> results = em.createQuery(jpql, Object[].class)
                .setParameter("id", id)
                .getResultList();

        Map<Long, Integer> quantities = new HashMap<>();

        for (Object[] result : results) {
            Long productId = (Long) result[0];
            Integer quantity = (Integer) result[1];
            quantities.put(productId, quantity);
        }

        return quantities;
    }

    public Map<Long, BigDecimal> findPricesByOrderId(Long id) {
        String jpql = "SELECT op.product.id, op.price FROM Order o JOIN o.items op WHERE o.id = :id";

        List<Object[]> results = em.createQuery(jpql, Object[].class)
                .setParameter("id", id)
                .getResultList();

        Map<Long, BigDecimal> prices = new HashMap<>();

        for (Object[] result : results) {
            Long productId = (Long) result[0];
            Double price = (Double) result[1];
            if (price != null) {
                BigDecimal priceBD = BigDecimal.valueOf(price);
                prices.put(productId, priceBD);
            }
        }
        return prices;
    }


}
