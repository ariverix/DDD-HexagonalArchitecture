package domain.model;

public enum OrderStatus {
    CREATED("Создан"),
    CONFIRMED("Подтвержден"),
    SENT("Отправлен поставщику"),
    IN_TRANSIT("В пути"),
    DELIVERED("Доставлен"),
    QUALITY_CHECK("Проверка качества"),
    ACCEPTED("Принят"),
    REJECTED("Отклонен"),
    RETURNED("Возвращен"),
    COMPLETED("Завершен"),
    CANCELLED("Отменен");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}