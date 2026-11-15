import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PengelolaPutusan {

    public static class HasilPencarian {
        public final Putusan putusan;
        public final int indeks;
        public final int jumlahLangkah;

        public HasilPencarian(Putusan putusan, int indeks, int jumlahLangkah) {
            this.putusan = putusan;
            this.indeks = indeks;
            this.jumlahLangkah = jumlahLangkah;
        }

        public boolean ditemukan() {
            return putusan != null;
        }
    }

    public static class RingkasanStatistik {
        public final int totalKasus;
        public final int vonisMinimumBulan;
        public final int vonisMaksimumBulan;
        public final double rataRataVonisBulan;
        public final double totalBeratGram;
        public final double rataRataBeratGram;
        public final int jumlahJenisUnik;
        public final String[] daftarJenis;
        public final int[] jumlahPerJenis;

        public RingkasanStatistik(
                int totalKasus,
                int vonisMinimumBulan,
                int vonisMaksimumBulan,
                double rataRataVonisBulan,
                double totalBeratGram,
                double rataRataBeratGram,
                int jumlahJenisUnik,
                String[] daftarJenis,
                int[] jumlahPerJenis) {
            this.totalKasus = totalKasus;
            this.vonisMinimumBulan = vonisMinimumBulan;
            this.vonisMaksimumBulan = vonisMaksimumBulan;
            this.rataRataVonisBulan = rataRataVonisBulan;
            this.totalBeratGram = totalBeratGram;
            this.rataRataBeratGram = rataRataBeratGram;
            this.jumlahJenisUnik = jumlahJenisUnik;
            this.daftarJenis = daftarJenis;
            this.jumlahPerJenis = jumlahPerJenis;
        }
    }

    private final ArrayDinamis<Putusan> daftarPutusan;
    private final StackAudit<StackAudit.EntriAudit> riwayatAudit;

    public PengelolaPutusan() {
        this.daftarPutusan = new ArrayDinamis<>(16);
        this.riwayatAudit = new StackAudit<>();
    }

    public int muatDariCsv(String pathFile) throws IOException {
        int berhasilDimuat = 0;
        int nomorBaris = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String baris;
            boolean barisHeader = true;

            while ((baris = reader.readLine()) != null) {
                nomorBaris++;
                if (baris.trim().isEmpty()) {
                    continue;
                }
                if (barisHeader) {
                    barisHeader = false;
                    continue;
                }

                String[] k = CsvParser.parseBaris(baris);
                if (k.length < 16) {
                    System.err.println("[CSV SKIP] Baris " + nomorBaris + " hanya memiliki " + k.length + " kolom.");
                    continue;
                }

                String provenance = (k.length >= 17 && !k[16].trim().isEmpty())
                        ? k[16]
                        : "Direktori Putusan Mahkamah Agung RI";

                try {
                    Putusan p = new Putusan(
                            k[0], k[1], k[2], k[3], k[4], k[5],
                            parseDoubleAman(k[6]),
                            k[7], k[8],
                            parseIntAman(k[9]),
                            parseIntAman(k[10]),
                            parseLongAman(k[11]),
                            k[12], k[13], k[14], k[15], provenance
                    );

                    if (tambahPutusanTanpaAudit(p)) {
                        berhasilDimuat++;
                    } else {
                        System.err.println("[CSV SKIP] Baris " + nomorBaris + " (caseId=" + k[0] + ") ditolak validasi/duplikat.");
                    }
                } catch (NumberFormatException e) {
                    System.err.println("[CSV SKIP] Baris " + nomorBaris + " (caseId=" + k[0] + ") format angka tidak valid: " + e.getMessage());
                }
            }
        }
        return berhasilDimuat;
    }

    private double parseDoubleAman(String teks) {
        if (teks == null) return 0.0;
        String bersih = teks.trim().replace(",", ".");
        if (bersih.isEmpty() || bersih.equalsIgnoreCase("null") || bersih.equals("-")) {
            return 0.0;
        }
        return Double.parseDouble(bersih);
    }

    private int parseIntAman(String teks) {
        if (teks == null) return 0;
        String bersih = teks.trim();
        if (bersih.isEmpty() || bersih.equalsIgnoreCase("null") || bersih.equals("-")) {
            return 0;
        }
        return Integer.parseInt(bersih);
    }

    private long parseLongAman(String teks) {
        if (teks == null) return 0L;
        String bersih = teks.trim();
        if (bersih.isEmpty() || bersih.equalsIgnoreCase("null") || bersih.equals("-")) {
            return 0L;
        }
        return Long.parseLong(bersih);
    }

    private int cariIndeksByCaseId(String caseId) {
        if (caseId == null) {
            return -1;
        }
        String target = caseId.trim();
        for (int i = 0; i < daftarPutusan.size(); i++) {
            if (daftarPutusan.get(i).getCaseId().equalsIgnoreCase(target)) {
                return i;
            }
        }
        return -1;
    }

    private boolean tambahPutusanTanpaAudit(Putusan p) {
        if (!validasiPutusan(p)) {
            return false;
        }
        if (cariIndeksByCaseId(p.getCaseId()) != -1) {
            return false;
        }
        daftarPutusan.add(p);
        return true;
    }

    public boolean tambahPutusan(Putusan p) {
        if (!validasiPutusan(p)) {
            return false;
        }
        if (cariIndeksByCaseId(p.getCaseId()) != -1) {
            return false;
        }
        int indeksBaru = daftarPutusan.size();
        daftarPutusan.add(p);
        riwayatAudit.push(new StackAudit.EntriAudit(
                "TAMBAH", p.salin(), indeksBaru, "Menambahkan putusan caseId=" + p.getCaseId()
        ));
        return true;
    }

    public boolean validasiPutusan(Putusan p) {
        if (p == null) return false;
        if (p.getCaseId() == null || p.getCaseId().trim().isEmpty()) return false;
        if (p.getNomorPerkara() == null || p.getNomorPerkara().trim().isEmpty()) return false;
        if (p.getJenisNarkotika() == null || p.getJenisNarkotika().trim().isEmpty()) return false;
        if (p.getBeratBarangBuktiGram() < 0) return false;
        if (p.getTuntutanBulan() < 0) return false;
        if (p.getVonisBulan() < 0) return false;
        if (p.getDendaRupiah() < 0) return false;
        return true;
    }

    public Putusan cariByCaseId(String caseId) {
        int idx = cariIndeksByCaseId(caseId);
        return (idx == -1) ? null : daftarPutusan.get(idx);
    }

    public HasilPencarian cariDenganLangkah(String kataKunci) {
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return new HasilPencarian(null, -1, 0);
        }
        String target = kataKunci.trim();
        int langkah = 0;

        for (int i = 0; i < daftarPutusan.size(); i++) {
            langkah++;
            Putusan p = daftarPutusan.get(i);
            if (p.getCaseId().equalsIgnoreCase(target) || p.getNomorPerkara().equalsIgnoreCase(target)) {
                return new HasilPencarian(p, i, langkah);
            }
        }
        return new HasilPencarian(null, -1, langkah);
    }

    public ArrayDinamis<Putusan> filterRentangVonis(int minBulan, int maxBulan) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            if (p.getVonisBulan() >= minBulan && p.getVonisBulan() <= maxBulan) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> filterJenisNarkotika(String kataKunci) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        if (kataKunci == null || kataKunci.trim().isEmpty()) {
            return hasil;
        }
        String kunci = kataKunci.trim().toLowerCase();
        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            if (p.getJenisNarkotika().toLowerCase().contains(kunci)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> filterRentangBerat(double minBeratGram, double maxBeratGram) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            double berat = p.getBeratBarangBuktiGram();
            if (berat >= minBeratGram && berat <= maxBeratGram) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> filterPasalTerbukti(String kataKunciPasal) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        if (kataKunciPasal == null || kataKunciPasal.trim().isEmpty()) {
            return hasil;
        }
        String kunci = kataKunciPasal.trim().toLowerCase();
        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            if (p.getPasalTerbukti() != null && p.getPasalTerbukti().toLowerCase().contains(kunci)) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> filterMultiKriteria(String jenisNarkotika, int minVonisBulan, int maxVonisBulan, double minBeratGram) {
        return filterLengkap(jenisNarkotika, "", minVonisBulan, maxVonisBulan, minBeratGram, Double.MAX_VALUE);
    }

    public ArrayDinamis<Putusan> filterLengkap(
            String jenisNarkotika, String pasalTerbukti,
            int minVonisBulan, int maxVonisBulan,
            double minBeratGram, double maxBeratGram) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        String kunciJenis = (jenisNarkotika == null) ? "" : jenisNarkotika.trim().toLowerCase();
        String kunciPasal = (pasalTerbukti == null) ? "" : pasalTerbukti.trim().toLowerCase();

        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            boolean cocokJenis = kunciJenis.isEmpty() || p.getJenisNarkotika().toLowerCase().contains(kunciJenis);
            boolean cocokPasal = kunciPasal.isEmpty() || (p.getPasalTerbukti() != null && p.getPasalTerbukti().toLowerCase().contains(kunciPasal));
            boolean cocokVonis = p.getVonisBulan() >= minVonisBulan && p.getVonisBulan() <= maxVonisBulan;
            boolean cocokBerat = p.getBeratBarangBuktiGram() >= minBeratGram && p.getBeratBarangBuktiGram() <= maxBeratGram;

            if (cocokJenis && cocokPasal && cocokVonis && cocokBerat) {
                hasil.add(p);
            }
        }
        return hasil;
    }

    public boolean ubahVonisBulan(String caseId, int vonisBaru) {
        if (vonisBaru < 0) {
            return false;
        }
        int idx = cariIndeksByCaseId(caseId);
        if (idx == -1) {
            return false;
        }
        Putusan target = daftarPutusan.get(idx);
        Putusan sebelum = target.salin();
        target.setVonisBulan(vonisBaru);

        riwayatAudit.push(new StackAudit.EntriAudit(
                "UBAH_VONIS", sebelum, idx,
                "Ubah vonis caseId=" + caseId + " dari " + sebelum.getVonisBulan() + " ke " + vonisBaru + " bulan"
        ));
        return true;
    }

    public boolean hapusPutusan(String caseId) {
        int idx = cariIndeksByCaseId(caseId);
        if (idx == -1) {
            return false;
        }
        Putusan terhapus = daftarPutusan.remove(idx);
        riwayatAudit.push(new StackAudit.EntriAudit(
                "HAPUS", terhapus.salin(), idx,
                "Menghapus putusan caseId=" + caseId + " (" + terhapus.getNomorPerkara() + ")"
        ));
        return true;
    }

    public boolean undo() {
        if (riwayatAudit.isEmpty()) {
            return false;
        }
        StackAudit.EntriAudit aksiTerakhir = riwayatAudit.pop();

        switch (aksiTerakhir.jenisAksi) {
            case "TAMBAH":
                int idxHapus = cariIndeksByCaseId(aksiTerakhir.snapshotSebelum.getCaseId());
                if (idxHapus != -1) {
                    daftarPutusan.remove(idxHapus);
                }
                return true;

            case "UBAH_VONIS":
                int idxUbah = cariIndeksByCaseId(aksiTerakhir.snapshotSebelum.getCaseId());
                if (idxUbah != -1) {
                    daftarPutusan.get(idxUbah).setVonisBulan(aksiTerakhir.snapshotSebelum.getVonisBulan());
                }
                return true;

            case "HAPUS":
                int posisi = Math.min(aksiTerakhir.indeksAsal, daftarPutusan.size());
                daftarPutusan.sisipkan(posisi, aksiTerakhir.snapshotSebelum);
                return true;

            default:
                return false;
        }
    }

    public ArrayDinamis<StackAudit.EntriAudit> getSemuaRiwayatAudit() {
        ArrayDinamis<StackAudit.EntriAudit> daftarAudit = new ArrayDinamis<>(Math.max(4, riwayatAudit.size()));
        for (int i = 0; i < riwayatAudit.size(); i++) {
            daftarAudit.add(riwayatAudit.get(i));
        }
        return daftarAudit;
    }

    public boolean validateInvariants() {
        return daftarPutusan.validateInvariant() && riwayatAudit.validateInvariant();
    }

    public int jumlahData() {
        return daftarPutusan.size();
    }

    public int jumlahRiwayatAudit() {
        return riwayatAudit.size();
    }

    public StackAudit.EntriAudit lihatAuditTerakhir() {
        return riwayatAudit.peek();
    }

    public ArrayDinamis<Putusan> getSemuaPutusan() {
        return daftarPutusan;
    }

    // =========================================================================
    // FITUR TAMBAHAN SULTAN: AGREGASI STATISTIK, SORTING MANUAL, & EKSPOR CSV
    // =========================================================================

    public RingkasanStatistik hitungStatistik() {
        int n = daftarPutusan.size();
        if (n == 0) {
            return new RingkasanStatistik(0, 0, 0, 0.0, 0.0, 0.0, 0, new String[0], new int[0]);
        }

        int minVonis = Integer.MAX_VALUE;
        int maxVonis = Integer.MIN_VALUE;
        long totalVonis = 0L;
        double totalBerat = 0.0;

        String[] tempJenis = new String[n];
        int[] tempJumlah = new int[n];
        int unik = 0;

        for (int i = 0; i < n; i++) {
            Putusan p = daftarPutusan.get(i);
            int vonis = p.getVonisBulan();
            double berat = p.getBeratBarangBuktiGram();

            if (vonis < minVonis) minVonis = vonis;
            if (vonis > maxVonis) maxVonis = vonis;
            totalVonis += vonis;
            totalBerat += berat;

            String jenis = (p.getJenisNarkotika() == null || p.getJenisNarkotika().trim().isEmpty())
                    ? "Tidak Diketahui"
                    : p.getJenisNarkotika().trim();

            int indeksJenis = -1;
            for (int j = 0; j < unik; j++) {
                if (tempJenis[j].equalsIgnoreCase(jenis)) {
                    indeksJenis = j;
                    break;
                }
            }

            if (indeksJenis != -1) {
                tempJumlah[indeksJenis]++;
            } else {
                tempJenis[unik] = jenis;
                tempJumlah[unik] = 1;
                unik++;
            }
        }

        String[] daftarJenis = new String[unik];
        int[] jumlahPerJenis = new int[unik];
        for (int i = 0; i < unik; i++) {
            daftarJenis[i] = tempJenis[i];
            jumlahPerJenis[i] = tempJumlah[i];
        }

        double rataVonis = (double) totalVonis / n;
        double rataBerat = totalBerat / n;

        return new RingkasanStatistik(
                n, minVonis, maxVonis, rataVonis, totalBerat, rataBerat, unik, daftarJenis, jumlahPerJenis
        );
    }

    public ArrayDinamis<Putusan> urutkanByVonis(boolean ascending) {
        int n = daftarPutusan.size();
        Putusan[] arr = new Putusan[n];
        for (int i = 0; i < n; i++) {
            arr[i] = daftarPutusan.get(i);
        }

        for (int i = 1; i < n; i++) {
            Putusan kunci = arr[i];
            int j = i - 1;
            while (j >= 0 && (ascending
                    ? arr[j].getVonisBulan() > kunci.getVonisBulan()
                    : arr[j].getVonisBulan() < kunci.getVonisBulan())) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = kunci;
        }

        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(Math.max(4, n));
        for (int i = 0; i < n; i++) {
            hasil.add(arr[i]);
        }
        return hasil;
    }

    public ArrayDinamis<Putusan> urutkanByBerat(boolean ascending) {
        int n = daftarPutusan.size();
        Putusan[] arr = new Putusan[n];
        for (int i = 0; i < n; i++) {
            arr[i] = daftarPutusan.get(i);
        }

        for (int i = 1; i < n; i++) {
            Putusan kunci = arr[i];
            int j = i - 1;
            while (j >= 0 && (ascending
                    ? Double.compare(arr[j].getBeratBarangBuktiGram(), kunci.getBeratBarangBuktiGram()) > 0
                    : Double.compare(arr[j].getBeratBarangBuktiGram(), kunci.getBeratBarangBuktiGram()) < 0)) {
                arr[j + 1] = arr[j];
                j--;
            }
            arr[j + 1] = kunci;
        }

        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(Math.max(4, n));
        for (int i = 0; i < n; i++) {
            hasil.add(arr[i]);
        }
        return hasil;
    }

    public int eksporKeCsv(String pathFile, ArrayDinamis<Putusan> sumberData) throws IOException {
        ArrayDinamis<Putusan> data = (sumberData != null) ? sumberData : daftarPutusan;
        File targetFile = new File(pathFile);
        File parent = targetFile.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        int ditulis = 0;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(targetFile))) {
            writer.write("case_id,nomor_perkara,jenis_narkotika,berat_barang_bukti_gram,pasal_terbukti,tuntutan_bulan,vonis_bulan,denda_rupiah");
            writer.newLine();

            for (int i = 0; i < data.size(); i++) {
                Putusan p = data.get(i);
                String baris = escapeCsv(p.getCaseId()) + ","
                        + escapeCsv(p.getNomorPerkara()) + ","
                        + escapeCsv(p.getJenisNarkotika()) + ","
                        + p.getBeratBarangBuktiGram() + ","
                        + escapeCsv(p.getPasalTerbukti()) + ","
                        + p.getTuntutanBulan() + ","
                        + p.getVonisBulan() + ","
                        + p.getDendaRupiah();
                writer.write(baris);
                writer.newLine();
                ditulis++;
            }
        }
        return ditulis;
    }

    private String escapeCsv(String nilai) {
        if (nilai == null) {
            return "";
        }
        if (nilai.contains(",") || nilai.contains("\"") || nilai.contains("\n")) {
            return "\"" + nilai.replace("\"", "\"\"") + "\"";
        }
        return nilai;
    }
}