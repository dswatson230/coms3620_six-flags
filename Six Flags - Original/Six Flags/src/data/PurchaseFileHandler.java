package data;

import interfaces.Location;
import interfaces.PurchaseFileHandlerInterface;
import models.InventoryItem;
import models.Item;
import models.PurchaseRecord;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class PurchaseFileHandler implements PurchaseFileHandlerInterface {
    private final Path purchaseFilePath;

    public PurchaseFileHandler(String purchaseFilePath) {
        this.purchaseFilePath = Paths.get(purchaseFilePath);
    }

    @Override
    public PurchaseRecord findRecordById(String recordId) throws IOException {
        for (PurchaseRecord record : readAllRecords()) {
            if (record.getRecordId().equals(recordId)) return record;
        }
        return null;
    }

    @Override
    public List<PurchaseRecord> readAllRecords() throws IOException {
        List<PurchaseRecord> records = new ArrayList<>();
        if (Files.notExists(purchaseFilePath)) return records;

        for (String line : Files.readAllLines(purchaseFilePath)) {
            if (line.trim().isEmpty()) continue;
            PurchaseRecord record = parseRecord(line);
            if (record != null) records.add(record);
        }
        return records;
    }

    @Override
    public boolean updateRecordStatus(String recordId, String status) throws IOException {
        List<PurchaseRecord> records = readAllRecords();
        boolean updated = false;
        for (PurchaseRecord record : records) {
            if (record.getRecordId().equals(recordId)) {
                record.setStatus(status);
                updated = true;
                break;
            }
        }
        if (updated) writeAllRecords(records);
        return updated;
    }

    @Override
    public boolean updateItemQuantity(String recordId, int itemIndex, int newQuantity) throws IOException {
        List<PurchaseRecord> records = readAllRecords();
        boolean updated = false;
        for (PurchaseRecord record : records) {
            if (record.getRecordId().equals(recordId)) {
                List<Item> items = record.getItems();
                if (itemIndex >= 0 && itemIndex < items.size()) {
                    Item item = items.get(itemIndex);
                    items.set(itemIndex, new InventoryItem(
                        item.getName(), item.getPrice(), item.getLocation(), newQuantity));
                    updated = true;
                }
                break;
            }
        }
        if (updated) writeAllRecords(records);
        return updated;
    }

    public boolean writeRecord(PurchaseRecord record) throws IOException {
        if (purchaseFilePath.getParent() != null) {
            Files.createDirectories(purchaseFilePath.getParent());
        }
        if (Files.notExists(purchaseFilePath)) {
            Files.createFile(purchaseFilePath);
        }
        String line = record.toFileString() + System.lineSeparator();
        Files.writeString(purchaseFilePath, line, StandardOpenOption.APPEND);
        return true;
    }

    private void writeAllRecords(List<PurchaseRecord> records) throws IOException {
        StringBuilder contents = new StringBuilder();
        for (PurchaseRecord record : records) {
            contents.append(record.toFileString()).append(System.lineSeparator());
        }
        Files.writeString(purchaseFilePath, contents.toString(),
            StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
    }

    private PurchaseRecord parseRecord(String line) {
        // Format: recordId|status|name,price,qty,location;name,price,qty,location
        String[] parts = line.split("\\|");
        if (parts.length != 3) return null;

        String recordId  = parts[0].trim();
        String status    = parts[1].trim();
        String itemsPart = parts[2].trim();

        List<Item> items = new ArrayList<>();
        if (!itemsPart.isEmpty()) {
            for (String itemStr : itemsPart.split(";")) {
                String[] fields = itemStr.split(",");
                if (fields.length == 4) {
                    items.add(new InventoryItem(
                        fields[0].trim(),
                        Double.parseDouble(fields[1].trim()),
                        Location.valueOf(fields[3].trim()),
                        Integer.parseInt(fields[2].trim())
                    ));
                }
            }
        }
        return new PurchaseRecord(recordId, items, status);
    }
}