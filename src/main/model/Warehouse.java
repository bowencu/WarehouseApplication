package model;

// Represents listings of all products currently for sale.

import java.util.List;

public class Warehouse {
    private List<Product> inventory;

    public Warehouse() {

    }

    // REQUIRES: product is in person's inventory
    // MODIFIES: this and person
    // EFFECTS: uploads product to warehouse of products for sale from person's inventory
    public void sell(Product product, Person person) {
        inventory.add(product);
        person.sell(product);
    }

    // REQUIRES: product is in warehouse inventory and person's balance is >= product price
    // MODIFIES: this, person, and product
    // EFFECTS: adds product to person's inventory, removes product from warehouse inventory, deducts product price from
    // person's balance, and changes ownership of product to person
    public void buy(Product product, Person buyer) {
        buyer.buy(product);
        product.buy(buyer);
        inventory.remove(product);
    }

}
