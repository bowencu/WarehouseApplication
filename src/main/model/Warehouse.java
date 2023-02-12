package model;

// Represents listings of all products currently for sale.

import java.util.ArrayList;
import java.util.List;

public class Warehouse {
    private List<Product> inventory;

    // EFFECTS: constructs an empty warehouse with no products for sale
    public Warehouse() {
        inventory = new ArrayList<>();
    }

    // REQUIRES: product is in person's inventory
    // MODIFIES: this and person
    // EFFECTS: uploads product to warehouse, removes product from owner's inventory, and make it used
    public void sell(Product product) {
        product.getOwner().removeFromInventory(product);
        product.makeUsed();
        inventory.add(product);
    }

    // REQUIRES: product is in warehouse inventory
    // MODIFIES: this, person, and product
    // EFFECTS: adds product to person's inventory, removes product from warehouse inventory, deducts appropriate
    //          product price from person's balance, changes ownership of product from seller to buyer, and adds
    //          appropriate price to seller's balance return true if purchase is made, false otherwise
    public boolean makeSale(Product product, Person buyer) {
        if (product.isOnSale()) {
            if (product.getSalePrice() <= buyer.getBalance()) {
                inventory.remove(product);
                product.getOwner().loadBalance(product.getSalePrice());
                product.switchOwner(buyer);
                buyer.makeTransaction(product);
                buyer.addToInventory(product);
                return true;
            } else {
                return false;
            }
        } else {
            if (product.getPrice() <= buyer.getBalance()) {
                inventory.remove(product);
                product.getOwner().loadBalance(product.getPrice());
                product.switchOwner(buyer);
                buyer.makeTransaction(product);
                buyer.addToInventory(product);
                return true;
            } else {
                return false;
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: adds product to warehouse inventory
    public void addToInventory(Product product) {
        inventory.add(product);
    }

    // REQUIRES: low and high >= 0
    // EFFECTS: returns all products in inventory with price in range [low, high]
    public List<Product> filterWithinPriceRange(Double low, Double high) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (p.isOnSale()) {
                if (low <= p.getSalePrice() && p.getSalePrice() <= high) {
                    filteredInventory.add(p);
                }
            } else if (low <= p.getPrice() && p.getPrice() <= high) {
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
            if (p.getCategory() == category) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: returns all used products in inventory if isUsed is true,
    //          otherwise return all unused products
    public List<Product> filterByUsed(boolean isUsed) {
        List<Product> filteredInventory = new ArrayList<>();
        if (isUsed) {
            for (Product p : inventory) {
                if (p.isUsed()) {
                    filteredInventory.add(p);
                }
            }
        } else {
            for (Product p : inventory) {
                if (!p.isUsed()) {
                    filteredInventory.add(p);
                }
            }
        }
        return filteredInventory;
    }

    // GETTERS
    public List<Product> getInventory() {
        return inventory;
    }
}