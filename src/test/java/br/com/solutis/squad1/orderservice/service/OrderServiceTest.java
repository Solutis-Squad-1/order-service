package br.com.solutis.squad1.orderservice.service;

import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;/*
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepositoryCustom;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepositoryCustom;
import br.com.solutis.squad1.orderservice.service.OrderService;
import br.com.solutis.squad1.orderservice.service.ProductService;*/
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;
/*
    @Test
    @DisplayName("Returns a list of orders")
    void findAll_ShouldReturnListOfOrders() {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        when(orderRepository.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));

        Page<OrderResponseDto> result = orderService.findAll(pageable);

        assertNotNull(result);
        assertEquals(0, result.getContent().size());
    }

    @Test
    @DisplayName("Returns a EntityNotFoundException when you can't find orders")
    void findAll_ShouldReturnEntityNotFoundException() throws Exception {
        when(orderRepository.findAllAndAndCanceledFalse(any(Pageable.class)))
                .thenReturn(Page.empty());

        Page<OrderResponseDto> result = orderService.findAll(Pageable.unpaged());

        assertTrue(result.isEmpty());

    }
*/
    /*

    @Mock
    private OrderProductRepositoryCustom orderProductRepositoryCustom;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void whenListOrderIsCalledThenReturnAListOfOrders() {
        PageRequest pageable = PageRequest.of(0, 10);
        Order order = new Order();
        when(orderRepositoryCustom.findAll(pageable)).thenReturn(new PageImpl<>(Arrays.asList(order)));
        Page<OrderResponseDto> result = orderService.findAll(pageable);
        assertEquals(1, result.getContent().size());
        verify(orderRepositoryCustom, times(1)).findAll(pageable);
    }

    @Test
    void whenListOrderIsCalledThenReturnEmptyList() {
        PageRequest pageable = PageRequest.of(0, 10);
        when(orderRepositoryCustom.findAll(pageable)).thenReturn(new PageImpl<>(Collections.emptyList()));
        Page<OrderResponseDto> result = orderService.findAll(pageable);
        assertEquals(0, result.getContent().size());
        verify(orderRepositoryCustom, times(1)).findAll(pageable);
    }

    @Test
    void whenFindByIdIsCalledThenReturnOrder() {
        Long id = 1L;
        Order order = new Order();
        when(orderRepositoryCustom.findById(id)).thenReturn(order);
        when(orderService.findProductsByOrderId(order.getId())).thenReturn(new ArrayList<>());
        OrderResponseDto result = orderService.findById(id);
        assertNotNull(result);
        verify(orderRepositoryCustom, times(1)).findById(id);
    }

    @Test
    void whenFindByIdIsCalledThenThrowEntityNotFoundException() {
        Long id = 1L;
        when(orderRepositoryCustom.findById(id)).thenThrow(new NoResultException());
        assertThrows(EntityNotFoundException.class, () -> orderService.findById(id));
        verify(orderRepositoryCustom, times(1)).findById(id);
    }*/
}