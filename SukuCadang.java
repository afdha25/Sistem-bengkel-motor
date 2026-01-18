public class SukuCadang {
    private String id;
    private String nama;
    private double harga;
    private int stok;

    public SukuCadang(String id, String nama, double harga, int stok) {
        this.id = id;
        this.nama = nama;
        this.harga = harga;
        this.stok = stok;
    }

    public String getNama() {
        return nama;
    }
    public double getHarga() {
        return harga;
    }
    public int getStok() {
        return stok;
    }

    public void kurangiStok(int jumlah) {
        if (stok >= jumlah) {
            this.stok = this.stok - jumlah;
        }
    }
    @Override
    public String toString() {
        return "Kode: " + id + " | Nama: " + nama + " | Biaya: Rp" + harga + " | Tersedia: " + stok + " unit";
    }
}