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
    // EFFECTS: uploads product to warehouse of products for sale from person's inventory
    public void sell(Product product) {
        product.getOwner().removeFromInventory(product);
        inventory.add(product);
    }

    // REQUIRES: product is in warehouse inventory
    // MODIFIES: this, person, and product
    // EFFECTS: adds product to person's inventory, removes product from warehouse inventory, deducts product price from
    // person's balance, changes ownership of product from seller to buyer, and adds price amount to seller's balance
    // return true if purchase is made, false otherwise
    public boolean makeSale(Product product, Person buyer) {
        if (product.getPrice() <= buyer.getBalance()) {
            inventory.remove(product);
            product.getOwner().loadBalance(product.getPrice());
            product.switchOwner(buyer);
            buyer.makePurchase(product);
            buyer.addToInventory(product);
            return true;
        } else {
            return false;
        }
    }

    // REQUIRES: low and high >= 0
    // EFFECTS: returns all products in inventory with price in range [low, high]
    public List<Product> filterWithinPriceRange(Double low, Double high) {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (low <= p.getPrice() && p.getPrice() <= high) {
                filteredInventory.add(p);
            }
        }
        return filteredInventory;
    }

    // EFFECTS: returns all products in inventory currently on sale
    public List<Product> filterForSale() {
        List<Product> filteredInventory = new ArrayList<>();
        for (Product p : inventory) {
            if (p.isOnSale()) {
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
    // otherwise return all unused products
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
}