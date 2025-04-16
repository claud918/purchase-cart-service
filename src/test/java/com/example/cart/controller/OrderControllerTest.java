package com.example.cart.controller;

import com.example.cart.model.Item;
import com.example.cart.model.Order;
import com.example.cart.model.OrderRequest;
import com.example.cart.model.OrderResponse;
import com.example.cart.service.OrderService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private MockMvc mockMvc;
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(orderController).build();
        objectMapper = new ObjectMapper();
    }

    @Test
    public void createOrder_ReturnsCorrectResponse() throws Exception {

        Item item = new Item(1, 2, 10.0, 1.0);
        Order order = new Order();
        order.setItems(List.of(item));

        OrderRequest request = new OrderRequest();
        request.setOrder(order);

        OrderResponse expectedResponse = OrderResponse.builder()
                .orderId(1L)
                .orderPrice(20.0)
                .orderVat(2.0)
                .items(List.of(item))
                .build();

        when(orderService.calculateOrder(any(Order.class))).thenReturn(expectedResponse);

        MvcResult result = mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andReturn();

        String responseContent = result.getResponse().getContentAsString();
        OrderResponse actualResponse = objectMapper.readValue(responseContent, OrderResponse.class);

        assertEquals(expectedResponse.getOrderId(), actualResponse.getOrderId());
        assertEquals(expectedResponse.getOrderPrice(), actualResponse.getOrderPrice());
        assertEquals(expectedResponse.getOrderVat(), actualResponse.getOrderVat());
        assertEquals(expectedResponse.getItems().size(), actualResponse.getItems().size());

        verify(orderService, times(1)).calculateOrder(any(Order.class));
    }

    @Test
    public void createOrder_WithEmptyOrder_ReturnsResponse() throws Exception {
        // Arrange
        Order order = new Order();
        order.setItems(List.of());

        OrderRequest request = new OrderRequest();
        request.setOrder(order);

        OrderResponse emptyResponse = OrderResponse.builder()
                .orderId(1L)
                .orderPrice(0.0)
                .orderVat(0.0)
                .items(List.of())
                .build();

        when(orderService.calculateOrder(any(Order.class))).thenReturn(emptyResponse);

        mockMvc.perform(post("/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());

        verify(orderService, times(1)).calculateOrder(any(Order.class));
    }
}