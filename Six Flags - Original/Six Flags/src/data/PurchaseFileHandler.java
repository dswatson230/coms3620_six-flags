package data;

import interfaces.types.ItemType;
import interfaces.types.Location;
import interfaces.PurchaseFileHandlerInterface;
import models.item.CartItem;
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
                List<CartItem> items = record.getItems();
                if (itemIndex >= 0 && itemIndex < items.size()) {
                    CartItem item = items.get(itemIndex);
                    items.set(itemIndex, new CartItem(
                        item.getItem(), item.getType(), item.getLocation(), newQuantity));
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
        // Format: recordId|status|type,name,price,location,qty;type,name,price,location,qty
        String[] parts = line.split("\\|");
        if (parts.length != 3) return null;

        String recordId  = parts[0].trim();
        String status    = parts[1].trim();
        String itemsPart = parts[2].trim();

        List<CartItem> items = new ArrayList<>();
        if (!itemsPart.isEmpty()) {
            for (String itemStr : itemsPart.split(";")) {
                String[] fields = itemStr.split(",");
                if (fields.length == 5) {
                    ItemType type = ItemType.valueOf(fields[0].trim());
                    items.add(new CartItem(
                        type.create(fields[1].trim(), Double.parseDouble(fields[2].trim())),
                        type,
                        Location.valueOf(fields[3].trim()),
                        Integer.parseInt(fields[4].trim())
                    ));
                }
            }
        }
        return new PurchaseRecord(recordId, items, status);
    }
}