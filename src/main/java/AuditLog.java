public class AuditLog {
    public final String jenisAksi; // "TAMBAH", "UBAH_VONIS", "HAPUS"
    public final Putusan snapshotSebelum;
    public final int indeksAsal;
    public final String keterangan;

    public AuditLog(String jenisAksi, Putusan snapshotSebelum, int indeksAsal, String keterangan) {
        this.jenisAksi = jenisAksi;
        this.snapshotSebelum = snapshotSebelum;
        this.indeksAsal = indeksAsal;
        this.keterangan = keterangan;
    }

    public String getJenisAksi() {
        return jenisAksi;
    }

    public Putusan getSnapshotSebelum() {
        return snapshotSebelum;
    }

    public int getIndeksAsal() {
        return indeksAsal;
    }

    public String getKeterangan() {
        return keterangan;
    }

    @Override
    public String toString() {
        return "[" + jenisAksi + "] (Indeks: " + indeksAsal + ") " + keterangan;
    }
}