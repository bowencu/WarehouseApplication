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
        assertEquals(new ArrayList<>(), person.getCart());
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
    public void addToCartOneItemTest() {
        person.addToCart(product1);
        assertEquals(1, person.getCart().size());
        assertEquals(product1, person.getCart().get(0));
    }

    @Test
    public void addToCartMultipleItemsTest() {
        person.addToCart(product1);
        person.addToCart(product2);
        assertEquals(2, person.getCart().size());
        assertEquals(product1, person.getCart().get(0));
        assertEquals(product2, person.getCart().get(1));
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
    public void removeFromCartOneItemTest() {
        person.addToCart(product2);
        person.addToCart(product1);
        person.removeFromCart(product2);
        assertEquals(1, person.getCart().size());
    }

    @Test
    public void removeFromCartMultipleItemsTest() {
        person.addToCart(product1);
        person.addToCart(product2);
        person.addToCart(product3);
        person.removeFromCart(product2);
        assertEquals(2, person.getCart().size());
        person.removeFromCart(product3);
        assertEquals(1, person.getCart().size());
        person.removeFromCart(product1);
        assertEquals(0, person.getCart().size());
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

}