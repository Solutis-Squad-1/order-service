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

        when(orderRepository.findOrdersByUserAndAndCanceledFalse(userId, pageable)).thenReturn(new PageImpl<>(orders, pageable, 2));
        Page<OrderResponseDto> result = orderService.findOrdersByUserId(userId, pageable);

        verify(orderRepository, times(1)).findOrdersByUserAndAndCanceledFalse(userId, pageable);
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        assertTrue(result.getContent().stream().allMatch(dto -> dto instanceof OrderResponseDto));
    }

    @Test
    @DisplayName("Returns an empty Page for an invalid user id")
    void findOrdersByUserId_WithInvalidUserId_ShouldReturnEmptyPage() {
        Long userId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findOrdersByUserAndAndCanceledFalse(userId, pageable)).thenReturn(Page.empty());
        Page<OrderResponseDto> result = orderService.findOrdersByUserId(userId, pageable);

        verify(orderRepository, times(1)).findOrdersByUserAndAndCanceledFalse(userId, pageable);
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
        return new Order(
                1L,
                1L,
                StatusPayment.IN_PROCESSING,
                "New description",
                new HashSet<>(),
                20.0,
                false,
                LocalDateTime.now(),
                null
        );
        /*return builder
                .id(1L)
                .userId(1L)
                .statusPayment(StatusPayment.IN_PROCESSING)
                .summary("New description")
                .items(Set.of(createOrderItem()))
                .price(20.0)
                .canceled(false)
                .createdAt(LocalDateTime.now())
                .canceledAt(null)
                .build();*/
    }

    private Order updatedOrder() {
        OrderPutDto dto = createOrderPutDto();

        return new Order(
                1L,
                1L,
                (dto.statusPayment() != null) ? dto.statusPayment() : StatusPayment.CONFIRMED,
                (dto.summary() != null) ? dto.summary() : "Updated description",
                Set.of(new OrderItem(1L, createOrder(), 2, 3, 40.0, 40.0, false, LocalDateTime.now(), null)),
                20.0,
                false,
                LocalDateTime.now(),
                null
        );
    }

    private OrderItem createOrderItem() {
        /*return OrderBuilder
                .id(1)
                .order(createOrder())
                .productId(1)
                .quantity(10)
                .price(10.0)
                .deleted(false)
                .createdAt(LocalDateTime.now())
                .deletedAt(null)
                .build();
*/
        return new OrderItem(
                1L,
                createOrder(),
                1,
                10,
                10.0,
                10.0,
                false,
                LocalDateTime.now(),
                null
        );
    }

    private OrderItem updateOrderItem() {
        return new OrderItem(
                1L,
                updatedOrder(),
                2,
                3,
                40.0,
                120.0,
                false,
                LocalDateTime.now(),
                null
        );
    }

    private OrderPutDto createOrderPutDto() {
        return new OrderPutDto(
                "Updated description",
                List.of(new OrderItemPostDto(2, 3, 40.0)),
                StatusPayment.CONFIRMED
        );
    }

    private OrderPutDto createOrderPutDtoIncomplete() {
        return new OrderPutDto(
                null,
                null,
                StatusPayment.CONFIRMED
        );
    }

    private OrderPostDto createOrderPostDto() {
        return new OrderPostDto(
                1L,
                "New description",
                List.of(createOrderItemPostDto())
        );
    }

    private OrderItemPostDto createOrderItemPostDto() {
        return new OrderItemPostDto(
                2,
                2,
                40.0
        );
    }

    private OrderResponseDto createOrderResponseDto() {
        return new OrderResponseDto(
                1L,
                1L,
                "New description",
                40.0,
                List.of(createOrderItemResponseDto()),
                StatusPayment.IN_PROCESSING
        );
    }

    private OrderItemResponseDto createOrderItemResponseDto() {
        return new OrderItemResponseDto(
                1L,
                1,
                1,
                40.0,
                40.0
        );
    }
}