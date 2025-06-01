package domain.model;

import java.time.LocalDateTime;
import java.util.*;

public class Order {
    private final String id;
    private String supplierId;
    private OrderStatus status;
    private final LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Map<Product, Integer> items; // продукт и количество
    private String comments;
    private String qualityControlResult;

    public Order(String id, String supplierId, OrderStatus status,
                 LocalDateTime createdAt, LocalDateTime updatedAt,
                 Map<Product, Integer> items, String comments) {
        this.id = id;
        this.supplierId = supplierId;
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.items = new HashMap<>(items);
        this.comments = comments;
    }

    public Order(String supplierId, Map<Product, Integer> items) {
        this.id = UUID.randomUUID().toString();
        this.supplierId = supplierId;
        this.status = OrderStatus.CREATED;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = this.createdAt;
        this.items = new HashMap<>(items);
        this.comments = "";
    }

    public void confirm() {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Нельзя подтвердить заказ, который не находится в статусе 'Создан'");
        }
        status = OrderStatus.CONFIRMED;
        updateTimestamp();
    }

    public void send() {
        if (status != OrderStatus.CONFIRMED) {
            throw new IllegalStateException("Нельзя отправить заказ, который не был подтвержден");
        }
        status = OrderStatus.SENT;
        updateTimestamp();
    }

    public void markInTransit() {
        if (status != OrderStatus.SENT) {
            throw new IllegalStateException("Заказ должен быть в статусе 'Отправлен' перед переходом в статус 'В пути'");
        }
        status = OrderStatus.IN_TRANSIT;
        updateTimestamp();
    }

    public void markDelivered() {
        if (status != OrderStatus.IN_TRANSIT) {
            throw new IllegalStateException("Заказ должен быть в пути перед доставкой");
        }
        status = OrderStatus.DELIVERED;
        updateTimestamp();
    }

    public void startQualityCheck() {
        if (status != OrderStatus.DELIVERED) {
            throw new IllegalStateException("Проверка качества возможна только для доставленных заказов");
        }
        status = OrderStatus.QUALITY_CHECK;
        updateTimestamp();
    }

    public void acceptDelivery(String qualityCheckResult) {
        if (status != OrderStatus.QUALITY_CHECK) {
            throw new IllegalStateException("Принятие заказа возможно только после проверки качества");
        }
        this.qualityControlResult = qualityCheckResult;
        status = OrderStatus.ACCEPTED;
        updateTimestamp();
    }

    public void rejectDelivery(String reason) {
        if (status != OrderStatus.QUALITY_CHECK) {
            throw new IllegalStateException("Отклонение заказа возможно только после проверки качества");
        }
        this.qualityControlResult = reason;
        status = OrderStatus.REJECTED;
        updateTimestamp();
    }

    public void returnToSupplier(String reason) {
        if (status != OrderStatus.REJECTED) {
            throw new IllegalStateException("Возврат возможен только для отклоненных заказов");
        }
        this.comments = this.comments + "\nВозврат: " + reason;
        status = OrderStatus.RETURNED;
        updateTimestamp();
    }

    public void cancel(String reason) {
        if (status == OrderStatus.COMPLETED || status == OrderStatus.CANCELLED) {
            throw new IllegalStateException("Нельзя отменить завершенный или уже отмененный заказ");
        }
        this.comments = this.comments + "\nОтмена: " + reason;
        status = OrderStatus.CANCELLED;
        updateTimestamp();
    }

    public void complete() {
        if (status != OrderStatus.ACCEPTED) {
            throw new IllegalStateException("Только принятый заказ может быть завершен");
        }
        status = OrderStatus.COMPLETED;
        updateTimestamp();
    }

    private void updateTimestamp() {
        this.updatedAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public Map<Product, Integer> getItems() {
        return Collections.unmodifiableMap(items);
    }

    public String getComments() {
        return comments;
    }

    public String getQualityControlResult() {
        return qualityControlResult;
    }

    public void setComments(String comments) {
        this.comments = comments;
        updateTimestamp();
    }

    public void addItem(Product product, int quantity) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Нельзя изменять состав заказа после его подтверждения");
        }
        items.put(product, items.getOrDefault(product, 0) + quantity);
        updateTimestamp();
    }

    public void removeItem(Product product) {
        if (status != OrderStatus.CREATED) {
            throw new IllegalStateException("Нельзя изменять состав заказа после его подтверждения");
        }
        items.remove(product);
        updateTimestamp();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Order{" +
                "id='" + id + '\'' +
                ", supplierId='" + supplierId + '\'' +
                ", status=" + status +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", items=" + items.size() +
                ", comments='" + comments + '\'' +
                ", qualityControlResult='" + qualityControlResult + '\'' +
                '}';
    }
}