package ui.gui;

import model.Person;
import model.Product;
import model.Warehouse;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class User extends JPanel implements ActionListener, ListSelectionListener {
    // Graphical user interface for a user viewing personal information

    private JFrame frame;
    private JPanel panel;
    private JPanel topPanel;
    private JScrollPane listScrollPane;
    private JPanel bottomPanel;
    private JList list;
    private DefaultListModel listModel;
    private JTextField amount;
    private JButton loadButton;
    private JButton viewButton;
    private JTextField price;
    private JButton sellButton;
    private JButton buyButton;
    private JLabel label1;
    private JLabel label2;
    private JLabel label3;
    private Warehouse warehouse;
    private Person user;

    public User(Warehouse warehouse, Person user) {
        super(new BorderLayout());
        this.warehouse = warehouse;
        this.user = user;
        listPanel();
        setPanelComponents();
        setPanels();

        frame = new JFrame(user.getName() + "'s Account");
        frame.setSize(1000, 700);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        panel = new JPanel();
        frame.add(panel);

        panel.add(topPanel, BorderLayout.PAGE_START);
        panel.add(listScrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.PAGE_END);

        frame.setVisible(true);
        frame.setResizable(false);
    }

    // MODIFIES: this
    // EFFECTS: creates model for listScrollPane
    private void listPanel() {
        listModel = new DefaultListModel();
        for (Product p : user.getInventory()) {
            listModel.addElement(p.getTitle());
        }
        list = new JList(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setSelectedIndex(0);
        list.addListSelectionListener(this);
        list.setVisibleRowCount(35);
        listScrollPane = new JScrollPane(list);
    }

    // MODIFIES: this
    // EFFECTS: creates objects for each component in topPanel and bottomPanel
    private void setPanelComponents() {
        amount = new JTextField("$ Amount", 10);
        amount.addActionListener(this);

        loadButton = new JButton("Load");
        loadButton.setActionCommand("load");
        loadButton.addActionListener(this);

        viewButton = new JButton("View Balance");
        viewButton.setActionCommand("view");
        viewButton.addActionListener(this);

        price = new JTextField("Enter Selling Price $");
        price.addActionListener(this);

        sellButton = new JButton("Sell Selected Product");
        sellButton.setActionCommand("sell");
        sellButton.addActionListener(this);

        buyButton = new JButton("Buy Products");
        buyButton.setActionCommand("buy");
        buyButton.addActionListener(this);
    }

    // MODIFIES: this
    // EFFECTS: creates panels for top and bottom panels of buttons and text fields
    private void setPanels() {
        topPanel = new JPanel();
        topPanel.setLayout(new BoxLayout(topPanel, BoxLayout.X_AXIS));
        topPanel.add(amount);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(loadButton);
        topPanel.add(Box.createHorizontalStrut(5));
        topPanel.add(viewButton);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        bottomPanel = new JPanel();
        bottomPanel.setLayout(new BoxLayout(bottomPanel, BoxLayout.X_AXIS));
        bottomPanel.add(price);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(sellButton);
        bottomPanel.add(Box.createHorizontalStrut(5));
        bottomPanel.add(buyButton);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }


    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("load")) {
            user.loadBalance(Double.parseDouble(amount.getText()));
            label1 = new JLabel("Current Balance: $" + user.getBalance());
            panel.add(label1);
            tempLabel(label1);
        } else if (e.getActionCommand().equals("view")) {
            label2 = new JLabel("Current Balance: $" + user.getBalance());
            panel.add(label2);
            tempLabel(label2);
        } else if (e.getActionCommand().equals("sell")) {
            String productName = listModel.getElementAt(list.getSelectedIndex()).toString();
            for (Product p : user.getInventory()) {
                if (p.getTitle().equals(productName)) {
                    p.changePrice(Double.parseDouble(price.getText()));
                    warehouse.sell(p);
                    label3 = new JLabel("Your " + p.getTitle() + " is now for sale for $" + p.getPrice());
                    panel.add(label3);
                    tempLabel(label3);
                }
            }
        } else if (e.getActionCommand().equals("buy")) {
            new Market(warehouse, user);
        }
    }


    @Override
    public void valueChanged(ListSelectionEvent e) {
        if (!e.getValueIsAdjusting()) {
            sellButton.setEnabled(list.getSelectedIndex() != -1);
        }
    }

    // EFFECTS: lets label only appear for 5 seconds
    private void tempLabel(JLabel label) {
        label.setBounds(100, 625, 400, 25);
        Timer timer = new Timer(5000, e -> {
            label.setVisible(false);
        });
        timer.setRepeats(false);
        timer.start();
    }
}
