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
import br.com.solutis.squad1.orderservice.model.repository.ProductRepositoryCustom;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductRepositoryCustom productRepositoryCustom;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductMapper productMapper;
    private final CategoryMapper categoryMapper;
    private final ImageMapper imageMapper;

    public Page<ProductResponseDto> findAll(Pageable pageable){
        return productRepository.findAll(pageable)
                .map(product -> new ProductResponseDto(product, product.getCategories(), product.getImage()));
    }

    public Product findById(Long id){
        Optional<Product> product = productRepository.findById(id);
        return product.orElse(null);
    }

    private Image findImageByProduct(Product product) {
        return productRepositoryCustom.findImageByProduct(product);
    }

    private List<Category> findAllCategoriesByProduct(Product product) {
        return productRepositoryCustom.findAllCategoriesByProduct(product);
    }

    public Image saveImage(ProductPostDto productPostDto){
        Image image = imageRepository.findById(productPostDto.image().id()).orElse(new Image(productPostDto.image()));
        imageRepository.save(image);

        return image;
    }

    public List<Category> saveAllCategories(ProductPostDto productPostDto){
        List<Category> categories = new ArrayList<>();

        for (CategoryResponseDto categoryResponseDto : productPostDto.categories()) {
            Category category = categoryRepository.findById(categoryResponseDto.id()).orElse(new Category(categoryResponseDto.id(), categoryResponseDto.name()));

            categoryRepository.save(category);
            categories.add(category);
        }

        return categories;
    }

    public Product saveProduct(Product product) {
        if (findById(product.getId()) == null){
            List<CategoryResponseDto> categories = categoryMapper.toEntitiesToDtos(product.getCategories());

            ProductPostDto productPostDto = new ProductPostDto(product, categories, imageMapper.toResponseDto(product.getImage()));

            saveAllCategories(productPostDto);
            saveImage(productPostDto);

            return productRepository.save(product);
        }

        return findById(product.getId());
    }
}
