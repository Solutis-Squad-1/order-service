package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<Image, Long> {
}
