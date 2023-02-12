package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WarehouseTest {
    Warehouse warehouse;
    Product product1;
    Product product2;
    Product product3;
    Person owner;
    Person buyer;

    @BeforeEach
    public void setup() {
        warehouse = new Warehouse();
        owner = new Person("Owner!");
        buyer = new Person("Buyer!!");
    }

    public void setupForFilteringMethods() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, owner);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, owner);
        product2.markSale(90.0);
        product1.makeUsed();
        warehouse.addToInventory(product1);
        warehouse.addToInventory(product2);
        warehouse.addToInventory(product3);

    }

    @Test
    public void constructorTest() {
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(new ArrayList<>(), warehouse.getInventory());
    }

    @Test
    public void sellOnceTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        owner.addToInventory(product1);
        assertEquals(1, owner.getInventory().size());
        assertEquals(0, warehouse.getInventory().size());
        assertFalse(product1.isUsed());
        warehouse.sell(product1);
        assertTrue(product1.isUsed());
        assertEquals(1, warehouse.getInventory().size());
        assertEquals(0, owner.getInventory().size());
        assertEquals(product1, warehouse.getInventory().get(0));
    }

    @Test
    public void sellMultipleTimesTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, owner);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, owner);
        assertFalse(product1.isUsed());
        assertFalse(product2.isUsed());
        assertFalse(product3.isUsed());
        warehouse.sell(product1);
        warehouse.sell(product2);
        warehouse.sell(product3);
        assertTrue(product1.isUsed());
        assertTrue(product2.isUsed());
        assertTrue(product3.isUsed());
        assertEquals(3, warehouse.getInventory().size());
        assertEquals(0, owner.getInventory().size());
        assertEquals(product1, warehouse.getInventory().get(0));
        assertEquals(product2, warehouse.getInventory().get(1));
        assertEquals(product3, warehouse.getInventory().get(2));
    }

    @Test
    public void makeSaleOnceSucceedOnSaleTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product1.markSale(40.0);
        warehouse.addToInventory(product1);
        buyer.loadBalance(40.5);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(0.5, buyer.getBalance());
        assertEquals(40.0, owner.getBalance());
        assertEquals(1, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
    }

    @Test
    public void makeSaleOnceSucceedOnSaleBoundaryTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product1.markSale(40.0);
        warehouse.addToInventory(product1);
        buyer.loadBalance(40.0);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(0.0, buyer.getBalance());
        assertEquals(40.0, owner.getBalance());
        assertEquals(1, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
    }

    @Test
    public void makeSaleOnceSucceedNotOnSaleTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        warehouse.addToInventory(product1);
        buyer.loadBalance(50.5);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(0.5, buyer.getBalance());
        assertEquals(50.0, owner.getBalance());
        assertEquals(1, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
    }

    @Test
    public void makeSaleOnceSucceedNotOnSaleBoundaryTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        warehouse.addToInventory(product1);
        buyer.loadBalance(50.0);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(0.0, buyer.getBalance());
        assertEquals(50.0, owner.getBalance());
        assertEquals(1, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
    }

    @Test
    public void makeSaleOnceFailedOnSaleTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product1.markSale(40.0);
        warehouse.addToInventory(product1);
        buyer.loadBalance(39.0);
        assertFalse(warehouse.makeSale(product1, buyer));
        assertEquals(1, warehouse.getInventory().size());
        assertEquals(39.0, buyer.getBalance());
        assertEquals(0.0, owner.getBalance());
        assertEquals(0, buyer.getInventory().size());
        assertEquals(owner, product1.getOwner());
    }

    @Test
    public void makeSaleOnceFailedNotOnSaleTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        warehouse.addToInventory(product1);
        buyer.loadBalance(49.0);
        assertFalse(warehouse.makeSale(product1, buyer));
        assertEquals(1, warehouse.getInventory().size());
        assertEquals(49.0, buyer.getBalance());
        assertEquals(0.0, owner.getBalance());
        assertEquals(0, buyer.getInventory().size());
        assertEquals(owner, product1.getOwner());
    }

    @Test
    public void makeSaleMultipleTimesAllSucceedTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, owner);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, owner);
        product2.markSale(90.0);
        warehouse.addToInventory(product1);
        warehouse.addToInventory(product2);
        warehouse.addToInventory(product3);
        buyer.loadBalance(1000.0);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertTrue(warehouse.makeSale(product2, buyer));
        assertTrue(warehouse.makeSale(product3, buyer));
        assertEquals(0, warehouse.getInventory().size());
        assertEquals(1000.0 - 50.0 - 90.0 - 60.3, buyer.getBalance());
        assertEquals(50.0 + 90.0 + 60.3, owner.getBalance());
        assertEquals(3, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
        assertEquals(buyer, product2.getOwner());
        assertEquals(buyer, product3.getOwner());
    }

    @Test
    public void makeSaleMultipleTimesOneFailedTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, owner);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, owner);
        product2.markSale(90.0);
        warehouse.addToInventory(product1);
        warehouse.addToInventory(product2);
        warehouse.addToInventory(product3);
        buyer.loadBalance(140.0);
        assertTrue(warehouse.makeSale(product1, buyer));
        assertTrue(warehouse.makeSale(product2, buyer));
        assertFalse(warehouse.makeSale(product3, buyer));
        assertEquals(1, warehouse.getInventory().size());
        assertEquals(0.0, buyer.getBalance());
        assertEquals(140.0, owner.getBalance());
        assertEquals(2, buyer.getInventory().size());
        assertEquals(buyer, product1.getOwner());
        assertEquals(buyer, product2.getOwner());
        assertEquals(owner, product3.getOwner());
    }

    @Test
    public void makeSaleMultipleTimesAllFailedTest() {
        product1 = new Product("Truck", 50.0, Category.AUTOMOTIVE, owner);
        product2 = new Product("Kettle", 100.0, Category.APPLIANCES, owner);
        product3 = new Product("Jacket", 60.3, Category.CLOTHING, owner);
        product2.markSale(90.0);
        warehouse.addToInventory(product1);
        warehouse.addToInventory(product2);
        warehouse.addToInventory(product3);
        buyer.loadBalance(49.99);
        assertFalse(warehouse.makeSale(product1, buyer));
        assertFalse(warehouse.makeSale(product2, buyer));
        assertFalse(warehouse.makeSale(product3, buyer));
        assertEquals(3, warehouse.getInventory().size());
        assertEquals(49.99, buyer.getBalance());
        assertEquals(0.0, owner.getBalance());
        assertEquals(0, buyer.getInventory().size());
        assertEquals(owner, product1.getOwner());
        assertEquals(owner, product2.getOwner());
        assertEquals(owner, product3.getOwner());
    }

    @Test
    public void addToInventoryOnceTest() {
        warehouse.addToInventory(product1);
        assertEquals(1, warehouse.getInventory().size());
    }

    @Test
    public void addToInventoryMultipleTimesTest() {
        warehouse.addToInventory(product1);
        warehouse.addToInventory(product2);
        warehouse.addToInventory(product3);
        assertEquals(3, warehouse.getInventory().size());
    }

    @Test
    public void filterWithinPriceRangeTest() {
        setupForFilteringMethods();
        assertEquals(3, warehouse.filterWithinPriceRange(50.0, 90.0).size());
        assertEquals(2, warehouse.filterWithinPriceRange(50.0, 60.3).size());
        assertEquals(1, warehouse.filterWithinPriceRange(60.4, 90.0).size());
        assertEquals(0, warehouse.filterWithinPriceRange(0.01, 49.99).size());
    }

    @Test
    public void filterForSaleMixedTest() {
        setupForFilteringMethods();
        ArrayList<Product> products = new ArrayList<>();
        products.add(product2);
        assertEquals(1, warehouse.filterForSale(true).size());
        assertEquals(products, warehouse.filterForSale(true));
        assertEquals(2, warehouse.filterForSale(false).size());
    }

    @Test
    public void filterForSaleNoneOnSaleTest() {
        setupForFilteringMethods();
        product2.removeSale();
        assertEquals(0, warehouse.filterForSale(true).size());
        assertEquals(3, warehouse.filterForSale(false).size());
    }

    @Test
    public void filterForSaleAllOnSaleTest() {
        setupForFilteringMethods();
        product1.markSale(1.98);
        product3.markSale(1.99);
        assertEquals(3, warehouse.filterForSale(true).size());
        assertEquals(0, warehouse.filterForSale(false).size());
    }

    @Test
    public void filterByCategoryTest() {
        setupForFilteringMethods();
        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        assertEquals(products, warehouse.filterByCategory(Category.AUTOMOTIVE));
        products.remove(product1);
        products.add(product2);
        assertEquals(products, warehouse.filterByCategory(Category.APPLIANCES));
        products.remove(product2);
        products.add(product3);
        assertEquals(products, warehouse.filterByCategory(Category.CLOTHING));
    }

    @Test
    public void filterByCategoryNoResultTest() {
        setupForFilteringMethods();
        assertEquals(0, warehouse.filterByCategory(Category.BOOKS).size());
    }

    @Test
    public void filterByUsedMixedTest() {
        setupForFilteringMethods();
        ArrayList<Product> products = new ArrayList<>();
        products.add(product1);
        assertEquals(products, warehouse.filterByUsed(true));
        assertEquals(2, warehouse.filterByUsed(false).size());
    }

    @Test
    public void filterByUsedNoResultTest() {
        setupForFilteringMethods();
        warehouse.getInventory().remove(product1);
        assertEquals(0, warehouse.filterByUsed(true).size());
        warehouse.getInventory().add(product1);
        product2.makeUsed();
        product3.makeUsed();
        assertEquals(0, warehouse.filterByUsed(false).size());
    }
}