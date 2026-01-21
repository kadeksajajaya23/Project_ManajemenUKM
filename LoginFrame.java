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

    
}
