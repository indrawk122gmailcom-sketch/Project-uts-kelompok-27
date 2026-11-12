import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditLog {
    private String aksi;
    private String deskripsi;
    private LocalDateTime timestamp;

    public AuditLog(String aksi, String deskripsi) {
        this.aksi = aksi;
        this.deskripsi = deskripsi;
        this.timestamp = LocalDateTime.now();
    }

    @Override
    public String toString() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return "[" + timestamp.format(dtf) + "] " + aksi + ": " + deskripsi;
    }
}