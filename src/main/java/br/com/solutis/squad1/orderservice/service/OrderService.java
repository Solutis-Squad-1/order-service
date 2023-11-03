package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepositoryCustom;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepositoryCustom;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderRepositoryCustom orderRepositoryCustom;
    private final OrderMapper mapper;
    private final ProductMapper productMapper;
    private final ProductService productService;
    private final OrderProductRepositoryCustom orderProductRepositoryCustom;

    public Page<OrderResponseDto> findAll(Pageable pageable) {
        return orderRepositoryCustom
                .findAll(pageable)
                .map(order -> new OrderResponseDto(order, findProductsByOrderId(order.getId())));
    }

    public OrderResponseDto findById(Long id){
        try {
            Order order = orderRepositoryCustom.findById(id);
            return new OrderResponseDto(order, findProductsByOrderId(order.getId()));
        } catch (NoResultException e){
            throw new EntityNotFoundException("Order not found");
        }
    }

    public Page<ProductResponseDto> findProductsById(Long id, Pageable pageable) {
        try {
            return orderRepositoryCustom
                    .findProductsById(id, pageable)
                    .map(product -> new ProductResponseDto(product, product.getCategories(), product.getImage()));

        } catch (Exception e){
            throw e;
        }
    }

    public Page<OrderResponseDto> findOrdersByUserId(Long id, Pageable pageable) {
        return orderRepositoryCustom
                .findOrdersByUserAndNotCanceled(id, pageable)
                .map(order -> new OrderResponseDto(order, findProductsByOrderId(order.getId())));
    }

    public Order save(Order order) {
        try {
            return orderRepository.save(order);
        } catch (Exception e) {
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

    public void update(Long id, OrderPutDto orderPutDto) {
        try {
            Order order = orderRepository.findById(id).get();
            order.update(mapper.putDtoToEntity(orderPutDto));

            orderRepositoryCustom.update(order);
        } catch (Exception e) {
            throw e;
        }
    }

    private List<Product> findProductsByOrderId(Long id) {
        try {
            return orderProductRepositoryCustom.findProductsByOrderId(id);
        } catch (Exception e) {
            throw e;
        }
    }
}


