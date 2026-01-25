import java.awt.*;
import javax.swing.*;

public class MainMenuFrame extends JFrame {
    public MainMenuFrame() {
        setTitle("Main Menu - Sistem Informasi UKM Multimedia");
        setSize(1000, 650);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel(new GridBagLayout());
        sidebar.setBackground(Style.COLOR_PANEL); 
        sidebar.setPreferredSize(new Dimension(280, 0));
        sidebar.setBorder(BorderFactory.createMatteBorder(0, 0, 0, 1, new Color(60,60,70)));

        JPanel navPanel = new JPanel(new GridLayout(3, 1, 15, 15));
        navPanel.setOpaque(false);
        navPanel.setPreferredSize(new Dimension(240, 480)); 

        JButton btnKetua = menuButton("Login Ketua");
        JButton btnPengurus = menuButton("Login Pengurus");
        JButton btnAnggota = menuButton("Login Anggota");

        navPanel.add(btnKetua); 
        navPanel.add(btnPengurus); 
        navPanel.add(btnAnggota);
        
        sidebar.add(navPanel);

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setBackground(Style.COLOR_BG); 
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.CENTER;

        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        java.io.File fileGambar = new java.io.File("logo_mm no txt.jpg");
        if (fileGambar.exists()) {
            try {
                ImageIcon iconOriginal = new ImageIcon(fileGambar.getAbsolutePath());
                Image img = iconOriginal.getImage().getScaledInstance(240, 240, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        } else lblLogo.setText("<html><font color='white'>[Logo Tidak Ditemukan]</font></html>");
        centerPanel.add(lblLogo, gbc);

        gbc.gridy++;
        JLabel lblTitle = new JLabel("<html><center>SELAMAT DATANG DI<br>SISTEM UKM MULTIMEDIA</center></html>", SwingConstants.CENTER);
        lblTitle.setFont(Style.FONT_HEADER);
        lblTitle.setForeground(Style.COLOR_TEXT_MAIN);
        centerPanel.add(lblTitle, gbc);

        gbc.gridy++;
        JLabel lblDesc = new JLabel("<html><center><p style='width:500px; color:#A0A0A0;'><i>\"Salam Desain!\"</i><br><br>Platform manajemen administrasi & karya digital.<br>Silakan login untuk memulai.</p></center></html>", SwingConstants.CENTER);
        lblDesc.setFont(Style.FONT_NORMAL);
        centerPanel.add(lblDesc, gbc);

        add(sidebar, BorderLayout.WEST);
        add(centerPanel, BorderLayout.CENTER);

        // Events
        btnKetua.addActionListener(e -> openLogin("ketua"));
        btnPengurus.addActionListener(e -> openLogin("pengurus"));
        btnAnggota.addActionListener(e -> openLogin("anggota"));
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        Style.styleButton(b); 
        b.setFont(new Font("Segoe UI", Font.BOLD, 18));
        return b;
    }

    private void openLogin(String role) {
        dispose();
        new LoginFrame(role).setVisible(true);
    }
}