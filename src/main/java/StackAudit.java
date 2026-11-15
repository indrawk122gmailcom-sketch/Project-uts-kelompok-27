public class StackAudit<T> implements StrukturData<T> {

    public static class EntriAudit {
        public final String jenisAksi; // "TAMBAH", "UBAH_VONIS", "HAPUS"
        public final Putusan snapshotSebelum;
        public final int indeksAsal;
        public final String keterangan;

        public EntriAudit(String jenisAksi, Putusan snapshotSebelum, int indeksAsal, String keterangan) {
            this.jenisAksi = jenisAksi;
            this.snapshotSebelum = snapshotSebelum;
            this.indeksAsal = indeksAsal;
            this.keterangan = keterangan;
        }
    }

    private static class Node<E> {
        E data;
        Node<E> next;

        Node(E data, Node<E> next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node<T> top;
    private int ukuran;

    public StackAudit() {
        this.top = null;
        this.ukuran = 0;
    }

    // --- Operasi Stack LIFO ---

    public void push(T entri) {
        if (entri == null) {
            throw new IllegalArgumentException("Elemen stack tidak boleh null");
        }
        top = new Node<>(entri, top);
        ukuran++;
    }

    public T pop() {
        if (isEmpty()) {
            return null;
        }
        T hasil = top.data;
        top = top.next;
        ukuran--;
        return hasil;
    }

    public T peek() {
        return isEmpty() ? null : top.data;
    }

    // --- Implementasi Kontrak StrukturData<T> ---

    @Override
    public void add(T element) {
        push(element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= ukuran) {
            throw new IndexOutOfBoundsException("Indeks stack tidak valid: " + index);
        }
        Node<T> current = top;
        for (int i = 0; i < index; i++) {
            current = current.next;
        }
        return current.data;
    }

    @Override
    public T remove(int index) {
        if (index != 0) {
            throw new UnsupportedOperationException("Stack LIFO hanya mendukung penghapusan elemen puncak (indeks 0)");
        }
        return pop();
    }

    @Override
    public int size() {
        return ukuran;
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public boolean validateInvariant() {
        if (ukuran < 0) {
            return false;
        }
        if (ukuran == 0) {
            return top == null;
        }
        if (top == null) {
            return false;
        }
        int hitungNode = 0;
        Node<T> current = top;
        while (current != null && hitungNode <= ukuran) {
            if (current.data == null) {
                return false;
            }
            hitungNode++;
            current = current.next;
        }
        return hitungNode == ukuran && current == null;
    }

    // --- Alias Kompatibilitas ---

    public boolean kosong() {
        return isEmpty();
    }

    public int ukuran() {
        return size();
    }
}