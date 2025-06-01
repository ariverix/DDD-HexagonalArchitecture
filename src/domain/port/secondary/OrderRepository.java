package domain.port.secondary;

import domain.model.Order;

import java.util.List;
import java.util.Optional;

public interface OrderRepository {

    Order save(Order order);

    Optional<Order> findById(String orderId);

    List<Order> findAll();

    List<Order> findBySupplier(String supplierId);

    void delete(String orderId);
}