package persistence;

import model.Category;
import model.Person;
import model.Product;
import model.Warehouse;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Warehouse warehouse = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyWarehouse() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyWarehouse.json");
        try {
            Warehouse warehouse = reader.read();
            assertEquals(0, warehouse.getUsers().size());
            assertEquals(0, warehouse.getInventory().size());
        } catch (IOException e) {
            fail("No exception should have been thrown.");
        }
    }

    @Test
    void testReaderRegularWarehouse() {
        JsonReader reader = new JsonReader("./data/testReaderRegularWarehouse.json");
        try {
            Warehouse warehouse = reader.read();
            assertEquals(2, warehouse.getUsers().size());
            Person person = warehouse.getUsers().get(1);
            checkPerson(1000.0, "Bowen", 1, person);

            Product item = person.getInventory().get(0);
            checkProduct(true, "Store Owner", true, Category.ELECTRONICS, 999.99,
                    "iPad Pro", 899.99, item);

            Product product1 = warehouse.getInventory().get(0);
            Product product3 = warehouse.getInventory().get(2);
            assertEquals(3, warehouse.getInventory().size());
            checkProduct(false, "Store Owner", false, Category.APPLIANCES, 39.98,
                    "Toaster", -1, product1);
            checkProduct(true, "Store Owner", true, Category.CLOTHING, 49.99,
                    "UBC Sweater", 39.99, product3);
        } catch (IOException e) {
            fail("No exception should have been thrown.");
        }
    }
}
