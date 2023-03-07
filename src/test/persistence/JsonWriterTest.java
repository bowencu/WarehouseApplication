package persistence;

import model.Category;
import model.Person;
import model.Product;
import model.Warehouse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class JsonWriterTest extends JsonTest {
    @Test
    void testWriterInvalidFile() {
        try {
            Warehouse warehouse = new Warehouse();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWarehouse() {
        try {
            Warehouse warehouse = new Warehouse();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyWarehouse.json");
            writer.open();
            writer.write(warehouse);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyWarehouse.json");
            warehouse = reader.read();
            assertEquals(0, warehouse.getUsers().size());
            assertEquals(0, warehouse.getInventory().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Warehouse warehouse = new Warehouse();
            Person person1 = new Person("Store Owner");
            warehouse.addToUsers(person1);

            Product product = new Product("iPad Pro", 999.99, Category.ELECTRONICS, person1);
            product.makeUsed();
            product.markSale(899.99);

            Person person2 = new Person("Bowen");
            person2.loadBalance(1000.0);
            person2.addToInventory(product);
            warehouse.addToUsers(person2);

            Product product1 = new Product("Toaster", 39.98, Category.APPLIANCES, person1);
            Product product2 = new Product("School Bus", 50001.0, Category.AUTOMOTIVE, person1);
            Product product3 = new Product("UBC Sweater", 49.99, Category.CLOTHING, person1);
            product3.makeUsed();
            product3.markSale(39.99);
            warehouse.addToInventory(product1);
            warehouse.addToInventory(product2);
            warehouse.addToInventory(product3);

            JsonWriter writer = new JsonWriter("./data/testWriterRegularWarehouse.json");
            writer.open();
            writer.write(warehouse);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterRegularWarehouse.json");
            warehouse = reader.read();
            assertEquals(2, warehouse.getUsers().size());
            assertEquals(3, warehouse.getInventory().size());
            person1 = warehouse.getUsers().get(0);
            person2 = warehouse.getUsers().get(1);
            checkPerson(0, "Store Owner", 0, person1);
            checkPerson(1000, "Bowen", 1, person2);
            product = person2.getInventory().get(0);
            checkProduct(true, "Store Owner", true, Category.ELECTRONICS, 999.99,
                    "iPad Pro", 899.99, product);
            product1 = warehouse.getInventory().get(0);
            product2 = warehouse.getInventory().get(1);
            product3 = warehouse.getInventory().get(2);
            checkProduct(false, "Store Owner", false, Category.APPLIANCES, 39.98,
                    "Toaster", -1, product1);
            checkProduct(false, "Store Owner", false, Category.AUTOMOTIVE, 50001,
                    "School Bus", -1, product2);
            checkProduct(true, "Store Owner", true, Category.CLOTHING, 49.99,
                    "UBC Sweater", 39.99, product3);
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
