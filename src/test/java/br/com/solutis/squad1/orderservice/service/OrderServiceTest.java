package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.exception.EntityNotFoundException;
import br.com.solutis.squad1.orderservice.mapper.OrderItemMapper;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.model.builder.OrderBuilder;
import br.com.solutis.squad1.orderservice.model.builder.OrderItemBuilder;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import br.com.solutis.squad1.orderservice.model.repository.OrderItemRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderItemRepository orderItemRepository;
    @Mock
    private OrderMapper orderMapper;
    @Mock
    private OrderItemMapper orderItemMapper;
    @Captor
    private ArgumentCaptor<Order> orderCaptor;

    @Test
    @DisplayName("Returns a list of orders")
    @Transactional
    void findAll_ShouldReturnListOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Order order = createOrder();

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        orderRepository.save(order);
        when(orderRepository.findAllAndCanceledFalse(pageable)).thenReturn(new PageImpl<>(Collections.singletonList(order), pageable, 1));
        Page<OrderResponseDto> response = orderService.findAll(pageable);

        assertNotNull(response);
        assertEquals(1, response.getContent().size());
        assertEquals(OrderResponseDto.class, response.getContent().get(0).getClass());
        verify(orderRepository, times(1)).findAllAndCanceledFalse(pageable);
    }

    @Test
    @DisplayName("Returns an empty order list")
    void findAll_ShouldReturnAnEmptyListOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAllAndCanceledFalse(pageable)).thenReturn(new PageImpl<>(Collections.EMPTY_LIST, pageable, 0));
        Page<OrderResponseDto> response = orderService.findAll(pageable);

        assertEquals(0, response.getContent().size());
        verify(orderRepository, times(1)).findAllAndCanceledFalse(pageable);
    }

    @Test
    @DisplayName("Return OrderResponseDto if order is found")
    void findById_ShouldReturnOrderResponseDto() {
        Order order = createOrder();

        when(orderRepository.save(any(Order.class))).thenReturn(order);
        orderRepository.save(order);
        when(orderRepository.findByIdAndCanceledFalse(order.getId())).thenReturn(Optional.of(order));
        OrderResponseDto response = orderService.findById(order.getId());

        assertNotNull(response);
        assertEquals(order.getId(), response.id());
    }

    @Test
    @DisplayName("Throws EntityNotFoundException if order is not found")
    void findById_WithInvalidOrderId_ShouldThrowEntityNotFoundException() {
        Long orderId = 999L;

        when(orderRepository.findByIdAndCanceledFalse(orderId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> orderService.findById(orderId));
    }

    @Test
    @DisplayName("Returns Page of OrderResponseDto for a valid user id")
    void findOrdersByUserId_ShouldReturnPageOfOrderResponseDto() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        List<Order> orders = Arrays.asList(createOrder(), createOrder());

        when(orderRepository.findOrdersByUserAndCanceledFalse(userId, pageable)).thenReturn(new PageImpl<>(orders, pageable, 2));
        Page<OrderResponseDto> result = orderService.findOrdersByUserId(userId, pageable);

        verify(orderRepository, times(1)).findOrdersByUserAndCanceledFalse(userId, pageable);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(dto -> dto instanceof OrderResponseDto));
    }

    @Test
    @DisplayName("Returns an empty Page for an invalid user id")
    void findOrdersByUserId_WithInvalidUserId_ShouldReturnEmptyPage() {
        Long userId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findOrdersByUserAndCanceledFalse(userId, pageable)).thenReturn(Page.empty());
        Page<OrderResponseDto> result = orderService.findOrdersByUserId(userId, pageable);

        verify(orderRepository, times(1)).findOrdersByUserAndCanceledFalse(userId, pageable);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Should return the order and its items after successfully saving it")
    void save_ShouldReturnOrderResponseDto() {
        OrderPostDto orderPostDto = createOrderPostDto();
        Order order = createOrder();
        Set<OrderItem> items = Set.of(createOrderItem());
        List<OrderItem> orderItems = new ArrayList<>(items);
        List<OrderItemResponseDto> orderItemResponseDto = List.of(createOrderItemResponseDto());

        when(orderMapper.postDtoToEntity(orderPostDto)).thenReturn(order);
        when(orderItemMapper.postDtoToEntity(orderPostDto.items())).thenReturn(items);
        when(orderItemMapper.toResponseDto(orderItems)).thenReturn(orderItemResponseDto);
        when(orderRepository.save(order)).thenReturn(order);
        when(orderItemRepository.saveAll(items)).thenReturn(orderItems);

        OrderResponseDto response = orderService.save(orderPostDto);

        assertAll(
                () -> assertNotNull(response),
                () -> assertEquals(orderPostDto.userId(), response.userId()),
                () -> assertEquals(orderPostDto.summary(), response.summary()),
                () -> assertIterableEquals(orderItemResponseDto, response.items()),
                () -> verify(orderRepository, times(1)).save(any(Order.class)),
                () -> verify(orderItemRepository, times(1)).saveAll(anySet())
        );
    }

    @Test
    @DisplayName("Should update the order and its items successfully")
    void update_ShouldUpdateOrderAndItems() {
        Order existingOrder = createOrder();
        OrderPutDto orderPutDto = createOrderPutDto();
        Order updatedOrder = updatedOrder();
        Set<OrderItem> updateItems = Set.of(updateOrderItem());
        List<OrderItem> updateOrderItems = new ArrayList<>(updateItems);

        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        existingOrder = orderRepository.save(existingOrder);
        when(orderRepository.getReferenceById(existingOrder.getId())).thenReturn(existingOrder);
        when(orderMapper.putDtoToEntity(orderPutDto)).thenReturn(updatedOrder);
        when(orderItemMapper.postDtoToEntity(orderPutDto.items())).thenReturn(updateItems);
        when(orderItemRepository.saveAll(updateItems)).thenReturn(updateOrderItems);

        orderService.update(existingOrder.getId(), orderPutDto);
        Order orderUpdate = orderRepository.getReferenceById(existingOrder.getId());

        assertAll(
                () -> assertNotNull(orderUpdate),
                () -> assertEquals(orderPutDto.summary(), orderUpdate.getSummary()),
                () -> assertEquals(orderPutDto.statusPayment(), orderUpdate.getStatusPayment()),
                () -> verify(orderRepository, times(1)).save(any(Order.class)),
                () -> verify(orderItemRepository, times(1)).saveAll(anySet())
        );
    }

    @Test
    @DisplayName("Should update the order incomplete successfully")
    void update_ShouldUpdateOrderIncomplete() {
        Order existingOrder = createOrder();
        OrderPutDto orderPutDto = createOrderPutDtoIncomplete();
        Order updatedOrder = updatedOrder();
        Set<OrderItem> updateItems = Set.of(updateOrderItem());
        List<OrderItem> updateOrderItems = new ArrayList<>(updateItems);

        when(orderRepository.save(existingOrder)).thenReturn(existingOrder);
        existingOrder = orderRepository.save(existingOrder);
        when(orderRepository.getReferenceById(existingOrder.getId())).thenReturn(existingOrder);
        when(orderMapper.putDtoToEntity(orderPutDto)).thenReturn(updatedOrder);

        if (orderPutDto.items() != null) {
            when(orderItemMapper.postDtoToEntity(orderPutDto.items())).thenReturn(updateItems);
            when(orderItemRepository.saveAll(updateItems)).thenReturn(updateOrderItems);
        }

        orderService.update(existingOrder.getId(), orderPutDto);
        Order orderUpdate = orderRepository.getReferenceById(existingOrder.getId());

        assertNotNull(orderUpdate);

        if (orderPutDto.summary() != null){
            assertEquals(orderPutDto.summary(), orderUpdate.getSummary());
        }

        if (orderPutDto.statusPayment() != null){
            assertEquals(orderPutDto.statusPayment(), orderUpdate.getStatusPayment());
        }

        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    @DisplayName("Should delete order and order items")
    void delete_ShouldDeleteOrderAndOrderItems() {
        Long orderId = 1L;
        Order order = new Order();

        when(orderRepository.getReferenceById(orderId)).thenReturn(order);
        orderService.delete(orderId);

        verify(orderRepository, times(1)).getReferenceById(orderId);
        verify(orderRepository, times(1)).deleteAllOrderItemsFromOrderId(orderId);
    }

    private Order createOrder() {
        OrderBuilder builder = new OrderBuilder();
        return builder
                .id(1L)
                .userId(1L)
                .statusPayment(StatusPayment.IN_PROCESSING)
                .summary("New description")
                .items(new HashSet<>())
                .total(20.0)
                .canceled(false)
                .createdAt(LocalDateTime.now())
                .canceledAt(null)
                .build();
    }

    private Order updatedOrder() {
        OrderPutDto dto = createOrderPutDto();
        OrderBuilder builder = new OrderBuilder();

        return builder
                .id(1L)
                .userId(1L)
                .statusPayment((dto.statusPayment() != null) ? dto.statusPayment() : StatusPayment.CONFIRMED)
                .summary((dto.summary() != null) ? dto.summary() : "Updated description")
                .items(Set.of(new OrderItem(1L, createOrder(), 2, 3, 40.0, 40.0, false, LocalDateTime.now(), null)))
                .total(20.0)
                .canceled(false)
                .createdAt(LocalDateTime.now())
                .canceledAt(null)
                .build();
    }

    private OrderItem createOrderItem() {
        OrderItemBuilder builder = new OrderItemBuilder();

        return builder
                .id(1L)
                .order(createOrder())
                .productId(1)
                .quantity(10)
                .price(10.0)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
    }

    private OrderItem updateOrderItem() {
        OrderItemBuilder builder = new OrderItemBuilder();

        return builder
                .id(1L)
                .order(updatedOrder())
                .productId(2)
                .quantity(3)
                .price(40.0)
                .total(120.0)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
    }

    private OrderPutDto createOrderPutDto() {
        OrderBuilder builder  = new OrderBuilder();

        return builder
                .summary("Updated description")
                .listOrderItemsPostDto(List.of(new OrderItemPostDto(2, 3, 40.0)))
                .statusPayment(StatusPayment.CONFIRMED)
                .buildOrderPutDto();
    }

    private OrderPutDto createOrderPutDtoIncomplete() {
        OrderBuilder builder  = new OrderBuilder();

        return builder
                .summary(null)
                .listOrderItemsPostDto(Collections.emptyList())
                .statusPayment(StatusPayment.CONFIRMED)
                .buildOrderPutDto();
    }

    private OrderPostDto createOrderPostDto() {
        OrderBuilder builder  = new OrderBuilder();

        return builder
                .userId(1L)
                .summary("New description")
                .listOrderItemsPostDto(List.of(createOrderItemPostDto()))
                .buildOrderPostDto();
    }

    private OrderItemPostDto createOrderItemPostDto() {
        OrderItemBuilder builder  = new OrderItemBuilder();

        return builder
                .productId(2)
                .quantity(3)
                .price(40.0)
                .buildOrderItemPostDto();
    }

    private OrderResponseDto createOrderResponseDto() {
        OrderBuilder builder  = new OrderBuilder();

        return builder
                .id(1L)
                .userId(1L)
                .summary("New description")
                .total(40.0)
                .listOrderItemsResponseDto(List.of(createOrderItemResponseDto()))
                .statusPayment(StatusPayment.IN_PROCESSING)
                .buildOrderResponseDto();
    }

    private OrderItemResponseDto createOrderItemResponseDto() {
        OrderItemBuilder builder  = new OrderItemBuilder();

        return builder
                .id(1L)
                .productId(1)
                .quantity(1)
                .price(40.0)
                .total(40.0)
                .buildOrderItemResponseDto();
    }
}