package model;

// Represents a shopper with a name, remaining balance, cart of unpaid products, and products purchased.

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.JsonReader;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class Person implements Writable {
    private String name;
    private double balance;
    private List<Product> inventory;

    // EFFECTS: constructs a shopping user with a name, no account balance, an empty cart, and empty inventory
    public Person(String name) {
        this.name = name;
        balance = 0;
        inventory = new ArrayList<>();
    }

    // MODIFIES: this
    // EFFECTS: deducts actual product price from balance
    public void makeTransaction(Product product) {
        balance -= product.getActualPrice();
    }

    // MODIFIES: this
    // EFFECTS: adds product to this person's inventory
    public void addToInventory(Product product) {
        inventory.add(product);
    }

    // REQUIRES: product is in this person's inventory
    // MODIFIES: this
    // EFFECTS: removes product from person's inventory
    public void removeFromInventory(Product product) {
        inventory.remove(product);
    }

    // REQUIRES: amount > 0
    // MODIFIES: this
    // EFFECTS: adds amount to current balance, logs the event
    public void loadBalance(Double amount) {
        balance += amount;
        EventLog.getInstance().logEvent(new Event(name + " has loaded $" + amount + " to balance."));
    }

    // EFFECTS: creates and returns this as a JSONObject with name, balance, and inventory attributes
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Name", name);
        json.put("Balance", balance);
        json.put("Personal Inventory", productsToJson());
        return json;
    }

    // EFFECTS: returns products in inventory as JSONArray
    private JSONArray productsToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Product p : inventory) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
    }

    // GETTERS
    public String getName() {
        return name;
    }

    public double getBalance() {
        return balance;
    }

    public List<Product> getInventory() {
        return inventory;
    }

}
