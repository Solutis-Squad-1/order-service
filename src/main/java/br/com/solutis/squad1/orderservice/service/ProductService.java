package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.product.CategoryResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ImageResponseDto;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.repository.ProductQueryRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductQueryRepository productQueryRepository;
    private final ProductMapper productMapper;



    public ImageResponseDto findImageById(){
        return null;
    }

    public List<CategoryResponseDto> findCategoryById(){
        return null;
    }
}
