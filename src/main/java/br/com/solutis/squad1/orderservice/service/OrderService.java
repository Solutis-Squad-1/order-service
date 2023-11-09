package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.exception.EntityNotFoundException;
import br.com.solutis.squad1.orderservice.mapper.OrderItemMapper;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
<<<<<<< HEAD
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import br.com.solutis.squad1.orderservice.model.repository.OrderItemRepository;
=======
import br.com.solutis.squad1.orderservice.model.entity.Product;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepositoryCustom;
>>>>>>> dd23967ec97342b196610233750e6920ad83b8ff
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;

    public Page<OrderResponseDto> findAll(Pageable pageable) {
        log.info("Find all orders");
        Page<Order> orders = orderRepository.findAllAndAndCanceledFalse(pageable);
        return getOrderResponseDtos(orders);
    }

    public OrderResponseDto findById(Long id) {
        log.info("Find order by id {}", id);
        Order order = orderRepository.findByIdAndCanceledFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found"));
        List<OrderItem> items = orderRepository.findOrderItemsFromOrder(order);
        return new OrderResponseDto(order, orderItemMapper.toResponseDto(items));
    }

    public Page<OrderResponseDto> findOrdersByUserId(Long id, Pageable pageable) {
        log.info("Find orders by user id {}", id);
        Page<Order> orders = orderRepository.findOrdersByUserAndAndCanceledFalse(id, pageable);
        return getOrderResponseDtos(orders);
    }

    public OrderResponseDto save(OrderPostDto orderPostDto) {
        log.info("Save order");

        Order order = orderMapper.postDtoToEntity(orderPostDto);
        Set<OrderItem> items = orderItemMapper.postDtoToEntity(orderPostDto.items());
        order.setTotal(items.stream().mapToDouble(OrderItem::getTotal).sum());

        order = orderRepository.save(order);

        Order finalOrder = order;
        items.forEach(item -> {
            item.setOrder(finalOrder);
            item.setTotal(item.getQuantity() * item.getPrice());
        });
        List<OrderItem> orderItems = orderItemRepository.saveAll(items);
        List<OrderItemResponseDto> orderItemResponseDtos = orderItemMapper.toResponseDto(orderItems);

        return new OrderResponseDto(order, orderItemResponseDtos);
    }

    public void update(Long id, OrderPutDto orderPutDto) {
        log.info("Update order by id {}", id);
        Order order = orderRepository.getReferenceById(id);
        order.update(orderMapper.putDtoToEntity(orderPutDto));

        if (orderPutDto.items() != null) {
            Set<OrderItem> items = orderItemMapper.postDtoToEntity(orderPutDto.items());
            order.setTotal(items.stream().mapToDouble(OrderItem::getTotal).sum());

            items.forEach(item -> {
                item.setOrder(order);
                item.setTotal(item.getQuantity() * item.getPrice());
            });
            orderItemRepository.saveAll(items);
        }
    }

    public void delete(Long id) {
        log.info("Delete order by id {}", id);
        Order order = orderRepository.getReferenceById(id);
        order.delete();
        orderRepository.deleteAllOrderItemsFromOrderId(id);
    }

    private Page<OrderResponseDto> getOrderResponseDtos(Page<Order> orders) {
        List<OrderItem> items = orderRepository.findOrderItemsFromOrders(orders.getContent());
        return orders.map(order -> {
            List<OrderItem> orderItems = items
                    .stream()
                    .filter(item -> item.getOrder().getId().equals(order.getId()))
                    .toList();
            return new OrderResponseDto(order, orderItemMapper.toResponseDto(orderItems));
        });
    }
<<<<<<< HEAD
}
=======

    public List<Product> findProductsByOrderId(Long id) {
        try {
            return orderProductRepositoryCustom.findProductsByOrderId(id);
        } catch (Exception e) {
            throw e;
        }
    }
}


>>>>>>> dd23967ec97342b196610233750e6920ad83b8ff
