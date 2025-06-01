package adapter.secondary;

import domain.model.Order;
import domain.port.secondary.NotificationPort;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class NotificationService implements NotificationPort {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    @Override
    public boolean sendOrderNotification(Order order) {
        String message = String.format(
                "[%s] Уведомление для поставщика %s: Новый заказ #%s отправлен. " +
                        "Статус: %s. Количество позиций: %d",
                LocalDateTime.now().format(formatter),
                order.getSupplierId(),
                order.getId(),
                order.getStatus().getDescription(),
                order.getItems().size()
        );

        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendCancellationNotification(Order order, String reason) {
        String message = String.format(
                "[%s] Уведомление для поставщика %s: Заказ #%s отменен. " +
                        "Причина: %s",
                LocalDateTime.now().format(formatter),
                order.getSupplierId(),
                order.getId(),
                reason
        );

        System.out.println(message);
        return true;
    }

    @Override
    public boolean sendReturnNotification(Order order, String reason) {
        String message = String.format(
                "[%s] Уведомление для поставщика %s: Заказ #%s возвращен. " +
                        "Причина: %s",
                LocalDateTime.now().format(formatter),
                order.getSupplierId(),
                order.getId(),
                reason
        );

        System.out.println(message);
        return true;
    }

    @Override
    public boolean requestOrderConfirmation(Order order) {
        String message = String.format(
                "[%s] Запрос подтверждения для поставщика %s: Заказ #%s ожидает подтверждения. " +
                        "Количество позиций: %d",
                LocalDateTime.now().format(formatter),
                order.getSupplierId(),
                order.getId(),
                order.getItems().size()
        );

        System.out.println(message);
        return true;
    }
}