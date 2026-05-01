package controllers;

import data.ItemInventory;
import interfaces.PurchaseFileHandlerInterface;
import interfaces.RefundFileHandlerInterface;
import interfaces.RefundSystemInterface;
import models.PurchaseRecord;
import models.item.CartItem;

import java.io.IOException;

public class RefundSystem implements RefundSystemInterface {
    private static final String SUCCESS_MESSAGE        = "Refund processed successfully.";
    private static final String INVALID_DATA_MESSAGE   = "Invalid refund data entered.";
    private static final String RECORD_NOT_FOUND       = "No purchase record found.";
    private static final String ITEM_NOT_FOUND         = "Item not found in purchase record.";
    private static final String NON_REFUNDABLE_MESSAGE = "Item cannot be refunded.";
    private static final String INVALID_QUANTITY       = "Invalid quantity. Must be greater than zero and not exceed purchased amount.";
    private static final String FILE_ERROR_MESSAGE     = "Unable to process refund because of a file error.";

    private final PurchaseFileHandlerInterface purchaseFileHandler;
    private final RefundFileHandlerInterface   refundFileHandler;
    private final ItemInventory                inventory;

    public RefundSystem(PurchaseFileHandlerInterface purchaseFileHandler,
                        RefundFileHandlerInterface refundFileHandler,
                        ItemInventory inventory) {
        this.purchaseFileHandler = purchaseFileHandler;
        this.refundFileHandler   = refundFileHandler;
        this.inventory           = inventory;
    }

    @Override
    public String processRefundRequest(String recordId, int itemIndex, int quantity) {
        if (isBlank(recordId)) return INVALID_DATA_MESSAGE;

        try {
            // Step 1: retrieve purchase record
            PurchaseRecord record = purchaseFileHandler.findRecordById(recordId.trim());
            if (record == null) return RECORD_NOT_FOUND;

            // Step 2: find item by index to avoid duplicate name collision
            CartItem item = record.findItemByIndex(itemIndex);
            if (item == null) return ITEM_NOT_FOUND;

            // Step 3: check refundable
            if (!record.isRefundable(item.getItem().getName())) return NON_REFUNDABLE_MESSAGE;

            // Step 4: validate quantity
            if (quantity <= 0 || quantity > item.getQuantity()) return INVALID_QUANTITY;

            // Step 5: restore inventory for only the refunded item
            inventory.increaseQuantity(item.getType(), item.getName(), item.getPrice(), item.getLocation(), quantity);

            // Step 6: log refund
            if (!refundFileHandler.logRefund(recordId.trim(), item.getName(), quantity)) {
                inventory.increaseQuantity(item.getType(), item.getName(), item.getPrice(), item.getLocation(), -quantity);
                return FILE_ERROR_MESSAGE;
            }

            // Step 7: update item quantity in purchase record
            int newQuantity = item.getQuantity() - quantity;
            purchaseFileHandler.updateItemQuantity(recordId.trim(), itemIndex, newQuantity);

            // Step 8: update status - REFUNDED only if all items at qty 0
            boolean allRefunded = record.getItems().stream()
                .allMatch(i -> i == item
                    ? newQuantity == 0
                    : i.getQuantity() == 0);
            String newStatus = allRefunded ? "REFUNDED" : "PARTIAL_REFUND";
            purchaseFileHandler.updateRecordStatus(recordId.trim(), newStatus);

            return SUCCESS_MESSAGE;

        } catch (IOException e) {
            return FILE_ERROR_MESSAGE;
        }
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}