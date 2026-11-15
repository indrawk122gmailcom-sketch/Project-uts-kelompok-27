import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class PengelolaPutusan {
    private final ArrayDinamis<Putusan> daftarPutusan;
    private final StackAudit<StackAudit.EntriAudit> riwayatAudit;

    public PengelolaPutusan() {
        this.daftarPutusan = new ArrayDinamis<>(16);
        this.riwayatAudit = new StackAudit<>();
    }

    public int muatDariCsv(String pathFile) throws IOException {
        int berhasilDimuat = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(pathFile))) {
            String baris;
            boolean barisHeader = true;

            while ((baris = reader.readLine()) != null) {
                if (baris.trim().isEmpty()) {
                    continue;
                }
                if (barisHeader) {
                    barisHeader = false;
                    continue;
                }

                String[] k = CsvParser.parseBaris(baris);
                if (k.length < 17) {
                    continue;
                }

                Putusan p = new Putusan(
                        k[0], k[1], k[2], k[3], k[4], k[5],
                        Double.parseDouble(k[6]),
                        k[7], k[8],
                        Integer.parseInt(k[9]),
                        Integer.parseInt(k[10]),
                        Long.parseLong(k[11]),
                        k[12], k[13], k[14], k[15], k[16]
                );

                if (tambahPutusanTanpaAudit(p)) {
                    berhasilDimuat++;
                }
            }
        }
        return berhasilDimuat;
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

    public ArrayDinamis<Putusan> filterMultiKriteria(String jenisNarkotika, int minVonisBulan, int maxVonisBulan, double minBeratGram) {
        ArrayDinamis<Putusan> hasil = new ArrayDinamis<>(10);
        String kunciJenis = (jenisNarkotika == null) ? "" : jenisNarkotika.trim().toLowerCase();

        for (int i = 0; i < daftarPutusan.size(); i++) {
            Putusan p = daftarPutusan.get(i);
            boolean cocokJenis = kunciJenis.isEmpty() || p.getJenisNarkotika().toLowerCase().contains(kunciJenis);
            boolean cocokVonis = p.getVonisBulan() >= minVonisBulan && p.getVonisBulan() <= maxVonisBulan;
            boolean cocokBerat = p.getBeratBarangBuktiGram() >= minBeratGram;

            if (cocokJenis && cocokVonis && cocokBerat) {
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
}