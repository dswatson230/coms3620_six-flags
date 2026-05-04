package models;
 
import data.TestDataProvider;
import interfaces.ItemStatus;
 
public class ItemConfirmation implements interfaces.ItemConfirmationInterface {
    private String genMessage;
    private Item item;
 
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
        verifyItemStatus(item);
        return item.getStatus() == ItemStatus.VALID;
    }
 
    // Step 2: Retrieve item from system
    public Item retrieveItemInformation(String id) {
        return dataProvider.getTestItem(id);
    }
 
    // Step 3: Check if item is valid
    public boolean isItemValid(Item item) {
        return item != null && item.getStatus() != ItemStatus.INVALID;
    }
 
    // Step 4 & 6: Verify and update item status
    public void verifyItemStatus(Item item) {
        if (item.getStatus() == ItemStatus.USED) {
            setGenMessage("Item has already been used or processed. Process denied.");
        } else if (item.getStatus() == ItemStatus.VALID) {
            item.setStatus(ItemStatus.USED);
            setGenMessage("Item is valid. Proceed.");
        }
    }
 
    public String getGenMessage()          { return genMessage; }
    public void setGenMessage(String msg)  { this.genMessage = msg; }
    public Item getItem()                  { return item; }
    public void setItem(Item item)         { this.item = item; }
    public data.TestDataProvider getDataProvider() { return dataProvider; }
}