package domain.model;

import java.util.Objects;
import java.util.UUID;

public class Product {
    private final String id;
    private final String name;
    private final String category;
    private final String temperatureMode;
    private final int shelfLifeDays;

    public Product(String id, String name, String category, String temperatureMode, int shelfLifeDays) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.temperatureMode = temperatureMode;
        this.shelfLifeDays = shelfLifeDays;
    }

    public Product(String name, String category, String temperatureMode, int shelfLifeDays) {
        this(UUID.randomUUID().toString(), name, category, temperatureMode, shelfLifeDays);
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getTemperatureMode() {
        return temperatureMode;
    }

    public int getShelfLifeDays() {
        return shelfLifeDays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Product{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", temperatureMode='" + temperatureMode + '\'' +
                ", shelfLifeDays=" + shelfLifeDays +
                '}';
    }
}