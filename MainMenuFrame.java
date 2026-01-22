import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Main Menu - Sistem Informasi UKM Multimedia");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (KIRI - NAVIGASI) ---
        JPanel sidebar = new JPanel(new GridLayout(3, 1, 15, 15));
        sidebar.setBackground(new Color(30, 30, 30)); // Warna Gelap (Dark Theme)
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        sidebar.setPreferredSize(new Dimension(240, 0)); // Lebar sidebar tetap

        JButton btnKetua = menuButton("Login Ketua");
        JButton btnPengurus = menuButton("Login Pengurus");
        JButton btnAnggota = menuButton("Login Anggota");

        sidebar.add(btnKetua);
        sidebar.add(btnPengurus);
        sidebar.add(btnAnggota);

        // --- 2. CENTER PANEL (KANAN - KONTEN SAMBUTAN) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE); // Background putih agar logo bersih
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;

        // A. LOGO UKM
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        
        // Cek apakah file benar-benar ada
        java.io.File fileGambar = new java.io.File("logo_mm no txt.jpg");
        
        if (fileGambar.exists()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(fileGambar.getAbsolutePath());
                // Resize gambar ke 200x200
                Image img = iconOriginal.getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
                System.out.println("SUKSES: Gambar ditemukan di " + fileGambar.getAbsolutePath());
            } catch (Exception e) {
                e.printStackTrace();
                lblLogo.setText("Gagal Load Gambar");
            }
        } else {
            // Jika file tidak ketemu, tampilkan teks merah & print lokasi yang dicari
            lblLogo.setText("<html><font color='red'>[Gambar Tidak Ditemukan]</font></html>");
            System.out.println("ERROR: Sistem mencari gambar di: " + fileGambar.getAbsolutePath());
            System.out.println("Pastikan nama file 'logo_mm no txt.jpg' ada di folder tersebut.");
        }
        
        centerPanel.add(lblLogo, gbc);

        // B. JUDUL UTAMA
        gbc.gridy++;
        JLabel lblTitle = new JLabel("<html><center>SELAMAT DATANG DI<br>SISTEM UKM MULTIMEDIA</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 28));
        lblTitle.setForeground(new Color(30, 30, 30));
        centerPanel.add(lblTitle, gbc);

        // C. KATA SAMBUTAN (MOTIVASI)
        gbc.gridy++;
        String kataSambutan = "<html><center><p style='width:450px; color:#555555;'>" +
                "<i>\"Salam Desain!\"</i><br><br>" +
                "Sistem ini dirancang sebagai wadah manajemen administrasi, " +
                "portofolio karya, dan database keanggotaan UKM Multimedia.<br><br>" +
                "Silakan pilih akses login di menu sebelah kiri untuk memulai." +
                "</p></center></html>";
        
        JLabel lblDesc = new JLabel(kataSambutan, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        centerPanel.add(lblDesc, gbc);

        // Masukkan panel ke Frame Utama
        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // --- EVENTS (Aksi Tombol) ---
        btnKetua.addActionListener(e -> openLogin("ketua"));
        btnPengurus.addActionListener(e -> openLogin("pengurus"));
        btnAnggota.addActionListener(e -> openLogin("anggota"));
    }

    // Helper untuk Style Tombol (Agar konsisten Mewah/Gold)
    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(212, 175, 55)); // Warna Emas (Gold)
        b.setForeground(Color.BLACK);             // Teks Hitam
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return b;
    }

    private void openLogin(String role) {
        dispose();
        new LoginFrame(role).setVisible(true);
    }
}