package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProdutoController {
    private final ProductService productService;

    @GetMapping
    public Page<ProductResponseDto> findAll(){
        return null;
    }

    @PostMapping
    public ProductResponseDto save(){
        return null;
    }
}
