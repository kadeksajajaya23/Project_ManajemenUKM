import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;

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

    // ===== CONSTRUCTOR ROLE =====
    public LoginFrame(String role) {
        this.role = role;
        setTitle("UKM Multimedia System");
        setSize(450, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }

    // ===== UI =====
    private void initUI() {

        JPanel panel = new JPanel(new GridBagLayout());
        Style.stylePanel(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        lblTitle = new JLabel("", SwingConstants.CENTER);
        lblTitle.setFont(Style.FONT_HEADER);
        lblTitle.setForeground(Style.COLOR_ACCENT);

        // === REGISTER FIELD ===
        txtNim = new JTextField();
        txtNama = new JTextField();

        cmbKelas = new JComboBox<>(new String[]{"A", "B1", "B2"});
        cmbDivisi = new JComboBox<>(new String[]{
                "3D Modelling",
                "Graphic Design",
                "Audio & Video Editing"
        });

        cmbJabatan = new JComboBox<>(new String[]{
                "Sekretaris",
                "Bendahara",
                "Koordinator",
                "Anggota Inti"
        });
        cmbJabatan.setBorder(BorderFactory.createTitledBorder("Pilih Jabatan"));

        btnPilihFoto = new JButton("Upload Foto Profil");
        Style.styleButton(btnPilihFoto);
        btnPilihFoto.setBackground(Color.GRAY);

        lblFotoPreview = new JLabel("", SwingConstants.CENTER);
        lblFotoPreview.setForeground(Color.WHITE);

        // === LOGIN FIELD ===
        txtUser = new JTextField();
        txtPass = new JPasswordField();

        btnAction = new JButton();
        Style.styleButton(btnAction);

        btnSwitch = new JButton();
        btnSwitch.setContentAreaFilled(false);
        btnSwitch.setBorderPainted(false);
        btnSwitch.setForeground(Color.WHITE);

        // === LAYOUT ===
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
        gbc.gridy = y++; panel.add(btnSwitch, gbc);

        add(new JScrollPane(panel));

        refreshMode();

        // === EVENTS ===
        btnSwitch.addActionListener(e -> {
            isLoginMode = !isLoginMode;
            refreshMode();
        });

        btnPilihFoto.addActionListener(e -> uploadFoto());

        btnAction.addActionListener(e -> processAction());
    }

    // ===== MODE SWITCH =====
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
            }
        }

        lblTitle.setText(isLoginMode
                ? "LOGIN " + role.toUpperCase()
                : "REGISTRASI " + role.toUpperCase());

        btnAction.setText(isLoginMode ? "LOGIN" : "DAFTAR SEKARANG");
        btnSwitch.setText(isLoginMode
                ? "Belum punya akun? Daftar"
                : "Sudah punya akun? Login");

        revalidate();
        repaint();
    }

    // ===== ACTION =====
    private void processAction() {

        AnggotaDAO dao = new AnggotaDAO();

        if (isLoginMode) {
            Anggota a = dao.login(
                    txtUser.getText(),
                    new String(txtPass.getPassword())
            );

           
        }
        else {
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
            }

            dao.register(a);

            JOptionPane.showMessageDialog(this,
                    "Registrasi " + role.toUpperCase() + " berhasil");

            isLoginMode = true;
            refreshMode();
        }
    }

    
}
