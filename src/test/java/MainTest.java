public class MainTest {

    private static int lulus = 0;
    private static int gagal = 0;

    public static void main(String[] args) {
        System.out.println("=== MENJALANKAN SUITE PENGUJIAN (7 SKENARIO WAJIB) ===");

        test1_EkspansiKapasitasArrayDinamis();
        test2_TambahPutusanDanTolakDuplikatCaseId();
        test3_ValidasiInputNegatifDanKosong();
        test4_PencarianCaseIdDanFilterRentangVonis();
        test5_UbahVonisDanPencatatanStackAudit();
        test6_HapusDataDanPemulihanUndoStack();
        test7_ParsingCsvDenganTandaKutipDanKoma();

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
        ArrayDinamis arr = new ArrayDinamis(2);
        arr.tambah(buatSampel("1", "Sabu", 0.6087, 72, 72, 4000000000L));
        arr.tambah(buatSampel("2", "Sabu", 2.1169, 96, 84, 1000000000L));
        arr.tambah(buatSampel("3", "Sabu", 412.76, 204, 180, 2000000000L));

        assertTrue(arr.ukuran() == 3 && arr.kapasitas() == 4 && arr.ambil(2).getCaseId().equals("3"),
                "Skenario 1: Ekspansi otomatis kapasitas ArrayDinamis (2 -> 4)");
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
        ArrayDinamis rentang = pengelola.filterRentangVonis(70, 90);

        assertTrue(ditemukan != null && ditemukan.getVonisBulan() == 84
                        && tidakAda == null && rentang.ukuran() == 2,
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
}