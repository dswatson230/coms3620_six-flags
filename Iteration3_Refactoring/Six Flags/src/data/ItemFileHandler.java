package data;
 
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.InventoryItem;

import java.io.*;
import java.util.ArrayList;
 
public class ItemFileHandler {
    //private static final String FILE_PATH = "data/inventory.txt";
    private static final String FILE_PATH = "C:/Users/longi/COMS/coms3620/six-flags/Six Flags/data/inventory.txt";
 
    public boolean writeItem(InventoryItem item) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write(item.getType().name() + "," +
                         item.getName() + "," +
                         item.getPrice() + "," +
                         item.getLocation().name() + "," +
                         item.getQuantity());
            writer.newLine();
            return true;
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
            return false;
        }
    }
 
    public boolean writeAllItems(ArrayList<InventoryItem> items) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, false))) {
            for (InventoryItem item : items) {
                writer.write(item.getType().name() + "," +
                             item.getName() + "," +
                             item.getPrice() + "," +
                             item.getLocation().name() + "," +
                             item.getQuantity());
                writer.newLine();
            }
            return true;
        } catch (IOException e) {
            System.out.println("File write error: " + e.getMessage());
            return false;
        }
    }
 
    public ArrayList<InventoryItem> readItems() {
        ArrayList<InventoryItem> items = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return items;
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 5) {
                    ItemType type = ItemType.valueOf(parts[0].trim());
                    items.add(new InventoryItem(type.create(parts[1].trim(), Double.parseDouble(parts[2])),
                        type,
                        Location.valueOf(parts[3].trim()),
                        Integer.parseInt(parts[4].trim())
                    ));
                }
            }
        } catch (IOException e) {
            System.out.println("File read error: " + e.getMessage());
        }
        return items;
    }
}