package com.example.cart.service;

import com.example.cart.model.*;
import com.example.cart.repository.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class OrderService {

    private static final double VAT_RATE = 0.10;
    private static final AtomicLong ID_GENERATOR = new AtomicLong(1);

    private final ItemRepository itemRepository;

    public OrderService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public OrderResponse calculateOrder(Order order) {

        double totalPrice = 0;
        double totalVat = 0;

        List<Item> processedItems = new ArrayList<>();

        for (Item requestedItem : order.getItems()) {
            Item product = itemRepository.getItem(requestedItem.getProductId());

            Item calculatedItem = calculateItemPrice(product, requestedItem.getQuantity());
            totalPrice += calculatedItem.getPrice();
            totalVat += calculatedItem.getVat();

            processedItems.add(calculatedItem);
        }

        return OrderResponse.builder()
                .orderId(ID_GENERATOR.getAndIncrement())
                .orderPrice(round(totalPrice))
                .orderVat(round(totalVat))
                .items(processedItems)
                .build();
    }

    private Item calculateItemPrice(Item product, int quantity) {

        double totalPrice = product.getPrice() * quantity;

        return new Item(
                product.getProductId(),
                quantity,
                round(totalPrice),
                round(totalPrice * VAT_RATE)
        );
    }

    private double round(double value) {
        return Math.round(value * 100.0) / 100.0;
    }
}
