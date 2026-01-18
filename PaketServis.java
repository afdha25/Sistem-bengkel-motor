public class PaketServis implements LayananBengkel {
    private String namaPaket;
    private double biayaPaket;
    private String deskripsi;

    public PaketServis(String namaPaket, double biayaPaket, String deskripsi) {
        this.namaPaket = namaPaket;
        this.biayaPaket = biayaPaket;
        this.deskripsi = deskripsi;
    }

    @Override
    public double hitungBiayaJasa() {
        return biayaPaket;
    }

    @Override
    public String getDeskripsiLayanan() {
        return "Paket " + namaPaket + " (" + deskripsi + ")";
    }
}