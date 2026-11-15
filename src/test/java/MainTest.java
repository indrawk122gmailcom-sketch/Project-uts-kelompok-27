public class MainTest {

    private static int lulus = 0;
    private static int gagal = 0;

    public static void main(String[] args) {
        System.out.println("=== MENJALANKAN SUITE PENGUJIAN (9 SKENARIO) ===");

        test1_EkspansiKapasitasArrayDinamis();
        test2_TambahPutusanDanTolakDuplikatCaseId();
        test3_ValidasiInputNegatifDanKosong();
        test4_PencarianCaseIdDanFilterRentangVonis();
        test5_UbahVonisDanPencatatanStackAudit();
        test6_HapusDataDanPemulihanUndoStack();
        test7_ParsingCsvDenganTandaKutipDanKoma();
        test8_StrukturDataGenerikDanValidateInvariant();
        test9_FilterMultiKriteriaLanjutan();

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
        String terhapus = daftarGenerik.remove(1);

        StrukturData<String> stackGenerik = new StackAudit<>();
        stackGenerik.add("Log-1");
        stackGenerik.add("Log-2");
        String puncak = stackGenerik.remove(0);

        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("20", "Ganja", 12.5, 60, 48, 800000000L));
        pengelola.hapusPutusan("20");
        pengelola.undo();

        assertTrue(daftarGenerik.validateInvariant()
                        && stackGenerik.validateInvariant()
                        && pengelola.validateInvariants()
                        && terhapus.equals("Item-B")
                        && puncak.equals("Log-2"),
                "Skenario 8: Kontrak generik StrukturData<T> & validateInvariant() terverifikasi");
    }

    private static void test9_FilterMultiKriteriaLanjutan() {
        PengelolaPutusan pengelola = new PengelolaPutusan();
        pengelola.tambahPutusan(buatSampel("31", "Sabu", 0.5, 60, 60, 1000000000L));
        pengelola.tambahPutusan(buatSampel("32", "Sabu", 5.2, 120, 96, 1500000000L));
        pengelola.tambahPutusan(buatSampel("33", "Ganja", 150.0, 108, 90, 1000000000L));

        ArrayDinamis<Putusan> hasil = pengelola.filterMultiKriteria("Sabu", 70, 120, 2.0);
        assertTrue(hasil.size() == 1 && hasil.get(0).getCaseId().equals("32") && hasil.validateInvariant(),
                "Skenario 9: Filter multi-kriteria (jenis, vonis & berat) mengembalikan hasil akurat");
    }
}