package model;

// Represents a product for sale with attributes.

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

public class Product implements Writable {
    private String title;
    private double price;
    private boolean onSale;
    private double salePrice;
    private Category category;
    private boolean used;
    private Person owner;

    // EFFECTS: constructs a new product listing for sale
    public Product(String title, double price, Category category, Person owner) {
        this.title = title;
        this.price = price;
        onSale = false;
        this.salePrice = -1.0;
        this.category = category;
        used = false;
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

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Title", title);
        json.put("Price", price);
        json.put("On Sale?", onSale);
        json.put("Sale Price", salePrice);
        json.put("Category", category);
        json.put("Used?", used);
        json.put("Owner", owner.getName());
        return json;
    }

//    // EFFECTS: returns a string that describes product's main features
//    public String getString() {
//        String usedString;
//        if (used) {
//            usedString = "USED";
//        } else {
//            usedString = "NEW";
//        }
//        if (onSale) {
//            return title + "On Sale for: $" + salePrice + ", Regular: $" + price + ", " + category + " " + usedString;
//        } else {
//            return title + "Regular Price: $" + price + ", " + category + " " + usedString;
//        }
//    }

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

    public Person getOwner() {
        return owner;
    }

}