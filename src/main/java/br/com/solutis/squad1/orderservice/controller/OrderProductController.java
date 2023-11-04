package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderProductDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDetailsDto;
import br.com.solutis.squad1.orderservice.service.OrderProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;

    @PostMapping("/products")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('order:create')")
    public void save(
            @RequestBody @Valid List<OrderProductDto> orderProductDto
    ){
        orderProductService.save(orderProductDto);
    }
}
