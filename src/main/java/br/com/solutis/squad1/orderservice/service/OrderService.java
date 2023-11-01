package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPaymentDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepositoryCustom;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final OrderMapper mapper;
    private final ProductMapper productMapper;

    public Page<OrderResponseDto> findAll(Pageable pageable) {
        return orderRepositoryCustom
                .findAll(pageable)
                .map(order -> new OrderResponseDto(order, order.getProducts()));
    }

    public OrderResponseDto findById(Long id){
        try {
            Order order = orderRepositoryCustom.findById(id);
            return new OrderResponseDto(order, order.getProducts());
        } catch (NoResultException e){
            throw new EntityNotFoundException("Order not found");
        }
    }

    public Page<ProductResponseDto> findProductsById(Long id, Pageable pageable) {
        try {
            return orderRepositoryCustom
                    .findProductsById(id, pageable)
                    .map(productMapper::toResponseDto);

        } catch (Exception e){
            throw e;
        }
    }

    public Page<OrderResponseDto> findOrdersByUserId(Long id, Pageable pageable) {
        return orderRepositoryCustom
                .findOrdersByUserAndNotCanceled(id, pageable)
                .map(order -> new OrderResponseDto(order, order.getProducts()));
    }

    public OrderResponseDto save(OrderPostDto orderPostDto) {
        try{
            Order order = mapper.postDtoToEntity(orderPostDto);

            BigDecimal total = order.getProducts().stream()
                    .map(product -> product.getPrice())
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            // Enviar Requisição ao Microsserviço de Pagamento
            new OrderPaymentDto(order, total);

            orderRepository.save(order);
            return new OrderResponseDto(order, order.getProducts());
        } catch (Exception e){
            throw e;
        }
    }

    public void delete(Long id) {
        try {
            Order order = orderRepository.getReferenceById(id);
            order.delete(order);
            orderRepositoryCustom.delete(order);
        } catch (Exception e) {
            throw e;
        }
    }
}
