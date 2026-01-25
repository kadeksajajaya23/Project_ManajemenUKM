import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.MatteBorder;

public class LoginFrame extends JFrame {
    
    JPanel mainPanel, cardPanel, pnlFoto;
    JLabel lblTitle, lblSubtitle, lblFotoPreview;
    
    JTextField txtNim, txtNama, txtUser;
    JPasswordField txtPass;
    JComboBox<String> cmbKelas, cmbDivisi, cmbJabatan;
    JButton btnAction, btnSwitch, btnPilihFoto;
    
    String role = "anggota"; 
    String selectedPhotoPath = null;
    boolean isLoginMode = true;

    public LoginFrame(String role) {
        this.role = role;
        setTitle("AKSES MASUK - " + role.toUpperCase());
        setSize(500, 800);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        mainPanel = new JPanel(new GridBagLayout()); 
        mainPanel.setBackground(Style.COLOR_BG);
        
        cardPanel = new JPanel(new GridBagLayout());
        cardPanel.setBackground(Style.COLOR_PANEL); // Warna Panel Baru
        
        cardPanel.setBorder(new CompoundBorder(
            new LineBorder(Style.COLOR_ACCENT, 1, true), 
            new EmptyBorder(30, 40, 30, 40)
        ));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(0, 0, 15, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL; 
        gbc.gridx = 0; 
        gbc.weightx = 1.0;
        int row = 0;

        lblTitle = new JLabel("LOGIN " + role.toUpperCase(), SwingConstants.CENTER);
        lblTitle.setFont(Style.FONT_HEADER);
        lblTitle.setForeground(Style.COLOR_ACCENT); 
        gbc.gridy = row++; cardPanel.add(lblTitle, gbc);
        
        lblSubtitle = new JLabel("Silakan masuk untuk melanjutkan", SwingConstants.CENTER);
        lblSubtitle.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        lblSubtitle.setForeground(Color.LIGHT_GRAY);
        gbc.gridy = row++; gbc.insets = new Insets(0, 0, 25, 0);
        cardPanel.add(lblSubtitle, gbc);

        gbc.insets = new Insets(0, 0, 15, 0); 

        txtNim = createStyledTextField();
        addField(cardPanel, "NIM", txtNim, gbc, row++);

        txtNama = createStyledTextField();
        addField(cardPanel, "Nama Lengkap", txtNama, gbc, row++);

        cmbKelas = createStyledCombo(new String[]{"Pilih Kelas...", "A", "B1", "B2"});
        addField(cardPanel, "Kelas", cmbKelas, gbc, row++);

        cmbDivisi = createStyledCombo(new String[]{"Pilih Divisi...", "3D Modelling", "Graphic Design", "Audio & Video Editing"});
        addField(cardPanel, "Divisi Pilihan", cmbDivisi, gbc, row++);

        cmbJabatan = createStyledCombo(new String[]{"Pilih Jabatan...", "Wakil Ketua", "Sekretaris", "Bendahara", "HUMAS", "Kreatif"});
        addField(cardPanel, "Jabatan", cmbJabatan, gbc, row++);

        btnPilihFoto = new JButton("Pilih Foto...");
        styleSmallButton(btnPilihFoto);
        lblFotoPreview = new JLabel(" Belum ada file", SwingConstants.LEFT);
        lblFotoPreview.setForeground(Style.COLOR_ACCENT);
        lblFotoPreview.setFont(new Font("Segoe UI", Font.ITALIC, 11));
        
        pnlFoto = new JPanel(new BorderLayout()); 
        pnlFoto.setOpaque(false);
        pnlFoto.add(btnPilihFoto, BorderLayout.WEST);
        pnlFoto.add(lblFotoPreview, BorderLayout.CENTER);
        addField(cardPanel, "Foto Profil", pnlFoto, gbc, row++);

        txtUser = createStyledTextField();
        addField(cardPanel, "Username", txtUser, gbc, row++);

        txtPass = new JPasswordField();
        styleTextField(txtPass);
        addField(cardPanel, "Password", txtPass, gbc, row++);

        gbc.gridy = row++;
        gbc.insets = new Insets(15, 0, 10, 0);
        btnAction = new JButton("MASUK SEKARANG");
        Style.styleButton(btnAction); // Tombol Biru
        btnAction.setPreferredSize(new Dimension(100, 45));
        cardPanel.add(btnAction, gbc);

        gbc.gridy = row++;
        gbc.insets = new Insets(0, 0, 0, 0);
        btnSwitch = new JButton("Belum punya akun? Daftar");
        btnSwitch.setContentAreaFilled(false);
        btnSwitch.setBorderPainted(false);
        btnSwitch.setForeground(Color.GRAY);
        btnSwitch.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        if (!role.equalsIgnoreCase("ketua")) {
            cardPanel.add(btnSwitch, gbc);
        }

        mainPanel.add(cardPanel); 
        JScrollPane scrollPane = new JScrollPane(mainPanel);
        scrollPane.setBorder(null);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);
        add(scrollPane);

        btnSwitch.addActionListener(e -> { isLoginMode = !isLoginMode; refreshMode(); });
        btnPilihFoto.addActionListener(e -> uploadFoto());
        btnAction.addActionListener(e -> processAction());

        refreshMode(); 
    }

