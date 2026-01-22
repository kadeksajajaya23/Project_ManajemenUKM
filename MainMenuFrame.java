import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Main Menu - Sistem Informasi UKM Multimedia");
        // Ukuran default
        setSize(950, 600); 
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (KIRI - NAVIGASI) ---
        // UPDATE: Gunakan GridBagLayout agar tombol tidak melar saat dimaximize
        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(new Color(30, 30, 30)); 
        sidebar.setPreferredSize(new Dimension(260, 0)); // Lebar sidebar sedikit diperbesar

        // Konfigurasi posisi tombol di sidebar
        GridBagConstraints gbcSidebar = new GridBagConstraints();
        gbcSidebar.gridx = 0;
        gbcSidebar.gridy = GridBagConstraints.RELATIVE; // Menumpuk ke bawah otomatis
        gbcSidebar.fill = GridBagConstraints.HORIZONTAL; // Isi lebar sidebar
        gbcSidebar.insets = new Insets(15, 20, 15, 20); // Jarak antar tombol (Top, Left, Bottom, Right)
        gbcSidebar.weightx = 1.0; // Agar tombol memenuhi lebar panel
        gbcSidebar.anchor = GridBagConstraints.CENTER; // Posisi tombol di tengah vertikal

        // Membuat Tombol
        JButton btnKetua = menuButton("Login Ketua");
        JButton btnPengurus = menuButton("Login Pengurus");
        JButton btnAnggota = menuButton("Login Anggota");

        // Menambahkan tombol ke sidebar dengan constraints
        sidebar.add(btnKetua, gbcSidebar);
        sidebar.add(btnPengurus, gbcSidebar);
        sidebar.add(btnAnggota, gbcSidebar);


        // --- 2. CENTER PANEL (KANAN - KONTEN SAMBUTAN) ---
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbcCenter = new GridBagConstraints();
        gbcCenter.insets = new Insets(10, 10, 10, 10);
        gbcCenter.gridx = 0;
        gbcCenter.gridy = 0;
        gbcCenter.anchor = GridBagConstraints.CENTER;

        // A. LOGO UKM
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        
        // Load Gambar
        java.io.File fileGambar = new java.io.File("logo_mm no txt.jpg");
        if (fileGambar.exists()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(fileGambar.getAbsolutePath());
                // Resize gambar (Tetap 200x200 agar konsisten di layar besar/kecil)
                Image img = iconOriginal.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblLogo.setText("Error Loading Image");
            }
        } else {
            lblLogo.setText("<html><font color='red'>[Logo Tidak Ditemukan]</font></html>");
        }
        centerPanel.add(lblLogo, gbcCenter);

        // B. JUDUL UTAMA
        gbcCenter.gridy++;
        JLabel lblTitle = new JLabel("<html><center>SELAMAT DATANG DI<br>SISTEM UKM MULTIMEDIA</center></html>", SwingConstants.CENTER);
        // Menggunakan font yang sedikit lebih besar
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32)); 
        lblTitle.setForeground(new Color(30, 30, 30));
        centerPanel.add(lblTitle, gbcCenter);

        // C. KATA SAMBUTAN (MOTIVASI)
        gbcCenter.gridy++;
        String kataSambutan = "<html><center><p style='width:500px; color:#555555; font-size:11px;'>" +
                "<i>\"Creativity, Innovation, Technology\"</i><br><br>" +
                "Sistem ini dirancang sebagai wadah manajemen administrasi, " +
                "portofolio karya, dan database keanggotaan UKM Multimedia.<br><br>" +
                "Silakan pilih akses login di menu sebelah kiri untuk memulai." +
                "</p></center></html>";
        
        JLabel lblDesc = new JLabel(kataSambutan, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(lblDesc, gbcCenter);

        // Masukkan panel ke Frame Utama
        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // --- EVENTS (Aksi Tombol) ---
        btnKetua.addActionListener(e -> openLogin("ketua"));
        btnPengurus.addActionListener(e -> openLogin("pengurus"));
        btnAnggota.addActionListener(e -> openLogin("anggota"));
    }

    // Helper untuk Style Tombol
    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(212, 175, 55)); // Warna Emas
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        // PENTING: Set ukuran preferensi tinggi tombol agar terlihat "tebal" tapi tidak melar
        b.setPreferredSize(new Dimension(0, 60)); // Lebar 0 (ikut fill), Tinggi 60px
        
        return b;
    }

    private void openLogin(String role) {
        dispose();
        new LoginFrame(role).setVisible(true);
    }
}