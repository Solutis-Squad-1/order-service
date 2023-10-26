package br.com.solutis.squad1.orderservice.model.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class ProductQueryRepository {

    @PersistenceContext
    private EntityManager em;



}
