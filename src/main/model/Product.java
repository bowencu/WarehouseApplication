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

    // EFFECTS: constructs a new product listing for sale
    public Product(String title, double price, Category category, Person owner) {
        this.title = title;
        this.price = price;
        onSale = false;
        this.salePrice = -1.0;
        this.category = category;
        used = false;
        description = "";
        this.owner = owner;
    }

    // MODIFIES: this
    // EFFECTS: switches the product's ownership to the buyer
    public void switchOwner(Person buyer) {
        owner = buyer;
    }

    // REQUIRES: 0 < salePrice < price
    // MODIFIES: this
    // EFFECTS: mark a product as on sale by making onSale true and setting product's salePrice as salePrice
    public void markSale(double salePrice) {
        onSale = true;
        this.salePrice = salePrice;
    }

    // MODIFIES: this
    // EFFECTS: remove the sale on a product by making onSale false and setting product's salePrice as -1.0
    public void removeSale() {
        onSale = false;
        salePrice = -1.0;
    }

    public void changePrice(Double newPrice) {
        price = newPrice;
    }

    // MODIFIES: this
    // EFFECTS: makes the product used
    public void makeUsed() {
        used = true;
    }

    // EFFECTS: returns the actual sale price of product, depending on if it's on sale
    public double getActualPrice() {
        if (isOnSale()) {
            return salePrice;
        } else {
            return price;
        }
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

    public Person getOwner() {
        return owner;
    }

}