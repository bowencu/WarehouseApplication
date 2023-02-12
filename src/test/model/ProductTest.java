package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ProductTest {
    Product product;
    Person owner;
    Person buyer;

    @BeforeEach
    public void setup() {
        owner = new Person("Owner!");
        buyer = new Person("Buyer!!");
        product = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
    }

    @Test
    public void constructorTest() {
        assertEquals("Truck", product.getTitle());
        assertEquals(50.0, product.getPrice());
        assertFalse(product.isOnSale());
        assertEquals(-1.0, product.getSalePrice());
        assertEquals(Category.AUTOMOTIVE, product.getCategory());
        assertFalse(product.isUsed());
        assertEquals("", product.getDescription());
        assertEquals(owner, product.getOwner());
    }

    @Test
    public void switchOwnerTest() {
        product.switchOwner(buyer);
        assertEquals(buyer, product.getOwner());
    }

    @Test
    public void markSaleTest() {
        product.markSale(49.99);
        assertTrue(product.isOnSale());
        assertEquals(49.99, product.getSalePrice());
        assertEquals(50.0, product.getPrice());
    }

    @Test
    public void markSaleAlreadyOnSaleTest() {
        product.markSale(39.99);
        product.markSale(45.99);
        assertTrue(product.isOnSale());
        assertEquals(45.99, product.getSalePrice());
        assertEquals(50.0, product.getPrice());
    }

    @Test
    public void removeSaleTest() {
        product.markSale(47.99);
        product.removeSale();
        assertFalse(product.isOnSale());
        assertEquals(-1.0, product.getSalePrice());
        assertEquals(50.0, product.getPrice());
    }

    @Test
    public void removeSaleAlreadyNotOnSaleTest() {
        product.removeSale();
        assertFalse(product.isOnSale());
        assertEquals(-1.0, product.getSalePrice());
        assertEquals(50.0, product.getPrice());
    }

    @Test
    public void makeUsedTest() {
        product.makeUsed();
        assertTrue(product.isUsed());
    }
}