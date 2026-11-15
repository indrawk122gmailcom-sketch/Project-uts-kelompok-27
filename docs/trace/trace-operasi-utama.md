# Penelusuran Manual (*Manual Trace*) Operasi Struktur Data

Dokumen ini mencatat penelusuran langkah demi langkah perubahan keadaan memori pada `ArrayDinamis` dan `StackAudit` selama eksekusi skenario pengujian.

---

## Trace 1: Ekspansi Kapasitas Otomatis `ArrayDinamis`
**Kondisi Awal:** `ArrayDinamis arr = new ArrayDinamis(2);`
- `data` = `[null, null]` | `jumlah` = `0` | `kapasitas()` = `2`

| Langkah | Operasi | Kondisi Array `data` | `jumlah` | `kapasitas()` | Keterangan |
| :--- | :--- | :--- | :---: | :---: | :--- |
| 1 | `arr.tambah(P1)` | `[P1, null]` | 1 | 2 | Disimpan di indeks `0`, `jumlah` menjadi `1`. |
| 2 | `arr.tambah(P2)` | `[P1, P2]` | 2 | 2 | Disimpan di indeks `1`, `jumlah` menjadi `2` (penuh). |
| 3 | `arr.tambah(P3)` | `[P1, P2, P3, null]` | 3 | 4 | `jumlah == data.length` (2 == 2) memicu `perbesarKapasitas()`: array baru ukuran `2 * 2 = 4` dibuat, `P1` & `P2` disalin, lalu `P3` disimpan di indeks `2`. |

---

## Trace 2: Penghapusan Data & Pemulihan Lewat `StackAudit` (Undo)
**Kondisi Awal:**
- `daftarPutusan`: `[P1 (vonis: 72), P2 (vonis: 84)]` (`ukuran = 2`)
- `riwayatAudit`: `top -> null` (`ukuran = 0`)

| Langkah | Operasi | Kondisi `daftarPutusan` | Kondisi `riwayatAudit` (`top`) | Keterangan |
| :--- | :--- | :--- | :--- | :--- |
| 1 | `hapusPutusan("1")` | `[P2 (vonis: 84)]` (`ukuran = 1`) | `Node("HAPUS", salinan P1, indeksAsal=0) -> null` | `P1` ditemukan di indeks `0`, dihapus dengan menggeser `P2` ke indeks `0`, dan snapshot `P1` di-*push* ke `top`. |
| 2 | `cariByCaseId("1")` | `[P2 (vonis: 84)]` | `Node("HAPUS", salinan P1, indeksAsal=0) -> null` | Mengembalikan `null` karena `P1` sudah tidak ada di array utama. |
| 3 | `undo()` | `[P1 (vonis: 72), P2 (vonis: 84)]` (`ukuran = 2`) | `top -> null` (`ukuran = 0`) | Entri di-*pop* dari `StackAudit`, lalu `sisipkan(0, snapshotSebelum)` menggeser `P2` ke indeks `1` dan memulihkan `P1` ke indeks `0`. |