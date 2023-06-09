package ui.gui;

import model.Category;
import model.Person;
import model.Product;
import model.Warehouse;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class Market extends JPanel implements ActionListener, ListSelectionListener {
    // Graphical user interface of Warehouse's marketplace

    private Warehouse warehouse;
    private Person user;
    private JFrame frame;
    private JPanel panel;
    private JScrollPane listScrollPane;
    private JList list;
    private DefaultListModel listModel;
    private JPanel bottomPanel;
    private JTextField minPrice;
    private JTextField maxPrice;
    private JButton rangeButton;
    private JCheckBox onSale;
    private JCheckBox isUsed;
    private JComboBox<String> categories;
    private JButton buyButton;
    private JLabel label1;
    private JLabel label2;

    // EFFECTS: constructs a market window, allowing current user to make a purchase
    public Market(Warehouse warehouse, Person user) {
        super(new BorderLayout());
        this.warehouse = warehouse;
        this.user = user;
        setPanelComponents();
        listPanel();
        setPanel();

        frame = new JFrame("Warehouse Market");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        frame.add(panel);

        panel.add(listScrollPane, BorderLayout.PAGE_START);
        panel.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setVisible(true);
        frame.setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: creates model for listScrollPane
    private void listPanel() {
        listModel = new DefaultListModel();
        for (Product p : warehouse.getInventory()) {
            listModel.addElement(p.getTitle());
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(35);
        listScrollPane = new JScrollPane(list);
        listScrollPane.setSize(600, 500);
    }

    // MODIFIES: this
    // EFFECTS: creates objects for each component in bottomPanel
    private void setPanelComponents() {
        minPrice = new JTextField("min. price");
        maxPrice = new JTextField("max. price");

        rangeButton = new JButton("Price Filter");
        rangeButton.setActionCommand("range");
        rangeButton.addActionListener(this);

        onSale = new JCheckBox("Discounted");
        onSale.setActionCommand("sale");
        onSale.addActionListener(this);
        isUsed = new JCheckBox("Used");
        isUsed.setActionCommand("used");
        isUsed.addActionListener(this);

        String[] items = {"ALL PRODUCTS", "APPLIANCES", "AUTOMOTIVE", "BOOKS", "CLOTHING", "ELECTRONICS", "OTHER"};
        categories = new JComboBox<>(items);
        categories.setActionCommand("category");
        categories.addActionListener(this);

        buyButton = new JButton("Buy");
        buyButton.setActionCommand("buy");
        buyButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: creates bottom panel of buttons, text fields, combo boxes, and checkboxes
    private void setPanel() {
        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.Y_AXIS));
        bottomPanel.add(minPrice);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(maxPrice);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(rangeButton);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(onSale);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(isUsed);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(categories);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(buyButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("range")) {
            filterPriceRange();
        } else if (e.getActionCommand().equals("sale")) {
            filterSale();
        } else if (e.getActionCommand().equals("used")) {
            filterUsed();
        } else if (e.getActionCommand().equals("category")) {
            filterCategory();
        } else if (e.getActionCommand().equals("buy")) {
            buy();
        }
    }

    // MODIFIES: this
    // EFFECTS: filters products in listModel that are in range [min, max]
    private void filterPriceRange() {
        listModel.clear();
        List<Product> filtered;
        double min = Double.parseDouble(minPrice.getText());
        double max = Double.parseDouble(maxPrice.getText());
        filtered = warehouse.filterWithinPriceRange(min, max);
        for (Product p : filtered) {
            listModel.addElement(p.getTitle());
        }
        list = new JList<>(listModel);
    }

    // MODIFIES: this
    // EFFECTS: filters products in listModel that are on sale
    private void filterSale() {
        listModel.clear();
        List<Product> filtered;
        boolean isSelected = onSale.isSelected();
        filtered = warehouse.filterForSale(isSelected);
        for (Product p : filtered) {
            listModel.addElement(p.getTitle());
        }
        list = new JList<>(listModel);
    }

    // MODIFIES: this
    // EFFECTS: filters products in listModel that are used
    private void filterUsed() {
        listModel.clear();
        List<Product> filtered;
        boolean isSelected = isUsed.isSelected();
        filtered = warehouse.filterByUsed(isSelected);
        for (Product p : filtered) {
            listModel.addElement(p.getTitle());
        }
        list = new JList<>(listModel);
    }

    // MODIFIES: this
    // EFFECTS: filters products in listModel that are on sale
    private void filterCategory() {
        listModel.clear();
        List<Product> filtered;
        if (categories.getSelectedItem().toString().equals("ALL PRODUCTS")) {
            filtered = warehouse.getInventory();
        } else {
            Category category = Category.valueOf(categories.getSelectedItem().toString());
            filtered = warehouse.filterByCategory(category);
        }
        for (Product p : filtered) {
            listModel.addElement(p.getTitle());
        }
        list = new JList<>(listModel);
    }

    // MODIFIES: this
    // EFFECTS: allows user to buy product selected in list
    private void buy() {
        for (Product product : warehouse.getInventory()) {
            if (product.getTitle().equals(list.getSelectedValue().toString())) {
                if (warehouse.makeSale(product, user)) {
                    label1 = new JLabel("You have successfully bought " + product.getTitle() + " for $"
                            + product.getActualPrice());
                    tempLabel(label1);
                    panel.add(label1);
                } else {
                    label2 = new JLabel("Insufficient Funds.");
                    tempLabel(label2);
                    panel.add(label2);
                }
            }
        }
    }

    @Override
    public void valueChanged(ListSelectionEvent e) {

    }

    // EFFECTS: makes label only visible for 5 seconds
    private void tempLabel(JLabel label) {
        label.setBounds(100, 625, 400, 25);
        Timer timer = new Timer(5000, e -> {
            label.setVisible(false);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
