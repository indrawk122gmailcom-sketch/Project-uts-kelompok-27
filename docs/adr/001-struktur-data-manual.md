# ADR 001: Penggunaan Struktur Data Manual untuk Manajemen Putusan Narkotika

- **Status:** Diterima
- **Kelompok:** Kelompok 27
- **Konteks:** Implementasi sistem pengelolaan 50 data putusan narkotika berbasis Java CLI tanpa menggunakan koleksi bawaan pustaka `java.util`.

---

## 1. Penyimpanan Utama: `ArrayDinamis` (Dynamic Array)
- **Keputusan:** Membuat struktur data `ArrayDinamis` berbasis array internal `Putusan[]` dengan kapasitas awal 10 elemen dan faktor pertumbuhan eksponensial (`kapasitas * 2`) saat penuh.
- **Alasan:**
    - Jumlah data dapat bertambah secara dinamis melalui input pengguna tanpa batas kaku seperti array statis.
    - Akses elemen berdasarkan indeks berjalan dalam waktu konstan $O(1)$, sehingga proses iterasi untuk tampilan daftar dan penyaringan (*filter*) rentang vonis maupun jenis narkotika efisien.
    - Biaya amortisasi penambahan elemen tetap $O(1)$ meskipun sesekali terjadi penyalinan array saat kapasitas digandakan.

## 2. Riwayat Perubahan & Fitur Undo: `StackAudit` (Singly Linked List Stack)
- **Keputusan:** Mengimplementasikan struktur data `StackAudit` menggunakan simpul (*node*) *singly linked list* manual dengan pointer `top`.
- **Alasan:**
    - Operasi pembatalan perubahan terakhir (*Undo*) mengikuti prinsip **LIFO (Last-In, First-Out)** yang paling alami dimodelkan dengan *Stack*.
    - Menggunakan *linked list* menjamin kompleksitas waktu konstan murni $O(1)$ untuk operasi `push`, `pop`, dan `peek` tanpa perlu realokasi array.
    - Setiap entri menyimpan objek salinan mendalam (`Putusan.salin()`) beserta indeks asalnya, sehingga perubahan nilai atau penghapusan data dapat dipulihkan tepat ke posisi dan kondisi semula.

## 3. Pemrosesan Dataset: `CsvParser` (Character-by-Character State Machine)
- **Keputusan:** Membuat *parser* CSV manual yang membaca baris karakter demi karakter dengan status boolean `dalamKutip` alih-alih menggunakan `String.split(",")`.
- **Alasan:**
    - Kolom deskripsi `barang_bukti` pada dataset putusan pengadilan sering mengandung tanda koma di dalam teks (misalnya: `"1 bungkus plastik klip sabu (netto 0,6087 gram), 1 alat hisap"`).
    - Penggunaan `String.split(",")` akan memecah satu kolom deskripsi menjadi beberapa kolom yang salah dan menggeser indeks kolom berikutnya.
    - Pendekatan *state machine* memastikan pemisahan kolom hanya terjadi pada tanda koma di luar tanda kutip (`!dalamKutip`) serta mendukung *escaped quotes* (`""`).