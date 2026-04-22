package interfaces;

public interface RefundControllerInterface {
    String processRefund(String recordId, int itemIndex, int quantity);
}