    private void addField(JPanel panel, String labelText, JComponent field, GridBagConstraints gbc, int gridy) {
        JLabel lbl = new JLabel(labelText);
        lbl.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lbl.setForeground(Style.COLOR_TEXT_DIM); // Label Abu Terang
        JPanel itemPanel = new JPanel(new BorderLayout(0, 5));
        itemPanel.setOpaque(false);
        itemPanel.add(lbl, BorderLayout.NORTH);
        itemPanel.add(field, BorderLayout.CENTER);
        field.putClientProperty("wrapper", itemPanel);
        gbc.gridy = gridy;
        panel.add(itemPanel, gbc);
    }

    private JTextField createStyledTextField() {
        JTextField t = new JTextField();
        styleTextField(t);
        return t;
    }

    private void styleTextField(JTextField t) {
        t.setBackground(Style.COLOR_INPUT_BG);
        t.setForeground(Color.WHITE);
        t.setCaretColor(Style.COLOR_ACCENT);
        t.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        t.setBorder(BorderFactory.createCompoundBorder(
            new MatteBorder(0, 0, 2, 0, Style.COLOR_ACCENT), // Garis Bawah Biru
            new EmptyBorder(5, 5, 5, 5)
        ));
        t.setPreferredSize(new Dimension(100, 35));
    }
    
    private JComboBox<String> createStyledCombo(String[] items) {
        JComboBox<String> c = new JComboBox<>(items);
        c.setBackground(Style.COLOR_INPUT_BG);
        c.setForeground(Color.WHITE);
        c.setPreferredSize(new Dimension(100, 35));
        return c;
    }

    private void styleSmallButton(JButton btn) {
        btn.setBackground(new Color(60, 60, 70));
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createLineBorder(new Color(80,80,90)));
        btn.setPreferredSize(new Dimension(100, 30));
    }

    private void refreshMode() {
        boolean isReg = !isLoginMode;
        setFieldVisible(txtNim, isReg);
        setFieldVisible(txtNama, isReg);
        setFieldVisible(pnlFoto, isReg);
        setFieldVisible(cmbKelas, false);
        setFieldVisible(cmbDivisi, false);
        setFieldVisible(cmbJabatan, false);

        if (isReg) {
            if (role.equalsIgnoreCase("anggota")) { setFieldVisible(cmbKelas, true); setFieldVisible(cmbDivisi, true); }
            else if (role.equalsIgnoreCase("pengurus")) { setFieldVisible(cmbJabatan, true); }
        }

        lblTitle.setText(isLoginMode ? "LOGIN " + role.toUpperCase() : "REGISTRASI");
        lblSubtitle.setText(isLoginMode ? "Masuk kembali untuk mengakses dashboard" : "Lengkapi data diri untuk bergabung");
        btnAction.setText(isLoginMode ? "MASUK" : "DAFTAR");
        
        btnSwitch.setText(isLoginMode ? "<html>Belum punya akun? <font color='#4361EE'>Daftar disini</font></html>" : "<html>Sudah punya akun? <font color='#4361EE'>Login disini</font></html>");
        
        if (role.equalsIgnoreCase("ketua")) setFieldVisible(pnlFoto, false);
        cardPanel.revalidate(); cardPanel.repaint();
    }

    private void setFieldVisible(JComponent field, boolean visible) {
        JPanel wrapper = (JPanel) field.getClientProperty("wrapper");
        if (wrapper != null) wrapper.setVisible(visible);
    }

    private void processAction() {
        AnggotaDAO dao = new AnggotaDAO();
        if (isLoginMode) {
            Anggota a = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
            if (a != null) {
                if (a.getStatus() != null && a.getStatus().equalsIgnoreCase("Pending")) { JOptionPane.showMessageDialog(this, "Akun Pending (Tunggu ACC Ketua)!"); return; }
                if (a.getRole().equalsIgnoreCase(role)) {
                    dispose(); 
                    if (role.equalsIgnoreCase("ketua") || role.equalsIgnoreCase("pengurus")) new AdminDashboard(a).setVisible(true);
                    else new MemberDashboard(a).setVisible(true);
                } else JOptionPane.showMessageDialog(this, "Salah Role!");
            } else JOptionPane.showMessageDialog(this, "Login Gagal!");
        } else {
            if (role.equalsIgnoreCase("ketua")) return;
            if (txtNim.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Isi data!"); return; }
            Anggota a = new Anggota(); 
            a.setNim(txtNim.getText()); a.setNama(txtNama.getText());
            a.setUsername(txtUser.getText()); a.setPassword(new String(txtPass.getPassword()));
            a.setFotoPath(selectedPhotoPath); a.setRole(role);
            if (role.equalsIgnoreCase("anggota")) { a.setKelas(cmbKelas.getSelectedItem().toString()); a.setDivisi(cmbDivisi.getSelectedItem().toString()); } 
            else if (role.equalsIgnoreCase("pengurus")) { a.setJabatan(cmbJabatan.getSelectedItem().toString()); }
            dao.register(a);
            JOptionPane.showMessageDialog(this, "Berhasil Daftar! Silakan Login.");
            isLoginMode = true; refreshMode();
        }
    }
    
    private void uploadFoto() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File f = fc.getSelectedFile();
                File d = new File("uploads/" + System.currentTimeMillis() + "_" + f.getName());
                d.getParentFile().mkdirs();
                Files.copy(f.toPath(), d.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedPhotoPath = d.getPath(); lblFotoPreview.setText(f.getName());
            } catch (Exception e) {}
        }
    }
}