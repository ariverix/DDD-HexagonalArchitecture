package adapter.primary;

import domain.model.Order;
import domain.model.OrderStatus;
import domain.model.Product;
import domain.port.primary.OrderUseCase;

import java.util.*;

public class ConsoleUI {
    private final OrderUseCase orderUseCase;
    private final Scanner scanner;
    private final Map<String, Product> productCatalog; // Каталог доступных продуктов

    public ConsoleUI(OrderUseCase orderUseCase) {
        this.orderUseCase = orderUseCase;
        this.scanner = new Scanner(System.in);
        this.productCatalog = initProductCatalog();
    }

    private Map<String, Product> initProductCatalog() {
        Map<String, Product> catalog = new HashMap<>();

        Product meat = new Product("Говядина", "Мясо", "Охлажденный", 5);
        Product chicken = new Product("Курица", "Мясо", "Охлажденный", 4);
        Product potatoes = new Product("Картофель", "Овощи", "Комнатная температура", 30);
        Product tomatoes = new Product("Помидоры", "Овощи", "Охлажденный", 7);
        Product bread = new Product("Хлеб", "Выпечка", "Комнатная температура", 3);
        Product cheese = new Product("Сыр", "Молочные продукты", "Охлажденный", 14);
        Product milk = new Product("Молоко", "Молочные продукты", "Охлажденный", 7);

        catalog.put(meat.getId(), meat);
        catalog.put(chicken.getId(), chicken);
        catalog.put(potatoes.getId(), potatoes);
        catalog.put(tomatoes.getId(), tomatoes);
        catalog.put(bread.getId(), bread);
        catalog.put(cheese.getId(), cheese);
        catalog.put(milk.getId(), milk);

        return catalog;
    }

    public void start() {
        boolean running = true;

        while (running) {
            displayMenu();
            int choice = readIntInput("Выберите опцию: ");

            try {
                switch (choice) {
                    case 1:
                        createNewOrder();
                        break;
                    case 2:
                        viewAllOrders();
                        break;
                    case 3:
                        processOrderAction();
                        break;
                    case 0:
                        running = false;
                        System.out.println("Выход из программы...");
                        break;
                    default:
                        System.out.println("Неверный выбор. Пожалуйста, попробуйте снова.");
                }
            } catch (Exception e) {
                System.out.println("Ошибка: " + e.getMessage());
            }
        }
    }

    private void displayMenu() {
        System.out.println("\n========= Система управления заказами FastFood Network =========");
        System.out.println("1. Создать новый заказ поставщику");
        System.out.println("2. Просмотреть все заказы");
        System.out.println("3. Обработать заказ");
        System.out.println("0. Выход");
        System.out.println("=================================================================");
    }

    private void createNewOrder() {
        System.out.println("\n--- Создание нового заказа ---");

        String supplierId = readStringInput("Введите ID поставщика: ");

        Map<Product, Integer> orderItems = new HashMap<>();
        boolean addingProducts = true;

        while (addingProducts) {
            displayProductCatalog();
            String productChoice = readStringInput("Выберите ID продукта (или 'q' для завершения): ");

            if ("q".equalsIgnoreCase(productChoice)) {
                addingProducts = false;
                continue;
            }

            Product product = productCatalog.get(productChoice);
            if (product == null) {
                System.out.println("Продукт не найден. Пожалуйста, выберите существующий ID.");
                continue;
            }

            int quantity = readIntInput("Введите количество: ");
            if (quantity <= 0) {
                System.out.println("Количество должно быть положительным числом.");
                continue;
            }

            orderItems.put(product, quantity);
            System.out.println("Продукт добавлен в заказ.");
        }

        if (orderItems.isEmpty()) {
            System.out.println("Заказ не может быть пустым.");
            return;
        }

        Order order = orderUseCase.createOrder(supplierId, orderItems);
        System.out.println("Заказ успешно создан с ID: " + order.getId());
    }

    private void viewAllOrders() {
        System.out.println("\n--- Список всех заказов ---");

        List<Order> orders = orderUseCase.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("Заказов не найдено.");
            return;
        }

