package ui.gui;

import model.Category;
import model.Person;
import model.Product;
import model.Warehouse;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class StoreManager extends JFrame implements ActionListener {
    // Graphical user interface for a store manager

    private JPanel panel;
    private JFrame frame;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JTextField textFieldName;
    private JTextField textFieldPrice;
    private JTextField textFieldCategory;
    private JTextField textFieldCategory1;
    private JTextField textFieldCategory2;
    private JTextField textFieldSale;
    private JCheckBox checkBox1;
    private JCheckBox checkBox2;
    private Person boss = new Person("Store Owner");
    private Warehouse warehouse;

    // MODIFIES: this
    // EFFECTS: offers methods available to a store manager
    public StoreManager(Warehouse warehouse) {
        panel = new JPanel();
        frame = new JFrame("Store Manager");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        this.warehouse = warehouse;
        init();

        stockProducts();
        applySale();
        removeSale();

        panel.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
    }

    private void init() {
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        panel.add(label5);
        panel.add(label6);
        button1 = new JButton();
        button2 = new JButton();
        button3 = new JButton();
        panel.add(button1);
        panel.add(button2);
        panel.add(button3);
    }

    // MODIFIES: this
    // EFFECTS: setup layout for store manager to stock new products to warehouse
    private void stockProducts() {
        label1.setText("Stock Products");
        label1.setBounds(100, 100, 100, 25);
        textFieldName = new JTextField("Name");
        textFieldName.setBounds(100, 150, 100,25);
        panel.add(textFieldName);
        textFieldPrice = new JTextField("Price");
        textFieldPrice.setBounds(210, 150, 100, 25);
        panel.add(textFieldPrice);
        textFieldCategory = new JTextField("Category");
        textFieldCategory.setBounds(320, 150, 100,25);
        panel.add(textFieldCategory);
        button1.setText("Stock");
        button1.setBounds(430, 150, 100, 25);
        button1.setActionCommand("stock");
        button1.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: setup layout for store manager to apply discount to certain products in warehouse
    private void applySale() {
        label2.setText("Apply Sale");
        label2.setBounds(100, 250, 100, 25);
        textFieldCategory1 = new JTextField("Category");
        textFieldCategory1.setBounds(100, 300, 100, 25);
        panel.add(textFieldCategory1);
        checkBox1 = new JCheckBox("All");
        checkBox1.setBounds(210, 300, 25, 25);
        checkBox1.setActionCommand("allSale");
        checkBox1.addActionListener(this);
        panel.add(checkBox1);
        textFieldSale = new JTextField("Sale %");
        textFieldSale.setBounds(245, 300, 100, 25);
        panel.add(textFieldSale);
        button2.setText("Apply");
        button2.setBounds(355, 300, 100, 25);
        button2.setActionCommand("sale");
        button2.addActionListener(this);
        panel.add(button2);
    }

    // MODIFIES: this
    // EFFECTS: setup layout for store manager to remove discount from certain products in warehouse
    private void removeSale() {
        label3.setText("Remove Sale");
        label3.setBounds(100, 350, 100, 25);
        textFieldCategory2 = new JTextField("Category");
        textFieldCategory2.setBounds(100, 400, 100, 25);
        panel.add(textFieldCategory2);
        checkBox2 = new JCheckBox("All");
        checkBox2.setBounds(210, 400, 25, 25);
        checkBox2.setActionCommand("allNoSale");
        checkBox2.addActionListener(this);
        panel.add(checkBox2);
        button3.setText("Remove");
        button3.setBounds(245, 400, 100, 25);
        button3.setActionCommand("reg");
        button3.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: performs store manager duties, based on action command
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("stock")) {
            stock();
        } else if (e.getActionCommand().equals("sale")) {
            sale();
        } else if (e.getActionCommand().equals("reg")) {
            regularPrice();
        }
    }

    // MODIFIES: this
    // EFFECTS: stocks product with given text input of product information to warehouse
    private void stock() {
        String name = textFieldName.getText();
        double price = Double.parseDouble(textFieldPrice.getText());
        Category category = Category.valueOf(textFieldCategory.getText().toUpperCase());
        warehouse.addToInventory(new Product(name, price, category, boss));
        label4.setText(name + " has been added to Warehouse's inventory.");
        label4.setBounds(100, 600, 400, 25);
        tempLabel(label4);
    }

    // MODIFIES: this
    // EFFECTS: applies % discount to all products in entered category, or all
    private void sale() {
        double salePercent = Double.parseDouble(textFieldSale.getText());
        if (checkBox1.isSelected()) {
            for (Product p : warehouse.getInventory()) {
                p.markSale((1 - salePercent / 100) * p.getPrice());
            }
        } else {
            Category category = Category.valueOf(textFieldCategory1.getText().toUpperCase());
            for (Product p : warehouse.getInventory()) {
                if (p.getCategory().equals(category)) {
                    p.markSale((1 - salePercent / 100) * p.getPrice());
                }
            }
            label5.setText("All products in " + category + " are now " + salePercent + "% off.");
            label5.setBounds(100, 600, 400, 25);
            tempLabel(label5);
        }
    }

    // MODIFIES: this
    // EFFECTS: removes discount from all products in entered category, or all products
    private void regularPrice() {
        Category category = Category.valueOf(textFieldCategory2.getText().toUpperCase());
        if (checkBox2.isSelected()) {
            for (Product p : warehouse.getInventory()) {
                p.removeSale();
            }
        } else {
            for (Product p : warehouse.getInventory()) {
                if (p.getCategory().equals(category)) {
                    p.removeSale();
                }
            }
        }
        label6.setText("All products in " + category + " are now regular price.");
        label6.setBounds(100, 600, 400, 25);
        tempLabel(label6);
    }

    // EFFECTS: lets label only appear for 5 seconds
    private void tempLabel(JLabel label) {
        Timer timer = new Timer(5000, e -> {
            label.setVisible(false);
        });
        timer.setRepeats(false);
        timer.start();
        frame.setVisible(true);
    }
}
