public class CsvParser {

    public static String[] parseBaris(String baris) {
        if (baris == null) {
            return new String[0];
        }

        String[] sementara = new String[32];
        int jumlahKolom = 0;

        StringBuilder kolomSaatIni = new StringBuilder();
        boolean dalamKutip = false;

        for (int i = 0; i < baris.length(); i++) {
            char c = baris.charAt(i);

            if (c == '"') {
                if (dalamKutip && i + 1 < baris.length() && baris.charAt(i + 1) == '"') {
                    kolomSaatIni.append('"');
                    i++;
                } else {
                    dalamKutip = !dalamKutip;
                }
            } else if (c == ',' && !dalamKutip) {
                sementara = simpanKolom(sementara, jumlahKolom++, kolomSaatIni.toString());
                kolomSaatIni.setLength(0);
            } else {
                kolomSaatIni.append(c);
            }
        }
        sementara = simpanKolom(sementara, jumlahKolom++, kolomSaatIni.toString());

        String[] hasil = new String[jumlahKolom];
        for (int i = 0; i < jumlahKolom; i++) {
            hasil[i] = sementara[i];
        }
        return hasil;
    }

    public static String escapeField(String nilai) {
        if (nilai == null) {
            return "";
        }
        boolean perluKutip = false;
        for (int i = 0; i < nilai.length(); i++) {
            char c = nilai.charAt(i);
            if (c == ',' || c == '"' || c == '\n' || c == '\r') {
                perluKutip = true;
                break;
            }
        }
        if (!perluKutip) {
            return nilai;
        }
        StringBuilder sb = new StringBuilder();
        sb.append('"');
        for (int i = 0; i < nilai.length(); i++) {
            char c = nilai.charAt(i);
            if (c == '"') {
                sb.append("\"\"");
            } else {
                sb.append(c);
            }
        }
        sb.append('"');
        return sb.toString();
    }

    public static boolean isTanggalValid(String tanggal) {
        if (tanggal == null) return false;
        String t = tanggal.trim();
        if (t.length() != 10 || t.charAt(4) != '-' || t.charAt(7) != '-') {
            return false;
        }
        for (int i = 0; i < 10; i++) {
            if (i == 4 || i == 7) continue;
            if (t.charAt(i) < '0' || t.charAt(i) > '9') return false;
        }
        int tahun = Integer.parseInt(t.substring(0, 4));
        int bulan = Integer.parseInt(t.substring(5, 7));
        int hari = Integer.parseInt(t.substring(8, 10));
        return tahun >= 1900 && tahun <= 2100 && bulan >= 1 && bulan <= 12 && hari >= 1 && hari <= 31;
    }

    private static String[] simpanKolom(String[] arr, int indeks, String nilai) {
        if (indeks >= arr.length) {
            String[] baru = new String[arr.length * 2];
            for (int i = 0; i < arr.length; i++) {
                baru[i] = arr[i];
            }
            arr = baru;
        }
        arr[indeks] = nilai.trim();
        return arr;
    }
}