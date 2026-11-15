import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        String pathCsv = "data/putusan-50.csv";

        try {
            int dimuat = pengelola.muatDariCsv(pathCsv);
            System.out.println("Berhasil memuat " + dimuat + " putusan dari " + pathCsv);
        } catch (IOException e) {
            System.out.println("Peringatan: Gagal memuat CSV otomatis (" + e.getMessage() + ")");
        }

        Scanner scanner = new Scanner(System.in);
        boolean berjalan = true;

        while (berjalan) {
            System.out.println("\n=== SISTEM MANAJEMEN PUTUSAN NARKOTIKA (KELOMPOK 27) ===");
            System.out.println("Total Data: " + pengelola.jumlahData() + " | Riwayat Audit: " + pengelola.jumlahRiwayatAudit());
            System.out.println("1. Tampilkan Semua Putusan");
            System.out.println("2. Cari Putusan berdasarkan Case ID");
            System.out.println("3. Filter Rentang Vonis (Bulan)");
            System.out.println("4. Filter Jenis Narkotika");
            System.out.println("5. Filter Multi-Kriteria (Jenis, Vonis & Berat Bukti)");
            System.out.println("6. Tambah Putusan Baru");
            System.out.println("7. Ubah Lama Vonis Putusan");
            System.out.println("8. Hapus Putusan");
            System.out.println("9. Undo Perubahan Terakhir (Stack Audit)");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    tampilkanDaftar(pengelola.getSemuaPutusan());
                    break;

                case "2":
                    System.out.print("Masukkan Case ID (contoh: 1): ");
                    String cariId = scanner.nextLine().trim();
                    Putusan p = pengelola.cariByCaseId(cariId);
                    if (p == null) {
                        System.out.println("Putusan dengan Case ID '" + cariId + "' tidak ditemukan.");
                    } else {
                        cetakDetail(p);
                    }
                    break;

                case "3":
                    try {
                        System.out.print("Vonis minimum (bulan): ");
                        int min = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Vonis maksimum (bulan): ");
                        int max = Integer.parseInt(scanner.nextLine().trim());
                        tampilkanDaftar(pengelola.filterRentangVonis(min, max));
                    } catch (NumberFormatException e) {
                        System.out.println("Input angka tidak valid.");
                    }
                    break;

                case "4":
                    System.out.print("Kata kunci jenis narkotika (contoh: Sabu / Ganja): ");
                    String jenis = scanner.nextLine().trim();
                    tampilkanDaftar(pengelola.filterJenisNarkotika(jenis));
                    break;

                case "5":
                    try {
                        System.out.print("Jenis narkotika (kosongkan jika semua): ");
                        String filterJenis = scanner.nextLine().trim();
                        System.out.print("Vonis minimum (bulan): ");
                        int minVonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Vonis maksimum (bulan): ");
                        int maxVonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Berat minimal barang bukti (gram): ");
                        double minBerat = Double.parseDouble(scanner.nextLine().trim());
                        tampilkanDaftar(pengelola.filterMultiKriteria(filterJenis, minVonis, maxVonis, minBerat));
                    } catch (NumberFormatException e) {
                        System.out.println("Input angka vonis atau berat tidak valid.");
                    }
                    break;

                case "6":
                    try {
                        System.out.print("Case ID: ");
                        String id = scanner.nextLine().trim();
                        System.out.print("Nomor Perkara: ");
                        String noPerkara = scanner.nextLine().trim();
                        System.out.print("Pengadilan: ");
                        String pengadilan = scanner.nextLine().trim();
                        System.out.print("Tanggal Putusan (YYYY-MM-DD): ");
                        String tanggal = scanner.nextLine().trim();
                        System.out.print("Kode Terdakwa: ");
                        String kodeTdw = scanner.nextLine().trim();
                        System.out.print("Jenis Narkotika: ");
                        String narkotika = scanner.nextLine().trim();
                        System.out.print("Berat Barang Bukti (gram): ");
                        double berat = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Pasal Dakwaan: ");
                        String dakwaan = scanner.nextLine().trim();
                        System.out.print("Pasal Terbukti: ");
                        String terbukti = scanner.nextLine().trim();
                        System.out.print("Tuntutan (bulan): ");
                        int tuntutan = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Vonis (bulan): ");
                        int vonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Denda (rupiah): ");
                        long denda = Long.parseLong(scanner.nextLine().trim());

                        Putusan baru = new Putusan(
                                id, noPerkara, pengadilan, tanggal, kodeTdw, narkotika,
                                berat, dakwaan, terbukti, tuntutan, vonis, denda,
                                "-", "-", "-", "1-1", "Input Manual CLI"
                        );

                        if (pengelola.tambahPutusan(baru)) {
                            System.out.println("Putusan berhasil ditambahkan dan dicatat ke Stack Audit.");
                        } else {
                            System.out.println("Gagal: Case ID duplikat atau nilai input tidak valid/negatif.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Gagal: Format angka berat/bulan/denda tidak valid.");
                    }
                    break;

                case "7":
                    try {
                        System.out.print("Case ID yang akan diubah vonisnya: ");
                        String idUbah = scanner.nextLine().trim();
                        System.out.print("Vonis baru (bulan): ");
                        int vonisBaru = Integer.parseInt(scanner.nextLine().trim());
                        if (pengelola.ubahVonisBulan(idUbah, vonisBaru)) {
                            System.out.println("Vonis berhasil diperbarui.");
                        } else {
                            System.out.println("Gagal memperbarui vonis (ID tidak ditemukan atau nilai negatif).");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Input bulan tidak valid.");
                    }
                    break;

                case "8":
                    System.out.print("Case ID yang akan dihapus: ");
                    String idHapus = scanner.nextLine().trim();
                    if (pengelola.hapusPutusan(idHapus)) {
                        System.out.println("Putusan berhasil dihapus.");
                    } else {
                        System.out.println("Case ID tidak ditemukan.");
                    }
                    break;

                case "9":
                    StackAudit.EntriAudit terakhir = pengelola.lihatAuditTerakhir();
                    if (terakhir == null) {
                        System.out.println("Tidak ada aksi yang dapat di-undo.");
                    } else {
                        pengelola.undo();
                        System.out.println("Undo berhasil untuk aksi: " + terakhir.keterangan);
                    }
                    break;

                case "0":
                    berjalan = false;
                    System.out.println("Program selesai.");
                    break;

                default:
                    System.out.println("Pilihan menu tidak dikenal.");
            }
        }
        scanner.close();
    }

    private static void tampilkanDaftar(ArrayDinamis<Putusan> daftar) {
        if (daftar.isEmpty()) {
            System.out.println("(Tidak ada data putusan yang cocok)");
            return;
        }
        for (int i = 0; i < daftar.size(); i++) {
            System.out.println((i + 1) + ". " + daftar.get(i));
        }
    }

    private static void cetakDetail(Putusan p) {
        System.out.println("\n--- DETAIL PUTUSAN ---");
        System.out.println("Case ID          : " + p.getCaseId());
        System.out.println("Nomor Perkara    : " + p.getNomorPerkara());
        System.out.println("Pengadilan       : " + p.getPengadilan());
        System.out.println("Tanggal Putusan  : " + p.getTanggalPutusan());
        System.out.println("Kode Terdakwa    : " + p.getTerdakwaKode());
        System.out.println("Jenis Narkotika  : " + p.getJenisNarkotika());
        System.out.println("Berat Bukti (gr) : " + p.getBeratBarangBuktiGram());
        System.out.println("Pasal Terbukti   : " + p.getPasalTerbukti());
        System.out.println("Tuntutan / Vonis : " + p.getTuntutanBulan() + " bln / " + p.getVonisBulan() + " bln");
        System.out.println("Denda            : Rp" + String.format("%,d", p.getDendaRupiah()));
        System.out.println("Barang Bukti     : " + p.getBarangBukti());
        System.out.println("Sumber Dokumen   : " + p.getProvenance() + " (Hal. " + p.getSourcePages() + ")");
    }
}