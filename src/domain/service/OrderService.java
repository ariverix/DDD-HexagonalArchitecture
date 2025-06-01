package domain.service;

import domain.model.Order;
import domain.model.Product;
import domain.port.primary.OrderUseCase;
import domain.port.secondary.NotificationPort;
import domain.port.secondary.OrderRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class  OrderService implements OrderUseCase {
    private final OrderRepository orderRepository;
    private final NotificationPort notificationPort;

    public OrderService(OrderRepository orderRepository, NotificationPort notificationPort) {
        this.orderRepository = orderRepository;
        this.notificationPort = notificationPort;
    }

    @Override
    public Order createOrder(String supplierId, Map<Product, Integer> items) {
        Order order = new Order(supplierId, items);
        return orderRepository.save(order);
    }

    @Override
    public Order confirmOrder(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.confirm();
        notificationPort.requestOrderConfirmation(order);
        return orderRepository.save(order);
    }

    @Override
    public Order sendOrderToSupplier(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.send();
        notificationPort.sendOrderNotification(order);
        return orderRepository.save(order);
    }

    @Override
    public Order markOrderInTransit(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.markInTransit();
        return orderRepository.save(order);
    }

    @Override
    public Order markOrderDelivered(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.markDelivered();
        return orderRepository.save(order);
    }

    @Override
    public Order startQualityCheck(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.startQualityCheck();
        return orderRepository.save(order);
    }

    @Override
    public Order acceptDelivery(String orderId, String qualityCheckResult) {
        Order order = getOrderOrThrow(orderId);
        order.acceptDelivery(qualityCheckResult);
        return orderRepository.save(order);
    }

    @Override
    public Order rejectDelivery(String orderId, String reason) {
        Order order = getOrderOrThrow(orderId);
        order.rejectDelivery(reason);
        return orderRepository.save(order);
    }

    @Override
    public Order returnToSupplier(String orderId, String reason) {
        Order order = getOrderOrThrow(orderId);
        order.returnToSupplier(reason);
        notificationPort.sendReturnNotification(order, reason);
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(String orderId, String reason) {
        Order order = getOrderOrThrow(orderId);
        order.cancel(reason);
        notificationPort.sendCancellationNotification(order, reason);
        return orderRepository.save(order);
    }

    @Override
    public Order completeOrder(String orderId) {
        Order order = getOrderOrThrow(orderId);
        order.complete();
        return orderRepository.save(order);
    }

    @Override
    public Optional<Order> getOrderById(String orderId) {
        return orderRepository.findById(orderId);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public List<Order> getOrdersBySupplier(String supplierId) {
        return orderRepository.findBySupplier(supplierId);
    }

    private Order getOrderOrThrow(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Заказ с ID " + orderId + " не найден"));
    }
}
