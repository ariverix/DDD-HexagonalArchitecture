package domain.port.primary;

import domain.model.Order;
import domain.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public interface OrderUseCase {

    Order createOrder(String supplierId, Map<Product, Integer> items);

    Order confirmOrder(String orderId);

    Order sendOrderToSupplier(String orderId);

    Order markOrderInTransit(String orderId);

    Order markOrderDelivered(String orderId);

    Order startQualityCheck(String orderId);

    Order acceptDelivery(String orderId, String qualityCheckResult);

    Order rejectDelivery(String orderId, String reason);

    Order returnToSupplier(String orderId, String reason);

    Order cancelOrder(String orderId, String reason);

    Order completeOrder(String orderId);

    Optional<Order> getOrderById(String orderId);

    List<Order> getAllOrders();

    List<Order> getOrdersBySupplier(String supplierId);
}