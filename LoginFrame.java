import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class LoginFrame extends JFrame {

    // ===== GLOBAL COMPONENT =====
    JLabel lblTitle;
    JTextField txtUser, txtNim, txtNama;
    JComboBox<String> cmbKelas, cmbDivisi, cmbJabatan;
    JPasswordField txtPass;
    JButton btnAction, btnSwitch, btnPilihFoto;
    JLabel lblFotoPreview;

    String role = "anggota"; 
    String selectedPhotoPath = null;
    boolean isLoginMode = true;

    public LoginFrame(String role) {
        this.role = role;
        setTitle("UKM Multimedia System - " + role.toUpperCase());
        setSize(450, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        Style.stylePanel(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblTitle = new JLabel("", SwingConstants.CENTER);
        lblTitle.setFont(Style.FONT_HEADER);
        lblTitle.setForeground(Style.COLOR_ACCENT);

        // --- REGISTER FIELDS ---
        txtNim = new JTextField();
        txtNama = new JTextField();

        // Pilihan untuk ANGGOTA
        cmbKelas = new JComboBox<>(new String[]{"A", "B1", "B2"});
        cmbDivisi = new JComboBox<>(new String[]{
            "3D Modelling", 
            "Graphic Design", 
            "Audio & Video Editing"
        });

        // Pilihan untuk PENGURUS
        cmbJabatan = new JComboBox<>(new String[]{
            "Wakil Ketua", 
            "Sekretaris", 
            "Bendahara", 
            "HUMAS", 
            "Kreatif"
        });

        btnPilihFoto = new JButton("Upload Foto Profil");
        Style.styleButton(btnPilihFoto);
        btnPilihFoto.setBackground(Color.GRAY);

        lblFotoPreview = new JLabel("", SwingConstants.CENTER);
        lblFotoPreview.setForeground(Color.WHITE);

        // --- LOGIN FIELDS ---
        txtUser = new JTextField();
        txtPass = new JPasswordField();
        
        btnAction = new JButton();
        Style.styleButton(btnAction);

        btnSwitch = new JButton();
        btnSwitch.setContentAreaFilled(false);
        btnSwitch.setBorderPainted(false);
        btnSwitch.setForeground(Color.WHITE);

        // --- LAYOUTING ---
        int y = 0;
        gbc.gridx = 0; 
        
        gbc.gridy = y++; panel.add(lblTitle, gbc);
        
        gbc.gridy = y++; panel.add(txtNim, gbc);
        gbc.gridy = y++; panel.add(txtNama, gbc);
        gbc.gridy = y++; panel.add(cmbKelas, gbc);
        gbc.gridy = y++; panel.add(cmbDivisi, gbc);
        gbc.gridy = y++; panel.add(cmbJabatan, gbc);
        gbc.gridy = y++; panel.add(btnPilihFoto, gbc);
        gbc.gridy = y++; panel.add(lblFotoPreview, gbc);

        txtUser.setBorder(BorderFactory.createTitledBorder("Username"));
        txtPass.setBorder(BorderFactory.createTitledBorder("Password"));
        
        gbc.gridy = y++; panel.add(txtUser, gbc);
        gbc.gridy = y++; panel.add(txtPass, gbc);
        gbc.gridy = y++; panel.add(btnAction, gbc);
        gbc.gridy = y++; panel.add(btnSwitch, gbc); // Tombol Daftar

        add(new JScrollPane(panel));
        
        // --- LOGIKA KHUSUS KETUA ---
        // Jika yang login adalah KETUA, Sembunyikan tombol daftar!
        if (role.equalsIgnoreCase("ketua")) {
            btnSwitch.setVisible(false);
        }

        refreshMode(); 

        // --- EVENTS ---
        btnSwitch.addActionListener(e -> {
            isLoginMode = !isLoginMode;
            refreshMode();
        });

        btnPilihFoto.addActionListener(e -> uploadFoto());
        btnAction.addActionListener(e -> processAction());
    }

    private void refreshMode() {
        boolean isRegister = !isLoginMode;

        txtNim.setVisible(isRegister);
        txtNama.setVisible(isRegister);
        btnPilihFoto.setVisible(isRegister);
        lblFotoPreview.setVisible(isRegister);

        cmbKelas.setVisible(false);
        cmbDivisi.setVisible(false);
        cmbJabatan.setVisible(false);

        if (isRegister) {
            txtNim.setBorder(BorderFactory.createTitledBorder("NIM"));
            txtNama.setBorder(BorderFactory.createTitledBorder("Nama Lengkap"));

            if (role.equalsIgnoreCase("anggota")) {
                cmbKelas.setVisible(true);
                cmbDivisi.setVisible(true);
                cmbKelas.setBorder(BorderFactory.createTitledBorder("Pilih Kelas"));
                cmbDivisi.setBorder(BorderFactory.createTitledBorder("Pilih Divisi"));
            } 
            else if (role.equalsIgnoreCase("pengurus")) {
                cmbJabatan.setVisible(true);
                cmbJabatan.setBorder(BorderFactory.createTitledBorder("Pilih Jabatan / Divisi"));
            }
        }

        lblTitle.setText(isLoginMode ? "LOGIN " + role.toUpperCase() : "REGISTRASI " + role.toUpperCase());
        btnAction.setText(isLoginMode ? "LOGIN" : "DAFTAR SEKARANG");
        btnSwitch.setText(isLoginMode ? "Belum punya akun? Daftar" : "Sudah punya akun? Login");
        
        revalidate();
        repaint();
    }

    private void processAction() {
        AnggotaDAO dao = new AnggotaDAO();

        if (isLoginMode) {
            Anggota a = dao.login(txtUser.getText(), new String(txtPass.getPassword()));
            if (a != null) {
                if (a.getRole().equalsIgnoreCase(role)) {
                    dispose(); 
                    if (role.equalsIgnoreCase("ketua") || role.equalsIgnoreCase("pengurus")) {
                        new AdminDashboard(a).setVisible(true);
                    } else {
                        new MemberDashboard(a).setVisible(true);
                    }
                } else {
                    JOptionPane.showMessageDialog(this, "Role tidak cocok! Silakan login di menu " + a.getRole());
                }
            } else {
                JOptionPane.showMessageDialog(this, "Login Gagal!");
            }
        } else {
            // Validasi: KETUA TIDAK BOLEH DAFTAR
            if (role.equalsIgnoreCase("ketua")) {
                JOptionPane.showMessageDialog(this, "Akses Ditolak: Pendaftaran Ketua Ditutup.");
                return;
            }

            if (txtNim.getText().isEmpty() || txtNama.getText().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Data wajib diisi!");
                return;
            }
            Anggota a = new Anggota();
            a.setNim(txtNim.getText());
            a.setNama(txtNama.getText());
            a.setUsername(txtUser.getText());
            a.setPassword(new String(txtPass.getPassword()));
            a.setFotoPath(selectedPhotoPath);
            a.setRole(role);

            if (role.equalsIgnoreCase("anggota")) {
                a.setKelas(cmbKelas.getSelectedItem().toString());
                a.setDivisi(cmbDivisi.getSelectedItem().toString());
            } else if (role.equalsIgnoreCase("pengurus")) {
                a.setJabatan(cmbJabatan.getSelectedItem().toString());
            }
            dao.register(a);
            JOptionPane.showMessageDialog(this, "Registrasi Berhasil! Silakan Login.");
            isLoginMode = true; refreshMode();
        }
    }

    private void uploadFoto() {
        JFileChooser fc = new JFileChooser();
        if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fc.getSelectedFile();
                String newName = System.currentTimeMillis() + "_" + file.getName();
                File dest = new File("uploads/" + newName);
                if(!dest.getParentFile().exists()) dest.getParentFile().mkdir();
                Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                selectedPhotoPath = dest.getPath();
                lblFotoPreview.setText(file.getName());
            } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}