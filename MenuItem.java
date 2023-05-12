public class MenuItem {
    private String category;
    private String description;
    private double price;

    public MenuItem(String description, String category, double price) {
        this.category = category;
        this.description = description;
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return description + " - " + price;
    }
}
