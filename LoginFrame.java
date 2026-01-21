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
}