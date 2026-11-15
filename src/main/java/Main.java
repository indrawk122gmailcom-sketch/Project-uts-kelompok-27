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
            System.out.println("2. Cari Putusan (Case ID / Nomor Perkara + Hitung Langkah)");
            System.out.println("3. Filter Rentang Vonis (Bulan)");
            System.out.println("4. Filter Jenis Narkotika");
            System.out.println("5. Filter Gabungan Lengkap (Jenis, Pasal, Vonis & Rentang Berat)");
            System.out.println("6. Tambah Putusan Baru");
            System.out.println("7. Ubah Lama Vonis Putusan");
            System.out.println("8. Hapus Putusan");
            System.out.println("9. Undo Perubahan Terakhir (Stack Audit)");
            System.out.println("10. Tampilkan Seluruh Riwayat Audit (Non-Destruktif)");
            System.out.println("11. Laporan Statistik & Agregasi Putusan");
            System.out.println("12. Urutkan Putusan (Insertion Sort Manual)");
            System.out.println("13. Ekspor Data Putusan ke CSV");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            String pilihan = scanner.nextLine().trim();

            switch (pilihan) {
                case "1":
                    tampilkanDaftar(pengelola.getSemuaPutusan());
                    break;

                case "2":
                    System.out.print("Masukkan Case ID atau Nomor Perkara: ");
                    String kataKunci = scanner.nextLine().trim();
                    PengelolaPutusan.HasilPencarian hasilCari = pengelola.cariDenganLangkah(kataKunci);
                    if (!hasilCari.ditemukan()) {
                        System.out.println("Putusan '" + kataKunci + "' tidak ditemukan (diperiksa dalam "
                                + hasilCari.jumlahLangkah + " langkah).");
                    } else {
                        System.out.println("Putusan ditemukan pada indeks " + hasilCari.indeks
                                + " dalam " + hasilCari.jumlahLangkah + " langkah.");
                        cetakDetail(hasilCari.putusan);
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
                        System.out.print("Pasal terbukti (kosongkan jika semua, contoh: 114): ");
                        String filterPasal = scanner.nextLine().trim();
                        System.out.print("Vonis minimum (bulan): ");
                        int minVonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Vonis maksimum (bulan): ");
                        int maxVonis = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Berat minimal barang bukti (gram): ");
                        double minBerat = Double.parseDouble(scanner.nextLine().trim());
                        System.out.print("Berat maksimal barang bukti (gram): ");
                        double maxBerat = Double.parseDouble(scanner.nextLine().trim());
                        tampilkanDaftar(pengelola.filterLengkap(
                                filterJenis, filterPasal, minVonis, maxVonis, minBerat, maxBerat));
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

                case "10":
                    ArrayDinamis<StackAudit.EntriAudit> riwayat = pengelola.getSemuaRiwayatAudit();
                    if (riwayat.isEmpty()) {
                        System.out.println("(Riwayat Stack Audit masih kosong)");
                    } else {
                        System.out.println("\n--- RIWAYAT AUDIT (URUTAN TOP -> BOTTOM) ---");
                        for (int i = 0; i < riwayat.size(); i++) {
                            StackAudit.EntriAudit entri = riwayat.get(i);
                            System.out.println((i + 1) + ". [" + entri.jenisAksi + "] " + entri.keterangan);
                        }
                    }
                    break;

                case "11":
                    PengelolaPutusan.RingkasanStatistik stat = pengelola.hitungStatistik();
                    if (stat.totalKasus == 0) {
                        System.out.println("(Belum ada data putusan untuk dihitung statistiknya)");
                    } else {
                        System.out.println("\n--- LAPORAN STATISTIK & AGREGASI PUTUSAN ---");
                        System.out.println("Total Kasus            : " + stat.totalKasus);
                        System.out.println("Vonis Minimum          : " + stat.vonisMinimumBulan + " bulan");
                        System.out.println("Vonis Maksimum         : " + stat.vonisMaksimumBulan + " bulan");
                        System.out.printf("Rata-Rata Vonis        : %.2f bulan%n", stat.rataRataVonisBulan);
                        System.out.printf("Total Berat Bukti      : %.4f gram%n", stat.totalBeratGram);
                        System.out.printf("Rata-Rata Berat Bukti  : %.4f gram%n", stat.rataRataBeratGram);
                        System.out.println("Distribusi Jenis Narkotika (" + stat.jumlahJenisUnik + " kategori):");
                        for (int i = 0; i < stat.jumlahJenisUnik; i++) {
                            System.out.println("  - " + stat.daftarJenis[i] + ": " + stat.jumlahPerJenis[i] + " kasus");
                        }
                    }
                    break;

                case "12":
                    System.out.println("Pilih mode pengurutan (non-destruktif):");
                    System.out.println("1. Vonis Terpendek -> Terlama (Ascending)");
                    System.out.println("2. Vonis Terlama -> Terpendek (Descending)");
                    System.out.println("3. Berat Barang Bukti Terkecil -> Terbesar (Ascending)");
                    System.out.println("4. Berat Barang Bukti Terbesar -> Terkecil (Descending)");
                    System.out.print("Pilihan (1-4): ");
                    String modeUrut = scanner.nextLine().trim();
                    switch (modeUrut) {
                        case "1":
                            tampilkanDaftar(pengelola.urutkanByVonis(true));
                            break;
                        case "2":
                            tampilkanDaftar(pengelola.urutkanByVonis(false));
                            break;
                        case "3":
                            tampilkanDaftar(pengelola.urutkanByBerat(true));
                            break;
                        case "4":
                            tampilkanDaftar(pengelola.urutkanByBerat(false));
                            break;
                        default:
                            System.out.println("Mode pengurutan tidak valid.");
                    }
                    break;

                case "13":
                    System.out.println("Pilih data yang akan diekspor:");
                    System.out.println("1. Seluruh Data Putusan (Urutan Asli)");
                    System.out.println("2. Hasil Urut Vonis Terlama -> Terpendek");
                    System.out.println("3. Hasil Urut Berat Barang Bukti Terbesar -> Terkecil");
                    System.out.print("Pilihan (1-3): ");
                    String sumberPilih = scanner.nextLine().trim();

                    ArrayDinamis<Putusan> dataEkspor;
                    if ("2".equals(sumberPilih)) {
                        dataEkspor = pengelola.urutkanByVonis(false);
                    } else if ("3".equals(sumberPilih)) {
                        dataEkspor = pengelola.urutkanByBerat(false);
                    } else {
                        dataEkspor = pengelola.getSemuaPutusan();
                    }

                    System.out.print("Path file tujuan (Enter untuk default 'output/hasil-ekspor.csv'): ");
                    String tujuan = scanner.nextLine().trim();
                    if (tujuan.isEmpty()) {
                        tujuan = "output/hasil-ekspor.csv";
                    }

                    try {
                        int jumlahEkspor = pengelola.eksporKeCsv(tujuan, dataEkspor);
                        System.out.println("Berhasil mengekspor " + jumlahEkspor + " baris data ke: " + tujuan);
                    } catch (IOException e) {
                        System.out.println("Gagal mengekspor CSV: " + e.getMessage());
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