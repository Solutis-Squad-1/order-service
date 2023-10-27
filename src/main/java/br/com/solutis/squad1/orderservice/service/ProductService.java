package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.product.CategoryResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductPostDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.mapper.CategoryMapper;
import br.com.solutis.squad1.orderservice.mapper.ImageMapper;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Category;
import br.com.solutis.squad1.orderservice.model.entity.Image;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.repository.CategoryRepository;
import br.com.solutis.squad1.orderservice.model.repository.ImageRepository;
import br.com.solutis.squad1.orderservice.model.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductMapper productMapper;
    private final ImageMapper imageMapper;

    public Page<ProductResponseDto> findAll(Pageable pageable){
        return productRepository.findAll(pageable).map(productMapper::toResponseDto);
    }

    public ProductResponseDto save(ProductPostDto productPostDto) {
        List<Category> categories = saveAllCategories(productPostDto);
        Image image = saveImage(productPostDto);
        Product product = new Product(productPostDto, categories, image);

        productRepository.save(product);

        return productMapper.toResponseDto(product);
    }

    public Image saveImage(ProductPostDto productPostDto){
        Image image = imageMapper.postDtoToEntity(productPostDto.image());
        Image existingImage = imageRepository.findById(productPostDto.image().id()).orElse(null);
        if (existingImage == null) {
            imageRepository.save(image);
        }

        return image;
    }

    public List<Category> saveAllCategories(ProductPostDto productPostDto){
        List<Category> categories = new ArrayList<>();

        for (CategoryResponseDto categoryResponseDto : productPostDto.categories()) {
            Category category = categoryRepository.findById(categoryResponseDto.id()).orElse(null);
            if (category == null) {
                categoryRepository.save(category);
            }
            categories.add(category);
        }

        return categories;
    }
}
