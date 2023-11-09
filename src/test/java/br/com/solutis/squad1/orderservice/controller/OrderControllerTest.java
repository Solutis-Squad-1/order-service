package br.com.solutis.squad1.orderservice.controller;

import br.com.solutis.squad1.orderservice.dto.order.OrderPutDto;
import br.com.solutis.squad1.orderservice.dto.order.OrderResponseDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemPostDto;
import br.com.solutis.squad1.orderservice.dto.orderItem.OrderItemResponseDto;
import br.com.solutis.squad1.orderservice.model.entity.enums.StatusPayment;
import br.com.solutis.squad1.orderservice.service.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

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

    private OrderPutDto createOrderPutDto(){
        return new OrderPutDto(
                "New description",
                List.of(createOrderItemPostDto()),
                StatusPayment.IN_PROCESSING
        );
    }
    private OrderResponseDto createOrderResponseDto(){
        return new OrderResponseDto(
                2L,
                3L,
                "New description",
                50.0,
                List.of(createOrderItemResponseDto()),
                StatusPayment.IN_PROCESSING
        );
    }
    private OrderItemResponseDto createOrderItemResponseDto() {
        return new OrderItemResponseDto(
                1L,
                2,
                2,
                20.0,
                40.0
        );
    }
    private OrderItemPostDto createOrderItemPostDto(){
        return new OrderItemPostDto(
                2,
                2,
                40.0
        );
    }
    private OrderItemPostDto createIncompleteOrderItemPostDto(){
        return new OrderItemPostDto(
                null,
                3,
                60.0
        );
    }
}