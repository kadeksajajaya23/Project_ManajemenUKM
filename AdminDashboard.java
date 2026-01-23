import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.util.List;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    
    CardLayout cardLayout;
    JPanel mainContainer; 
    AnggotaDAO dao;
    Anggota adminLogin; 
    
    JTable tableData, tablePorto;
    DefaultTableModel modelData, modelPorto;
    JTextField txtSearch;
    JLabel lblDataTitle;
    String currentViewRole; 

    public AdminDashboard(Anggota admin) {
        this.adminLogin = admin;
        this.dao = new AnggotaDAO();
        setTitle("DASHBOARD - " + admin.getRole().toUpperCase());
        setSize(1200, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(Style.COLOR_BG);
        
        mainContainer.add(createMenuPanel(), "MENU");
        mainContainer.add(createDataPanel(), "DATA");
        mainContainer.add(createPortofolioPanel(), "PORTO");
        mainContainer.add(createProfilePanel(), "PROFIL");

        add(mainContainer);
        cardLayout.show(mainContainer, "MENU");
    }

    // ==========================================
    // 1. PANEL MENU (LAYOUT BARU: TERPISAH)
    // ==========================================
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);

        // --- HEADER ---
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        File f = new File("logo_mm no txt.jpg");
        if (f.exists()) {
            try {
                ImageIcon icon = new ImageIcon(f.getAbsolutePath());
                Image img = icon.getImage().getScaledInstance(110, 110, Image.SCALE_SMOOTH);
                lblLogo.setIcon(new ImageIcon(img));
            } catch (Exception e) {}
        }

        JLabel lblWelcome = new JLabel("<html><center>Halo, " + adminLogin.getNama() + "<br>Selamat Datang di Dashboard " + adminLogin.getRole() + "</center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(Style.FONT_HEADER);
        lblWelcome.setForeground(Style.COLOR_TEXT_MAIN); 
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(15)); 
        headerPanel.add(lblWelcome);

        panel.add(headerPanel, BorderLayout.NORTH);

        // --- GRID MENU BARU ---
        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        
        // POSISI 1: MENU KHUSUS KETUA (Full Width di Atas)
        if (adminLogin.getRole().equalsIgnoreCase("ketua")) {
            gbc.gridx = 0; 
            gbc.gridy = 0;
            gbc.gridwidth = 2; // Membentang 2 kolom
            
            JButton btnPengurus = createBigButton("Data Pengurus", "ACC & Kelola Akun Admin");
            btnPengurus.setBackground(Style.COLOR_PURPLE); 
            btnPengurus.addActionListener(e -> showDataView("Pengurus"));
            
            gridPanel.add(btnPengurus, gbc);
            
            gbc.gridy++;
            gridPanel.add(Box.createVerticalStrut(20), gbc);
        }

        // Reset grid untuk menu umum (2 Kolom)
        gbc.gridwidth = 1; 
        int row = (adminLogin.getRole().equalsIgnoreCase("ketua")) ? 2 : 0;

        // BARIS 1: MANAJEMEN ANGGOTA
        gbc.gridy = row;
        gbc.gridx = 0;
        JButton btnAnggota = createBigButton("Data Anggota", "Database Lengkap");
        btnAnggota.setBackground(Style.COLOR_ACCENT); // Biru
        btnAnggota.addActionListener(e -> showDataView("Anggota"));
        gridPanel.add(btnAnggota, gbc);

        gbc.gridx = 1;
        JButton btnKarya = createBigButton("Galeri Karya", "Lihat Portofolio");
        btnKarya.setBackground(Style.COLOR_TEAL); // Teal
        btnKarya.addActionListener(e -> showPortofolioView());
        gridPanel.add(btnKarya, gbc);

        // BARIS 2: TOOLS
        row++;
        gbc.gridy = row;
        
        gbc.gridx = 0;
        JButton btnExport = createBigButton("Export CSV", "Simpan Laporan");
        btnExport.setBackground(Style.COLOR_ORANGE); 
        btnExport.setForeground(Color.BLACK); 
        btnExport.addActionListener(e -> processExport());
        gridPanel.add(btnExport, gbc);
        
        gbc.gridx = 1;
        JButton btnEditSelf = createBigButton("ID Card Saya", "Kartu Identitas");
        btnEditSelf.setBackground(new Color(80, 80, 90)); 
        btnEditSelf.addActionListener(e -> cardLayout.show(mainContainer, "PROFIL"));
        gridPanel.add(btnEditSelf, gbc);
        
        panel.add(gridPanel, BorderLayout.CENTER);

        // FOOTER (LOGOUT)
        JPanel bottomPanel = new JPanel(); 
        bottomPanel.setOpaque(false);
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
        
        JButton btnLogout = new JButton("LOGOUT SYSTEM");
        Style.styleButtonDanger(btnLogout);
        btnLogout.setPreferredSize(new Dimension(250, 50));
        btnLogout.addActionListener(e -> { new MainMenuFrame().setVisible(true); dispose(); });
        
        bottomPanel.add(btnLogout);
        panel.add(bottomPanel, BorderLayout.SOUTH);
        
        return panel;
    }

    // --- PANEL PROFIL (ID CARD) ---
    private JPanel createProfilePanel() {
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(Style.COLOR_BG);
        JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT)); header.setOpaque(false); header.setBorder(new EmptyBorder(10, 10, 10, 10));
        JButton btnBack = new JButton("<< KEMBALI"); Style.styleButton(btnBack); btnBack.setBackground(new Color(50,50,60)); btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU")); header.add(btnBack); wrapper.add(header, BorderLayout.NORTH);
        
        JPanel centerPanel = new JPanel(new GridBagLayout()); centerPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints(); gbc.insets = new Insets(0, 30, 0, 30); gbc.gridx = 0; gbc.gridy = 0;

        JPanel idCard = new GradientCardPanel(); idCard.setLayout(new BoxLayout(idCard, BoxLayout.Y_AXIS)); idCard.setPreferredSize(new Dimension(350, 520)); idCard.setBorder(new EmptyBorder(0, 0, 0, 0));
        idCard.add(Box.createVerticalStrut(30)); 
        JLabel lblFoto = new JLabel(); lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT); lblFoto.setPreferredSize(new Dimension(140, 140)); lblFoto.setMaximumSize(new Dimension(140, 140)); lblFoto.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); 
        if (adminLogin.getFotoPath() != null) { try { ImageIcon icon = new ImageIcon(adminLogin.getFotoPath()); Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH); lblFoto.setIcon(new ImageIcon(img)); } catch (Exception e) { lblFoto.setText("No Photo"); lblFoto.setForeground(Color.WHITE); } } else { lblFoto.setText("No Photo"); lblFoto.setForeground(Color.WHITE); lblFoto.setHorizontalAlignment(SwingConstants.CENTER); } idCard.add(lblFoto);
        idCard.add(Box.createVerticalStrut(20));
        JLabel lblNama = new JLabel(adminLogin.getNama().toUpperCase()); lblNama.setAlignmentX(Component.CENTER_ALIGNMENT); lblNama.setFont(new Font("Segoe UI", Font.BOLD, 22)); lblNama.setForeground(Color.WHITE); idCard.add(lblNama);
        JLabel lblRole = new JLabel("  " + adminLogin.getRole().toUpperCase() + "  "); lblRole.setAlignmentX(Component.CENTER_ALIGNMENT); lblRole.setFont(new Font("Consolas", Font.BOLD, 14)); lblRole.setForeground(Style.COLOR_ACCENT); lblRole.setOpaque(true); lblRole.setBackground(new Color(20, 20, 30)); lblRole.setBorder(BorderFactory.createLineBorder(Style.COLOR_ACCENT, 1)); idCard.add(Box.createVerticalStrut(5)); idCard.add(lblRole);
        idCard.add(Box.createVerticalStrut(20));
        JSeparator sep = new JSeparator(); sep.setMaximumSize(new Dimension(280, 2)); sep.setForeground(new Color(100, 100, 100)); idCard.add(sep); idCard.add(Box.createVerticalStrut(20));
        JPanel infoPanel = new JPanel(new GridLayout(3, 1, 5, 8)); infoPanel.setOpaque(false); infoPanel.setMaximumSize(new Dimension(280, 120));
        infoPanel.add(detailLbl("ID ADMIN", (adminLogin.getNomorAnggota() == null ? "-" : adminLogin.getNomorAnggota()))); infoPanel.add(detailLbl("NIM", adminLogin.getNim())); infoPanel.add(detailLbl("JABATAN", (adminLogin.getJabatan() == null ? "-" : adminLogin.getJabatan()))); idCard.add(infoPanel);
        idCard.add(Box.createVerticalGlue());
        JLabel lblStatus = new JLabel("STATUS: ACTIVE", SwingConstants.CENTER); lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT); lblStatus.setForeground(new Color(50, 255, 100)); lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12)); idCard.add(lblStatus); idCard.add(Box.createVerticalStrut(20));
        centerPanel.add(idCard, gbc);

        gbc.gridx++; JPanel actionPanel = new JPanel(new GridBagLayout()); actionPanel.setOpaque(false);
        JButton btnEditData = new JButton("<html><center>EDIT DATA<br>PRIBADI</center></html>"); Style.styleButton(btnEditData); btnEditData.setPreferredSize(new Dimension(150, 60));
        JLabel lblKet = new JLabel("<html><center>Ingin mengubah<br>Password / Foto?</center></html>"); lblKet.setForeground(Color.GRAY);
        GridBagConstraints agbc = new GridBagConstraints(); agbc.gridy=0; actionPanel.add(lblKet, agbc); agbc.gridy++; actionPanel.add(Box.createVerticalStrut(10), agbc); agbc.gridy++; actionPanel.add(btnEditData, agbc);
        centerPanel.add(actionPanel, gbc);
        btnEditData.addActionListener(e -> { EditDialog dialog = new EditDialog(this, adminLogin); dialog.setVisible(true); if (dialog.isSaved()) { JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate! Silakan Login Ulang."); } });

        JScrollPane scrollPane = new JScrollPane(centerPanel); scrollPane.setBorder(null); scrollPane.getViewport().setBackground(Style.COLOR_BG); scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        wrapper.add(scrollPane, BorderLayout.CENTER); return wrapper;
    }

    private JPanel detailLbl(String label, String val) { JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false); JLabel l1 = new JLabel(label); l1.setForeground(Color.LIGHT_GRAY); l1.setFont(new Font("Segoe UI", Font.PLAIN, 10)); JLabel l2 = new JLabel(val); l2.setForeground(Color.WHITE); l2.setFont(new Font("Segoe UI", Font.BOLD, 14)); p.add(l1, BorderLayout.NORTH); p.add(l2, BorderLayout.CENTER); return p; }
    
    // --- DATA & PORTO ---
    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new BorderLayout()); Style.stylePanel(panel);
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false); header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnBack = new JButton("<< KEMBALI"); Style.styleButton(btnBack); btnBack.setBackground(Color.DARK_GRAY); btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));
        lblDataTitle = new JLabel("DATA VIEW", SwingConstants.CENTER); lblDataTitle.setFont(Style.FONT_HEADER); lblDataTitle.setForeground(Style.COLOR_TEXT_MAIN);
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT)); searchPanel.setOpaque(false);
        txtSearch = new JTextField(20); txtSearch.addKeyListener(new java.awt.event.KeyAdapter() { public void keyReleased(java.awt.event.KeyEvent evt) { loadTableData(txtSearch.getText()); } });
        searchPanel.add(new JLabel("<html><font color='white'>Cari:</font></html>")); searchPanel.add(txtSearch);
        header.add(btnBack, BorderLayout.WEST); header.add(lblDataTitle, BorderLayout.CENTER); header.add(searchPanel, BorderLayout.EAST);
        panel.add(header, BorderLayout.NORTH);
        String[] columns = {"ID", "No. Anggota", "NIM", "Nama", "Kelas/Jbt", "Divisi", "Username", "Status"};
        modelData = new DefaultTableModel(columns, 0); tableData = new JTable(modelData); Style.styleTable(tableData);
        panel.add(new JScrollPane(tableData), BorderLayout.CENTER);
        JPanel footer = new JPanel(); Style.stylePanel(footer);
        JButton btnApprove = new JButton("ACC PENGURUS"); Style.styleButton(btnApprove); btnApprove.setBackground(new Color(46, 204, 113));
        JButton btnEdit = new JButton("Update"); Style.styleButton(btnEdit); JButton btnDelete = new JButton("Hapus"); Style.styleButton(btnDelete); btnDelete.setBackground(Color.RED);
        footer.add(btnApprove); footer.add(btnEdit); footer.add(btnDelete); panel.add(footer, BorderLayout.SOUTH);
        btnEdit.addActionListener(e -> processEdit()); btnDelete.addActionListener(e -> processDelete());
        btnApprove.addActionListener(e -> { int row = tableData.getSelectedRow(); if (row != -1) { String status = (String) tableData.getValueAt(row, 7); if (status != null && status.equalsIgnoreCase("Pending")) { dao.approvePengurus((int) tableData.getValueAt(row, 0)); JOptionPane.showMessageDialog(this, "Berhasil ACC!"); loadTableData(""); } else JOptionPane.showMessageDialog(this, "User sudah Active!"); } }); return panel;
    }

    private JPanel createPortofolioPanel() {
        JPanel panel = new JPanel(new BorderLayout()); Style.stylePanel(panel);
        JPanel header = new JPanel(new BorderLayout()); header.setOpaque(false); header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        JButton btnBack = new JButton("<< KEMBALI"); Style.styleButton(btnBack); btnBack.setBackground(Color.DARK_GRAY); btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));
        JLabel lblTitle = new JLabel("GALERI KARYA", SwingConstants.CENTER); lblTitle.setFont(Style.FONT_HEADER); lblTitle.setForeground(Style.COLOR_TEXT_MAIN);
        header.add(btnBack, BorderLayout.WEST); header.add(lblTitle, BorderLayout.CENTER); panel.add(header, BorderLayout.NORTH);
        String[] cols = {"ID", "Nama Pengupload", "Judul Karya", "File Path"}; modelPorto = new DefaultTableModel(cols, 0); tablePorto = new JTable(modelPorto); Style.styleTable(tablePorto);
        panel.add(new JScrollPane(tablePorto), BorderLayout.CENTER);
        JPanel footer = new JPanel(); Style.stylePanel(footer); JButton btnOpen = new JButton("BUKA FILE"); Style.styleButton(btnOpen);
        btnOpen.addActionListener(e -> { int row = tablePorto.getSelectedRow(); if (row != -1) { try { File file = new File((String) tablePorto.getValueAt(row, 3)); if (file.exists()) Desktop.getDesktop().open(file); else JOptionPane.showMessageDialog(this, "File hilang!"); } catch (Exception ex) { JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage()); } } });
        footer.add(btnOpen); panel.add(footer, BorderLayout.SOUTH); return panel;
    }

    private void showDataView(String roleTarget) { this.currentViewRole = roleTarget; lblDataTitle.setText("DATA " + roleTarget.toUpperCase()); txtSearch.setText(""); loadTableData(""); cardLayout.show(mainContainer, "DATA"); }
    private void showPortofolioView() { modelPorto.setRowCount(0); List<Portofolio> list = dao.getAllPortofolio(); for (Portofolio p : list) modelPorto.addRow(new Object[]{ p.getId(), p.getNamaAnggota(), p.getJudul(), p.getFilePath() }); cardLayout.show(mainContainer, "PORTO"); }
    private void loadTableData(String keyword) { modelData.setRowCount(0); List<Anggota> list = dao.getAnggotaByRole(currentViewRole); for (Anggota a : list) { if (!keyword.isEmpty()) { if (!a.getNama().toLowerCase().contains(keyword.toLowerCase()) && !a.getNim().toLowerCase().contains(keyword.toLowerCase())) continue; } String info = (a.getRole().equalsIgnoreCase("pengurus") || a.getRole().equalsIgnoreCase("ketua")) ? a.getJabatan() : a.getKelas(); modelData.addRow(new Object[]{ a.getId(), a.getNomorAnggota(), a.getNim(), a.getNama(), info, a.getDivisi(), a.getUsername(), a.getStatus() }); } }
    private void processEdit() { int row = tableData.getSelectedRow(); if (row != -1) { String nama = (String) tableData.getValueAt(row, 3); List<Anggota> res = dao.cariAnggota(nama); if (!res.isEmpty()) { EditDialog d = new EditDialog(this, res.get(0)); d.setVisible(true); if (d.isSaved()) loadTableData(""); } } }
    private void processDelete() { int row = tableData.getSelectedRow(); if (row != -1) { if (JOptionPane.showConfirmDialog(this, "Hapus?", "Konfirm", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) { dao.delete((int) tableData.getValueAt(row, 0)); loadTableData(""); } } }
    private void processExport() {
        // 1. Siapkan File Chooser (Dialog Simpan)
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("Simpan Laporan CSV");
        fileChooser.setSelectedFile(new File("Laporan_Data.csv")); // Nama default

        // 2. Tampilkan Dialog Save
        int userSelection = fileChooser.showSaveDialog(this);

        if (userSelection == JFileChooser.APPROVE_OPTION) {
            File fileToSave = fileChooser.getSelectedFile();
            
            // Pastikan ekstensi .csv
            if (!fileToSave.getAbsolutePath().endsWith(".csv")) {
                fileToSave = new File(fileToSave.getAbsolutePath() + ".csv");
            }

            try {
                // 3. Proses Tulis File di Lokasi yang Dipilih
                java.io.FileWriter fw = new java.io.FileWriter(fileToSave);
                
                // Header CSV
                fw.write("ID,No Anggota,NIM,Nama,Kelas/Jbt,Divisi,Username,Role,Status\n");
                
                // Ambil Data
                List<Anggota> list = adminLogin.getRole().equalsIgnoreCase("ketua") ? dao.getAnggotaByRole("pengurus") : dao.getAnggotaByRole("anggota");
                if(adminLogin.getRole().equalsIgnoreCase("ketua")) list.addAll(dao.getAnggotaByRole("anggota"));
                
                // Tulis Baris Data
                for(Anggota a : list) {
                    String info = a.getRole().equalsIgnoreCase("pengurus") ? a.getJabatan() : a.getKelas();
                    // Handle null values untuk mencegah error "null" di CSV
                    String safeInfo = (info == null) ? "-" : info;
                    
                    fw.write(a.getId() + "," + 
                             a.getNomorAnggota() + "," + 
                             a.getNim() + "," + 
                             a.getNama() + "," + 
                             safeInfo + "," + 
                             a.getDivisi() + "," + 
                             a.getUsername() + "," + 
                             a.getRole() + "," + 
                             a.getStatus() + "\n");
                }
                
                fw.close(); 
                JOptionPane.showMessageDialog(this, "Data Berhasil Disimpan di:\n" + fileToSave.getAbsolutePath());
                
            } catch (Exception ex) {
                ex.printStackTrace();
                JOptionPane.showMessageDialog(this, "Gagal Menyimpan File: " + ex.getMessage());
            }
        }
    }
    
    private JButton createBigButton(String title, String subtitle) {
        JButton btn = new JButton("<html><center><h2 style='margin:0'>" + title + "</h2><p style='margin-top:5px; font-weight:normal; font-size:10px;'>" + subtitle + "</p></center></html>");
        btn.setPreferredSize(new Dimension(240, 120));
        Style.styleButton(btn);
        return btn;
    }

    class GradientCardPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int w = getWidth(); int h = getHeight();
            g2.setColor(new Color(35, 39, 50));
            g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 30, 30));
            GradientPaint gp = new GradientPaint(0, 0, Style.COLOR_ACCENT, w, 0, new Color(114, 9, 183));
            g2.setPaint(gp);
            g2.fill(new RoundRectangle2D.Double(0, 0, w, 80, 30, 30));
            g2.fillRect(0, 40, w, 40);
            g2.setColor(new Color(60, 60, 70));
            g2.draw(new RoundRectangle2D.Double(0, 0, w-1, h-1, 30, 30));
            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 16));
            FontMetrics fm = g2.getFontMetrics();
            String title = "UKM MULTIMEDIA";
            int x = (w - fm.stringWidth(title)) / 2;
            g2.drawString(title, x, 30);
            g2.setFont(new Font("Segoe UI", Font.PLAIN, 10));
            String sub = "Official Admin Card";
            fm = g2.getFontMetrics();
            int x2 = (w - fm.stringWidth(sub)) / 2;
            g2.drawString(sub, x2, 45);
        }
    }
}