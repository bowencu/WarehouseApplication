package persistence;

import model.Category;
import model.Person;
import model.Product;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkPerson(double balance, String name, int personalInventorySize, Person person) {
        assertEquals(balance, person.getBalance());
        assertEquals(name, person.getName());
        assertEquals(personalInventorySize, person.getInventory().size());
    }

    protected void checkProduct(boolean isUsed, String ownerName, boolean isOnSale, Category category, double price,
                                String title, double salePrice, Product product) {
        assertEquals(isUsed, product.isUsed());
        assertEquals(ownerName, product.getOwner().getName());
        assertEquals(isOnSale, product.isOnSale());
        assertEquals(category, product.getCategory());
        assertEquals(price, product.getPrice());
        assertEquals(title, product.getTitle());
        assertEquals(salePrice, product.getSalePrice());
    }
}
