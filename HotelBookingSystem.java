import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;

class HotelBookingSystem extends JFrame {

    private JTextArea summaryArea;
    private JComboBox<String> itemComboBox;
    private JTextField quantityField;
    private ArrayList<OrderDetails> orderList;
    private double gstRate = 0.18; // GST rate of 18%

    // Menu items with their respective prices
    private String[] menuItems = {
        "Burger - Rs.80", 
        "Noodles - Rs.150", 
        "Veg Farmhouse Pizza - Rs.220", 
        "Garlic Bread - Rs.170", 
        "Coke (300ml) - Rs.50", 
        "Veg Sandwich - Rs.110"
    };
    private double[] itemPrices = { 80, 150, 220, 170, 50, 110 };

    // Hotel name and address
    private String hotelName = "Shlok Restaurant";
    private String hotelAddress = "Kansari Road, Khambhat, 388620";
    
    public HotelBookingSystem() {
        setTitle("Restaurant Order Booking System");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        orderList = new ArrayList<>();

        // Main Panel Layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 2));  // Adjusted the layout size
        panel.setBackground(new Color(255, 255, 255)); // White background

        // Hotel Name label in bold and large font
        JLabel hotelLabel = new JLabel(hotelName, SwingConstants.CENTER);
        hotelLabel.setFont(new Font("Serif", Font.BOLD, 20));
        hotelLabel.setForeground(new Color(255, 87, 34)); // Warm Orange color
        panel.add(hotelLabel);

        // Hotel Address label in smaller font
        JLabel addressLabel = new JLabel(hotelAddress, SwingConstants.CENTER);
        addressLabel.setFont(new Font("Serif", Font.PLAIN, 14));
        addressLabel.setForeground(new Color(33, 33, 33)); // Dark Text color
        panel.add(addressLabel);

        // Item selection dropdown
        panel.add(new JLabel("Select Item:"));
        itemComboBox = new JComboBox<>(menuItems);
        panel.add(itemComboBox);

        // Quantity input
        panel.add(new JLabel("Quantity:"));
        quantityField = new JTextField();
        panel.add(quantityField);

        // Add button with "+" icon
        JButton addButton = new JButton("Add (+)");
        addButton.setBackground(new Color(33, 150, 243)); // Blue
        addButton.setForeground(Color.WHITE);
        addButton.setFont(new Font("Arial", Font.BOLD, 14));
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                addOrder();
            }
        });

        // Cancel order and summary buttons
        JButton cancelButton = new JButton("Cancel Order");
        cancelButton.setBackground(new Color(244, 67, 54)); // Red
        cancelButton.setForeground(Color.WHITE);
        JButton summaryButton = new JButton("Print Summary");
        summaryButton.setBackground(new Color(33, 150, 243)); // Blue
        summaryButton.setForeground(Color.WHITE);
        JButton billButton = new JButton("Print Bill");
        billButton.setBackground(new Color(255, 193, 7)); // Yellow
        billButton.setForeground(Color.WHITE);

        // Action listeners for buttons
        cancelButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cancelOrder();
            }
        });

        summaryButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printSummary();
            }
        });

        billButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                printBill();
            }
        });

        // Add buttons to the panel
        panel.add(addButton);
        panel.add(cancelButton);
        panel.add(summaryButton);
        panel.add(billButton);

        // Summary Text Area with custom background and text color
        summaryArea = new JTextArea(5, 20);
        summaryArea.setEditable(false);
        summaryArea.setBackground(new Color(240, 240, 240)); // Light Grey background
        summaryArea.setForeground(new Color(33, 33, 33)); // Dark Text color
        JScrollPane scroll = new JScrollPane(summaryArea);

        // Final Layout
        getContentPane().setLayout(new BorderLayout());
        getContentPane().add(panel, BorderLayout.NORTH);
        getContentPane().add(scroll, BorderLayout.CENTER);
    }

    private void addOrder() {
        String selectedItem = (String) itemComboBox.getSelectedItem();
        int quantity;

        try {
            quantity = Integer.parseInt(quantityField.getText());

            if (quantity <= 0) {
                JOptionPane.showMessageDialog(this, "Please enter a valid quantity.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Get the item index
            int itemIndex = itemComboBox.getSelectedIndex();
            String itemName = selectedItem.split(" - ")[0];
            double itemPrice = itemPrices[itemIndex];

            // Create the order and add it to the list
            OrderDetails order = new OrderDetails(itemName, itemPrice, quantity);
            orderList.add(order);
            JOptionPane.showMessageDialog(this, quantity + " " + itemName + "(s) Added to Order!");
            quantityField.setText(""); // Reset quantity field after adding
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid quantity entered!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void cancelOrder() {
        String selectedItem = (String) itemComboBox.getSelectedItem();
        String itemName = selectedItem.split(" - ")[0];
        boolean found = false;

        for (OrderDetails order : orderList) {
            if (order.getItemName().equalsIgnoreCase(itemName)) {
                orderList.remove(order);
                found = true;
                JOptionPane.showMessageDialog(this, "Order Cancelled Successfully!");
                break;
            }
        }

        if (!found) {
            JOptionPane.showMessageDialog(this, "Order not found!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void printSummary() {
        if (orderList.isEmpty()) {
            summaryArea.setText("No orders placed yet.");
        } else {
            StringBuilder summary = new StringBuilder("Current Orders:\n");
            for (OrderDetails order : orderList) {
                summary.append(order.getItemName()).append(" x").append(order.getQuantity())
                        .append(" - Rs.").append(order.getTotalPrice()).append("\n");
            }
            summaryArea.setText(summary.toString());
        }
    }

    private void printBill() {
        if (orderList.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No orders to generate bill.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            double totalCost = 0;
            for (OrderDetails order : orderList) {
                totalCost += order.getTotalPrice();
            }
            double gst = totalCost * gstRate; // GST of 18%
            double finalBill = totalCost + gst;

            // Bill output with hotel name and address
            String bill = "<html><h1 style='font-family: Arial; color: #FF5722;'>" + hotelName + "</h1>"
                        + "<h3>" + hotelAddress + "</h3>"
                        + "Total Cost: Rs." + totalCost + "<br>"
                        + "GST (18%): Rs." + gst + "<br>"
                        + "Final Bill: Rs." + finalBill + "</html>";

            JLabel billLabel = new JLabel(bill);
            JOptionPane.showMessageDialog(this, billLabel, "Bill Summary", JOptionPane.INFORMATION_MESSAGE);

            // Prompt for downloading the bill
            int choice = JOptionPane.showConfirmDialog(this, "Would you like to download the bill?", "Download Bill", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                saveBillToFile(totalCost, gst, finalBill);
            }
        }
    }

    private void saveBillToFile(double totalCost, double gst, double finalBill) {
        try {
            // Create the file in D:/ with a timestamp
            String filename = "D:/HotelBill_" + System.currentTimeMillis() + ".txt";
            BufferedWriter writer = new BufferedWriter(new FileWriter(filename));

            // Write the bill content to the text file
            writer.write("Shlok Restaurant\n");
            writer.write("Kansari Road, Khambhat, 388620\n\n");
            writer.write("Total Cost: Rs." + totalCost + "\n");
            writer.write("GST (18%): Rs." + gst + "\n");
            writer.write("Final Bill: Rs." + finalBill + "\n");

            writer.close();
            JOptionPane.showMessageDialog(this, "Bill saved successfully to " + filename);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error saving the bill.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new HotelBookingSystem().setVisible(true);
            }
        });
    }
}

class OrderDetails {
    private String itemName;
    private double itemPrice;
    private int quantity;

    public OrderDetails(String itemName, double itemPrice, int quantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return itemPrice * quantity;
    }
}