package adapter.primary;

import domain.model.Order;
import domain.model.Product;
import domain.port.primary.OrderUseCase;


import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class RestApi {
    private final OrderUseCase orderUseCase;

    public RestApi(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
    }

    public List<Order> getAllOrders() {
        System.out.println("REST API: GET /api/orders");
        return orderUseCase.getAllOrders();
    }

    public Optional<Order> getOrderById(String orderId) {
        System.out.println("REST API: GET /api/orders/" + orderId);
        return orderUseCase.getOrderById(orderId);
    }

    public List<Order> getOrdersBySupplier(String supplierId) {
        System.out.println("REST API: GET /api/orders?supplier=" + supplierId);
        return orderUseCase.getOrdersBySupplier(supplierId);
    }

    public Order createOrder(String supplierId, Map<String, Integer> productQuantities, Map<String, Product> productCatalog) {
        System.out.println("REST API: POST /api/orders");

        Map<Product, Integer> items = new HashMap<>();
        for (Map.Entry<String, Integer> entry : productQuantities.entrySet()) {
            Product product = productCatalog.get(entry.getKey());
            if (product != null) {
                items.put(product, entry.getValue());
            }
        }

        return orderUseCase.createOrder(supplierId, items);
    }

    public Order confirmOrder(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/confirm");
        return orderUseCase.confirmOrder(orderId);
    }

    public Order sendOrderToSupplier(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/send");
        return orderUseCase.sendOrderToSupplier(orderId);
    }

    public Order markOrderInTransit(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/in-transit");
        return orderUseCase.markOrderInTransit(orderId);
    }

    public Order markOrderDelivered(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/delivered");
        return orderUseCase.markOrderDelivered(orderId);
    }

    public Order startQualityCheck(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/quality-check");
        return orderUseCase.startQualityCheck(orderId);
    }

    public Order acceptDelivery(String orderId, String qualityCheckResult) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/accept");
        return orderUseCase.acceptDelivery(orderId, qualityCheckResult);
    }

    public Order rejectDelivery(String orderId, String reason) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/reject");
        return orderUseCase.rejectDelivery(orderId, reason);
    }

    public Order returnToSupplier(String orderId, String reason) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/return");
        return orderUseCase.returnToSupplier(orderId, reason);
    }

    public Order cancelOrder(String orderId, String reason) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/cancel");
        return orderUseCase.cancelOrder(orderId, reason);
    }

    public Order completeOrder(String orderId) {
        System.out.println("REST API: PUT /api/orders/" + orderId + "/complete");
        return orderUseCase.completeOrder(orderId);
    }
}