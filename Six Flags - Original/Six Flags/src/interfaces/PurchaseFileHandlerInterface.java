package interfaces;

import models.PurchaseRecord;

import java.io.IOException;
import java.util.List;

public interface PurchaseFileHandlerInterface {
    PurchaseRecord findRecordById(String recordId) throws IOException;
    List<PurchaseRecord> readAllRecords() throws IOException;
    boolean updateRecordStatus(String recordId, String status) throws IOException;
    boolean updateItemQuantity(String recordId, int itemIndex, int newQuantity) throws IOException;
}