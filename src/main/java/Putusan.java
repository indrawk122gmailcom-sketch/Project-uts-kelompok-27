public class Putusan {
    private final String caseId;
    private final String nomorPerkara;
    private final String pengadilan;
    private final String tanggalPutusan;
    private final String terdakwaKode;
    private final String jenisNarkotika;
    private double beratBarangBuktiGram;
    private final String pasalDakwaan;
    private String pasalTerbukti;
    private final int tuntutanBulan;
    private int vonisBulan;
    private long dendaRupiah;
    private final String barangBukti;
    private final String keadaanMemberatkan;
    private final String keadaanMeringankan;
    private final String sourcePages;
    private final String provenance;

    public Putusan(String caseId, String nomorPerkara, String pengadilan, String tanggalPutusan,
                   String terdakwaKode, String jenisNarkotika, double beratBarangBuktiGram,
                   String pasalDakwaan, String pasalTerbukti, int tuntutanBulan, int vonisBulan,
                   long dendaRupiah, String barangBukti, String keadaanMemberatkan,
                   String keadaanMeringankan, String sourcePages, String provenance) {
        this.caseId = caseId;
        this.nomorPerkara = nomorPerkara;
        this.pengadilan = pengadilan;
        this.tanggalPutusan = tanggalPutusan;
        this.terdakwaKode = terdakwaKode;
        this.jenisNarkotika = jenisNarkotika;
        this.beratBarangBuktiGram = beratBarangBuktiGram;
        this.pasalDakwaan = pasalDakwaan;
        this.pasalTerbukti = pasalTerbukti;
        this.tuntutanBulan = tuntutanBulan;
        this.vonisBulan = vonisBulan;
        this.dendaRupiah = dendaRupiah;
        this.barangBukti = barangBukti;
        this.keadaanMemberatkan = keadaanMemberatkan;
        this.keadaanMeringankan = keadaanMeringankan;
        this.sourcePages = sourcePages;
        this.provenance = provenance;
    }

    public String getCaseId() { return caseId; }
    public String getNomorPerkara() { return nomorPerkara; }
    public String getPengadilan() { return pengadilan; }
    public String getTanggalPutusan() { return tanggalPutusan; }
    public String getTerdakwaKode() { return terdakwaKode; }
    public String getJenisNarkotika() { return jenisNarkotika; }
    public double getBeratBarangBuktiGram() { return beratBarangBuktiGram; }
    public String getPasalDakwaan() { return pasalDakwaan; }
    public String getPasalTerbukti() { return pasalTerbukti; }
    public int getTuntutanBulan() { return tuntutanBulan; }
    public int getVonisBulan() { return vonisBulan; }
    public long getDendaRupiah() { return dendaRupiah; }
    public String getBarangBukti() { return barangBukti; }
    public String getKeadaanMemberatkan() { return keadaanMemberatkan; }
    public String getKeadaanMeringankan() { return keadaanMeringankan; }
    public String getSourcePages() { return sourcePages; }
    public String getProvenance() { return provenance; }

    public void setBeratBarangBuktiGram(double beratBarangBuktiGram) {
        this.beratBarangBuktiGram = beratBarangBuktiGram;
    }

    public void setPasalTerbukti(String pasalTerbukti) {
        this.pasalTerbukti = pasalTerbukti;
    }

    public void setVonisBulan(int vonisBulan) {
        this.vonisBulan = vonisBulan;
    }

    public void setDendaRupiah(long dendaRupiah) {
        this.dendaRupiah = dendaRupiah;
    }

    public Putusan salin() {
        return new Putusan(
                caseId, nomorPerkara, pengadilan, tanggalPutusan, terdakwaKode,
                jenisNarkotika, beratBarangBuktiGram, pasalDakwaan, pasalTerbukti,
                tuntutanBulan, vonisBulan, dendaRupiah, barangBukti,
                keadaanMemberatkan, keadaanMeringankan, sourcePages, provenance
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Putusan)) return false;
        Putusan other = (Putusan) obj;
        return this.caseId != null && other.caseId != null
                && this.caseId.equalsIgnoreCase(other.caseId);
    }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | %s (%.2f gr) | Pasal %s | Vonis: %d bln | Denda: Rp%,d",
                caseId, nomorPerkara, pengadilan, jenisNarkotika,
                beratBarangBuktiGram, pasalTerbukti, vonisBulan, dendaRupiah);
    }
}