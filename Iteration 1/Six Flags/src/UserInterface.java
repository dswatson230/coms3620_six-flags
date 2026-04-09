import javax.swing.JOptionPane;

public class UserInterface {
    public static void main(String[] args) {
        String operationNumber=JOptionPane.showInputDialog(
                "Enter operation number \n"+
                        " 1. Purchase Ticket");
        int code = Integer.parseInt(operationNumber);
        try {
            if (code == 1) {
                boolean b = new ItemController().purchaseItem();
                System.out.println("Operation Successful: " + b);
            }
        }
        catch (Exception e){
            System.err.println("Error Moment");
        }
    }
}
