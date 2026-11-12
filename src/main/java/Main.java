import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;

public class Main {
    private static DynamicArray<Putusan> dataset = new DynamicArray<>();
    private static final AuditStack<AuditLog> auditStack = new AuditStack<>();
    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        boolean running = true;
        System.out.println("==================================================");
        System.out.println("  APLIKASI PENGELOLAAN PUTUSAN PENGADILAN NARKOTIKA ");
        System.out.println("  Struktur Data - Universitas Muhammadiyah Malang ");
        System.out.println("==================================================");

        while (running) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1":
                    importCsvMenu();
                    break;
                case "2":
                    tampilkanSemuaPutusan();
                    break;
                case "3":
                    tambahPutusanManual();
                    break;
                case "4":
                    hapusPutusanMenu();
                    break;
                case "5":
                    cariPerkaraMenu();
                    break;
                case "6":
                    filterDataMenu();
                    break;
                case "7":
                    jalankanAgregasi();
                    break;
                case "8":
                    urutkanBerdasarkanVonis();
                    break;
                case "9":
                    tampilkanAuditRiwayat();
                    break;
                case "10":
                    eksporHasilMenu();
                    break;
                case "0":
                    running = false;
                    System.out.println("Keluar dari aplikasi. Terima kasih!");
                    break;
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
            System.out.println();
        }
    }

    private static void printMenu() {
        System.out.println("\n--- MENU UTAMA ---");
        System.out.println("1. Impor Data dari CSV (data/putusan-50.csv)");
        System.out.println("2. Tampilkan Semua Ringkasan Putusan");
        System.out.println("3. Tambah Putusan Baru (Operasi Native)");
        System.out.println("4. Hapus Putusan Berdasarkan Indeks");
        System.out.println("5. Cari Perkara (Nomor Perkara / Case ID & Hitung Langkah)");
        System.out.println("6. Filter Data (Jenis Narkotika / Berat / Vonis)");
        System.out.println("7. Agregasi Data (Statistik Kasus)");
        System.out.println("8. Urutkan Putusan Berdasarkan Vonis (Bulan)");
        System.out.println("9. Tampilkan Riwayat Audit (Stack Sekunder)");
        System.out.println("10. Ekspor Data ke File Teks/CSV");
        System.out.println("0. Keluar");
        System.out.print("Pilih menu [0-10]: ");
    }

    private static void importCsvMenu() {
        System.out.print("Masukkan path file CSV (default: data/putusan-50.csv): ");
        String path = scanner.nextLine().trim();
        if (path.isEmpty()) {
            path = "data/putusan-50.csv";
        }
        dataset = CsvReader.loadPutusanFromCsv(path);
        auditStack.push(new AuditLog("IMPOR_CSV", "Berhasil memuat " + dataset.size() + " record dari " + path));
        System.out.println("Status Invariant Struktur Utama: " + dataset.validateInvariant());
    }

    private static void tampilkanSemuaPutusan() {
        if (dataset.isEmpty()) {
            System.out.println("Data masih kosong. Silakan impor CSV terlebih dahulu.");
            return;
        }
        System.out.println("\n--- DAFTAR PUTUSAN (" + dataset.size() + " Record) ---");
        for (int i = 0; i < dataset.size(); i++) {
            Putusan p = dataset.get(i);
            System.out.println("[" + i + "] ID: " + p.getCaseId() +
                    " | No: " + p.getNomorPerkara() +
                    " | PN: " + p.getPengadilan() +
                    " | Vonis: " + p.getVonisBulan() + " bln" +
                    " | Denda: Rp" + p.getDendaRupiah());
        }
        auditStack.push(new AuditLog("TAMPIL_SEMUA", "Menampilkan " + dataset.size() + " record putusan."));
    }

    private static void tambahPutusanManual() {
        System.out.print("Case ID: ");
        String id = scanner.nextLine();
        System.out.print("Nomor Perkara: ");
        String no = scanner.nextLine();
        System.out.print("Pengadilan: ");
        String pn = scanner.nextLine();
        System.out.print("Jenis Narkotika: ");
        String jenis = scanner.nextLine();
        System.out.print("Vonis Penjara (Bulan): ");
        int vonis = Integer.parseInt(scanner.nextLine());
        System.out.print("Denda Rupiah: ");
        long denda = Long.parseLong(scanner.nextLine());

        Putusan p = new Putusan(id, no, pn, LocalDate.now(), "T-X", jenis, 1.0,
                "Pasal 114", "Pasal 114", vonis, vonis, denda, "Barang bukti uji", "Memberatkan", "Meringankan", "H1");

        dataset.add(p);
        auditStack.push(new AuditLog("TAMBAH", "Menambahkan perkara: " + no));
        System.out.println("Putusan berhasil ditambahkan. Total sekarang: " + dataset.size());
        System.out.println("Status Invariant: " + dataset.validateInvariant());
    }

    private static void hapusPutusanMenu() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        System.out.print("Masukkan indeks record yang ingin dihapus (0 s.d " + (dataset.size() - 1) + "): ");
        try {
            int idx = Integer.parseInt(scanner.nextLine());
            Putusan removed = dataset.remove(idx);
            auditStack.push(new AuditLog("HAPUS", "Menghapus perkara " + removed.getNomorPerkara()));
            System.out.println("Berhasil menghapus perkara: " + removed.getNomorPerkara());
            System.out.println("Status Invariant: " + dataset.validateInvariant());
        } catch (Exception e) {
            System.out.println("Gagal menghapus: " + e.getMessage());
        }
    }

    private static void cariPerkaraMenu() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        System.out.print("Masukkan Case ID atau Nomor Perkara yang dicari: ");
        String keyword = scanner.nextLine().trim();

        int steps = 0;
        Putusan found = null;
        for (int i = 0; i < dataset.size(); i++) {
            steps++;
            Putusan p = dataset.get(i);
            if (p.getCaseId().equalsIgnoreCase(keyword) || p.getNomorPerkara().equalsIgnoreCase(keyword)) {
                found = p;
                break;
            }
        }

        System.out.println("--- HASIL PENCARIAN ---");
        System.out.println("Jumlah Langkah (Steps) Dieksekusi: " + steps);
        if (found != null) {
            System.out.println("Ditemukan: ID=" + found.getCaseId() + ", No=" + found.getNomorPerkara() +
                    ", PN=" + found.getPengadilan() + ", Vonis=" + found.getVonisBulan() + " bulan");
        } else {
            System.out.println("Perkara tidak ditemukan.");
        }
        auditStack.push(new AuditLog("CARI", "Mencari keyword '" + keyword + "' dalam " + steps + " langkah."));
    }

    private static void filterDataMenu() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        System.out.print("Masukkan kata kunci jenis narkotika (misal: sabu / tembakau): ");
        String keyword = scanner.nextLine().toLowerCase().trim();

        int count = 0;
        System.out.println("\n--- HASIL FILTER: " + keyword + " ---");
        for (int i = 0; i < dataset.size(); i++) {
            Putusan p = dataset.get(i);
            if (p.getJenisNarkotika().toLowerCase().contains(keyword)) {
                count++;
                System.out.println("[" + count + "] " + p.getNomorPerkara() + " | " + p.getJenisNarkotika() + " | Vonis: " + p.getVonisBulan() + " bln");
            }
        }
        System.out.println("Total ditemukan: " + count + " record.");
        auditStack.push(new AuditLog("FILTER", "Filter jenis narkotika '" + keyword + "', hasil: " + count + " record."));
    }

    private static void jalankanAgregasi() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        int totalPerkara = dataset.size();
        long totalDenda = 0;
        long totalVonis = 0;

        for (int i = 0; i < totalPerkara; i++) {
            Putusan p = dataset.get(i);
            totalDenda += p.getDendaRupiah();
            totalVonis += p.getVonisBulan();
        }
        double rataVonis = (double) totalVonis / totalPerkara;

        System.out.println("\n--- AGREGASI STATISTIK PUTUSAN ---");
        System.out.println("Total Perkara        : " + totalPerkara);
        System.out.println("Rata-rata Vonis      : " + String.format("%.2f", rataVonis) + " bulan");
        System.out.println("Akumulasi Total Denda: Rp" + totalDenda + "\n");
        auditStack.push(new AuditLog("AGREGASI", "Menghitung statistik dari " + totalPerkara + " record."));
    }

    private static void urutkanBerdasarkanVonis() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        for (int i = 0; i < dataset.size() - 1; i++) {
            for (int j = 0; j < dataset.size() - i - 1; j++) {
                if (dataset.get(j).getVonisBulan() > dataset.get(j + 1).getVonisBulan()) {
                    Putusan temp = dataset.get(j);
                    dataset.set(j, dataset.get(j + 1));
                    dataset.set(j + 1, temp);
                }
            }
        }
        System.out.println("Data berhasil diurutkan berdasarkan lama vonis (Ascending).");
        auditStack.push(new AuditLog("SORT", "Mengurutkan data berdasarkan vonis bulan."));
        tampilkanSemuaPutusan();
    }

    private static void tampilkanAuditRiwayat() {
        System.out.println("\n--- RIWAYAT AUDIT OPERASI (STACK SEKUNDER) ---");
        if (auditStack.isEmpty()) {
            System.out.println("Belum ada riwayat audit.");
            return;
        }
        for (int i = 0; i < auditStack.size(); i++) {
            System.out.println((i + 1) + ". " + auditStack.get(i));
        }
        auditStack.push(new AuditLog("AUDIT_VIEW", "Melihat riwayat audit operasi."));
    }

    private static void eksporHasilMenu() {
        if (dataset.isEmpty()) {
            System.out.println("Data kosong.");
            return;
        }
        String filename = "data/export_laporan_putusan.csv";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filename))) {
            writer.write("caseId,nomorPerkara,pengadilan,jenisNarkotika,vonisBulan,dendaRupiah\n");
            for (int i = 0; i < dataset.size(); i++) {
                Putusan p = dataset.get(i);
                writer.write(String.format("\"%s\",\"%s\",\"%s\",\"%s\",%d,%d\n",
                        p.getCaseId(), p.getNomorPerkara(), p.getPengadilan(),
                        p.getJenisNarkotika(), p.getVonisBulan(), p.getDendaRupiah()));
            }
            System.out.println("Berhasil mengekspor data ke " + filename);
            auditStack.push(new AuditLog("EKSPOR", "Mengekspor data ke file " + filename));
        } catch (IOException e) {
            System.out.println("Gagal ekspor: " + e.getMessage());
        }
    }
}