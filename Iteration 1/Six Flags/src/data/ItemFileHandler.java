package data;
 
import interfaces.Location;
import models.InventoryItem;
import models.Item;
 
import java.io.*;
import java.util.ArrayList;
 
public class ItemFileHandler {
    private static final String FILE_PATH = "inventory.txt";
 
    public boolean writeItem(Item item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.getName() + "," +
                         item.getPrice() + "," +
                         item.getQuantity() + "," +
                         item.getLocation().name());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
            return false;
        }
    }
 
    public boolean writeAllItems(ArrayList<Item> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (Item item : items) {
                writer.write(item.getName() + "," +
                             item.getPrice() + "," +
                             item.getQuantity() + "," +
                             item.getLocation().name());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
            return false;
        }
    }
 
    public ArrayList<Item> readItems() {
        ArrayList<Item> items = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return items;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 4) {
                    items.add(new InventoryItem(
                        parts[0],
                        Double.parseDouble(parts[1]),
                        Location.valueOf(parts[3]),
                        Integer.parseInt(parts[2])
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("File read error: " + e.getMessage());
        }
        return items;
    }
}