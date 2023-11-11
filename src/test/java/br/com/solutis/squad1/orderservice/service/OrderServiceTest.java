package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.exception.EntityNotFoundException;
import br.com.solutis.squad1.orderservice.mapper.OrderItemMapper;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import br.com.solutis.squad1.orderservice.model.repository.OrderItemRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.*;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

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

   /* @Test
    @DisplayName("Should return the order and its items after successfully saving it")
    void save_ShouldReturnOrderResponseDto() {
        OrderPostDto orderPostDto = createOrderPostDto();

        when(orderRepository.save(any(Order.class))).thenReturn(createOrder());
        when(orderItemRepository.saveAll(anySet())).thenReturn(new ArrayList<>());
        OrderResponseDto response = orderService.save(orderPostDto);

        assertNotNull(response);
        assertEquals(orderPostDto.userId(), response.userId());
        assertEquals(orderPostDto.summary(), response.summary());
        assertIterableEquals(orderPostDto.items(), response.items());
    }*/

    @Test
    @DisplayName("")
    void update(){}

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

    private Order createOrder(){
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
    }
    private OrderPutDto createOrderPutDto(){
        return new OrderPutDto(
                "New description",
                List.of(createOrderItemPostDto()),
                StatusPayment.IN_PROCESSING
        );
    }
    private OrderPostDto createOrderPostDto(){
        return new OrderPostDto(
                1L,
                "New description",
                List.of(createOrderItemPostDto())
        );
    }
    private OrderItemPostDto createOrderItemPostDto(){
        return new OrderItemPostDto(
                2,
                2,
                40.0
        );
    }

}