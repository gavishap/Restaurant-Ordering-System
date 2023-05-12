import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.io.FileNotFoundException;

public class RestaurantAppGUI {
    private JFrame frame;
    private JPanel mainPanel;
    private JButton orderButton;
    private HashMap<MenuItem, JCheckBox> checkBoxes;
    private HashMap<MenuItem, JComboBox<Integer>> comboBoxes;

    public RestaurantAppGUI(Menu menu) {
        frame = new JFrame("Restaurant App");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        checkBoxes = new HashMap<>();
        comboBoxes = new HashMap<>();

        LinkedHashMap<String, ArrayList<MenuItem>> categorizedItems = new LinkedHashMap<>();
        for (MenuItem item : menu.getItems()) {
            String category = item.getCategory();
            if (!categorizedItems.containsKey(category)) {
                categorizedItems.put(category, new ArrayList<>());
            }
            categorizedItems.get(category).add(item);
        }

        for (String category : categorizedItems.keySet()) {
            JPanel categoryPanel = new JPanel();
            categoryPanel.setLayout(new BoxLayout(categoryPanel, BoxLayout.Y_AXIS));
            categoryPanel.setBorder(BorderFactory.createTitledBorder(category));

            for (MenuItem item : categorizedItems.get(category)) {
                JPanel itemPanel = new JPanel();
                itemPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

                JCheckBox checkBox = new JCheckBox(item.getDescription() + " - $" + item.getPrice());
                itemPanel.add(checkBox);
                checkBoxes.put(item, checkBox);

                Integer[] quantities = new Integer[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };
                JComboBox<Integer> comboBox = new JComboBox<>(quantities);
                itemPanel.add(comboBox);
                comboBoxes.put(item, comboBox);

                categoryPanel.add(itemPanel);
            }

            mainPanel.add(categoryPanel);
        }

        orderButton = new JButton("Order");
        orderButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                double total = 0;
                StringBuilder orderSummary = new StringBuilder("Your order:\n");

                for (MenuItem item : menu.getItems()) {
                    if (checkBoxes.get(item).isSelected()) {
                        int quantity = (int) comboBoxes.get(item).getSelectedItem();
                        double itemTotal = item.getPrice() * quantity;
                        total += itemTotal;
                        orderSummary.append(item.getDescription()).append(" x").append(quantity).append(" - $")
                                .append(itemTotal).append("\n");
                    }
                }

                orderSummary.append("Total price: $").append(total);

                String[] options = { "Confirm", "Update", "Cancel" };
                int choice = JOptionPane.showOptionDialog(frame, orderSummary.toString(), "Order Summary",
                        JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, options[0]);

                if (choice == 0) { // Confirm
                    String name = JOptionPane.showInputDialog(frame, "Enter your full name:", "Name",
                            JOptionPane.QUESTION_MESSAGE);
                    String id = JOptionPane.showInputDialog(frame, "Enter your ID number:", "ID",
                            JOptionPane.QUESTION_MESSAGE);

                    String filename = name + id + ".txt";
                    while (Files.exists(Paths.get(filename))) {
                        JOptionPane.showMessageDialog(frame,
                                "Name and ID combination already exists. Please try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                        name = JOptionPane.showInputDialog(frame, "Enter your full name:", "Name",
                                JOptionPane.QUESTION_MESSAGE);
                        id = JOptionPane.showInputDialog(frame, "Enter your ID number:", "ID",
                                JOptionPane.QUESTION_MESSAGE);
                        filename = name + id + ".txt";
                    }

                    try (FileWriter writer = new FileWriter(new File(filename))) {
                        writer.write(orderSummary.toString());
                    } catch (IOException ioException) {
                        JOptionPane.showMessageDialog(frame, "Error saving order. Please try again.", "Error",
                                JOptionPane.ERROR_MESSAGE);
                    }

                    resetOrder();
                } else if (choice == 2) { // Cancel
                    resetOrder();
                }
                // choice == 1, do nothing (Update)
            }

        });

        mainPanel.add(orderButton);

        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
    }

    private void resetOrder() {
        for (JCheckBox checkBox : checkBoxes.values()) {
            checkBox.setSelected(false);
        }
        for (JComboBox<Integer> comboBox : comboBoxes.values()) {
            comboBox.setSelectedIndex(0);
        }
    }

    public void show() {
        frame.setVisible(true);
    }

    public static void main(String[] args) {
        try {
            Menu menu = new Menu("menu.txt");
            RestaurantAppGUI app = new RestaurantAppGUI(menu);
            app.show();
        } catch (FileNotFoundException e) {
            System.out.println("Menu file not found.");
        }
    }
}
