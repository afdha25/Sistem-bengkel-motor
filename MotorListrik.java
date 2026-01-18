public class MotorListrik extends Kendaraan {

    public MotorListrik(String platNomor, String merk, String pemilik, int healthBaterai) {
        super(platNomor, merk, pemilik);
    }

    @Override
    public String getJenisEnergi() {
        return "Electric ";
    }

    @Override
    public boolean isSukuCadangCocok(SukuCadang sc) {
        String namaBarang = sc.getNama().toLowerCase();
        return !(namaBarang.contains("oli") || namaBarang.contains("busi") ||
                namaBarang.contains("belt") || namaBarang.contains("filter") ||
                namaBarang.contains("gear"));
    }

    @Override
    public String getSaranMekanik() {
        return "Cek Baterai, Update Software, dan Sistem Rem.\n";
    }
}