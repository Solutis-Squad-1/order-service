package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.service.OrderService;
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

    /**
     * Find all orders
     * @param pageable
     * @return Page<OrderResponseDto>
     */
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Page<OrderResponseDto> findAll(
            Pageable pageable
    ) {
        return orderService.findAll(pageable);
    }

    /**
     * Find order by id
     * @param id
     * @return OrderResponseDto
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public OrderResponseDto findById(
            @PathVariable Long id
    ) {
        return orderService.findById(id);
    }

    /**
     * Find orders by user id
     * @param id
     * @param pageable
     * @return Page<OrderResponseDto>
     */
    @GetMapping("/users/{id}")
    @PreAuthorize("hasAuthority('order:read')")
    public Page<OrderResponseDto> findOrdersByUserId(
            @PathVariable Long id,
            Pageable pageable
    ) {
        return orderService.findOrdersByUserId(id, pageable);
    }

    /**
     * Save order
     * @param orderPostDto
     * @return OrderResponseDto
     */
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('order:create')")
    public OrderResponseDto save(
            @RequestBody OrderPostDto orderPostDto
    ) {
        return orderService.save(orderPostDto);
    }

    /**
     * Update order
     * @param id
     * @param orderPutDto
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('order:update')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void update(@PathVariable Long id, @RequestBody OrderPutDto orderPutDto) {
        orderService.update(id, orderPutDto);
    }

    /**
     * Delete order
     * @param id
     */
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('order:delete')")
    public void delete(
            @PathVariable Long id
    ) {
        orderService.delete(id);
    }
}
