import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboard extends JFrame {
    
    CardLayout cardLayout;
    JPanel mainContainer; 
    AnggotaDAO dao;
    Anggota adminLogin; // Data user yang sedang login (PENTING UNTUK EDIT PROFIL SENDIRI)
    
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
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        mainContainer.add(createMenuPanel(), "MENU");
        mainContainer.add(createDataPanel(), "DATA");

        add(mainContainer);
        cardLayout.show(mainContainer, "MENU");
    }

    // --- PANEL MENU ---
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);

        JLabel lblWelcome = new JLabel("<html><center>Halo, " + adminLogin.getNama() + "<br>Selamat Datang di Dashboard " + adminLogin.getRole() + "</center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(Style.FONT_HEADER);
        lblWelcome.setForeground(Style.COLOR_ACCENT);
        lblWelcome.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));
        panel.add(lblWelcome, BorderLayout.NORTH);

        JPanel gridPanel = new JPanel(new GridBagLayout());
        gridPanel.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0;

        // 1. MENU: DATA PENGURUS (HANYA UNTUK KETUA)
        if (adminLogin.getRole().equalsIgnoreCase("ketua")) {
            JButton btnPengurus = createBigButton("Data Pengurus", "Cari & Kelola Pengurus");
            btnPengurus.addActionListener(e -> showDataView("Pengurus"));
            gridPanel.add(btnPengurus, gbc);
            gbc.gridx++; 
        }

        // 2. MENU: DATA ANGGOTA (SEMUA ADMIN BISA AKSES)
        JButton btnAnggota = createBigButton("Data Anggota", "Cari, Update & Hapus Anggota");
        btnAnggota.addActionListener(e -> showDataView("Anggota"));
        gridPanel.add(btnAnggota, gbc);
        gbc.gridx++;

        // 3. MENU: EXPORT DATA
        JButton btnExport = createBigButton("Laporan / Export", "Simpan Data ke CSV");
        btnExport.addActionListener(e -> processExport());
        gridPanel.add(btnExport, gbc);
        
        // 4. MENU KHUSUS PENGURUS: EDIT PROFIL SAYA (Agar bisa update data sendiri)
        if (adminLogin.getRole().equalsIgnoreCase("pengurus")) {
            gbc.gridx++;
            JButton btnEditSelf = createBigButton("Profil Saya", "Update Data Pribadi");
            btnEditSelf.setBackground(Color.GRAY); // Warna beda dikit biar spesial
            btnEditSelf.addActionListener(e -> {
                // Buka Dialog Edit dengan data DIRI SENDIRI (adminLogin)
                EditDialog dialog = new EditDialog(this, adminLogin);
                dialog.setVisible(true);
                if (dialog.isSaved()) {
                    // Update label sambutan jika nama berubah
                    lblWelcome.setText("<html><center>Halo, " + adminLogin.getNama() + "<br>Selamat Datang di Dashboard " + adminLogin.getRole() + "</center></html>");
                }
            });
            gridPanel.add(btnEditSelf, gbc);
        }
        
        // 5. TOMBOL LOGOUT
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

    // --- PANEL DATA ---
    private JPanel createDataPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);

        // Header
        JPanel header = new JPanel(new BorderLayout());
        header.setOpaque(false);
        header.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        JButton btnBack = new JButton("<< KEMBALI KE MENU");
        Style.styleButton(btnBack);
        btnBack.setBackground(Color.GRAY);
        btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU"));

        lblDataTitle = new JLabel("DATA ANGGOTA", SwingConstants.CENTER);
        lblDataTitle.setFont(Style.FONT_HEADER);
        lblDataTitle.setForeground(Style.COLOR_ACCENT);

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

        // Tabel
        String[] columns = {"ID", "No. Anggota", "NIM", "Nama", "Kelas/Jbt", "Divisi", "Username", "Role"};
        model = new DefaultTableModel(columns, 0);
        table = new JTable(model);
        table.setBackground(new Color(50,50,50));
        table.setForeground(Color.WHITE);
        table.setSelectionBackground(Style.COLOR_ACCENT);
        table.setSelectionForeground(Color.BLACK);
        table.setRowHeight(25);
        
        JScrollPane sp = new JScrollPane(table);
        sp.getViewport().setBackground(Style.COLOR_BG);
        panel.add(sp, BorderLayout.CENTER);

        // Footer Actions
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

    // --- LOGIKA ---
    private void showDataView(String roleTarget) {
        this.currentViewRole = roleTarget;
        lblDataTitle.setText("MANAJEMEN DATA " + roleTarget.toUpperCase());
        txtSearch.setText("");
        loadTableData(""); 
        cardLayout.show(mainContainer, "DATA");
    }

    private void loadTableData(String keyword) {
        model.setRowCount(0);
        List<Anggota> list = dao.getAnggotaByRole(currentViewRole);
        
        for (Anggota a : list) {
            if (!keyword.isEmpty()) {
                boolean matchNama = a.getNama().toLowerCase().contains(keyword.toLowerCase());
                boolean matchNim = a.getNim().toLowerCase().contains(keyword.toLowerCase());
                if (!matchNama && !matchNim) continue; 
            }
            String infoKhusus = (a.getRole().equalsIgnoreCase("pengurus")) ? a.getJabatan() : a.getKelas();
            model.addRow(new Object[]{
                a.getId(), (a.getNomorAnggota() == null ? "-" : a.getNomorAnggota()), 
                a.getNim(), a.getNama(), infoKhusus, (a.getDivisi() == null ? "-" : a.getDivisi()), 
                a.getUsername(), a.getRole()
            });
        }
    }

    private void processEdit() {
        int row = table.getSelectedRow();
        if (row == -1) { JOptionPane.showMessageDialog(this, "Pilih baris yang mau diedit!"); return; }
        
        String nama = (String) table.getValueAt(row, 3);
        List<Anggota> results = dao.cariAnggota(nama); 
        
        if (!results.isEmpty()) {
            Anggota fullData = results.get(0); 
            EditDialog dialog = new EditDialog(this, fullData);
            dialog.setVisible(true);
            if (dialog.isSaved()) loadTableData(""); 
        }
    }

    private void processDelete() {
        int row = table.getSelectedRow();
        if (row != -1) {
            int id = (int) table.getValueAt(row, 0);
            if (JOptionPane.showConfirmDialog(this, "Yakin hapus data ini?", "Konfirmasi", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
                dao.delete(id);
                loadTableData("");
            }
        } else { JOptionPane.showMessageDialog(this, "Pilih baris dulu!"); }
    }

    private void processExport() {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("Laporan_Data.csv");
            fw.write("ID,No Anggota,NIM,Nama,Kelas/Jbt,Divisi,Username,Role\n");
            
            List<Anggota> listExport;
            if(adminLogin.getRole().equalsIgnoreCase("ketua")) {
                listExport = dao.getAnggotaByRole("pengurus");
                listExport.addAll(dao.getAnggotaByRole("anggota"));
            } else {
                listExport = dao.getAnggotaByRole("anggota");
            }

            for(Anggota a : listExport) {
                String info = (a.getRole().equalsIgnoreCase("pengurus")) ? a.getJabatan() : a.getKelas();
                fw.write(a.getId() + "," + a.getNomorAnggota() + "," + a.getNim() + "," + 
                         a.getNama() + "," + info + "," + a.getDivisi() + "," + 
                         a.getUsername() + "," + a.getRole() + "\n");
            }
            fw.close(); 
            JOptionPane.showMessageDialog(this, "Export Berhasil: Laporan_Data.csv");
        } catch (Exception ex) { ex.printStackTrace(); }
    }

    private JButton createBigButton(String title, String subtitle) {
        JButton btn = new JButton("<html><center><h2 style='margin:0'>" + title + "</h2><p style='margin-top:5px'>" + subtitle + "</p></center></html>");
        btn.setPreferredSize(new Dimension(220, 120));
        btn.setBackground(Style.COLOR_ACCENT);
        btn.setForeground(Color.BLACK);
        btn.setFocusPainted(false);
        btn.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return btn;
    }
}