package model;

// Represents a product for sale with attributes.

public class Product {
    private String title;
    private double price;
    private boolean onSale;
    private Category category;
    private boolean used;
    private String description;
    private Person owner;

    // EFFECTS: constructs a new product listing for sale
    public Product(String title, double price, Category category, boolean used, Person owner) {
        this.title = title;
        this.price = price;
        onSale = false;
        this.category = category;
        this.used = used;
        description = "";
        this.owner = owner;
    }

    // MODIFIES: this
    // EFFECTS: switches the product's ownership to the buyer
    public void switchOwner(Person buyer) {
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

    public Category getCategory() {
        return category;
    }

    public boolean isUsed() {
        return used;
    }

    public String getDescription() {
        return description;
    }

    public Person getOwner() {
        return null;
    }
}