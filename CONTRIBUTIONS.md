# Pembagian Kontribusi Anggota Kelompok 27

Proyek: **Sistem Manajemen Putusan Narkotika (Java CLI & Struktur Data Manual)**

---

## 1. Indra (`indrawk122gmailcom-sketch`)
- **Kurasi Dataset & Provenance (`feat/dataset-part1` — PR #1):**
  - Menyusun dan memvalidasi 25 data awal putusan narkotika (`P001`–`P025`) ke dalam `data/putusan-50.csv` sesuai skema 17 kolom.
  - Mendokumentasikan sumber halaman putusan dan asal dokumen pada `docs/data-provenance.csv`.
- **Implementasi Struktur Data Manual, Aplikasi CLI, & Pengujian (`feat/app-enhancement-and-tests` — PR #2):**
  - Mengimplementasikan model entitas `Putusan.java` lengkap dengan mekanisme *deep copy*.
  - Membangun struktur data manual `ArrayDinamis.java` (ekspansi otomatis `2x`) dan `StackAudit.java` (*linked list stack* untuk fitur *Undo*).
  - Mengimplementasikan `CsvParser.java` untuk menangani parsing 17 kolom CSV dengan tanda kutip dan koma internal.
  - Menyusun logika validasi, pencarian, filter, dan antarmuka menu CLI pada `PengelolaPutusan.java` dan `Main.java`.
  - Membangun suite pengujian otomatis 7 skenario wajib pada `MainTest.java` (`7/7 PASS`).
- **Dokumentasi Arsitektur & Penelusuran (`docs/readme-and-adr` — PR #3):**
  - Menyusun dokumentasi utama pada `README.md`, catatan keputusan teknis pada `docs/adr/001-struktur-data-manual.md`, penelusuran struktur data pada `docs/trace/trace-operasi-utama.md`, serta laporan kontribusi dan penggunaan AI.

## 2. Rizky Sultan (`rizkysultan017-cell`)
- **Kurasi Dataset Lanjutan & Provenance:**
  - Melengkapi 25 data putusan berikutnya (`P026`–`P050`) pada `data/putusan-50.csv` beserta pencatatan `docs/data-provenance.csv`.
- **Code Review & Verifikasi Integrasi:**
  - Melakukan *review* silang terhadap Pull Request dataset, struktur data manual, pengujian, dan dokumentasi sebelum penggabungan ke branch `main`.