package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryCustom {

    @PersistenceContext
    private EntityManager em;

    public Image findImageByProduct(Product product) {
        String jpql = "SELECT p.image FROM Product p WHERE p.id = :productId";

        return em.createQuery(jpql, Image.class)
                .setParameter("productId", product.getId())
                .getSingleResult();
    }

    public List<Category> findAllCategoriesByProduct(Product product) {
        String jpql = "SELECT c FROM Product p JOIN p.categories c WHERE p.id = :productId";

        return em.createQuery(jpql, Category.class)
                .setParameter("productId", product.getId())
                .getResultList();
    }
}
