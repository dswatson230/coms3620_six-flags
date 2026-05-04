package interfaces;
 
import models.PurchaseRecord;
 
import java.io.IOException;
import java.util.List;
 
public interface RefundFileHandlerInterface {
    boolean logRefund(String recordId, String itemName, int quantity) throws IOException;
    List<String> readAllRefunds() throws IOException;
}
 
