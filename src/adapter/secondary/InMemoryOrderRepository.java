package adapter.secondary;

import domain.model.Order;
import domain.port.secondary.OrderRepository;


import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class InMemoryOrderRepository implements OrderRepository {
    private final Map<String, Order> orders = new ConcurrentHashMap<>();

    @Override
    public Order save(Order order) {
        orders.put(order.getId(), order);
        return order;
    }
    @Override
    public Optional<Order> findById(String orderId) {
        return Optional.ofNullable(orders.get(orderId));
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(orders.values());
    }

    @Override
    public List<Order> findBySupplier(String supplierId) {
        return orders.values().stream()
                .filter(order -> order.getSupplierId().equals(supplierId))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String orderId) {
        orders.remove(orderId);
    }
}
