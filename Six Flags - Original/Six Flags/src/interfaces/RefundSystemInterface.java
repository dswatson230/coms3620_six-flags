package interfaces;

public interface RefundSystemInterface {
    String processRefundRequest(String recordId, int itemIndex, int quantity);
}