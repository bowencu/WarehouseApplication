package persistence;

import model.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

import org.json.*;

// Source:
// https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo.git

// Represents a reader that reads warehouse from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads warehouse from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Warehouse read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWarehouse(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses warehouse from JSON object and returns it
    private Warehouse parseWarehouse(JSONObject jsonObject) {
        JSONArray users = jsonObject.getJSONArray("All Users");
        JSONArray products = jsonObject.getJSONArray("Warehouse Inventory");
        Warehouse warehouse = new Warehouse();
        addUsers(warehouse, users);
        addProducts(warehouse, products);
        return warehouse;
    }

    // MODIFIES: warehouse
    // EFFECTS: parses product list from JSON object and adds them to warehouse
    private void addProducts(Warehouse warehouse, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextProduct = (JSONObject) json;
            addProduct(warehouse, nextProduct);
        }
    }

    // MODIFIES: warehouse
    // EFFECTS: parses product from JSON object and adds it to warehouse
    private void addProduct(Warehouse warehouse, JSONObject jsonObject) {
        String title = jsonObject.getString("Title");
        double price = jsonObject.getDouble("Price");
        boolean onSale = jsonObject.getBoolean("On Sale?");
        double salePrice = jsonObject.getDouble("Sale Price");
        Category category = Category.valueOf(jsonObject.getString("Category"));
        boolean used = jsonObject.getBoolean("Used?");
        String owner = jsonObject.getString("Owner");

        Person ownerPerson = null;
        for (Person p : warehouse.getUsers()) {
            if (p.getName().equals(owner)) {
                ownerPerson = p;
            }
        }
        Product product = new Product(title, price, category, ownerPerson);
        if (onSale) {
            product.markSale(salePrice);
        }
        if (used) {
            product.makeUsed();
        }
        warehouse.addToInventory(product);
    }

    // MODIFIES: warehouse
    // EFFECTS: parses user list from JSON object and adds them to warehouse
    private void addUsers(Warehouse warehouse, JSONArray jsonArray) {
        for (Object json : jsonArray) {
            JSONObject nextUser = (JSONObject) json;
            addUser(warehouse, nextUser);
        }
    }

    // MODIFIES: warehouse
    // EFFECTS: parses user from JSON object and adds it to warehouse
    private void addUser(Warehouse warehouse, JSONObject jsonObject) {
        String name = jsonObject.getString("Name");
        double balance = jsonObject.getDouble("Balance");
        JSONArray jsonInventory = jsonObject.getJSONArray("Personal Inventory");

        Person user = new Person(name);
        user.loadBalance(balance);
        addToPersonalInventory(warehouse, user, jsonInventory);
        warehouse.addToUsers(user);
    }

    // MODIFIES: user
    // EFFECTS: parses inventory list from JSONArray and adds them to person's inventory
    private void addToPersonalInventory(Warehouse warehouse, Person user, JSONArray jsonInventory) {
        for (Object json : jsonInventory) {
            JSONObject nextProduct = (JSONObject) json;
            addProductPersonal(warehouse, user, nextProduct);
        }
    }

    // MODIFIES: user
    // EFFECTS: parses product from JSON object and adds it to person's inventory
    private void addProductPersonal(Warehouse warehouse, Person user, JSONObject jsonObject) {
        String title = jsonObject.getString("Title");
        double price = jsonObject.getDouble("Price");
        boolean onSale = jsonObject.getBoolean("On Sale?");
        double salePrice = jsonObject.getDouble("Sale Price");
        Category category = Category.valueOf(jsonObject.getString("Category"));
        boolean used = jsonObject.getBoolean("Used?");
        Product product = new Product(title, price, category, user);
        if (onSale) {
            product.markSale(salePrice);
        }
        if (used) {
            product.makeUsed();
        }
        user.addToInventory(product);
    }
}
