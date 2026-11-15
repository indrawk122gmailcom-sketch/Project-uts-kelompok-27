import java.io.File;
import java.io.IOException;

public class MainTest {

    private static int lulus = 0;
    private static int gagal = 0;

    public static void main(String[] args) {
        System.out.println("=== MENJALANKAN SUITE PENGUJIAN (16 SKENARIO LENGKAP) ===");

        test1_EkspansiKapasitasArrayDinamis();
        test2_TambahPutusanDanTolakDuplikatCaseId();
        test3_ValidasiInputNegatifDanKosong();
        test4_PencarianCaseIdDanFilterRentangVonis();
        test5_UbahVonisDanPencatatanStackAudit();
        test6_HapusDataDanPemulihanUndoStack();
        test7_ParsingCsvDenganTandaKutipDanKoma();
        test8_StrukturDataGenerikDanValidateInvariant();
        test9_FilterMultiKriteriaLanjutan();
        test10_Muat50RecordCsvAsliDanValidasiSize();
        test11_PencarianBatasDanPencacahLangkah();
        test12_HapusDanTambahKembaliPosisiPertamaTengahTerakhir();
        test13_PenelusuranStackAuditNonDestruktifDanClear();
        test14_AgregasiStatistikPutusan();
        test15_SortingManualNonDestruktif();
        test16_EksporHasilKeCsv();

        System.out.println("\n=== RINGKASAN HASIL PENGUJIAN ===");
        System.out.println("Total Skenario : " + (lulus + gagal));
        System.out.println("Lulus          : " + lulus);
        System.out.println("Gagal          : " + gagal);

        if (gagal > 0) {
            System.exit(1);
        }
    }

    private static Putusan buatSampel(String id, String narkotika, double berat, int tuntutan, int vonis, long denda) {
        return new Putusan(
                id, id + "/Pid.Sus/2024/PN Tng", "Pengadilan Negeri Tangerang",
                "2024-12-09", "TDW-" + id, narkotika, berat,
                "Pasal 114 ayat (1) UU No. 35 Tahun 2009",
                "Pasal 114 ayat (1) UU No. 35 Tahun 2009",
                tuntutan, vonis, denda,
                "1 bungkus plastik klip sabu",
                "Meresahkan masyarakat", "Bersikap sopan",
                "1-20", "Direktori Putusan MA"
        );
    }

    private static void assertTrue(boolean kondisi, String deskripsi) {
        if (kondisi) {
            System.out.println("[PASS] " + deskripsi);
            lulus++;
        } else {
            System.err.println("[FAIL] " + deskripsi);
            gagal++;
        }
    }

    private static void test1_EkspansiKapasitasArrayDinamis() {
        ArrayDinamis<Putusan> arr = new ArrayDinamis<>(2);
        arr.add(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        arr.add(buatSampel("2", "Sabu", 2.1169, 96, 84, 1000000000L));
        arr.add(buatSampel("3", "Sabu", 412.76, 204, 180, 2000000000L));

        assertTrue(arr.size() == 3 && arr.kapasitas() == 4 && arr.get(2).getCaseId().equals("3"),
                "Skenario 1: Ekspansi otomatis kapasitas ArrayDinamis<Putusan> (2 -> 4)");
    }

    private static void test2_TambahPutusanDanTolakDuplikatCaseId() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        boolean pertama = pengelola.tambahPutusan(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        boolean duplikat = pengelola.tambahPutusan(buatSampel("1", "Ganja", 5.0, 60, 60, 1000000000L));

        assertTrue(pertama && !duplikat && pengelola.jumlahData() == 1,
                "Skenario 2: Tambah putusan baru & tolak duplikat caseId");
    }

    private static void test3_ValidasiInputNegatifDanKosong() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        boolean vonisNegatif = pengelola.tambahPutusan(buatSampel("10", "Sabu", 1.0, 72, -12, 1000000000L));
        boolean dendaNegatif = pengelola.tambahPutusan(buatSampel("11", "Sabu", 1.0, 72, 60, -500L));
        boolean beratNegatif = pengelola.tambahPutusan(buatSampel("12", "Sabu", -0.5, 72, 60, 1000000000L));
        boolean jenisKosong = pengelola.tambahPutusan(buatSampel("13", "", 1.0, 72, 60, 1000000000L));

        assertTrue(!vonisNegatif && !dendaNegatif && !beratNegatif && !jenisKosong && pengelola.jumlahData() == 0,
                "Skenario 3: Validasi penolakan vonis/denda/berat negatif & jenis narkotika kosong");
    }

