public class MotorBensin extends Kendaraan {
    public MotorBensin(String platNomor, String merk, String pemilik, int kapasitasOli) {
        super(platNomor, merk, pemilik);
    }
    @Override
    public String getJenisEnergi() {
        return "(Bensin)";
    }
    @Override
    public boolean isSukuCadangCocok(SukuCadang sc) {
        String namaBarang = sc.getNama().toLowerCase();
        return !(namaBarang.contains("baterai") || namaBarang.contains("controller") ||
                namaBarang.contains("hub") || namaBarang.contains("throttle") ||
                namaBarang.contains("converter") || namaBarang.contains("charger") ||
                namaBarang.contains("sensor"));
    }

    @Override
    public String getSaranMekanik() {
        return "Penggantian Oli, Pembersihan Injeksi, dan Cek Busi.\n";
    }
}