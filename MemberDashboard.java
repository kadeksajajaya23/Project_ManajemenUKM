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

                JLabel lblFoto = new JLabel();
        if (user.getFotoPath() != null) {
            ImageIcon icon = new ImageIcon(user.getFotoPath());
            Image img = icon.getImage().getScaledInstance(120, 120, Image.SCALE_SMOOTH);
            lblFoto.setIcon(new ImageIcon(img));
        } else {
            lblFoto.setText("[Tidak Ada Foto]"); lblFoto.setForeground(Color.WHITE);
        }
        
        JLabel lblNama = new JLabel(user.getNama());
        lblNama.setFont(Style.FONT_HEADER); lblNama.setForeground(Style.COLOR_ACCENT);
        JLabel lblInfo = new JLabel("No: " + user.getNomorAnggota() + " | Divisi: " + user.getDivisi());
        lblInfo.setForeground(Color.WHITE);

        // Upload Panel
        JPanel pnlUpload = new JPanel();
        pnlUpload.setBorder(BorderFactory.createTitledBorder("Upload Portofolio"));
        pnlUpload.setBackground(Style.COLOR_BG);
        JTextField txtJudul = new JTextField(15);
        JButton btnUp = new JButton("Upload File"); Style.styleButton(btnUp);
        pnlUpload.add(new JLabel("Judul: ")); pnlUpload.add(txtJudul); pnlUpload.add(btnUp);

        JButton btnLogout = new JButton("Logout");
        Style.styleButton(btnLogout); btnLogout.setBackground(Color.RED);

        gbc.gridx=0; gbc.gridy=0; panel.add(lblFoto, gbc);
        gbc.gridy++; panel.add(lblNama, gbc);
        gbc.gridy++; panel.add(lblInfo, gbc);
        gbc.gridy++; panel.add(pnlUpload, gbc);
        gbc.gridy++; panel.add(btnLogout, gbc);
        add(panel);

        // Events
        btnUp.addActionListener(e -> {
            if (txtJudul.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Isi Judul!"); return; }
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    String newName = "PORTO_" + System.currentTimeMillis() + "_" + file.getName();
                    File dest = new File("uploads/" + newName);
                    Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    new AnggotaDAO().uploadPortofolio(user.getId(), txtJudul.getText(), dest.getPath());
                    JOptionPane.showMessageDialog(this, "Berhasil Upload!");
                    txtJudul.setText("");
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        btnLogout.addActionListener(e -> {
            new MainMenuFrame().setVisible(true);
            dispose();
        });

    }
}