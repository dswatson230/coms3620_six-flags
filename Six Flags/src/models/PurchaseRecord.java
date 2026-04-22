package models;

import interfaces.Location;

import java.util.ArrayList;
import java.util.List;

public class PurchaseRecord {
    private final String    recordId;
    private final List<Item> items;
    private String          status;

    // Non-refundable item names
    private static final List<String> NON_REFUNDABLE = List.of(
        "Funnel Cake", "Hot Dog", "Burger Combo", "Nachos",
        "Pizza Slice", "Lemonade", "BBQ Sandwich", "Cotton Candy",
        "Popcorn", "Soft Pretzel"
    );

    public PurchaseRecord(String recordId, List<Item> items, String status) {
        this.recordId = recordId;
        this.items    = new ArrayList<>(items);
        this.status   = status;
    }

    public String    getRecordId()  { return recordId; }
    public List<Item> getItems()    { return items; }
    public String    getStatus()    { return status; }
    public void setStatus(String status) { this.status = status; }

    public boolean isRefundable(String itemName) {
        for (String name : NON_REFUNDABLE) {
            if (name.equalsIgnoreCase(itemName)) return false;
        }
        return true;
    }

    // Find item by index (0-based) — avoids duplicate name collision
    public Item findItemByIndex(int index) {
        if (index < 0 || index >= items.size()) return null;
        return items.get(index);
    }

    // Find first item matching name — kept for compatibility
    public Item findItem(String itemName) {
        for (Item item : items) {
            if (item.getName().equalsIgnoreCase(itemName)) return item;
        }
        return null;
    }

    public String toFileString() {
        StringBuilder sb = new StringBuilder();
        sb.append(recordId).append("|").append(status).append("|");
        for (int i = 0; i < items.size(); i++) {
            Item item = items.get(i);
            sb.append(item.getName()).append(",")
              .append(item.getPrice()).append(",")
              .append(item.getQuantity()).append(",")
              .append(item.getLocation().name());
            if (i < items.size() - 1) sb.append(";");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "PurchaseRecord{id='" + recordId + "', status='" + status + "', items=" + items.size() + "}";
    }
}