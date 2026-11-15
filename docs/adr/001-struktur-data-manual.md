# ADR 001: Penggunaan Struktur Data Manual Generik untuk Manajemen Putusan Narkotika

- **Status:** Diterima
- **Kelompok:** Kelompok 27
- **Konteks:** Implementasi sistem pengelolaan 50 data putusan narkotika berbasis Java CLI tanpa menggunakan koleksi bawaan pustaka `java.util`[cite: 16].

---

## 1. Penyimpanan Utama: `ArrayDinamis<T>` (Generic Dynamic Array)
- **Keputusan:** Membangun struktur data generik `ArrayDinamis<T>` berbasis array internal `Object[]` dengan kapasitas awal 10 elemen, ekspansi otomatis (`kapasitas * 2`) saat penuh, serta metode pemeriksaan `validateInvariant()`.
- **Alasan:**
  - Tipe generik `<T>` memungkinkan struktur data dipakai ulang secara aman (*type-safe*) baik untuk daftar utama `ArrayDinamis<Putusan>` maupun salinan sementara saat pengurutan non-destruktif[cite: 17].
  - Akses elemen berdasarkan indeks berjalan dalam waktu konstan $O(1)$ untuk iterasi daftar, penyaringan (*filter*), dan perhitungan agregasi.
  - Biaya amortisasi penambahan elemen tetap $O(1)$ dengan *invariant* elemen aktif selalu rapat pada indeks `0` hingga `size() - 1` tanpa celah `null`.

## 2. Riwayat Perubahan & Fitur Undo: `StackAudit<T>` (Generic Singly Linked List Stack)
- **Keputusan:** Mengimplementasikan struktur data `StackAudit<T>` menggunakan simpul (*node*) *singly linked list* manual dengan pointer `top`.
- **Alasan:**
  - Operasi pembatalan perubahan terakhir (*Undo*) mengikuti prinsip **LIFO (Last-In, First-Out)** yang paling alami dimodelkan dengan *Stack*.
  - Menggunakan *linked list* menjamin kompleksitas waktu konstan murni $O(1)$ untuk `push`, `pop`, dan `peek` tanpa realokasi array.
  - Setiap entri menyimpan salinan mendalam objek (`Putusan.salin()`) beserta indeks asalnya sehingga mutasi (`TAMBAH`, `UBAH_VONIS`, `HAPUS`) dapat dipulihkan tepat ke posisi semula.

## 3. Pemrosesan Dataset: `CsvParser` (Character-by-Character State Machine)
- **Keputusan:** Membuat *parser* CSV manual yang membaca karakter demi karakter dengan status boolean `dalamKutip` alih-alih `String.split(",")`.
- **Alasan:**
  - Kolom `barangBukti` pada putusan pengadilan sering memuat tanda koma di dalam teks (misalnya: `"1 bungkus plastik klip sabu (netto 0,6087 gram), 1 alat hisap"`).
  - *State machine* memastikan pemisahan kolom hanya terjadi pada tanda koma di luar tanda kutip (`!dalamKutip`) serta mendukung *escaped quotes* (`""`).