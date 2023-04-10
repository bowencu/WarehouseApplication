package ui.gui;

import model.Event;
import model.EventLog;
import model.Person;
import model.Warehouse;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.event.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class WarehouseGUI extends JFrame implements ActionListener, WindowListener {
    // Graphical user interface of the Warehouse application

    private static final String JSON_STORE = "./data/warehouse.json";

    private Warehouse warehouse;
    private Person boss;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private JFrame frame;
    private JPanel panel;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private JLabel label4;
    private JLabel label5;
    private JLabel label6;
    private JLabel label7;
    private JLabel label8;
    private JLabel label9;
    private JTextField textField1;
    private JTextField textField2;
    private JButton button;
    private ImageIcon image;
    private JLabel lighthouse;

    // EFFECTS: calls the constructor to begin the Warehouse GUI
    public static void main(String[] args) {
        new WarehouseGUI();
    }

    // MODIFIES: this
    // EFFECTS: runs the Warehouse application for GUI
    public WarehouseGUI() {
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        warehouse = new Warehouse();
        boss = new Person("Store Owner");
        warehouse.addToUsers(boss);
        frame = new JFrame("Warehouse");
        panel = new JPanel();
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        setImage();
        labelInit();
        newUser();
        login();
        addButton("Store Manager", 0, "storeManager");
        addButton("Save", 200, "save");
        addButton("Open", 400, "open");

        panel.setLayout(null);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.addWindowListener(this);
    }

    // EFFECTS: initializes label objects
    private void labelInit() {
        label1 = new JLabel();
        label2 = new JLabel();
        label3 = new JLabel();
        label4 = new JLabel();
        label5 = new JLabel();
        label6 = new JLabel();
        label7 = new JLabel();
        label8 = new JLabel();
        label9 = new JLabel();
        panel.add(label1);
        panel.add(label2);
        panel.add(label3);
        panel.add(label4);
        panel.add(label5);
        panel.add(label6);
        panel.add(label7);
        panel.add(label8);
        panel.add(label9);
    }

    // EFFECTS: sets up a background of a lighthouse image for login window
    private void setImage() {
        image = new ImageIcon(getClass().getResource("lighthouse.jpg"));
        lighthouse = new JLabel(image);
        //lighthouse.setSize(700, 425);
        lighthouse.setBounds(350,100,600,425);
        panel.add(lighthouse);
    }

    // MODIFIES: this
    // EFFECTS: process command based on button presses
    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("newUser")) {
            Person newUser = new Person(textField1.getText());
            warehouse.addToUsers(newUser);
            new User(warehouse, newUser);
        } else if (e.getActionCommand().equals("login")) {
            for (Person user : warehouse.getUsers()) {
                if (user.getName().equals(textField2.getText())) {
                    new User(warehouse, user);
                }
            }
            label1.setText("Given user name not found");
            label1.setBounds(100, 600, 400, 25);
            tempLabel(label1);
        } else if (e.getActionCommand().equals("storeManager")) {
            new StoreManager(warehouse);
        } else if (e.getActionCommand().equals("save")) {
            saveWarehouse();
        } else if (e.getActionCommand().equals("open")) {
            openWarehouse();
        }
    }

    // MODIFIES: this
    // EFFECTS: setup layout for creating a new user
    private void newUser() {
        label2.setText("New User");
        label2.setBounds(85, 100, 150, 25);
        label8.setText("Name:");
        label8.setBounds(50, 150, 80, 25);
        textField1 = new JTextField();
        textField1.setBounds(130, 150, 165, 25);
        panel.add(textField1);
        button = new JButton("Create New User");
        button.setBounds(85, 200, 150, 25);
        button.setActionCommand("newUser");
        button.addActionListener(this);
        panel.add(button);
    }

    // MODIFIES: this
    // EFFECTS: setup layout for existing users to log in to their account
    private void login() {
        label3.setText("Returning User");
        label3.setBounds(85, 400, 150, 25);
        label9.setText("Name:");
        label9.setBounds(50, 450, 80, 25);
        textField2 = new JTextField();
        textField2.setBounds(130, 450, 165, 25);
        panel.add(textField2);
        button = new JButton("Login");
        button.setBounds(85, 500, 150, 25);
        button.setActionCommand("login");
        button.addActionListener(this);
        panel.add(button);
    }

    // REQUIRES: label is one of "Store Manager", "Save", or "Load"
    //           x is one of 0, 200, or 400
    //           buttonCommand is one of "storeManager", "save", or "load"
    // MODIFIES: this
    // EFFECTS: layout for a button for store manager, saving, and loading data
    private void addButton(String label, int x, String buttonCommand) {
        button = new JButton(label);
        button.setBounds(100 + x, 50, 150, 25);
        button.setActionCommand(buttonCommand);
        button.addActionListener(this);
        panel.add(button);
    }

    // EFFECTS: saves the warehouse to file
    private void saveWarehouse() {
        try {
            jsonWriter.open();
            jsonWriter.write(warehouse);
            jsonWriter.close();
            label4.setText("Saved Warehouse to " + JSON_STORE);
            label4.setBounds(100, 600, 400, 25);
            tempLabel(label4);
        } catch (FileNotFoundException e) {
            label5.setText("Unable to write to file: " + JSON_STORE);
            label5.setBounds(100, 600, 400, 25);
            tempLabel(label5);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads warehouse from file
    private void openWarehouse() {
        try {
            warehouse = jsonReader.read();
            label6.setText("Loaded Warehouse from " + JSON_STORE);
            label6.setBounds(100, 600, 400, 25);
            tempLabel(label6);
        } catch (IOException e) {
            label7.setText("Unable to read from file: " + JSON_STORE);
            label7.setBounds(100, 600, 400, 25);
            tempLabel(label7);
        }
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

    @Override
    public void windowOpened(WindowEvent e) {

    }

    @Override
    public void windowClosing(WindowEvent e) {
        for (Event event : EventLog.getInstance()) {
            System.out.println(event.toString());
        }
        System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {

    }

    @Override
    public void windowIconified(WindowEvent e) {

    }

    @Override
    public void windowDeiconified(WindowEvent e) {

    }

    @Override
    public void windowActivated(WindowEvent e) {

    }

    @Override
    public void windowDeactivated(WindowEvent e) {

    }

}
