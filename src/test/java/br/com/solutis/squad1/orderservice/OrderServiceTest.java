package br.com.solutis.squad1.orderservice;

import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.mapper.OrderMapper;
import br.com.solutis.squad1.orderservice.mapper.ProductMapper;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.repository.OrderProductRepositoryCustom;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepository;
import br.com.solutis.squad1.orderservice.model.repository.OrderRepositoryCustom;
import br.com.solutis.squad1.orderservice.service.OrderService;
import br.com.solutis.squad1.orderservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Arrays;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepositoryCustom orderRepositoryCustom;

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
}