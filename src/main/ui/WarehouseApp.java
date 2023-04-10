package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

public class WarehouseApp {
    // Warehouse marketplace application
    private static final String JSON_STORE = "./data/warehouse.json";

    private Warehouse warehouse;
    private Person boss;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private Scanner input;

    public static void main(String[] args) {
        try {
            new WarehouseApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }

    // EFFECTS: runs the warehouse application
    public WarehouseApp() throws FileNotFoundException {
        init();
        runWarehouse();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runWarehouse() {
        boolean keepGoing = true;
        String command;
        input = new Scanner(System.in);
        input.useDelimiter("\n");

        System.out.println("l -> Login");
        System.out.println("n -> New User");
        System.out.println("m -> Store Manager");
        System.out.println("save -> save warehouse to file");
        System.out.println("open -> load warehouse from file");
        System.out.println("q -> quit");

        while (keepGoing) {
            command = input.next();
            if (command.equals("q")) {
                for (Event e : EventLog.getInstance()) {
                    System.out.println(e.toString());
                }
                keepGoing = false;
            } else {
                processUser(command);
            }
        }

        System.out.println("\nPlease visit Warehouse again!");
    }

    // MODIFIES: this
    // EFFECTS: initializes warehouse with 3 initial products and no owner yet, and an empty list of users
    private void init() {
        warehouse = new Warehouse();
        boss = new Person("Store Owner");
        warehouse.addToUsers(boss);
        Product toaster = new Product("Toaster", 39.98, Category.APPLIANCES, boss);
        Product bus = new Product("School Bus", 50001, Category.AUTOMOTIVE, boss);
        Product sweater = new Product("UBC Sweater", 49.99, Category.CLOTHING, boss);
        warehouse.addToInventory(toaster);
        warehouse.addToInventory(bus);
        warehouse.addToInventory(sweater);
        input = new Scanner(System.in);
        input.useDelimiter("\n");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
    }

    // REQUIRES: no duplicates of users in users with the same name
    // MODIFIES: this
    // EFFECTS: processes user command by signing in or creating a new user, and starting their visit
    private void processUser(String command) {
        switch (command) {
            case "l":
                userLogin();
                break;
            case "m":
                storeManager();
                runWarehouse();
                break;
            case "n":
                userNew();
                break;
            case "save":
                saveWarehouse();
                break;
            case "open":
                openWarehouse();
                break;
            default:
                System.out.println("Selection not valid...");
                runWarehouse();
                break;
        }
    }

    // EFFECTS: saves the warehouse to file
    private void saveWarehouse() {
        try {
            jsonWriter.open();
            jsonWriter.write(warehouse);
            jsonWriter.close();
            System.out.println("Saved Warehouse to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads warehouse from file
    private void openWarehouse() {
        try {
            warehouse = jsonReader.read();
            System.out.println("Loaded Warehouse from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: begins Warehouse shopping with the account of entered name
    private void userLogin() {
        System.out.println("Enter your name:");
        String name = input.next();
        for (Person p : warehouse.getUsers()) {
            if (name.equals(p.getName())) {
                System.out.println("Welcome back " + p.getName() + "!");
                startVisit(p);
            }
        }
        System.out.println("User with given name not found.");
        runWarehouse();
    }

    // MODIFIES: this
    // EFFECTS: offers methods available to a store manager
    private void storeManager() {
        System.out.println("Select an option:");
        System.out.println("\tstock -> add new products to warehouse inventory");
        System.out.println("\tsale  -> apply a discount to a select category of products");
        System.out.println("\treg   -> remove discount from a select category of products");
        System.out.println("\tb     -> go back");
        String managerCommand = input.next();
        switch (managerCommand) {
            case "stock":
                storeManagerStock();
                break;
            case "sale":
                storeManagerSale();
                break;
            case "reg":
                storeManagerReg();
                break;
            case "b":
                runWarehouse();
                break;
            default:
                System.out.println("Selection not valid...");
                break;
        }
    }

    // MODIFIES: this
    // EFFECTS: add a new product with entered attributes to warehouse inventory
    private void storeManagerStock() {
        System.out.println("Enter the product name you wish to add:");
        String name = input.next();
        System.out.println("Enter the price:");
        double price = input.nextDouble();
        System.out.println("Enter the category:");
        Category category = Category.valueOf(input.next().toUpperCase());
        warehouse.addToInventory(new Product(name, price, category, boss));
        System.out.println(name + " has been added to Warehouse's inventory.");
    }

    // MODIFIES: this
    // EFFECTS: applies % discount to all products in entered category, or all
    private void storeManagerSale() {
        System.out.println("Enter percent discount (1-100%) to be applied to all products:");
        double salePercent = input.nextDouble();
        System.out.println("Enter category to apply discount, or 'all':");
        String category = input.next();
        if (category.equals("all")) {
            for (Product p : warehouse.getInventory()) {
                p.markSale((1 - salePercent / 100) * p.getPrice());
            }
        } else {
            for (Product p : warehouse.getInventory()) {
                if (p.getCategory().equals(Category.valueOf(category.toUpperCase()))) {
                    p.markSale((1 - salePercent / 100) * p.getPrice());
                }
            }
        }
        System.out.println("All products in " + category + " are now " + salePercent + "% off.");
    }

    // MODIFIES: this
    // EFFECTS: removes discount from all products in entered category, or all products
    private void storeManagerReg() {
        System.out.println("Enter category to remove discount from, or 'all':");
        String category = input.next();
        if (category.equals("all")) {
            for (Product p : warehouse.getInventory()) {
                p.removeSale();
            }
        } else {
            for (Product p : warehouse.getInventory()) {
                if (p.getCategory().equals(Category.valueOf(category.toUpperCase()))) {
                    p.removeSale();
                }
            }
        }
        System.out.println("All products in " + category + " are now regular price.");
    }

    // MODIFIES: this
    // EFFECTS: creates a new user with entered name and adds it to users, begins shopping experience
    private void userNew() {
        System.out.println("Enter your new name:");
        String name = input.next();
        Person user = new Person(name);
        warehouse.addToUsers(user);
        System.out.println("Welcome to Warehouse, " + user.getName() + "!");
        startVisit(user);
    }

    // MODIFIES: this
    // EFFECTS: begins a user's Warehouse experience with initial actions
    private void startVisit(Person user) {
        startVisitMenu();
        String userCommand = input.next();
        if (userCommand.equals("load")) {
            userLoad(user);
        } else if (userCommand.equals("bal")) {
            userBal(user);
        } else if (userCommand.equals("sell")) {
            userSell(user);
        } else if (userCommand.equals("buy")) {
            userBuy(user);
        } else if (userCommand.equals("view")) {
            userView(user);
        } else if (userCommand.equals("b")) {
            runWarehouse();
        } else {
            System.out.println("Selection not valid...");
            startVisit(user);
        }
    }

    // EFFECTS: prints start of shopping experience menu
    private void startVisitMenu() {
        System.out.println("\nWhat would you like to do:");
        System.out.println("\tload -> load money to my personal balance");
        System.out.println("\tbal  -> view my balance");
        System.out.println("\tsell -> I am here to sell products");
        System.out.println("\tbuy  -> I am here to buy products");
        System.out.println("\tview -> view my inventory");
        System.out.println("\tb    -> log out of my user account");
    }

    // REQUIRES: amount is > 0.0
    // MODIFIES: this
    // EFFECTS: loads amount to user's balance
    private void userLoad(Person user) {
        System.out.println("Enter $ amount to load:");
        double amount = input.nextDouble();
        user.loadBalance(amount);
        System.out.println("Current balance: $" + user.getBalance());
        startVisit(user);
    }

    // EFFECTS: prints out user's remaining balance
    private void userBal(Person user) {
        System.out.println("Your current balance is: $" + user.getBalance());
        startVisit(user);
    }

    // MODIFIES: this and user
    // EFFECTS: sells a product from user's inventory to the warehouse inventory
    private void userSell(Person user) {
        sellProducts(user);
        startVisit(user);
    }

    // MODIFIES: this and user
    // EFFECTS: buys a product from the warehouse inventory to a user's inventory
    private void userBuy(Person user) {
        System.out.println("all -> view all products");
        System.out.println("range -> filter within price range");
        System.out.println("sale -> filter by sale");
        System.out.println("category -> filter by category");
        System.out.println("used -> filter by used condition");
        String filterBy = input.next();
        buyProducts(user, filterBy);
        startVisit(user);
    }

    // EFFECTS: prints out user's inventory of products
    private void userView(Person user) {
        for (Product p : user.getInventory()) {
            System.out.println(p.getTitle());
        }
        System.out.println("You have " + user.getInventory().size() + " item(s) in your inventory.");
        startVisit(user);
    }


    // MODIFIES: this and user
    // EFFECTS: allows user to sell a product in their inventory
    private void sellProducts(Person user) {
        System.out.println("Enter the name of the product you wish to sell:");
        String title = input.next();
        for (Product p : user.getInventory()) {
            if (p.getTitle().equals(title)) {
                System.out.println("Enter the price you wish to sell it for:");
                Double price = input.nextDouble();
                p.changePrice(price);
                warehouse.sell(p);
                System.out.println("Your " + p.getTitle() + " is now for sale for $" + p.getPrice() + "!");
                startVisit(user);
            }
        }
        System.out.println("Product not found in your inventory.");
    }

    // MODIFIES: this and user
    // EFFECTS: provides viewing and buying options of products
    private void buyProducts(Person user, String filterBy) {
        if (filterBy.equals("all")) {
            System.out.println("All products:");
            printAllProducts();
        } else if (filterBy.equals("range")) {
            printPriceRangeProducts();
        } else if (filterBy.equals("sale")) {
            printSaleProducts();
        } else if (filterBy.equals("category")) {
            printCategoryProducts();
        } else if (filterBy.equals("used")) {
            printUsedProducts();
        } else {
            System.out.println("Selection not valid...");
            System.out.println("All products:");
            printAllProducts();
        }
        makePurchase(user);
    }

    // REQUIRES: productName is in warehouse inventory
    // MODIFIES: this
    // EFFECTS: allows user to make a purchase from warehouse after viewing products
    private void makePurchase(Person user) {
        System.out.println("Enter product name you would like to purchase:");
        String productName = input.next();
        for (Product p : warehouse.getInventory()) {
            if (p.getTitle().equals(productName)) {
                if (warehouse.makeSale(p, user)) {
                    System.out.println("You have successfully bought " + p.getTitle() + " for $" + p.getActualPrice());
                    startVisit(user);
                } else {
                    System.out.println("Insufficient funds.");
                }
            }
        }
    }

    private void printAllProducts() {
        for (Product p : warehouse.getInventory()) {
            System.out.println(p.getTitle() + " -$" + p.getActualPrice() + "- ");
        }
    }

    private void printPriceRangeProducts() {
        System.out.println("Enter minimum price:");
        double min = input.nextDouble();
        System.out.println("Enter maximum price:");
        double max = input.nextDouble();
        for (Product p : warehouse.filterWithinPriceRange(min, max)) {
            System.out.println(p.getTitle() + " -$" + p.getActualPrice() + "- ");
        }
    }

    private void printSaleProducts() {
        System.out.println("Do you want to view products on sale? (Enter 'true' or 'false')");
        for (Product p : warehouse.filterForSale(Boolean.parseBoolean(input.next()))) {
            System.out.println(p.getTitle() + " -$" + p.getActualPrice() + "- ");
        }
    }

    private void printCategoryProducts() {
        System.out.println("Choose a category to filter by:");
        System.out.println("\tappliances");
        System.out.println("\tautomotive");
        System.out.println("\tbooks");
        System.out.println("\tclothing");
        System.out.println("\telectronics");
        String category = input.next().toUpperCase();
        for (Product p : warehouse.filterByCategory(Category.valueOf(category))) {
            System.out.println(p.getTitle() + " -$" + p.getActualPrice() + "- ");
        }
    }

    private void printUsedProducts() {
        System.out.println("Do you want to view products that are used? (Enter 'true' or 'false')");
        for (Product p : warehouse.filterByUsed(Boolean.parseBoolean(input.next()))) {
            System.out.println(p.getTitle() + " -$" + p.getActualPrice() + "- ");
        }
    }

}
