package br.com.solutis.squad1.orderservice.model.repository;

import br.com.solutis.squad1.orderservice.model.builder.OrderBuilder;
import br.com.solutis.squad1.orderservice.model.builder.OrderItemBuilder;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.OrderItem;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
class OrderRepositoryTest {

    @MockBean
    private OrderRepository orderRepository;

    @Test
    @DisplayName("Returns a list of existing items in an order list")
    void findOrderItemsFromOrders_ShouldReturnAListOfItems() {
        List<Order> listOrders = new ArrayList<>();
        List<OrderItem> listOrderItems = new ArrayList<>();
        Order order = createOrder();
        OrderItem orderItem = createOrderItem();

        listOrders.add(order);
        order.setId(3L);
        listOrders.add(order);
        listOrderItems.add(orderItem);
        orderItem.setOrder(order);
        listOrderItems.add(orderItem);

        when(orderRepository.findOrderItemsFromOrders(listOrders)).thenReturn(listOrderItems);
        List<OrderItem> response = orderRepository.findOrderItemsFromOrders(listOrders);

        assertEquals(2, response.size());
        assertIterableEquals(listOrderItems, response);
        verify(orderRepository, times(1)).findOrderItemsFromOrders(listOrders);
    }

    @Test
    @DisplayName("Returns an empty list when orders from the list have been deleted")
    void findOrderItemsFromOrders_ShouldReturnAnEmptyList() {
        List<Order> listOrders = new ArrayList<>();
        List<OrderItem> listOrderItems = new ArrayList<>();
        Order order = createOrderDeleted();
        OrderItem orderItem = createOrderItem();

        listOrders.add(order);
        listOrderItems.add(orderItem);

        when(orderRepository.findOrderItemsFromOrders(listOrders)).thenReturn(Collections.emptyList());
        List<OrderItem> response = orderRepository.findOrderItemsFromOrders(listOrders);
        System.out.println(response);

        assertTrue(response.isEmpty());
        verify(orderRepository, times(1)).findOrderItemsFromOrders(listOrders);
    }

    @Test
    @DisplayName("Returns a list of existing items in an order")
    void findOrderItemsFromOrder_ShouldReturnAListOfItems() {
        List<OrderItem> listOrderItems = new ArrayList<>();
        Order order = createOrder();
        OrderItem orderItem = createOrderItem();
        listOrderItems.add(orderItem);
        listOrderItems.add(orderItem);

        when(orderRepository.findOrderItemsFromOrder(order)).thenReturn(listOrderItems);
        List<OrderItem> response = orderRepository.findOrderItemsFromOrder(order);

        assertEquals(2, response.size());
        assertIterableEquals(listOrderItems, response);
        verify(orderRepository, times(1)).findOrderItemsFromOrder(order);
    }

    @Test
    @DisplayName("Returns an empty list because the order was deleted")
    void findOrderItemsFromOrder_ShouldReturnAnEmptyList() {
        List<OrderItem> listOrderItems = new ArrayList<>();
        Order order = createOrderDeleted();
        OrderItem orderItem = createOrderItem();

        listOrderItems.add(orderItem);

        when(orderRepository.findOrderItemsFromOrder(order)).thenReturn(Collections.emptyList());
        List<OrderItem> response = orderRepository.findOrderItemsFromOrder(order);
        System.out.println(response);

        assertTrue(response.isEmpty());
        verify(orderRepository, times(1)).findOrderItemsFromOrder(order);
    }

    @Test
    @DisplayName("Return an order page for a user with uncancelled status")
    void findOrdersByUserAndCanceledFalse_ShouldReturnsPageOfOrders() {
        Long userId = 1L;
        Pageable pageable = PageRequest.of(0, 10);
        Order order = createOrder();
        Page<Order> expectedPage = new PageImpl<>(Collections.singletonList(order));

        when(orderRepository.findOrdersByUserAndCanceledFalse(anyLong(), any(Pageable.class))).thenReturn(expectedPage);
        Page<Order> response = orderRepository.findOrdersByUserAndCanceledFalse(userId, pageable);

        assertEquals(expectedPage, response);
    }

