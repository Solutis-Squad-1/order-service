package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.product.ProductPostDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductResponseDto> findAll(
            Pageable pageable
    ){
        return productService.findAll(pageable);
    }
}
