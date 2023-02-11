package model;

// Represents a shopper with a name, remaining balance, cart of unpaid products, and products purchased.

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private double balance;
    private List<Product> cart;
    private List<Product> inventory;

    // EFFECTS: constructs a shopping user with a name, no account balance, an empty cart, and empty inventory
    public Person(String name) {
        this.name = name;
        balance = 0;
        cart = new ArrayList<>();
        inventory = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: deducts product sale price from balance if on sale, otherwise deduct price
    public void makeTransaction(Product product) {
        if (product.isOnSale()) {
            balance -= product.getSalePrice();
        } else {
            balance -= product.getPrice();
        }
    }

    // MODIFIES: this
    // EFFECTS: adds product to this person's cart
    public void addToCart(Product product) {
        cart.add(product);
    }

    // MODIFIES: this
    // EFFECTS: adds product to this person's inventory
    public void addToInventory(Product product) {
        inventory.add(product);
    }

    // REQUIRES: product is in this person's cart
    // MODIFIES: this
    // EFFECTS: removes product from person's cart
    public void removeFromCart(Product product) {
        cart.remove(product);
    }

    // REQUIRES: product is in this person's inventory
    // MODIFIES: this
    // EFFECTS: removes product from person's inventory
    public void removeFromInventory(Product product) {
        inventory.remove(product);
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: adds amount to current balance
    public void loadBalance(Double amount) {
        balance += amount;
    }


    // GETTERS
    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public List<Product> getCart() {
        return cart;
    }

    public List<Product> getInventory() {
        return inventory;
    }

}
