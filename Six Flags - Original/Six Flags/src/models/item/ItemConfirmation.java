package models.item;
 
import data.TestDataProvider;
import interfaces.types.ItemStatus;
 
public class ItemConfirmation  {
    private String genMessage;
    private CartItem item;
 
    private final TestDataProvider dataProvider;
 
    public ItemConfirmation() {
        this.dataProvider = new TestDataProvider();
        this.genMessage   = "";
        this.item         = null;
    }
 
    // Step 1: Employee enters item ID — validate and retrieve
    public boolean validateItem(String itemId) {
        this.item = retrieveItemInformation(itemId);
        if (item == null) {
            setGenMessage("Item not found in records. Process denied.");
            return false;
        }
        if (!isItemValid(item)) {
            setGenMessage("Item is invalid. Process denied.");
            return false;
        }
        ItemStatus originalStatus = item.getStatus();
        verifyItemStatus(item);
        return originalStatus == ItemStatus.VALID;
    }
 
    // Step 2: Retrieve item from system
    public CartItem retrieveItemInformation(String id) {
        return dataProvider.getTestItem(id);
    }
 
    // Step 3: Check if item is valid
    public boolean isItemValid(CartItem item) {
        return item != null && item.getStatus() != ItemStatus.INVALID;
    }
 
    // Step 4 & 6: Verify and update item status
    public void verifyItemStatus(CartItem item) {
        if (item.getStatus() == ItemStatus.USED) {
            setGenMessage("Item has already been used or processed. Process denied.");
        } else if (item.getStatus() == ItemStatus.VALID) {
            item.setStatus(ItemStatus.USED);
            setGenMessage("Item is valid. Proceed.");
        }
    }
 
    public String getGenMessage()          { return genMessage; }
    public void setGenMessage(String msg)  { this.genMessage = msg; }
    public CartItem getItem()                  { return item; }
    public void setItem(CartItem item)         { this.item = item; }
    public data.TestDataProvider getDataProvider() { return dataProvider; }
}