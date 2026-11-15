# Sistem Manajemen Putusan Narkotika — Kelompok 27

Aplikasi berbasis **Java CLI (Command-Line Interface)** untuk mengelola, mencari, memfilter, mengagregasi, mengurutkan, mengekspor, dan mengaudit 50 data putusan pengadilan narkotika menggunakan **struktur data manual** yang dibangun dari nol tanpa koleksi bawaan `java.util`[cite: 15, 16, 17].

---

## Identitas Kelompok 27

| Peran | Nama Lengkap | NIM | Akun GitHub |
| :--- | :--- | :--- | :--- |
| Anggota 1 | Indra Wahyudin Kurniawan | *(Isi NIM Anda)* | `indrawk122gmailcom-sketch`[cite: 15] |
| Anggota 2 | Rizky Sultan | *(Isi NIM Sultan)* | `rizkysultan017-cell`[cite: 15] |

---

## Struktur Data yang Dipilih

1. **Struktur Utama — `ArrayDinamis<T>` (Generic Dynamic Array):**
    - Menyimpan koleksi objek `Putusan` dengan kapasitas awal `10`, ekspansi otomatis `2x` (`kapasitas * 2`), dan pemeriksaan `validateInvariant()`[cite: 17, 18].
    - **Invariant Utama:** `0 <= size <= kapasitas`, seluruh elemen aktif tersusun rapat pada indeks `0` hingga `size - 1` tanpa celah `null`.
2. **Struktur Sekunder — `StackAudit<T>` (Generic Singly Linked List Stack):**
    - Menyimpan riwayat operasi mutasi (`TAMBAH`, `UBAH_VONIS`, `HAPUS`) menggunakan simpul berantai (*node*) berprinsip **LIFO** untuk audit dan pembatalan perubahan terakhir (*Undo*).

---

## Struktur Direktori

```text
Project-uts-kelompok-27/
├── data/
│   └── putusan-50.csv                             # Dataset 50 putusan narkotika (P001-P050)[cite: 15]
├── docs/
│   ├── adr/
│   │   ├── 001-struktur-data-manual.md            # ADR 1: ArrayDinamis<T>, StackAudit<T>, CsvParser
│   │   └── 002-strategi-pencarian-dan-pengurutan.md # ADR 2: Pencarian & pengurutan non-destruktif[cite: 17]
│   ├── demo/                                      # Bukti tangkapan layar CLI & hasil ekspor laporan[cite: 17]
│   ├── trace/
│   │   └── trace-operasi-utama.md                 # Penelusuran manual operasi struktur data
│   └── data-provenance.csv                        # Catatan sumber & halaman per atribut (8 kolom)[cite: 15]
├── src/
│   ├── main/java/                                 # Kode sumber utama aplikasi
│   └── test/java/                                 # Suite pengujian otomatis (MainTest.java)[cite: 17]
├── AI_USAGE.md                                    # Log audit penggunaan AI (Bagian 6)
├── CONTRIBUTIONS.md                               # Rincian & tautan kontribusi anggota
└── README.md                                      # Dokumentasi utama proyek