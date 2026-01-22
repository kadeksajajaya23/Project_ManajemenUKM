import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Main Menu - Sistem Informasi UKM Multimedia");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // --- 1. SIDEBAR (KIRI) ---
        // Menggunakan GridLayout(3, 1) adalah KUNCI agar tombol memenuhi tinggi layar
        // Baik saat mini maupun maximize, tombol akan selalu terbagi 3 rata memenuhi panel.
        JPanel sidebar = new JPanel(new GridLayout(3, 1, 15, 15));
        sidebar.setBackground(new Color(30, 30, 30)); 
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));
        sidebar.setPreferredSize(new Dimension(260, 0)); 

        JButton btnKetua = menuButton("Login Ketua");
        JButton btnPengurus = menuButton("Login Pengurus");
        JButton btnAnggota = menuButton("Login Anggota");

        sidebar.add(btnKetua);
        sidebar.add(btnPengurus);
        sidebar.add(btnAnggota);

        // --- 2. CENTER PANEL (KANAN) ---
        // Menggunakan GridBagLayout agar konten (Logo & Teks) selalu di TENGAH (Center)
        // Meskipun layar dimaximize, konten tidak akan berantakan/melar.
        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Color.WHITE);
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.CENTER;

        // A. LOGO UKM
        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        java.io.File fileGambar = new java.io.File("logo_mm no txt.jpg");
        
        if (fileGambar.exists()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(fileGambar.getAbsolutePath());
                // Resize gambar ke 220x220
                Image img = iconOriginal.getImage().getScaledInstance(220, 220, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception e) {
                lblLogo.setText("Error Loading Image");
            }
        } else {
            lblLogo.setText("<html><font color='red'>[Logo Tidak Ditemukan]</font></html>");
        }
        centerPanel.add(lblLogo, gbc);

        // B. JUDUL UTAMA
        gbc.gridy++;
        JLabel lblTitle = new JLabel("<html><center>SELAMAT DATANG DI<br>SISTEM UKM MULTIMEDIA</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(new Font("Segoe UI", Font.BOLD, 32));
        lblTitle.setForeground(new Color(30, 30, 30));
        centerPanel.add(lblTitle, gbc);

        // C. KATA SAMBUTAN
        gbc.gridy++;
        String kataSambutan = "<html><center><p style='width:500px; color:#555555; font-size:11px;'>" +
                "<i>\"Salam Desain!\"</i><br><br>" +
                "Sistem ini dirancang sebagai wadah manajemen administrasi, " +
                "portofolio karya, dan database keanggotaan UKM Multimedia.<br><br>" +
                "Silakan pilih akses login di menu sebelah kiri untuk memulai." +
                "</p></center></html>";
        
        JLabel lblDesc = new JLabel(kataSambutan, SwingConstants.CENTER);
        lblDesc.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        centerPanel.add(lblDesc, gbc);

        // Masukkan panel ke Frame Utama
        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // --- EVENTS ---
        btnKetua.addActionListener(e -> openLogin("ketua"));
        btnPengurus.addActionListener(e -> openLogin("pengurus"));
        btnAnggota.addActionListener(e -> openLogin("anggota"));
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 18)); 
        b.setBackground(new Color(212, 175, 55)); // Warna Emas
        b.setForeground(Color.BLACK);
        b.setFocusPainted(false);
        b.setBorderPainted(false);
        b.setCursor(new Cursor(Cursor.HAND_CURSOR));
        // CATATAN: Jangan setPreferredSize tinggi disini agar GridLayout bisa mengontrol tingginya
        return b;
    }

    private void openLogin(String role) {
        dispose();
        new LoginFrame(role).setVisible(true);
    }
}