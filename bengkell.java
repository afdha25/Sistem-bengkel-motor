import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class bengkell extends JFrame {

    private JPanel mainPanel;
    private JTextField txtPemilik;
    private JTextField txtPlat;
    private JTextField txtMerk;
    private JRadioButton rbBensin;
    private JRadioButton rbListrik;
    private JComboBox<String> cbLayanan;
    private JTable tableSukuCadang;
    private JTextArea areaInvoice;
    private JTextField txtBayar;
    private JButton btnProses;
    private JLabel lblTotal;

    private DefaultTableModel tableModel;
    private List<SukuCadang> masterParts;
    private List<SukuCadang> Parts;
    private List<SukuCadang> pilihParts;
    private double totalBiaya = 0;

    public bengkell() {
        setTitle("Sistem Manajemen Bengkel Motor");
        setContentPane(mainPanel);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 700);
        setLocationRelativeTo(null);

        initData();
        setupTable();
        setupComboBox();
        Proses();
        updatePartsTable();
    }

    private void initData() {
        pilihParts = new ArrayList<>();
        masterParts = new ArrayList<>();

        masterParts.add(new SukuCadang("B01", "Oli MPX 2", 55000, 10));
        masterParts.add(new SukuCadang("B02", "Oli Shell Advance", 65000, 15));
        masterParts.add(new SukuCadang("B03", "Busi Iridium", 45000, 5));
        masterParts.add(new SukuCadang("B04", "Van Belt CVT", 125000, 5));
        masterParts.add(new SukuCadang("B05", "Filter Udara", 45000, 12));
        masterParts.add(new SukuCadang("B06", "Gear Set Racing", 350000, 4));

        masterParts.add(new SukuCadang("L01", "Baterai Lithium 60V", 2500000, 3));
        masterParts.add(new SukuCadang("L02", "Controller Module", 850000, 4));
        masterParts.add(new SukuCadang("L03", "Kabel Power DC", 95000, 8));
        masterParts.add(new SukuCadang("L04", "Motor Hub 1200W", 3500000, 2));
        masterParts.add(new SukuCadang("L05", "Throttle Handle", 150000, 6));
        masterParts.add(new SukuCadang("L06", "Converter DC-DC 12V", 180000, 5));
        masterParts.add(new SukuCadang("L07", "Soket Charger", 120000, 4));
        masterParts.add(new SukuCadang("L08", "Hall Sensor Dinamo", 85000, 10));

        masterParts.add(new SukuCadang("U01", "Kampas Rem", 75000, 8));
        masterParts.add(new SukuCadang("U02", "Ban Luar Ring 14", 185000, 6));
        masterParts.add(new SukuCadang("U03", "Aki GS Astra 12V", 215000, 7));
        masterParts.add(new SukuCadang("U04", "Lampu LED Utama", 85000, 10));
        masterParts.add(new SukuCadang("U05", "Shockbreaker Belakang", 420000, 2));
    }

    private void setupTable() {
        String[] columns = {"ID", "Nama", "Harga", "Stok", "Pilih"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return column == 4;
            }
            @Override
            public Class<?> getColumnClass(int columnIndex) {
                return columnIndex == 4 ? Boolean.class : super.getColumnClass(columnIndex);
            }
        };
        tableSukuCadang.setModel(tableModel);
    }

    private void setupComboBox() {

        cbLayanan.removeAllItems();
        cbLayanan.addItem("Servis Rutin (Rp75.000)");
        cbLayanan.addItem("Servis Besar (Rp200.000)");
        cbLayanan.addItem("Paket Ekonomis (Rp120.000)");
        cbLayanan.addItem("Paket Premium (Rp350.000)");
    }

    private void Proses() {
        rbBensin.addActionListener(e -> updatePartsTable());
        rbListrik.addActionListener(e -> updatePartsTable());
        btnProses.addActionListener(e -> hitungDanTampilkanInvoice());
    }

    private void updatePartsTable() {
        tableModel.setRowCount(0);
        pilihParts.clear();

        boolean isBensin = rbBensin.isSelected();
        Kendaraan tempKnd = isBensin ?
                new MotorBensin("", "", "", 1) :
                new MotorListrik("", "", "", 100);

        Parts = new ArrayList<>();
        for (SukuCadang sc : masterParts) {
            if (tempKnd.isSukuCadangCocok(sc)) {
                Parts.add(sc);
                tableModel.addRow(new Object[]{
                        sc.getId(), sc.getNama(), sc.getHarga(), sc.getStok(), false
                });
            }
        }
    }

    private void hitungDanTampilkanInvoice() {
        try {
            String pemilik = txtPemilik.getText();
            String plat = txtPlat.getText();
            String merk = txtMerk.getText();

            if (pemilik.isEmpty() || plat.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Mohon isi Nama Pemilik dan Plat Nomor!");
                return;
            }
            Kendaraan kendaraanInput = rbBensin.isSelected() ?
                    new MotorBensin(plat, merk, pemilik, 1) :
                    new MotorListrik(plat, merk, pemilik, 100);
            LayananBengkel layananTerpilih;
            int indexLayanan = cbLayanan.getSelectedIndex();
            switch (indexLayanan) {
                case 1: layananTerpilih = new ServisBesar(); break;
                case 2: layananTerpilih = new PaketServis("Ekonomis", 120000, "Cek Menyeluruh + Cuci"); break;
                case 3: layananTerpilih = new PaketServis("Premium", 350000, "Servis Lengkap + Polish Body"); break;
                default: layananTerpilih = new ServisRutin(); break;
            }
            totalBiaya = layananTerpilih.hitungBiayaJasa();
            StringBuilder sbParts = new StringBuilder();
            pilihParts.clear();

            for (int i = 0; i < tableModel.getRowCount(); i++) {
                Boolean isSelected = (Boolean) tableModel.getValueAt(i, 4);
                if (isSelected != null && isSelected) {
                    SukuCadang sc = Parts.get(i);
                    pilihParts.add(sc);
                    totalBiaya += sc.getHarga();
                    sbParts.append("- ").append(sc.getNama()).append(" (Rp").append(sc.getHarga()).append(")\n");
                }
            }

            lblTotal.setText("TOTAL: Rp " + String.format("%,.0f", totalBiaya));

            String tunaiStr = txtBayar.getText().replace(".", "").replace(",", "");
            if (tunaiStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Masukkan jumlah pembayaran!");
                return;
            }

            double bayar = Double.parseDouble(tunaiStr);
            if (bayar < totalBiaya) {
                JOptionPane.showMessageDialog(this, "Uang pembayaran kurang Rp" + (totalBiaya - bayar));
                return;
            }

            double kembalian = bayar - totalBiaya;
            StringBuilder inv = new StringBuilder();
            inv.append("====================================\n");
            inv.append("         INVOICE BENGKEL            \n");
            inv.append("====================================\n");
            inv.append("Tgl       : ").append(LocalDate.now()).append("\n");
            inv.append("Pemilik   : ").append(pemilik).append("\n");
            inv.append("Motor     : ").append(merk).append(" (").append(plat).append(")\n");
            inv.append("Tipe      : ").append(kendaraanInput.getJenisEnergi()).append("\n");
            inv.append("------------------------------------\n");
            inv.append("Jasa: ").append(layananTerpilih.getDeskripsiLayanan()).append("\n");
            inv.append("Biaya Jasa: Rp").append(layananTerpilih.hitungBiayaJasa()).append("\n");

            if (!pilihParts.isEmpty()) {
                inv.append("\nSuku Cadang:\n").append(sbParts);
            }

            inv.append("------------------------------------\n");
            inv.append("TOTAL     : Rp").append(totalBiaya).append("\n");
            inv.append("TUNAI     : Rp").append(bayar).append("\n");
            inv.append("KEMBALIAN : Rp").append(kembalian).append("\n");
            inv.append("====================================\n");
            inv.append("Saran Mekanik:\n").append(kendaraanInput.getSaranMekanik());

            areaInvoice.setText(inv.toString());
            JOptionPane.showMessageDialog(this, "Transaksi Berhasil!");

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Terjadi kesalahan: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new bengkell().setVisible(true);
        });
    }

    static abstract class Kendaraan {
        protected String platNomor, merk, pemilik;
        public Kendaraan(String plat, String merk, String pem) {
            this.platNomor = plat; this.merk = merk; this.pemilik = pem;
        }
        public abstract String getJenisEnergi();
        public abstract boolean isSukuCadangCocok(SukuCadang sc);
        public abstract String getSaranMekanik();
    }

    static class MotorBensin extends Kendaraan {
        public MotorBensin(String p, String m, String o, int k) {
            super(p, m, o);
        }
        public String getJenisEnergi() {
            return "Bensin";
        }
        public boolean isSukuCadangCocok(SukuCadang sc) {
            String n = sc.getNama().toLowerCase();
            return !(n.contains("baterai") || n.contains("controller") || n.contains("hub"));
        }
        public String getSaranMekanik() {
            return "Ganti oli rutin & bersihkan injeksi.";
        }
    }

    static class MotorListrik extends Kendaraan {
        public MotorListrik(String p, String m, String o, int h) {
            super(p, m, o);
        }
        public String getJenisEnergi() {
            return "Elektrik";
        }
        public boolean isSukuCadangCocok(SukuCadang sc) {
            String n = sc.getNama().toLowerCase();
            return !(n.contains("oli") || n.contains("busi") || n.contains("belt"));
        }
        public String getSaranMekanik() {
            return "Cek kesehatan baterai & modul.";
        }
    }

    interface LayananBengkel {
        double hitungBiayaJasa();
        String getDeskripsiLayanan();
    }

    static class ServisRutin implements LayananBengkel {
        public double hitungBiayaJasa() {
            return 75000;
         }
        public String getDeskripsiLayanan() {
            return "Servis Ringan";
        }
    }

    static class ServisBesar implements LayananBengkel {
        public double hitungBiayaJasa() {
            return 200000;
        }
        public String getDeskripsiLayanan() {
            return "Servis Besar / Bongkar";
        }
    }

    static class PaketServis implements LayananBengkel {
        private String n, d; private double b;
        public PaketServis(String n, double b, String d) {
            this.n = n; this.b = b; this.d = d;
        }
        public double hitungBiayaJasa() {
            return b;
        }
        public String getDeskripsiLayanan() {
            return "Paket " + n + " (" + d + ")";
        }
    }

    static class SukuCadang {
        private String id, nama; private double harga; private int stok;
        public SukuCadang(String id, String n, double h, int s) {
            this.id = id; this.nama = n; this.harga = h; this.stok = s;
        }
        public String getId() {
            return id;
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
    }
}