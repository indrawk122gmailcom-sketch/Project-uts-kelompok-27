public class StackAudit {

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

    private static class Node {
        EntriAudit data;
        Node next;

        Node(EntriAudit data, Node next) {
            this.data = data;
            this.next = next;
        }
    }

    private Node top;
    private int ukuran;

    public StackAudit() {
        this.top = null;
        this.ukuran = 0;
    }

    public void push(EntriAudit entri) {
        top = new Node(entri, top);
        ukuran++;
    }

    public EntriAudit pop() {
        if (kosong()) {
            return null;
        }
        EntriAudit hasil = top.data;
        top = top.next;
        ukuran--;
        return hasil;
    }

    public EntriAudit peek() {
        return kosong() ? null : top.data;
    }

    public boolean kosong() {
        return top == null;
    }

    public int ukuran() {
        return ukuran;
    }
}