package interfaces;
 
import models.Item;
 
public interface ItemConfirmationInterface {
    boolean validateItem(String itemId);
    Item retrieveItemInformation(String id);
    boolean isItemValid(Item item);
    void verifyItemStatus(Item item);
    String getGenMessage();
    void setGenMessage(String msg);
    Item getItem();
    void setItem(Item item);
}
 