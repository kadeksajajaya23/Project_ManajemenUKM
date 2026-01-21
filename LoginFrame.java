import javax.swing.*;

public class LoginFrame extends JFrame {

    JLabel lblTitle;

    JTextField txtUser, txtNim, txtNama;
    JComboBox<String> cmbKelas, cmbDivisi, cmbJabatan;
    JPasswordField txtPass;
    JButton btnAction, btnSwitch, btnPilihFoto;
    JLabel lblFotoPreview;

    String role = "anggota";
    String selectedPhotoPath = null;
    boolean isLoginMode = true;

}