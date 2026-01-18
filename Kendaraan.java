public abstract class Kendaraan {
    private String platNomor;
    private String merk;
    private String pemilik;

    public Kendaraan(String platNomor, String merk, String pemilik) {
        this.platNomor = platNomor;
        this.merk = merk;
        this.pemilik = pemilik;
    }

    public String getPlatNomor() {
        return platNomor;
    }
    public String getPemilik() {
        return pemilik;
    }
    public String getMerk() {
        return merk;
    }

    public abstract String getJenisEnergi();
    public abstract boolean isSukuCadangCocok(SukuCadang sc);
    public abstract String getSaranMekanik();
}