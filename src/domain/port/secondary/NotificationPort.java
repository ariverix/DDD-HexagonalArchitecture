package domain.port.secondary;

import domain.model.Order;

public interface NotificationPort {

    boolean sendOrderNotification(Order order);

    boolean sendCancellationNotification(Order order, String reason);

    boolean sendReturnNotification(Order order, String reason);

    boolean requestOrderConfirmation(Order order);
}