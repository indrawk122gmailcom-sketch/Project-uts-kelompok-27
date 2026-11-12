import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDate;

public class CsvReader {
    public static DynamicArray<Putusan> loadPutusanFromCsv(String filePath) {
        DynamicArray<Putusan> list = new DynamicArray<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line = br.readLine(); // Skip Header
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue;
                String[] cols = parseCsvLine(line);
                if (cols.length >= 16) {
                    Putusan p = new Putusan(
                            cols[0], cols[1], cols[2],
                            parseDate(cols[3]), cols[4], cols[5],
                            parseDouble(cols[6]), cols[7], cols[8],
                            parseInt(cols[9]), parseInt(cols[10]), parseLong(cols[11]),
                            cols[12], cols[13], cols[14], cols[15]
                    );
                    list.add(p);
                }
            }
            System.out.println("Berhasil memuat " + list.size() + " data dari " + filePath);
        } catch (IOException e) {
            System.out.println("Gagal membaca file CSV: " + e.getMessage());
        }
        return list;
    }

    private static String[] parseCsvLine(String line) {
        return line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1);
    }

    private static LocalDate parseDate(String val) {
        try { return LocalDate.parse(val.trim()); } catch (Exception e) { return LocalDate.now(); }
    }
    private static double parseDouble(String val) {
        try { return Double.parseDouble(val.trim()); } catch (Exception e) { return 0.0; }
    }
    private static int parseInt(String val) {
        try { return Integer.parseInt(val.trim()); } catch (Exception e) { return 0; }
    }
    private static long parseLong(String val) {
        try { return Long.parseLong(val.trim()); } catch (Exception e) { return 0L; }
    }
}