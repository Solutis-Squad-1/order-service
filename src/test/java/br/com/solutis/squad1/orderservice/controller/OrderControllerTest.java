package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPostDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.exception.EntityNotFoundException;
import br.com.solutis.squad1.orderservice.model.builder.OrderBuilder;
import br.com.solutis.squad1.orderservice.model.builder.OrderItemBuilder;
import br.com.solutis.squad1.orderservice.model.entity.Order;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import br.com.solutis.squad1.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.action.internal.EntityActionVetoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private OrderService service;

    @Test
    @DisplayName("Returns a list of orders")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void findAll_ShouldReturnListOfOrders() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<OrderResponseDto> listOrders = List.of(createOrderResponseDto(), createOrderResponseDto());
        Page<OrderResponseDto> orders = new PageImpl<>(listOrders);

        when(service.findAll(pageable)).thenReturn(orders);

        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns 403 if user lacks find authority")
    @WithAnonymousUser
    void findAll_ShouldReturnForbiddenStatus() throws Exception {
        Pageable pageable = PageRequest.of(0, 10, Sort.by("id").ascending());
        List<OrderResponseDto> listOrders = List.of(createOrderResponseDto(), createOrderResponseDto());
        Page<OrderResponseDto> orders = new PageImpl<>(listOrders);

        when(service.findAll(pageable)).thenReturn(orders);

        mvc.perform(get("/api/v1/orders"))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Returns a order by id")
    @WithMockUser(authorities = "ROLE_ADMIN")
    void findById_ShouldReturnOrderById() throws Exception {
        Long orderId = 1L;

        mvc.perform(get("/api/v1/orders/" + orderId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns 403 if user lacks find authority")
    @WithAnonymousUser
    void findById_ShouldReturnForbiddenStatus() throws Exception {
        Long orderId = 1L;

        mvc.perform(get("/api/v1/orders/" + orderId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Returns a list of orders by user id")
    @WithMockUser(authorities = "order:read")
    void findOrdersByUserId_ShouldReturnOrderByUserId() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<OrderResponseDto> listOrders = List.of(createOrderResponseDto(), createOrderResponseDto());
        Page<OrderResponseDto> orders = new PageImpl<>(listOrders);
        Long orderId = 1L;

        when(service.findOrdersByUserId(orderId, pageable)).thenReturn(orders);

        mvc.perform(get("/api/v1/orders/users/" + orderId))
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Returns 403 if user lacks find authority")
    @WithAnonymousUser
    void findOrdersByUserId_ShouldReturnForbiddenStatus() throws Exception {
        Pageable pageable = PageRequest.of(0, 10);
        List<OrderResponseDto> listOrders = List.of(createOrderResponseDto(), createOrderResponseDto());
        Page<OrderResponseDto> orders = new PageImpl<>(listOrders);
        Long orderId = 1L;

        when(service.findOrdersByUserId(orderId, pageable)).thenReturn(orders);

        mvc.perform(get("/api/v1/orders/users/" + orderId))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Should return HTTP 201 Created when creating a new order")
    @WithMockUser(authorities = "order:create")
    void save_ShouldReturnCreateStatus() throws Exception {
        OrderPostDto dto = createOrderPostDto();

        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isCreated());
    }

    @Test
    @DisplayName("Should return HTTP 201 Created when creating a new order")
    @WithAnonymousUser
    void save_ShouldReturnForbiddenStatus() throws Exception {
        OrderPostDto dto = createOrderPostDto();

        mvc.perform(post("/api/v1/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Update order with complete data - Should return No Content status")
    @WithMockUser(authorities = "order:update")
    void updateCompleteOrder_ShouldReturnNoContentStatus() throws Exception {
        Long paymentId = 1L;
        OrderPutDto dto = createOrderPutDto();

        mvc.perform(put("/api/v1/orders/" + paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Update order with incomplete data - Should return No Content status")
    @WithMockUser(authorities = "order:update")
    void updateIncompleteOrder_ShouldReturnNoContentStatus() throws Exception {
        Long paymentId = 1L;
        OrderItemPostDto dto = createIncompleteOrderItemPostDto();

        mvc.perform(put("/api/v1/orders/" + paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 403 if user lacks update authority")
    @WithAnonymousUser
    void updateOrder_ShouldReturnForbiddenStatus() throws Exception {
        Long paymentId = 1L;
        OrderPutDto dto = createOrderPutDto();

        mvc.perform(put("/api/v1/orders/" + paymentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(dto)))
                .andExpect(status().isForbidden());
    }

    @Test
    @DisplayName("Returns 204 after successful deletion")
    @WithMockUser(authorities = "order:delete")
    void delete_ShouldReturnNoContentStatus() throws Exception {
        Long orderId = 1L;

        mvc.perform(delete("/api/v1/orders/" + orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Returns 404 if ID is missing in the URL")
    @WithMockUser(authorities = "order:delete")
    void delete_ShouldReturnBadRequestStatus() throws Exception {
        mvc.perform(delete("/api/v1/orders/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    @DisplayName("Returns 403 if user lacks delete authority")
    @WithAnonymousUser
    void delete_ShouldReturnForbiddenStatus() throws Exception {
        mvc.perform(delete("/api/v1/orders/")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isForbidden());
    }

    private OrderPostDto createOrderPostDto() {
        OrderBuilder builder  = new OrderBuilder();

        return builder
                .userId(1L)
                .summary("New description")
                .listOrderItemsPostDto(List.of(createOrderItemPostDto()))
                .buildOrderPostDto();
    }

    private OrderPutDto createOrderPutDto(){
        OrderBuilder builder = new OrderBuilder();

        return builder
                .summary("New description")
                .listOrderItemsPostDto(List.of(createOrderItemPostDto()))
                .statusPayment(StatusPayment.IN_PROCESSING)
                .buildOrderPutDto();
    }

    private OrderResponseDto createOrderResponseDto(){
        OrderBuilder builder = new OrderBuilder();

        return builder
                .id(1L)
                .userId(3L)
                .summary("New description")
                .total(50.0)
                .listOrderItemsResponseDto(List.of(createOrderItemResponseDto()))
                .statusPayment(StatusPayment.IN_PROCESSING)
                .buildOrderResponseDto();
    }

    private OrderItemResponseDto createOrderItemResponseDto() {
        OrderItemBuilder builder = new OrderItemBuilder();

        return builder
                .id(1L)
                .productId(2)
                .quantity(2)
                .price(20.0)
                .total(40.0)
                .buildOrderItemResponseDto();
    }

    private OrderItemPostDto createOrderItemPostDto(){
        OrderItemBuilder builder = new OrderItemBuilder();

        return builder
                .productId(2)
                .quantity(2)
                .price(40.0)
                .buildOrderItemPostDto();
    }

    private OrderItemPostDto createIncompleteOrderItemPostDto(){
        OrderItemBuilder builder = new OrderItemBuilder();

        return builder
                .productId(null)
                .quantity(3)
                .price(60.0)
                .buildOrderItemPostDto();
    }
}