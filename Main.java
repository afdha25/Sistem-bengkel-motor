import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;

public class Main {
    private static final String FILE_NOMOR_URUT = "noakhir.txt";
    private static int nomorUrut = muatNomorTerakhir ();

    private String idTransaksi;
    private Kendaraan kendaraan;
    private LayananBengkel layanan;
    private List<SukuCadang> listSukuCadang;
    private String waktu;
    private double totalBiaya;

    private static int muatNomorTerakhir() {
        try (Scanner fileScanner = new Scanner(new File(FILE_NOMOR_URUT))) {
            if (fileScanner.hasNextInt()) {
                return fileScanner.nextInt();
            }
        } catch (IOException e) {
        }return 100;
    }

    private static void simpanNomorTerakhir(int nomor) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_NOMOR_URUT))) {
            pw.print(nomor-1);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan progres nomor urut.");
        }
    }

    public Main(Kendaraan kendaraan, LayananBengkel layanan) {
        this.idTransaksi = "Invoice-" + nomorUrut;
        nomorUrut++;
        simpanNomorTerakhir(nomorUrut);

        this.kendaraan = kendaraan;
        this.layanan = layanan;
        this.listSukuCadang = new ArrayList<>();
        this.waktu = LocalDate.now().toString();
        this.totalBiaya = layanan.hitungBiayaJasa();
    }

    public void tambahSukuCadang(SukuCadang sc) {
        if (sc.getStok() > 0) {
            listSukuCadang.add(sc);
            sc.kurangiStok(1);
            this.totalBiaya += sc.getHarga();
        } else {
            System.out.println("Gagal: Stok " + sc.getNama() + " habis!");
        }
    }

    public double getTotalBiaya() {
        return totalBiaya;
    }

    public void prosesDanSimpanInvoice(double uangDibayar, double kembalian) {
        String namaFile = idTransaksi + ".txt";

        try (PrintWriter writer = new PrintWriter(new FileWriter(namaFile))) {
            writer.write("\n======================================\n");
            writer.write("                 INVOICE                \n");
            writer.write("========================================\n");
            writer.write("ID Transaksi : " + idTransaksi + "\n");
            writer.write("Waktu        : " + waktu + "\n");
            writer.write("Pemilik      : " + kendaraan.getPemilik() + "\n");
            writer.write("Kendaraan    : " + kendaraan.getMerk() + " (" + kendaraan.getPlatNomor() + ")\n");
            writer.write("Tipe Mesin   : " + kendaraan.getJenisEnergi() + "\n");
            writer.write("----------------------------------------\n");
            writer.write("Jasa: " + layanan.getDeskripsiLayanan() + " -> Rp" + layanan.hitungBiayaJasa() + "\n");

            if (!listSukuCadang.isEmpty()) {
                writer.write("Suku Cadang:\n");
                for (SukuCadang sc : listSukuCadang) {
                    writer.write("- " + sc.getNama() + " -> Rp" + sc.getHarga() + "\n");
                }
            }

            writer.write("----------------------------------------\n");
            writer.write("TOTAL BIAYA  : Rp" + totalBiaya + "\n");
            writer.write("TUNAI        : Rp" + uangDibayar + "\n");
            writer.write("KEMBALIAN    : Rp" + kembalian + "\n");
            writer.write("========================================\n");
            writer.write("Saran Mekanik: " + kendaraan.getSaranMekanik());
            System.out.println("\nInvoice berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("Terjadi kesalahan penyimpanan: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<SukuCadang> masterPart = new ArrayList<>();

        masterPart.add(new SukuCadang("B01", "Oli MPX 2", 55000, 10));
        masterPart.add(new SukuCadang("B02", "Oli Shell Advance", 65000, 15));
        masterPart.add(new SukuCadang("B03", "Busi Iridium", 45000, 5));
        masterPart.add(new SukuCadang("B04", "Van Belt CVT", 125000, 5));
        masterPart.add(new SukuCadang("B05", "Filter Udara", 45000, 12));
        masterPart.add(new SukuCadang("B06", "Gear Set Racing", 350000, 4));
        masterPart.add(new SukuCadang("L01", "Baterai Lithium 60V", 2500000, 3));
        masterPart.add(new SukuCadang("L02", "Controller Module", 850000, 4));
        masterPart.add(new SukuCadang("L03", "Kabel Power DC", 95000, 8));
        masterPart.add(new SukuCadang("L04", "Motor Hub 1200W", 3500000, 2));
        masterPart.add(new SukuCadang("L05", "Throttle Handle", 150000, 6));
        masterPart.add(new SukuCadang("L06", "Converter DC-DC 12V", 180000, 5));
        masterPart.add(new SukuCadang("L07", "Soket Charger", 120000, 4));
        masterPart.add(new SukuCadang("L08", "Hall Sensor Dinamo", 85000, 10));
        masterPart.add(new SukuCadang("U01", "Kampas Rem", 75000, 8));
        masterPart.add(new SukuCadang("U02", "Ban Luar Ring 14", 185000, 6));
        masterPart.add(new SukuCadang("U03", "Aki GS Astra 12V", 215000, 7));
        masterPart.add(new SukuCadang("U04", "Lampu LED Utama", 85000, 10));
        masterPart.add(new SukuCadang("U05", "Shockbreaker Belakang", 420000, 2));

        boolean programBerjalan = true;

        while (programBerjalan) {
            System.out.println("\n=== SISTEM BENGKEL MOTOR ===");
            System.out.print("Masukkan Nama Pemilik: ");
            String pemilik = scanner.nextLine();
            System.out.print("Masukkan Plat Nomor: ");
            String plat = scanner.nextLine();
            System.out.print("Masukkan Merk/Model: ");
            String merk = scanner.nextLine();

            System.out.println("Pilih Jenis Kendaraan (1. Bensin / 2. Listrik): ");
            int pilihanMotor = scanner.nextInt();
            scanner.nextLine();

            Kendaraan knd = (pilihanMotor == 1) ?
                    new MotorBensin(plat, merk, pemilik, 1) :
                    new MotorListrik(plat, merk, pemilik, 100);

            System.out.println("\nPilih Layanan:");
            System.out.println("1. Servis Rutin (Rp75.000)");
            System.out.println("2. Servis Besar (Rp200.000)");
            System.out.println("3. Paket Ekonomis (Rp120.000 - Cek Menyeluruh + Cuci)");
            System.out.println("4. Paket Premium (Rp350.000 - Servis Lengkap + Polish Body)");
            System.out.print("Pilihan: ");
            int pilihanLayanan = scanner.nextInt();
            scanner.nextLine();

            LayananBengkel lyn;
            switch (pilihanLayanan) {
                case 2: lyn = new ServisBesar();
                        break;
                case 3: lyn = new PaketServis("Ekonomis", 120000, "Cek Menyeluruh + Cuci");
                        break;
                case 4: lyn = new PaketServis("Premium", 350000, "Servis Lengkap + Polish Body");
                        break;
                default: lyn = new ServisRutin();
                        break;
            }

            Main transaksi = new Main(knd, lyn);

            List<SukuCadang> menuTersedia = new ArrayList<>();
            for (SukuCadang sc : masterPart) {
                if (knd.isSukuCadangCocok(sc)) {
                    menuTersedia.add(sc);
                }
            }

            boolean tambahLagi = true;
            while (tambahLagi) {
                System.out.println("\n--- Pilih Suku Cadang (" + knd.getJenisEnergi() + ") ---");
                for (int i = 0; i < menuTersedia.size(); i++) {
                    System.out.println((i + 1) + ". " + menuTersedia.get(i).getNama() + " (Rp" + menuTersedia.get(i).getHarga() + ")");
                }
                System.out.println("0. Selesai");
                System.out.print("Pilihan: ");
                int pilPart = scanner.nextInt();
                scanner.nextLine();

                if (pilPart > 0 && pilPart <= menuTersedia.size()) {
                    transaksi.tambahSukuCadang(menuTersedia.get(pilPart - 1));
                    System.out.println("Suku cadang berhasil ditambahkan.");
                } else {
                    tambahLagi = false;
                }
            }

            double total = transaksi.getTotalBiaya();
            System.out.println("\n----------------------------------------");
            System.out.println("TOTAL YANG HARUS DIBAYAR: Rp" + total);
            System.out.println("----------------------------------------");

            double bayar = 0;
            boolean lunas = false;

            while (!lunas) {
                System.out.print("Masukkan jumlah uang pembayaran: Rp");
                bayar = scanner.nextDouble();
                scanner.nextLine();

                if (bayar < total) {
                    System.out.println("UANG TIDAK CUKUP! (Kurang: Rp" + (total - bayar) + ")");
                } else {
                    double kembalian = bayar - total;
                    System.out.println("Pembayaran Berhasil.");
                    if (kembalian > 0) System.out.println("Uang Kembalian: Rp" + kembalian);
                    transaksi.prosesDanSimpanInvoice(bayar, kembalian);
                    lunas = true;
                }
            }

            System.out.print("\nApakah ingin memproses kendaraan lain? (y/n): ");
            String lanjut = scanner.nextLine().toLowerCase();
            if (!lanjut.equals("y")) programBerjalan = false;
        }

        System.out.println("Terima kasih telah melakukan servis di Bengkel kami.");
        scanner.close();
    }
}