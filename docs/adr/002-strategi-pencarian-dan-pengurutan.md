# ADR 002: Strategi Pencarian, Penyaringan, dan Pengurutan Non-Destruktif

- **Status:** Diterima
- **Kelompok:** Kelompok 27
- **Konteks:** Kebutuhan pencarian dengan pelaporan jumlah langkah, filter multi-kriteria, agregasi statistik, dan pengurutan lama vonis/berat barang bukti tanpa merusak urutan asli pada struktur data utama[cite: 17].

---

## 1. Strategi Pencarian: Sequential Search dengan Pencacah Langkah
- **Keputusan:** Menggunakan pencarian berurutan (*linear/sequential search*) pada `ArrayDinamis<Putusan>` dengan menghitung jumlah iterasi perbandingan (`langkah`) hingga `caseId` atau `nomorPerkara` ditemukan.
- **Alasan:**
    - Data pada `ArrayDinamis<Putusan>` disimpan berdasarkan urutan pemasukan dari `data/putusan-50.csv`, sehingga pencarian linier langsung bekerja tanpa pra-pengurutan.
    - Kompleksitas waktu kasus terbaik $O(1)$ dan kasus terburuk $O(n)$, ringan untuk 50 putusan sekaligus menampilkan metrik jumlah langkah pencarian secara transparan.

## 2. Strategi Pengurutan: Non-Destructive Copy Sorting
- **Keputusan:** Menyalin seluruh elemen dari struktur data utama ke objek baru `ArrayDinamis<Putusan>` lalu mengurutkan salinan tersebut menggunakan algoritma manual (*Selection Sort* / *Bubble Sort*) tanpa `Arrays.sort()` atau `Collections.sort()`[cite: 16, 17].
- **Alasan:**
    - Menjaga *invariant* urutan penyimpanan asli pada struktur data utama tetap identik sebelum dan sesudah operasi pengurutan[cite: 17].
    - Mematuhi larangan penggunaan pustaka `java.util.*`[cite: 16].