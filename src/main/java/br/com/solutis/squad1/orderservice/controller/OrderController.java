package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDetailsDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public Page<OrderResponseDto> findAll(
            Pageable pageable
    ){
        return orderService.findAll(pageable);
    }

    @GetMapping("/{id}")
    public OrderResponseDto findById(
            @PathVariable Long id
    ){
        return orderService.findById(id);
    }

    @GetMapping("/products/{id}")
    public Page<ProductResponseDto> findProductsById(
            Pageable pageable,
            @PathVariable Long id
    ){
        return orderService.findProductsById(id, pageable);
    }

    @GetMapping("/user/{id}")
    @PreAuthorize("hasAuthority('order:get')")
    public Page<OrderResponseDto> findOrdersByUserId(
            @PathVariable Long id,
            Pageable pageable
    ){
        return orderService.findOrdersByUserId(id, pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('order:update')")
    public void update(@PathVariable Long id, @RequestBody OrderPutDto orderPutDto){
        orderService.update(id, orderPutDto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('order:delete')")
    public void delete(
            @PathVariable Long id
    ) {
        orderService.delete(id);
    }
}
