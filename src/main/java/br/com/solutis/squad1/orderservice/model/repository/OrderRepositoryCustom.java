package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OrderRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public Page<Order> findAll(Pageable pageable){
        String jpql = "SELECT o FROM Order o WHERE o.canceled = false";

        List<Order> resultList = em.createQuery(jpql, Order.class)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList);
    }

    public Order findById(Long id) {
        String jpql = "SELECT c FROM Order c WHERE c.canceled = false AND c.id = :id";

        return em.createQuery(jpql, Order.class)
                .setParameter("id", id)
                .getSingleResult();
    }

    public Page<Product> findProductsById(Long orderId, Pageable pageable) {
        String jpql = "SELECT op.product FROM Order o JOIN o.items op WHERE o.id IN :id";

        List<Product> products = em.createQuery(jpql, Product.class)
                .setParameter("id", orderId)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(products);
    }

    public List<Product> findListProductsById(Long id) {
        String jpql = "SELECT op.product FROM Order o JOIN o.items op WHERE o.id IN :id";

        return em.createQuery(jpql, Product.class)
                .setParameter("id", id)
                .getResultList();
    }

    public Page<Order> findOrdersByUserAndNotCanceled(Long id, Pageable pageable) {
        String jpql = "SELECT o FROM Order o WHERE o.canceled = false AND o.userId = :userId";

        List<Order> resultList = em.createQuery(jpql, Order.class)
                .setParameter("userId", id)
                .setFirstResult(pageable.getPageNumber() * pageable.getPageSize())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        return new PageImpl<>(resultList);
    }

    public void delete(Order order) {
        em.merge(order);
    }

    public void update(Order order) {
        em.merge(order);
    }
}