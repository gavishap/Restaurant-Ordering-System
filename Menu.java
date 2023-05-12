import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.FileNotFoundException;

public class Menu {
    private ArrayList<MenuItem> items;

    public Menu(String filename) throws FileNotFoundException {
        items = new ArrayList<>();
        try {
            List<String> lines = Files.readAllLines(Paths.get(filename));

            for (int i = 0; i < lines.size(); i += 3) {
                String itemName = lines.get(i).trim();
                String itemCategory = lines.get(i + 1).trim();
                double itemPrice = Double.parseDouble(lines.get(i + 2).trim());
                items.add(new MenuItem(itemName, itemCategory, itemPrice));
            }
        } catch (IOException e) {
            System.out.println("Error reading the menu file.");
        }
    }

    public ArrayList<MenuItem> getItems() {
        return items;
    }
}
