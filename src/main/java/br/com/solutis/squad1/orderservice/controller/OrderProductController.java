package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderProductDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDetailsDto;
import br.com.solutis.squad1.orderservice.service.OrderProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orderProduct")
@RequiredArgsConstructor
public class OrderProductController {
    private final OrderProductService orderProductService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrderResponseDetailsDto save(
            @RequestBody @Valid OrderProductDto orderProductDto
    ){
        return orderProductService.save(orderProductDto);
    }
}
