package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.product.ProductResponseDto;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.repository.OrderQueryRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderMapper mapper;
    private final ProductMapper productMapper;

    public Page<OrderResponseDto> findAll(Pageable pageable) {
        return orderQueryRepository.findAll(pageable).map(mapper::toResponseDto);
    }

    public OrderResponseDto findById(Long id){
        try {
            Order order = orderQueryRepository.findById(id);
            return mapper.toResponseDto(order);
        } catch (NoResultException e){
            throw new EntityNotFoundException("Order not found");
        }
    }

    public Page<ProductResponseDto> findProductsById(Long id, Pageable pageable) {
        try {
            return orderQueryRepository
                    .findProductsById(id, pageable)
                    .map(productMapper::toResponseDto);

        } catch (Exception e){
            throw e;
        }
    }

    public Page<OrderResponseDto> findOrdersByUserId(Long id, Pageable pageable) {
        return orderQueryRepository.findOrdersByUserAndNotCanceled(id, pageable)
                .map(mapper::toResponseDto);
    }

    public OrderResponseDto save(OrderPostDto orderPostDto) {
        try{
            Order order = mapper.postDtoToEntity(orderPostDto);

            orderRepository.save(order);
            return mapper.toResponseDto(order);
        } catch (Exception e){
            throw e;
        }
    }

    public void delete(Long id) {
        try {
            Order order = orderRepository.getReferenceById(id);
            order.delete(order);
            orderRepository.save(order);
        } catch (Exception e) {
            throw e;
        }
    }
}
