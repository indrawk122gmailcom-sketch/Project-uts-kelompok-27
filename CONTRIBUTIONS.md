# Pembagian Kontribusi Anggota Kelompok 27

Repositori: `https://github.com/indrawk122gmailcom-sketch/Project-uts-kelompok-27`

---

## 1. Indra Wahyudin Kurniawan (`indrawk122gmailcom-sketch`)
- **Ekstraksi Dataset & Provenance Bagian 1 (`caseId` 1–25):**
  - Mengekstrak 25 putusan pertama dari Direktori Putusan Mahkamah Agung ke `data/putusan-50.csv` serta mencatat sumber halaman per atribut di `docs/data-provenance.csv`.
  - **Pull Request:** `#1` (`feat/dataset-part1` -> `main`)
- **Implementasi Struktur Data Manual, CLI, & Pengujian:**
  - Mengimplementasikan `Putusan.java`, `ArrayDinamis.java`, `StackAudit.java`, `CsvParser.java`, `PengelolaPutusan.java`, `Main.java`, serta 7 skenario pengujian di `MainTest.java`.
  - **Pull Request:** `#2` (`feat/app-enhancement-and-tests` -> `main`) — *Merged*
  - **Commit Utama:** `6db4094`
- **Dokumentasi Arsitektur, Trace Manual, & Transparansi AI:**
  - Menyusun `README.md`, `docs/adr/001-struktur-data-manual.md`, `docs/adr/002-strategi-pencarian-dan-pengurutan.md`, `docs/trace/trace-operasi-utama.md`, `CONTRIBUTIONS.md`, dan `AI_USAGE.md`.
  - **Pull Request:** `#4` (`docs/readme-and-adr` -> `main`)
- **Code Review:**
  - Melakukan *review* teknis terhadap PR `#5` (`feat/dataset-part2`) milik Rizky Sultan.

---

## 2. Rizky Sultan (`rizkysultan017-cell`)
- **Ekstraksi Dataset & Provenance Bagian 2 (`caseId` 26–50):**
  - Melengkapi 25 putusan berikutnya (baris ke-27 s.d. ke-51) pada `data/putusan-50.csv` serta mencatat sumber halaman per atribut di `docs/data-provenance.csv`.
  - **Pull Request:** `#5` (`feat/dataset-part2` -> `main`)
- **Code Review & Integrasi Cabang:**
  - Melakukan *review* dan *merge* terhadap PR `#2` (`feat/app-enhancement-and-tests` -> `main`), serta mereview PR `#1` dan PR `#4`.