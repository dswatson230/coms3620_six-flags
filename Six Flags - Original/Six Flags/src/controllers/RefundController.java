package controllers;

import interfaces.RefundControllerInterface;
import interfaces.RefundSystemInterface;

public class RefundController implements RefundControllerInterface {
    private final RefundSystemInterface refundSystem;

    public RefundController(RefundSystemInterface refundSystem) {
        this.refundSystem = refundSystem;
    }

    @Override
    public String processRefund(String recordId, int itemIndex, int quantity) {
        return refundSystem.processRefundRequest(recordId, itemIndex, quantity);
    }
}