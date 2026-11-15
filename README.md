# Sistem Manajemen Putusan Narkotika (Kelompok 27)

Aplikasi berbasis **Java CLI (Command-Line Interface)** untuk mengelola, mencari, memfilter, dan mengaudit data putusan tindak pidana narkotika menggunakan **struktur data manual** tanpa koleksi bawaan `java.util` (`ArrayList`, `LinkedList`, `Stack`, `HashMap`, dll.).

---

## Struktur Direktori

```text
proyek-uts-struktur-data/
├── data/
│   └── putusan-50.csv              # Dataset 50 putusan narkotika (skema 17 kolom)
├── docs/
│   ├── adr/                        # Architecture Decision Records (keputusan desain)
│   ├── demo/                       # Bukti tangkapan layar / log demo aplikasi
│   ├── trace/                      # Penelusuran manual alur struktur data
│   └── data-provenance.csv         # Catatan sumber dan halaman dokumen putusan
├── src/
│   ├── main/java/
│   │   ├── Putusan.java            # Model entitas 17 atribut & metode deep copy
│   │   ├── ArrayDinamis.java       # Dynamic Array manual dengan ekspansi kapasitas 2x
│   │   ├── StackAudit.java         # Stack berbasis Linked List untuk riwayat Undo
│   │   ├── CsvParser.java          # Parser CSV aman terhadap tanda koma dalam tanda kutip
│   │   ├── PengelolaPutusan.java   # Logika bisnis, validasi input, pencarian, & filter
│   │   └── Main.java               # Antarmuka menu interaktif CLI
│   └── test/java/
│       └── MainTest.java           # Suite pengujian otomatis (7 skenario wajib)
├── AI_USAGE.md                     # Dokumentasi transparansi penggunaan AI
├── CONTRIBUTIONS.md                # Rincian pembagian kontribusi anggota kelompok
└── README.md                       # Dokumentasi utama proyek