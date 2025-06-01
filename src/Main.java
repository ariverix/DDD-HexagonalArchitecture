import adapter.primary.ConsoleUI;
import adapter.primary.RestApi;
import adapter.secondary.InMemoryOrderRepository;
import adapter.secondary.NotificationService;
import domain.model.Order;
import domain.model.Product;
import domain.port.primary.OrderUseCase;
import domain.port.secondary.NotificationPort;
import domain.port.secondary.OrderRepository;
import domain.service.OrderService;


import java.util.HashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        OrderRepository orderRepository = new InMemoryOrderRepository();
        NotificationPort notificationService = new NotificationService();

        OrderUseCase orderService = new OrderService(orderRepository, notificationService);

        ConsoleUI consoleUI = new ConsoleUI(orderService);
        RestApi restApi = new RestApi(orderService);

        System.out.println("======== Система управления заказами поставщикам FastFood Network ========");
        System.out.println("Демонстрация гексагональной архитектуры (портов и адаптеров)\n");

        demonstrateRestApi(restApi);

        System.out.println("\n\nЗапуск консольного интерфейса...\n");
        consoleUI.start();
    }

    private static void demonstrateRestApi(RestApi restApi) {
        System.out.println("--- Демонстрация REST API ---");

        Map<String, Product> productCatalog = createDemoProductCatalog();

        Map<String, Integer> orderItems = new HashMap<>();
        for (String productId : productCatalog.keySet()) {
            if (orderItems.size() < 3) {
                orderItems.put(productId, 10);
            }
        }

        String supplierId = "supplier-123";
        Order order = restApi.createOrder(supplierId, orderItems, productCatalog);
        System.out.println("Создан заказ через REST API: " + order.getId());

        order = restApi.confirmOrder(order.getId());
        System.out.println("Заказ подтвержден, статус: " + order.getStatus().getDescription());

        order = restApi.sendOrderToSupplier(order.getId());
        System.out.println("Заказ отправлен поставщику, статус: " + order.getStatus().getDescription());

        System.out.println("\nСписок всех заказов в системе:");
        restApi.getAllOrders().forEach(o ->
                System.out.println("- Заказ #" + o.getId() + " | Статус: " + o.getStatus().getDescription())
        );
    }

    private static Map<String, Product> createDemoProductCatalog() {
        Map<String, Product> catalog = new HashMap<>();

        Product meat = new Product("Говядина", "Мясо", "Охлажденный", 5);
        Product chicken = new Product("Курица", "Мясо", "Охлажденный", 4);
        Product potatoes = new Product("Картофель", "Овощи", "Комнатная температура", 30);
        Product tomatoes = new Product("Помидоры", "Овощи", "Охлажденный", 7);

        catalog.put(meat.getId(), meat);
        catalog.put(chicken.getId(), chicken);
        catalog.put(potatoes.getId(), potatoes);
        catalog.put(tomatoes.getId(), tomatoes);

        return catalog;
    }
}