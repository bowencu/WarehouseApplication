package model;

// Represents listings of all products currently for sale.

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

public class Warehouse implements Writable {
    private List<Product> inventory;
    private List<Person> users;

    // EFFECTS: constructs an empty warehouse with no products for sale
    public Warehouse() {
        inventory = new ArrayList<>();
        users = new ArrayList<>();
    }

    // REQUIRES: product is in person's inventory
    // MODIFIES: this and person
    // EFFECTS: uploads product to warehouse, removes product from owner's inventory, and make it used, logs the event
    public void sell(Product product) {
        product.getOwner().removeFromInventory(product);
        product.makeUsed();
        inventory.add(product);
        EventLog.getInstance().logEvent(new Event(product.getOwner().getName() + " is selling " + product.getTitle()));
    }

    // REQUIRES: product is in warehouse inventory
    // MODIFIES: this
    // EFFECTS: adds product to person's inventory, removes product from warehouse inventory, deducts appropriate
    //          product price from person's balance, changes ownership of product from seller to buyer, and adds
    //          appropriate price to seller's balance return true if purchase is made, false otherwise,
    //          logs the event
    public boolean makeSale(Product product, Person buyer) {
        if (product.getActualPrice() <= buyer.getBalance()) {
            inventory.remove(product);
            product.getOwner().loadBalance(product.getActualPrice());
            buyer.makeTransaction(product);
            buyer.addToInventory(product);
            product.switchOwner(buyer);
            EventLog.getInstance().logEvent(new Event(buyer.getName() + " bought " + product.getTitle() + " for $"
                    + product.getActualPrice() + "."));
            return true;
        } else {
            return false;
        }
    }

    // MODIFIES: this
    // EFFECTS: adds product to warehouse inventory, logs the event
    public void addToInventory(Product p) {
        inventory.add(p);
        EventLog.getInstance().logEvent(new Event(p.getTitle() + " added to Warehouse Inventory for $"
                + p.getActualPrice()));
    }

    // REQUIRES: product is in warehouse inventory
    // MODIFIES: this
    // EFFECTS: removes product from warehouse inventory, logs the event
    public void removeFromInventory(Product product) {
        inventory.remove(product);
        EventLog.getInstance().logEvent(new Event(product.getTitle() + " removed from Warehouse Inventory"));
    }

    // MODIFIES: this
    // EFFECTS: adds user to warehouse user list, logs the event
    public void addToUsers(Person user) {
        users.add(user);
        EventLog.getInstance().logEvent(new Event("Welcome back " + user.getName() + "!"));
    }

    // MODIFIES: this
    // EFFECTS: removes user from warehouse user list, logs the event
    public void removeFromUsers(Person user) {
        users.remove(user);
        EventLog.getInstance().logEvent(new Event("Goodbye " + user.getName() + "!"));
    }

    // REQUIRES: low and high >= 0
    // EFFECTS: returns all products in inventory with price in range [low, high]
    public List<Product> filterWithinPriceRange(Double low, Double high) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (low <= p.getActualPrice() && p.getActualPrice() <= high) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: returns all products in inventory currently on sale if true input
    //          returns all products in inventory not on sale if false input
    public List<Product> filterForSale(boolean wantSale) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (p.isOnSale() == wantSale) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: returns all products in inventory of given category
    public List<Product> filterByCategory(Category category) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (p.getCategory().equals(category)) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: returns all used products in inventory if isUsed is true,
    //          otherwise return all unused products
    public List<Product> filterByUsed(boolean isUsed) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (p.isUsed() == isUsed) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: creates and returns this as a JSONObject with all its users and products in inventory
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("All Users", peopleToJson());
        json.put("Warehouse Inventory", productsToJson());
        return json;
    }

    // EFFECTS: returns people in users as JSONArray
    private JSONArray peopleToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Person p : users) {
            jsonArray.put(p.toJson());
        }
        return jsonArray;
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
    public List<Product> getInventory() {
        return inventory;
    }

    public List<Person> getUsers() {
        return users;
    }
}