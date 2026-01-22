import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;

public class MemberDashboard extends JFrame {
    Anggota user;
    
    public MemberDashboard(Anggota user) {
        this.user = user;
        setTitle("MEMBER AREA - " + user.getNomorAnggota());
        setSize(600, 550);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }
    
    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        Style.stylePanel(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10,10,10,10);