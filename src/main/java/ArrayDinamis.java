public class ArrayDinamis {
    private Putusan[] data;
    private int jumlah;

    public ArrayDinamis() {
        this(10);
    }

    public ArrayDinamis(int kapasitasAwal) {
        if (kapasitasAwal <= 0) {
            kapasitasAwal = 10;
        }
        this.data = new Putusan[kapasitasAwal];
        this.jumlah = 0;
    }

    public void tambah(Putusan item) {
        if (jumlah == data.length) {
            perbesarKapasitas();
        }
        data[jumlah++] = item;
    }

    public void sisipkan(int indeks, Putusan item) {
        if (indeks < 0 || indeks > jumlah) {
            throw new IndexOutOfBoundsException("Indeks di luar batas: " + indeks);
        }
        if (jumlah == data.length) {
            perbesarKapasitas();
        }
        for (int i = jumlah; i > indeks; i--) {
            data[i] = data[i - 1];
        }
        data[indeks] = item;
        jumlah++;
    }

    public Putusan ambil(int indeks) {
        if (indeks < 0 || indeks >= jumlah) {
            throw new IndexOutOfBoundsException("Indeks tidak valid: " + indeks);
        }
        return data[indeks];
    }

    public Putusan hapusPada(int indeks) {
        if (indeks < 0 || indeks >= jumlah) {
            throw new IndexOutOfBoundsException("Indeks tidak valid: " + indeks);
        }
        Putusan terhapus = data[indeks];
        for (int i = indeks; i < jumlah - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--jumlah] = null;
        return terhapus;
    }

    public int cariIndeksByCaseId(String caseId) {
        if (caseId == null) return -1;
        for (int i = 0; i < jumlah; i++) {
            if (data[i].getCaseId().equalsIgnoreCase(caseId.trim())) {
                return i;
            }
        }
        return -1;
    }

    public int ukuran() {
        return jumlah;
    }

    public int kapasitas() {
        return data.length;
    }

    public boolean kosong() {
        return jumlah == 0;
    }

    private void perbesarKapasitas() {
        Putusan[] baru = new Putusan[data.length * 2];
        for (int i = 0; i < jumlah; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }
}