    @Test
    @DisplayName("Return an empty order page for a user not found")
    void findOrdersByUserAndCanceledFalse_ShouldReturnsEmptyPage() {
        Long userId = 999L;
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findOrdersByUserAndCanceledFalse(anyLong(), any(Pageable.class))).thenReturn(Page.empty());
        Page<Order> response = orderRepository.findOrdersByUserAndCanceledFalse(userId, pageable);

        assertEquals(Page.empty(), response);
    }

    @Test
    @DisplayName("Returns a page of non-canceled orders")
    void findAllAndCanceledFalse_ShouldReturnsPageOfOrders() {
        Pageable pageable = PageRequest.of(0, 10);
        Order order = createOrder();
        Page<Order> expectedPage = new PageImpl<>(Collections.singletonList(order));

        when(orderRepository.findAllAndCanceledFalse(any(Pageable.class)))
                .thenReturn(expectedPage);

        Page<Order> response = orderRepository.findAllAndCanceledFalse(pageable);

        assertEquals(expectedPage, response);
    }

    @Test
    @DisplayName("Returns an empty page when no orders are found")
    void findAllAndCanceledFalse_ShouldReturnsEmptyPage() {
        Pageable pageable = PageRequest.of(0, 10);

        when(orderRepository.findAllAndCanceledFalse(any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<Order> response = orderRepository.findAllAndCanceledFalse(pageable);

        assertEquals(Page.empty(), response);
    }

    @Test
    @DisplayName("Deletes all order items associated with a specific order")
    void deleteAllOrderItemsFromOrderId_DeletesOrderItems() {
        Order order = createOrder();

        orderRepository.deleteAllOrderItemsFromOrderId(order.getId());
        when(orderRepository.findOrderItemsFromOrder(order)).thenReturn(Collections.emptyList());
        List<OrderItem> responseDeleted = orderRepository.findOrderItemsFromOrder(order);

        assertEquals(0, responseDeleted.size());
        verify(orderRepository, times(1)).findOrderItemsFromOrder(order);
        verify(orderRepository, times(1)).deleteAllOrderItemsFromOrderId(order.getId());
    }

    @Test
    @DisplayName("When the ID is null, the deleteAllOrderItemsFromOrderId method should not be called once")
    void deleteAllOrderItemsFromOrderId_WithNullId_DoesNothing() {
        Long id = null;

        orderRepository.deleteAllOrderItemsFromOrderId(id);

        verify(orderRepository, never()).deleteAllOrderItemsFromOrderId(anyLong());
    }

    @Test
    @DisplayName("Returns an order by ID not canceled")
    void findByIdAndCanceledFalse_ReturnsNonCanceledOrder() {
        Order order = createOrder();

        when(orderRepository.findByIdAndCanceledFalse(order.getId())).thenReturn(Optional.of(order));
        Optional<Order> response = orderRepository.findByIdAndCanceledFalse(order.getId());

        assertTrue(response.isPresent());
        assertEquals(order, response.get());
        verify(orderRepository, times(1)).findByIdAndCanceledFalse(order.getId());
    }

    @Test
    @DisplayName("Returns an empty optional<order> when the order cannot be found by ID")
    void findByIdAndCanceledFalse_DoesNotReturnCanceledOrder() {
        Order canceledOrder = createOrderDeleted();

        when(orderRepository.findByIdAndCanceledFalse(canceledOrder.getId())).thenReturn(Optional.empty());
        Optional<Order> response = orderRepository.findByIdAndCanceledFalse(canceledOrder.getId());

        assertTrue(response.isEmpty());
        verify(orderRepository, times(1)).findByIdAndCanceledFalse(canceledOrder.getId());
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

    private Order createOrderDeleted() {
        OrderBuilder builder = new OrderBuilder();
        return builder
                .id(2L)
                .userId(1L)
                .statusPayment(StatusPayment.IN_PROCESSING)
                .summary("New description")
                .items(new HashSet<>())
                .total(20.0)
                .canceled(true)
                .createdAt(LocalDateTime.now())
                .canceledAt(LocalDateTime.now().plusHours(1))
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
}