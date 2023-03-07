package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    Person person;
    Product product1;
    Product product2;
    Product product3;

    @BeforeEach
    public void setup() {
        person = new Person("First customer!");
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, person);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, person);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, person);
    }

    @Test
    public void constructorTest() {
        assertEquals("First customer!", person.getName());
        assertEquals(0, person.getBalance());
        assertEquals(new ArrayList<>(), person.getInventory());
    }

    @Test
    public void makeTransactionNoSaleTest() {
        person.loadBalance(100.0);
        person.makeTransaction(product1);
        assertEquals(50.0, person.getBalance());
    }

    @Test
    public void makeTransactionWithSalePriceTest () {
        person.loadBalance(90.0);
        product1.markSale(30.0);
        person.makeTransaction(product1);
        assertEquals(60.0, person.getBalance());
    }

    @Test
    public void addToInventoryOneItemTest() {
        person.addToInventory(product1);
        assertEquals(1, person.getInventory().size());
        assertEquals(product1, person.getInventory().get(0));
    }

    @Test
    public void addToInventoryMultipleItemsTest() {
        person.addToInventory(product1);
        person.addToInventory(product2);
        assertEquals(2, person.getInventory().size());
        assertEquals(product1, person.getInventory().get(0));
        assertEquals(product2, person.getInventory().get(1));
    }

    @Test
    public void removeFromInventoryOneItemTest() {
        person.addToInventory(product2);
        person.addToInventory(product1);
        person.removeFromInventory(product2);
        assertEquals(1, person.getInventory().size());
    }

    @Test
    public void removeFromInventoryMultipleItemsTest() {
        person.addToInventory(product1);
        person.addToInventory(product2);
        person.addToInventory(product3);
        person.removeFromInventory(product2);
        assertEquals(2, person.getInventory().size());
        person.removeFromInventory(product3);
        assertEquals(1, person.getInventory().size());
        person.removeFromInventory(product1);
        assertEquals(0, person.getInventory().size());
    }

    @Test
    public void loadBalanceOnceTest() {
        person.loadBalance(23.45);
        assertEquals(23.45, person.getBalance());
    }

    @Test
    public void loadBalanceMultipleTimesTest() {
        person.loadBalance(45.23);
        person.loadBalance(29.0);
        person.loadBalance(0.01);
        assertEquals(0.01 + 29.0 + 45.23, person.getBalance());
    }

    @Test
    public void toJsonTest() {
        person.addToInventory(product1);
        assertEquals("{\"Balance\":0,\"Name\":\"First customer!\",\"Personal Inventory\":[{\"Used?\":false," +
                "\"Owner\":\"First customer!\",\"On Sale?\":false,\"Category\":\"AUTOMOTIVE\",\"Price\":50," +
                "\"Title\":\"Truck\",\"Sale Price\":-1}]}", person.toJson().toString());
    }

}