package data;
 
import interfaces.Location;
import models.InventoryItem;
import models.Item;
 
import java.util.ArrayList;
import java.util.List;
 
public class TestDataProvider {
    private final List<String> itemIds   = new ArrayList<>();
    private final List<Item>   testItems = new ArrayList<>();
 
    public TestDataProvider() {
        add("T001", new InventoryItem("General Admission",  39.99, Location.MAGIC_MOUNTAIN, 100));
        add("T002", new InventoryItem("Gold Pass",          89.99, Location.MAGIC_MOUNTAIN,  50));
        add("T003", new InventoryItem("Burger Combo",        9.99, Location.FIESTA_TEXAS,    80));
        add("T004", new InventoryItem("Superman Ride Pass", 15.00, Location.MAGIC_MOUNTAIN,  60));
        add("T005", new InventoryItem("Iron Rattler Pass",  15.00, Location.FIESTA_TEXAS,    60));
    }
 
    private void add(String id, Item item) {
        itemIds.add(id);
        testItems.add(item);
    }
 
    public Item getTestItem(String itemId) {
        int index = itemIds.indexOf(itemId);
        return index >= 0 ? testItems.get(index) : null;
    }
 
    public List<String> getAllIds()   { return itemIds; }
    public List<Item>   getAllItems() { return testItems; }
}
 