public class StackAudit<T> implements StrukturData<T> {

    public static class EntriAudit extends AuditLog {
        public EntriAudit(String jenisAksi, Putusan snapshotSebelum, int indeksAsal, String keterangan) {
            super(jenisAksi, snapshotSebelum, indeksAsal, keterangan);
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

    @Override
    public void add(T element) {
        push(element);
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= ukuran) {
            throw new IndexOutOfBoundsException("Indeks stack tidak valid: " + index);
        }
        Node<T> curr = top;
        for (int i = 0; i < index; i++) {
            curr = curr.next;
        }
        return curr.data;
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= ukuran) {
            throw new IndexOutOfBoundsException("Indeks stack tidak valid: " + index);
        }
        if (index == 0) {
            return pop();
        }
        Node<T> prev = top;
        for (int i = 0; i < index - 1; i++) {
            prev = prev.next;
        }
        T terhapus = prev.next.data;
        prev.next = prev.next.next;
        ukuran--;
        return terhapus;
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
    public boolean contains(T value) {
        if (value == null) {
            return false;
        }
        Node<T> curr = top;
        while (curr != null) {
            if (value.equals(curr.data)) {
                return true;
            }
            curr = curr.next;
        }
        return false;
    }

    @Override
    public void clear() {
        top = null;
        ukuran = 0;
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
        int hitung = 0;
        Node<T> curr = top;
        while (curr != null && hitung <= ukuran) {
            if (curr.data == null) {
                return false;
            }
            hitung++;
            curr = curr.next;
        }
        return curr == null && hitung == ukuran;
    }

    public boolean kosong() { return isEmpty(); }
    public int ukuran() { return size(); }
}