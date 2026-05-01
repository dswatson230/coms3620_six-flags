package data;
 
import interfaces.types.ItemType;
import interfaces.types.Location;
import models.item.CartItem;
import models.item.Concession;
import models.item.DailyTicket;
import models.item.SeasonPass;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TestDataProvider {
    private final Map<String, CartItem> testData = new LinkedHashMap<String, CartItem>();
 
    public TestDataProvider() {
        add("T001", new CartItem(new DailyTicket("General Admission", 39.99), ItemType.DAILY_TICKET, Location.MAGIC_MOUNTAIN, 1));
        add("T002", new CartItem(new SeasonPass("Gold Pass", 89.99), ItemType.SEASON_PASS, Location.MAGIC_MOUNTAIN, 1));
        add("T003", new CartItem(new Concession("Burger Combo", 9.99), ItemType.CONCESSION, Location.FIESTA_TEXAS, 1));
    }
 
    private void add(String id, CartItem item) {
        testData.put(id, item);
    }
 
    public CartItem getTestItem(String itemId) {
        return testData.get(itemId);
    }
 
    public List<String> getAllIds()   { return List.copyOf(testData.keySet()); }
    public List<CartItem> getAllItems() { return List.copyOf(testData.values()); }
}
 