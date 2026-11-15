# Penelusuran Manual (*Manual Trace*) Operasi Struktur Data

Dokumen ini mencatat perubahan status array internal, indeks elemen, pointer `top` pada *stack*, dan jumlah langkah pencarian selama eksekusi skenario pengujian.

---

## Trace 1: Ekspansi Kapasitas Otomatis `ArrayDinamis`
**Kondisi Awal:** `ArrayDinamis arr = new ArrayDinamis(2);` -> `data = [null, null]`, `jumlah = 0`, `kapasitas = 2`

| Langkah | Operasi | Kondisi Array `data` | `jumlah` | `kapasitas` | Keterangan |
| :---: | :--- | :--- | :---: | :---: | :--- |
| 1 | `tambah(P1)` | `[P1, null]` | 1 | 2 | Disimpan pada indeks `0`. |
| 2 | `tambah(P2)` | `[P1, P2]` | 2 | 2 | Disimpan pada indeks `1` (kapasitas penuh). |
| 3 | `tambah(P3)` | `[P1, P2, P3, null]` | 3 | 4 | Memicu `perbesarKapasitas()`: array baru berukuran `4` dialokasikan, `P1` & `P2` disalin, lalu `P3` dimasukkan pada indeks `2`. |

---

## Trace 2: Penghapusan Elemen Pertama, Tengah, dan Terakhir (`hapusPada`)
**Kondisi Awal:** `data = [P1, P2, P3, P4]`, `jumlah = 4`

| Langkah | Operasi | Kondisi Array `data` | `jumlah` | Pergeseran Elemen |
| :---: | :--- | :--- | :---: | :--- |
| 1 | `hapusPada(0)` *(pertama)* | `[P2, P3, P4, null]` | 3 | `P2` digeser ke `[0]`, `P3` ke `[1]`, `P4` ke `[2]`; indeks `[3]` di-set `null`. |
| 2 | `hapusPada(1)` *(tengah)* | `[P2, P4, null, null]` | 2 | `P4` digeser dari indeks `[2]` ke `[1]`; indeks `[2]` di-set `null`. |
| 3 | `hapusPada(1)` *(terakhir)* | `[P2, null, null, null]` | 1 | Tidak ada pergeseran elemen ke kiri; indeks `[1]` langsung di-set `null`. |

---

## Trace 3: Penghapusan Data & Pemulihan Lewat `StackAudit` (Undo)
**Kondisi Awal:** `daftarPutusan = [P1, P2]` (`jumlah = 2`), `riwayatAudit.top = null`

| Langkah | Operasi | `daftarPutusan` | `riwayatAudit` (`top`) | Keterangan |
| :---: | :--- | :--- | :--- | :--- |
| 1 | `hapusPutusan("1")` | `[P2]` (`jumlah = 1`) | `Node("HAPUS", salinan P1, idx=0) -> null` | `P1` dihapus dari array dan snapshot-nya di-*push* ke puncak *stack*. |
| 2 | `cariByCaseId("1")` | `[P2]` (`jumlah = 1`) | `Node("HAPUS", salinan P1, idx=0) -> null` | Memeriksa 1 elemen (`P2`), tidak cocok -> mengembalikan `null` (1 langkah pencarian). |
| 3 | `undo()` | `[P1, P2]` (`jumlah = 2`) | `top -> null` (`ukuran = 0`) | Entri di-*pop*, lalu `sisipkan(0, P1)` menggeser `P2` ke indeks `1` dan memulihkan `P1` tepat di indeks `0`. |