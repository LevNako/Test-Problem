public class Product {
    private double price;
    private String currency;
    private String name;

    public Product(double price, String currency, String name) {
        this.price = price;
        this.currency = currency;
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name + " " + price + " " + currency;
    }
}
