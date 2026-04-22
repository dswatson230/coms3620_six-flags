package data;
 
import interfaces.RefundFileHandlerInterface;
 
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
 
public class RefundFileHandler implements RefundFileHandlerInterface {
    private final Path refundFilePath;
 
    public RefundFileHandler(String refundFilePath) {
        this.refundFilePath = Paths.get(refundFilePath);
    }
 
    @Override
    public boolean logRefund(String recordId, String itemName, int quantity) throws IOException {
        if (refundFilePath.getParent() != null) {
            Files.createDirectories(refundFilePath.getParent());
        }
        if (Files.notExists(refundFilePath)) {
            Files.createFile(refundFilePath);
        }
        String entry = recordId + "|" + itemName + "|" + quantity + "|" + LocalDateTime.now()
                     + System.lineSeparator();
        Files.writeString(refundFilePath, entry, StandardOpenOption.APPEND);
        return true;
    }
 
    @Override
    public List<String> readAllRefunds() throws IOException {
        List<String> refunds = new ArrayList<>();
        if (Files.notExists(refundFilePath)) return refunds;
 
        for (String line : Files.readAllLines(refundFilePath)) {
            if (!line.trim().isEmpty()) refunds.add(line);
        }
        return refunds;
    }
}
