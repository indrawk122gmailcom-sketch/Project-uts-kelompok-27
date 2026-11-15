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