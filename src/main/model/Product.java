package model;

// Represents a product for sale with attributes.

public class Product {
    private String title;
    private double price;
    private boolean onSale;
    private double salePrice;
    private Category category;
    private boolean used;
    private String description;
    private Person owner;

    // EFFECTS: creates a new product listing for sale
    public Product(String title, double price, Category category, boolean used) {
        this.title = title;
        this.price = price;
        onSale = false;
        salePrice = price;
        this.category = category;
        this.used = used;
        description = "";
    }

    // MODIFIES: this
    // EFFECTS: changes product owner to new buyer
    public void buy(Person buyer) {
        owner = buyer;
    }


    // GETTERS
    public String getTitle() {
        return title;
    }

    public double getPrice() {
        return price;
    }

    public boolean isOnSale() {
        return onSale;
    }

    public double getSalePrice() {
        return salePrice;
    }

    public Category getCategory() {
        return category;
    }

    public boolean isUsed() {
        return used;
    }

    public String getDescription() {
        return description;
    }
}