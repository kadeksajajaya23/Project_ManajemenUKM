import javax.swing.*;
import java.awt.*;

public class MainMenuFrame extends JFrame {

    public MainMenuFrame() {
        setTitle("Main Menu");
        setSize(900, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        JPanel sidebar = new JPanel(new GridLayout(3, 1, 15, 15));
        sidebar.setBackground(new Color(30, 45, 60));
        sidebar.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JButton btnKetua = menuButton("Login Ketua");
        JButton btnPengurus = menuButton("Login Pengurus");
        JButton btnAnggota = menuButton("Login Anggota");

        sidebar.add(btnKetua);
        sidebar.add(btnPengurus);
        sidebar.add(btnAnggota);

        JLabel centerText = new JLabel(
            "<html><center><h1>Welcome</h1><p>Select login role</p></center></html>",
            SwingConstants.CENTER
        );

        add(sidebar, BorderLayout.WEST);
        add(centerText, BorderLayout.CENTER);

        btnKetua.addActionListener(e -> openLogin("ketua"));
        btnPengurus.addActionListener(e -> openLogin("pengurus"));
        btnAnggota.addActionListener(e -> openLogin("anggota"));
    }

    private JButton menuButton(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("Segoe UI", Font.BOLD, 16));
        b.setBackground(new Color(52, 152, 219));
        b.setForeground(Color.WHITE);
        return b;
    }


}