        for (Order order : orders) {
            displayOrderSummary(order);
        }
    }

    private void processOrderAction() {
        String orderId = readStringInput("Введите ID заказа: ");

        Optional<Order> orderOpt = orderUseCase.getOrderById(orderId);
        if (!orderOpt.isPresent()) {
            System.out.println("Заказ с ID " + orderId + " не найден.");
            return;
        }

        Order order = orderOpt.get();
        displayOrderDetails(order);

        System.out.println("\nДоступные действия:");
        List<String> actions = getAvailableActions(order.getStatus());

        for (int i = 0; i < actions.size(); i++) {
            System.out.println((i + 1) + ". " + actions.get(i));
        }

        int actionChoice = readIntInput("Выберите действие: ");
        if (actionChoice < 1 || actionChoice > actions.size()) {
            System.out.println("Неверный выбор.");
            return;
        }

        String action = actions.get(actionChoice - 1);
        performOrderAction(order, action);
    }

    private List<String> getAvailableActions(OrderStatus status) {
        List<String> actions = new ArrayList<>();

        switch (status) {
            case CREATED:
                actions.add("Подтвердить заказ");
                actions.add("Отменить заказ");
                break;
            case CONFIRMED:
                actions.add("Отправить поставщику");
                actions.add("Отменить заказ");
                break;
            case SENT:
                actions.add("Пометить как 'В пути'");
                actions.add("Отменить заказ");
                break;
            case IN_TRANSIT:
                actions.add("Пометить как 'Доставлен'");
                actions.add("Отменить заказ");
                break;
            case DELIVERED:
                actions.add("Начать проверку качества");
                actions.add("Отменить заказ");
                break;
            case QUALITY_CHECK:
                actions.add("Принять доставку");
                actions.add("Отклонить доставку");
                break;
            case REJECTED:
                actions.add("Вернуть поставщику");
                break;
            case ACCEPTED:
                actions.add("Завершить заказ");
                break;
            default:
                // Для остальных статусов нет доступных действий
                break;
        }

        return actions;
    }

    private void performOrderAction(Order order, String action) {
        try {
            switch (action) {
                case "Подтвердить заказ":
                    orderUseCase.confirmOrder(order.getId());
                    System.out.println("Заказ подтвержден.");
                    break;
                case "Отправить поставщику":
                    orderUseCase.sendOrderToSupplier(order.getId());
                    System.out.println("Заказ отправлен поставщику.");
                    break;
                case "Пометить как 'В пути'":
                    orderUseCase.markOrderInTransit(order.getId());
                    System.out.println("Заказ отмечен как 'В пути'.");
                    break;
                case "Пометить как 'Доставлен'":
                    orderUseCase.markOrderDelivered(order.getId());
                    System.out.println("Заказ отмечен как 'Доставлен'.");
                    break;
                case "Начать проверку качества":
                    orderUseCase.startQualityCheck(order.getId());
                    System.out.println("Начата проверка качества.");
                    break;
                case "Принять доставку":
                    String qualityResult = readStringInput("Введите результат проверки качества: ");
                    orderUseCase.acceptDelivery(order.getId(), qualityResult);
                    System.out.println("Доставка принята.");
                    break;
                case "Отклонить доставку":
                    String rejectReason = readStringInput("Введите причину отклонения: ");
                    orderUseCase.rejectDelivery(order.getId(), rejectReason);
                    System.out.println("Доставка отклонена.");
                    break;
                case "Вернуть поставщику":
                    String returnReason = readStringInput("Введите причину возврата: ");
                    orderUseCase.returnToSupplier(order.getId(), returnReason);
                    System.out.println("Заказ возвращен поставщику.");
                    break;
                case "Завершить заказ":
                    orderUseCase.completeOrder(order.getId());
                    System.out.println("Заказ завершен.");
                    break;
                case "Отменить заказ":
                    String cancelReason = readStringInput("Введите причину отмены: ");
                    orderUseCase.cancelOrder(order.getId(), cancelReason);
                    System.out.println("Заказ отменен.");
                    break;
                default:
                    System.out.println("Действие не распознано.");
            }
        } catch (Exception e) {
            System.out.println("Ошибка при выполнении действия: " + e.getMessage());
        }
    }

    private void displayOrderSummary(Order order) {
        System.out.println("Заказ #" + order.getId() + " | Поставщик: " + order.getSupplierId() +
                " | Статус: " + order.getStatus().getDescription() +
                " | Создан: " + order.getCreatedAt() +
                " | Позиций: " + order.getItems().size());
    }

    private void displayOrderDetails(Order order) {
        System.out.println("\n=== Детали заказа ===");
        System.out.println("ID: " + order.getId());
        System.out.println("Поставщик: " + order.getSupplierId());
        System.out.println("Статус: " + order.getStatus().getDescription());
        System.out.println("Создан: " + order.getCreatedAt());
        System.out.println("Обновлен: " + order.getUpdatedAt());

        System.out.println("\nПозиции заказа:");
        order.getItems().forEach((product, quantity) ->
                System.out.println("- " + product.getName() + " (" + product.getCategory() + ") - " + quantity + " шт.")
        );

        if (order.getComments() != null && !order.getComments().isEmpty()) {
            System.out.println("\nКомментарии: " + order.getComments());
        }

        if (order.getQualityControlResult() != null && !order.getQualityControlResult().isEmpty()) {
            System.out.println("Результат проверки качества: " + order.getQualityControlResult());
        }
    }

    private void displayProductCatalog() {
        System.out.println("\n--- Каталог продуктов ---");

        productCatalog.values().forEach(product ->
                System.out.println(product.getId() + ". " + product.getName() +
                        " | Категория: " + product.getCategory() +
                        " | Режим хранения: " + product.getTemperatureMode() +
                        " | Срок годности: " + product.getShelfLifeDays() + " дней")
        );
    }

    private String readStringInput(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }

    private int readIntInput(String prompt) {
        while (true) {
            try {
                System.out.print(prompt);
                int value = Integer.parseInt(scanner.nextLine());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Пожалуйста, введите корректное число.");
            }
        }
    }
}