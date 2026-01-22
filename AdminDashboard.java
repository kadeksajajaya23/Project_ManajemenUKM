import java.awt.*;
import java.util.List;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

public class AdminDashboard extends JFrame {
    
    // -- KOMPONEN UTAMA (CardLayout) --
    CardLayout cardLayout;
    JPanel mainContainer; 
    AnggotaDAO dao;
    Anggota adminLogin; 
    
    // -- KOMPONEN TAMPILAN DATA --
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
        // SISTEM KARTU (MENU vs DATA)
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);

        // 1. Buat Panel Kartu 1: MENU UTAMA
        JPanel pnlMenu = createMenuPanel();
        
        // 2. Buat Panel Kartu 2: TABEL DATA
        JPanel pnlData = createDataPanel();

        // Masukkan keduanya ke Container
        mainContainer.add(pnlMenu, "MENU");
        mainContainer.add(pnlData, "DATA");

        add(mainContainer);
        
        // Tampilkan MENU terlebih dahulu
        cardLayout.show(mainContainer, "MENU");
    }

}