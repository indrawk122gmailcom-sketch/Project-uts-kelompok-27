public class ArrayDinamis<T> implements StrukturData<T> {
    private Object[] data;
    private int jumlah;

    public ArrayDinamis() {
        this(10);
    }

    public ArrayDinamis(int kapasitasAwal) {
        if (kapasitasAwal <= 0) {
            kapasitasAwal = 10;
        }
        this.data = new Object[kapasitasAwal];
        this.jumlah = 0;
    }

    // --- Implementasi Kontrak StrukturData<T> ---

    @Override
    public void add(T element) {
        if (element == null) {
            throw new IllegalArgumentException("Elemen tidak boleh null");
        }
        if (jumlah == data.length) {
            perbesarKapasitas();
        }
        data[jumlah++] = element;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T get(int index) {
        if (index < 0 || index >= jumlah) {
            throw new IndexOutOfBoundsException("Indeks tidak valid: " + index);
        }
        return (T) data[index];
    }

    @Override
    @SuppressWarnings("unchecked")
    public T remove(int index) {
        if (index < 0 || index >= jumlah) {
            throw new IndexOutOfBoundsException("Indeks tidak valid: " + index);
        }
        T terhapus = (T) data[index];
        for (int i = index; i < jumlah - 1; i++) {
            data[i] = data[i + 1];
        }
        data[--jumlah] = null;
        return terhapus;
    }

    @Override
    public int size() {
        return jumlah;
    }

    @Override
    public boolean isEmpty() {
        return jumlah == 0;
    }

    @Override
    public boolean validateInvariant() {
        if (data == null || data.length <= 0) {
            return false;
        }
        if (jumlah < 0 || jumlah > data.length) {
            return false;
        }
        // Elemen aktif [0 .. jumlah - 1] harus rapat dan tidak boleh null
        for (int i = 0; i < jumlah; i++) {
            if (data[i] == null) {
                return false;
            }
        }
        // Slot cadangan [jumlah .. data.length - 1] harus bersih (null)
        for (int i = jumlah; i < data.length; i++) {
            if (data[i] != null) {
                return false;
            }
        }
        return true;
    }

    // --- Operasi Tambahan & Alias Kompatibilitas ---

    public void set(int index, T element) {
        if (index < 0 || index >= jumlah) {
            throw new IndexOutOfBoundsException("Indeks tidak valid: " + index);
        }
        if (element == null) {
            throw new IllegalArgumentException("Elemen tidak boleh null");
        }
        data[index] = element;
    }

    public void sisipkan(int indeks, T item) {
        if (indeks < 0 || indeks > jumlah) {
            throw new IndexOutOfBoundsException("Indeks di luar batas: " + indeks);
        }
        if (item == null) {
            throw new IllegalArgumentException("Elemen tidak boleh null");
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

    public ArrayDinamis<T> salin() {
        ArrayDinamis<T> salinan = new ArrayDinamis<>(this.data.length);
        for (int i = 0; i < this.jumlah; i++) {
            salinan.add(this.get(i));
        }
        return salinan;
    }

    public void tambah(T item) {
        add(item);
    }

    public T ambil(int indeks) {
        return get(indeks);
    }

    public T hapusPada(int indeks) {
        return remove(indeks);
    }

    public int ukuran() {
        return size();
    }

    public int kapasitas() {
        return data.length;
    }

    public boolean kosong() {
        return isEmpty();
    }

    private void perbesarKapasitas() {
        Object[] baru = new Object[data.length * 2];
        for (int i = 0; i < jumlah; i++) {
            baru[i] = data[i];
        }
        data = baru;
    }
}