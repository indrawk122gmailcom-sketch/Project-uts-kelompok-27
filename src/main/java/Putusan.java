public class Putusan {
    private String caseId;
    private String nomorPerkara;
    private String pengadilan;
    private String tanggalPutusan;
    private String terdakwaKode;
    private String jenisNarkotika;
    private double beratBarangBuktiGram;
    private String pasalDakwaan;
    private String pasalTerbukti;
    private int tuntutanBulan;
    private int vonisBulan;
    private long dendaRupiah;
    private String barangBukti;
    private String keadaanMemberatkan;
    private String keadaanMeringankan;
    private String sourcePages;
    private String provenance;

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

    public Putusan salin() {
        return new Putusan(
                this.caseId, this.nomorPerkara, this.pengadilan, this.tanggalPutusan,
                this.terdakwaKode, this.jenisNarkotika, this.beratBarangBuktiGram,
                this.pasalDakwaan, this.pasalTerbukti, this.tuntutanBulan, this.vonisBulan,
                this.dendaRupiah, this.barangBukti, this.keadaanMemberatkan,
                this.keadaanMeringankan, this.sourcePages, this.provenance
        );
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

    public void setVonisBulan(int vonisBulan) { this.vonisBulan = vonisBulan; }
    public void setPasalTerbukti(String pasalTerbukti) { this.pasalTerbukti = pasalTerbukti; }

    @Override
    public String toString() {
        return String.format("[%s] %s | %s | Berat: %.4f g | Tuntutan: %d bln | Vonis: %d bln | Denda: Rp%,d",
                caseId, nomorPerkara, jenisNarkotika, beratBarangBuktiGram, tuntutanBulan, vonisBulan, dendaRupiah);
    }
}