    private static void test4_PencarianCaseIdDanFilterRentangVonis() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        pengelola.tambahPutusan(buatSampel("2", "Sabu", 2.1169, 96, 84, 1000000000L));
        pengelola.tambahPutusan(buatSampel("3", "Sabu", 412.76, 204, 180, 2000000000L));

        Putusan ditemukan = pengelola.cariByCaseId("2");
        Putusan tidakAda = pengelola.cariByCaseId("999");
        ArrayDinamis<Putusan> rentang = pengelola.filterRentangVonis(70, 90);

        assertTrue(ditemukan != null && ditemukan.getVonisBulan() == 84
                        && tidakAda == null && rentang.size() == 2,
                "Skenario 4: Pencarian caseId eksak & filter rentang vonis");
    }

    private static void test5_UbahVonisDanPencatatanStackAudit() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        int auditAwal = pengelola.jumlahRiwayatAudit();

        boolean diubah = pengelola.ubahVonisBulan("1", 60);
        int vonisSekarang = pengelola.cariByCaseId("1").getVonisBulan();

        assertTrue(diubah && vonisSekarang == 60 && pengelola.jumlahRiwayatAudit() == auditAwal + 1,
                "Skenario 5: Ubah lama vonis & pencatatan snapshot ke StackAudit");
    }

    private static void test6_HapusDataDanPemulihanUndoStack() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        pengelola.hapusPutusan("1");

        boolean terhapus = (pengelola.cariByCaseId("1") == null);
        boolean undoBerhasil = pengelola.undo();
        boolean pulih = (pengelola.cariByCaseId("1") != null && pengelola.cariByCaseId("1").getVonisBulan() == 72);

        assertTrue(terhapus && undoBerhasil && pulih,
                "Skenario 6: Hapus putusan & pemulihan data lewat Undo Stack");
    }

    private static void test7_ParsingCsvDenganTandaKutipDanKoma() {
        String barisAsli = "1,1619/Pid.Sus/2024/PN Tng,Pengadilan Negeri Tangerang,2024-12-09,TDW-01,Sabu (Metamfetamina),0.6087,Pasal 114 ayat (1) UU No. 35 Tahun 2009,Pasal 114 ayat (1) UU No. 35 Tahun 2009,72,72,4000000000,\"1 bungkus bekas rokok kuning berisi 5 plastik klip sabu (netto awal 0,6087 gram / akhir 0,5201 gram), 1 alat hisap sabu (bong) dan korek api\",Perbuatan terdakwa meresahkan masyarakat,Terdakwa belum pernah dihukum,1-26,Direktori Putusan Mahkamah Agung RI";
        String[] kolom = CsvParser.parseBaris(barisAsli);

        assertTrue(kolom.length == 17
                        && kolom[0].equals("1")
                        && kolom[12].contains("0,6087 gram")
                        && kolom[16].equals("Direktori Putusan Mahkamah Agung RI"),
                "Skenario 7: Parsing 17 kolom CSV aman terhadap koma di dalam tanda kutip");
    }

    private static void test8_StrukturDataGenerikDanValidateInvariant() {
        StrukturData<String> daftarGenerik = new ArrayDinamis<>(2);
        daftarGenerik.add("Item-A");
        daftarGenerik.add("Item-B");
        daftarGenerik.add("Item-C");
        boolean adaItemB = daftarGenerik.contains("Item-B");
        String terhapus = daftarGenerik.remove(1);

        StrukturData<String> stackGenerik = new StackAudit<>();
        stackGenerik.add("Log-1");
        stackGenerik.add("Log-2");
        String puncak = stackGenerik.remove(0);

        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("20", "Ganja", 12.5, 60, 48, 800000000L));
        pengelola.hapusPutusan("20");
        pengelola.undo();

        assertTrue(adaItemB
                        && daftarGenerik.validateInvariant()
                        && stackGenerik.validateInvariant()
                        && pengelola.validateInvariants()
                        && terhapus.equals("Item-B")
                        && puncak.equals("Log-2"),
                "Skenario 8: Kontrak generik StrukturData<T> (contains & validateInvariant) terverifikasi");
    }

    private static void test9_FilterMultiKriteriaLanjutan() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("31", "Sabu", 0.5, 60, 60, 1000000000L));
        pengelola.tambahPutusan(buatSampel("32", "Sabu", 5.2, 120, 96, 1500000000L));
        pengelola.tambahPutusan(buatSampel("33", "Ganja", 150.0, 108, 90, 1000000000L));

        ArrayDinamis<Putusan> hasil = pengelola.filterLengkap("Sabu", "114", 70, 120, 2.0, 10.0);
        assertTrue(hasil.size() == 1 && hasil.get(0).getCaseId().equals("32") && hasil.validateInvariant(),
                "Skenario 9: Filter gabungan lengkap (jenis, pasal, vonis & rentang berat)");
    }

    private static void test10_Muat50RecordCsvAsliDanValidasiSize() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        try {
            int dimuat = pengelola.muatDariCsv("data/putusan-50.csv");
            assertTrue(dimuat == 50 && pengelola.jumlahData() == 50 && pengelola.validateInvariants(),
                    "Skenario 10: Muat tepat 50 record dari data/putusan-50.csv & invariant valid");
        } catch (IOException e) {
            assertTrue(false, "Skenario 10 gagal membaca CSV: " + e.getMessage());
        }
    }

    private static void test11_PencarianBatasDanPencacahLangkah() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        try {
            pengelola.muatDariCsv("data/putusan-50.csv");
            int n = pengelola.jumlahData();
            String idPertama = pengelola.getSemuaPutusan().get(0).getCaseId();
            String idTerakhir = pengelola.getSemuaPutusan().get(n - 1).getCaseId();

            PengelolaPutusan.HasilPencarian awal = pengelola.cariDenganLangkah(idPertama);
            PengelolaPutusan.HasilPencarian akhir = pengelola.cariDenganLangkah(idTerakhir);
            PengelolaPutusan.HasilPencarian tidakAda = pengelola.cariDenganLangkah("ID-TIDAK-ADA-999");

            assertTrue(awal.ditemukan() && awal.indeks == 0 && awal.jumlahLangkah == 1
                            && akhir.ditemukan() && akhir.indeks == n - 1 && akhir.jumlahLangkah == n
                            && !tidakAda.ditemukan() && tidakAda.indeks == -1 && tidakAda.jumlahLangkah == n,
                    "Skenario 11: Pencarian batas (awal=1 langkah, akhir=" + n + " langkah, tidak ada=" + n + " langkah)");
        } catch (IOException e) {
            assertTrue(false, "Skenario 11 gagal: " + e.getMessage());
        }
    }

    private static void test12_HapusDanTambahKembaliPosisiPertamaTengahTerakhir() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        try {
            pengelola.muatDariCsv("data/putusan-50.csv");
            int n = pengelola.jumlahData();
            Putusan pertama = pengelola.getSemuaPutusan().get(0).salin();
            Putusan tengah = pengelola.getSemuaPutusan().get(n / 2).salin();
            Putusan terakhir = pengelola.getSemuaPutusan().get(n - 1).salin();

            boolean h1 = pengelola.hapusPutusan(pertama.getCaseId());
            boolean h2 = pengelola.hapusPutusan(tengah.getCaseId());
            boolean h3 = pengelola.hapusPutusan(terakhir.getCaseId());
            boolean berkurang = (pengelola.jumlahData() == n - 3);

            boolean u1 = pengelola.undo();
            boolean u2 = pengelola.undo();
            boolean u3 = pengelola.undo();
            boolean pulihSemua = (pengelola.jumlahData() == n)
                    && pengelola.getSemuaPutusan().get(0).getCaseId().equals(pertama.getCaseId())
                    && pengelola.getSemuaPutusan().get(n / 2).getCaseId().equals(tengah.getCaseId())
                    && pengelola.getSemuaPutusan().get(n - 1).getCaseId().equals(terakhir.getCaseId());

            assertTrue(h1 && h2 && h3 && berkurang && u1 && u2 && u3 && pulihSemua && pengelola.validateInvariants(),
                    "Skenario 12: Hapus & pulihkan kembali (undo) posisi pertama, tengah, dan terakhir");
        } catch (IOException e) {
            assertTrue(false, "Skenario 12 gagal: " + e.getMessage());
        }
    }

    private static void test13_PenelusuranStackAuditNonDestruktifDanClear() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("41", "Sabu", 1.0, 60, 48, 1000000000L));
        pengelola.ubahVonisBulan("41", 60);
        pengelola.hapusPutusan("41");

        int sebelumBaca = pengelola.jumlahRiwayatAudit();
        ArrayDinamis<StackAudit.EntriAudit> riwayat = pengelola.getSemuaRiwayatAudit();
        int setelahBaca = pengelola.jumlahRiwayatAudit();

        boolean nonDestruktif = (sebelumBaca == 3 && setelahBaca == 3 && riwayat.size() == 3
                && riwayat.get(0).jenisAksi.equals("HAPUS")
                && riwayat.get(2).jenisAksi.equals("TAMBAH"));

        StackAudit<String> stackUji = new StackAudit<>();
        stackUji.push("A");
        stackUji.push("B");
        boolean adaA = stackUji.contains("A");
        stackUji.clear();
        boolean kosongValid = stackUji.isEmpty() && stackUji.size() == 0 && stackUji.validateInvariant();

        assertTrue(nonDestruktif && adaA && kosongValid && pengelola.validateInvariants(),
                "Skenario 13: Penelusuran StackAudit non-destruktif & operasi clear()");
    }

    private static void test14_AgregasiStatistikPutusan() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("101", "Sabu", 2.0, 72, 60, 1000000000L));
        pengelola.tambahPutusan(buatSampel("102", "Sabu", 4.0, 96, 84, 1000000000L));
        pengelola.tambahPutusan(buatSampel("103", "Ganja", 6.0, 60, 48, 1000000000L));

        PengelolaPutusan.RingkasanStatistik stat = pengelola.hitungStatistik();
        boolean valid = (stat.totalKasus == 3)
                && (stat.vonisMinimumBulan == 48)
                && (stat.vonisMaksimumBulan == 84)
                && (Math.abs(stat.rataRataVonisBulan - 64.0) < 0.001)
                && (Math.abs(stat.rataRataBeratGram - 4.0) < 0.001)
                && (stat.jumlahJenisUnik == 2);

        assertTrue(valid, "Skenario 14: Agregasi statistik (rata-rata/min/max vonis, berat, distribusi jenis)");
    }

    private static void test15_SortingManualNonDestruktif() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("201", "Sabu", 5.0, 96, 84, 1000000000L));
        pengelola.tambahPutusan(buatSampel("202", "Ganja", 1.5, 60, 48, 1000000000L));
        pengelola.tambahPutusan(buatSampel("203", "Sabu", 12.0, 144, 120, 2000000000L));

        ArrayDinamis<Putusan> urutVonisAsc = pengelola.urutkanByVonis(true);
        ArrayDinamis<Putusan> urutBeratDesc = pengelola.urutkanByBerat(false);

        boolean terurut = urutVonisAsc.get(0).getVonisBulan() == 48
                && urutVonisAsc.get(2).getVonisBulan() == 120
                && Double.compare(urutBeratDesc.get(0).getBeratBarangBuktiGram(), 12.0) == 0;
        boolean asliUtuh = pengelola.getSemuaPutusan().get(0).getCaseId().equals("201");

        assertTrue(terurut && asliUtuh && pengelola.validateInvariants(),
                "Skenario 15: Sorting manual (vonis & berat) non-destruktif");
    }

    private static void test16_EksporHasilKeCsv() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("301", "Sabu", 1.25, 72, 60, 1000000000L));
        pengelola.tambahPutusan(buatSampel("302", "Ganja", 10.5, 84, 72, 1000000000L));

        String pathUji = "output/test-ekspor.csv";
        try {
            int jumlah = pengelola.eksporKeCsv(pathUji, pengelola.urutkanByVonis(false));
            File f = new File(pathUji);
            boolean ada = (jumlah == 2 && f.exists() && f.length() > 0);
            if (f.exists()) {
                f.delete();
            }
            assertTrue(ada, "Skenario 16: Ekspor hasil sorting ke berkas CSV menggunakan BufferedWriter");
        } catch (IOException e) {
            assertTrue(false, "Skenario 16 gagal ekspor CSV: " + e.getMessage());
        }
    }
}