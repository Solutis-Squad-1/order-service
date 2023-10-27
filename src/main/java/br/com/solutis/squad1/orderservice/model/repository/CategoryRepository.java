package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.Category;
import feign.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}
