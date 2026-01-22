import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    
    // -- KOMPONEN UTAMA (CardLayout) --
    CardLayout cardLayout;
    JPanel mainContainer; 
    AnggotaDAO dao;
    Anggota adminLogin; 
    
    // -- KOMPONEN TAMPILAN DATA --
    JTable table;
    DefaultTableModel model;
    JTextField txtSearch;
    JLabel lblDataTitle;
    String currentViewRole; 

    public AdminDashboard(Anggota admin) {
        this.adminLogin = admin;
        this.dao = new AnggotaDAO();
        
        setTitle("DASHBOARD - " + admin.getRole().toUpperCase());
        setSize(1000, 650);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        initUI();
    }

    private void initUI() {
        // SISTEM KARTU (MENU vs DATA)
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 1. Buat Panel Kartu 1: MENU UTAMA
        JPanel pnlMenu = createMenuPanel();
        
        // 2. Buat Panel Kartu 2: TABEL DATA
        JPanel pnlData = createDataPanel();

        // Masukkan keduanya ke Container
        mainContainer.add(pnlMenu, "MENU");
        mainContainer.add(pnlData, "DATA");

        add(mainContainer);
        
        // Tampilkan MENU terlebih dahulu
        cardLayout.show(mainContainer, "MENU");
    }

    // ==========================================
    // BAGIAN 1: HALAMAN MENU UTAMA (HOME)
    // ==========================================
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);

        // Header Sambutan
        JLabel lblWelcome = new JLabel("<html><center>Halo, " + adminLogin.getNama() + "<br>Selamat Datang di Dashboard " + adminLogin.getRole() + "</center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(Style.FONT_HEADER);
        lblTitleStyle(lblWelcome);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(lblWelcome, BorderLayout.NORTH);

        // Grid Menu Button
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0;

        // --- MENU 1: KELOLA PENGURUS (Khusus Ketua) ---
        if (adminLogin.getRole().equalsIgnoreCase("ketua")) {
            JButton btnPengurus = createBigButton("Data Pengurus", "Cari & Kelola Pengurus");
            btnPengurus.addActionListener(e -> showDataView("Pengurus"));
            gridPanel.add(btnPengurus, gbc);
            gbc.gridx++; 
        }

        // --- MENU 2: KELOLA ANGGOTA (Semua Admin) ---
        JButton btnAnggota = createBigButton("Data Anggota", "Cari, Update & Hapus Anggota");
        btnAnggota.addActionListener(e -> showDataView("Anggota"));
        gridPanel.add(btnAnggota, gbc);
        gbc.gridx++;

        // --- MENU 3: EXPORT DATA ---
        JButton btnExport = createBigButton("Laporan / Export", "Simpan Data ke CSV");
        btnExport.addActionListener(e -> processExport());
        gridPanel.add(btnExport, gbc);
        
        // --- MENU 4: LOGOUT (Di Bawah) ---
        JPanel logoutPanel = new JPanel();
        logoutPanel.setOpaque(false);
        JButton btnLogout = new JButton("LOGOUT SYSTEM");
        Style.styleButton(btnLogout);
        btnLogout.setBackground(Color.RED);
        btnLogout.setPreferredSize(new Dimension(200, 40));
        btnLogout.addActionListener(e -> {
            new MainMenuFrame().setVisible(true);
            dispose();
        });
        logoutPanel.add(btnLogout);
        logoutPanel.setBorder(BorderFactory.createEmptyBorder(20,0,50,0));

        panel.add(gridPanel, BorderLayout.CENTER);
        panel.add(logoutPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // BAGIAN 2: HALAMAN DATA (TABEL & FITUR)
    // ==========================================
    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);

        // --- HEADER DATA (Tombol Kembali & Search) ---
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnBack = new JButton("<< KEMBALI KE MENU");
        Style.styleButton(btnBack);
        btnBack.setBackground(Color.GRAY);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));

        lblDataTitle = new JLabel("DATA ANGGOTA", SwingConstants.CENTER);
        lblTitleStyle(lblDataTitle);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        searchPanel.setOpaque(false);
        JLabel lblCari = new JLabel("Cari Nama/NIM: "); lblCari.setForeground(Color.WHITE);
        txtSearch = new JTextField(20);
        
        txtSearch.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) { loadTableData(txtSearch.getText()); }
        });

        searchPanel.add(lblCari); searchPanel.add(txtSearch);

        header.add(btnBack, BorderLayout.WEST);
        header.add(lblDataTitle, BorderLayout.CENTER);
        header.add(searchPanel, BorderLayout.EAST);
        
        panel.add(header, BorderLayout.NORTH);

        // --- TABEL ---
        String[] columns = {"ID", "No. Anggota", "NIM", "Nama", "Kelas/Jbt", "Divisi", "Username", "Role"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        styleTable(table);
        
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Style.COLOR_BG);
        panel.add(sp, BorderLayout.CENTER);

        // --- FOOTER ACTIONS ---
        JPanel footer = new JPanel(); 
        Style.stylePanel(footer);
        
        JButton btnEdit = new JButton("Update Data Terpilih");
        Style.styleButton(btnEdit);
        
        JButton btnDelete = new JButton("Hapus Data");
        Style.styleButton(btnDelete); btnDelete.setBackground(Color.RED);

        footer.add(btnEdit);
        footer.add(btnDelete);
        panel.add(footer, BorderLayout.SOUTH);

        btnEdit.addActionListener(e -> processEdit());
        btnDelete.addActionListener(e -> processDelete());

        return panel;
    }

   
}