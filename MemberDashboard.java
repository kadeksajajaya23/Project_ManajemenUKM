import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class MemberDashboard extends JFrame {
    
    CardLayout cardLayout;
    JPanel mainContainer;
    Anggota user;
    
    public MemberDashboard(Anggota user) {
        this.user = user;
        setTitle("MEMBER AREA - " + user.getNama());
        setSize(1100, 750);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        initUI();
    }
    
    private void initUI() {
        cardLayout = new CardLayout();
        mainContainer = new JPanel(cardLayout);
        mainContainer.setBackground(Style.COLOR_BG);
        
        mainContainer.add(createMenuPanel(), "MENU");
        mainContainer.add(createIDCardPage(), "IDCARD"); 
        mainContainer.add(createUploadPage(), "UPLOAD"); 
        
        add(mainContainer);
    }
    
    private JPanel createMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        Style.stylePanel(panel);
        
        JPanel headerPanel = new JPanel();
        headerPanel.setLayout(new BoxLayout(headerPanel, BoxLayout.Y_AXIS));
        headerPanel.setOpaque(false);
        headerPanel.setBorder(BorderFactory.createEmptyBorder(40, 0, 30, 0));

        JLabel lblLogo = new JLabel("", SwingConstants.CENTER);
        lblLogo.setAlignmentX(Component.CENTER_ALIGNMENT);
        File f = new File("logo_mm no txt.jpg");
        if (f.exists()) {
            try { ImageIcon icon = new ImageIcon(f.getAbsolutePath()); Image img = icon.getImage().getScaledInstance(100, 100, Image.SCALE_SMOOTH); lblLogo.setIcon(new ImageIcon(img)); } catch (Exception e) {}
        }

        JLabel lblWelcome = new JLabel("<html><center>Halo, <font color='#4361EE'>" + user.getNama() + "</font><br>Selamat Datang di Member Area</center></html>", SwingConstants.CENTER);
        lblWelcome.setFont(Style.FONT_HEADER); 
        lblWelcome.setForeground(Color.WHITE);
        lblWelcome.setAlignmentX(Component.CENTER_ALIGNMENT);

        headerPanel.add(lblLogo);
        headerPanel.add(Box.createVerticalStrut(15));
        headerPanel.add(lblWelcome);

        panel.add(headerPanel, BorderLayout.NORTH);
        
        JPanel grid = new JPanel(new GridBagLayout()); 
        grid.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints(); 
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.gridx = 0; gbc.gridy = 0;
        
        JButton btnID = createBigButton("Lihat ID Card", "Tampilkan Kartu Digital");
        btnID.setBackground(Style.COLOR_ACCENT); // Biru
        btnID.addActionListener(e -> cardLayout.show(mainContainer, "IDCARD"));
        grid.add(btnID, gbc);
        gbc.gridx++;

        JButton btnEdit = createBigButton("Ubah Data", "Edit Profil & Password");
        btnEdit.setBackground(Style.COLOR_TEAL); // Teal Hijau
        btnEdit.addActionListener(e -> {
            EditDialog dialog = new EditDialog(this, user);
            dialog.setVisible(true);
            if (dialog.isSaved()) { lblWelcome.setText("<html><center>Halo, <font color='#4361EE'>" + user.getNama() + "</font><br>Selamat Datang di Member Area</center></html>"); }
        });
        grid.add(btnEdit, gbc);
        gbc.gridx++;

        JButton btnUpload = createBigButton("Upload Karya", "Kirim Portofolio Terbaru");
        btnUpload.setBackground(Style.COLOR_PURPLE); // Ungu
        btnUpload.addActionListener(e -> cardLayout.show(mainContainer, "UPLOAD"));
        grid.add(btnUpload, gbc);
        
        panel.add(grid, BorderLayout.CENTER); 
        
        JButton btnLogout = new JButton("LOGOUT");
        Style.styleButtonDanger(btnLogout);
        btnLogout.setPreferredSize(new Dimension(200, 50));
        btnLogout.addActionListener(e -> { new MainMenuFrame().setVisible(true); dispose(); });
        
        JPanel footer = new JPanel(); footer.setOpaque(false); 
        footer.setBorder(new EmptyBorder(0,0,40,0));
        footer.add(btnLogout);
        panel.add(footer, BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel createIDCardPage() {
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(Style.COLOR_BG);
        JPanel header = createHeaderBack(); wrapper.add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridBagLayout()); center.setOpaque(false);
        JPanel idCard = new GradientCardPanel(); idCard.setLayout(new BoxLayout(idCard, BoxLayout.Y_AXIS));
        idCard.setPreferredSize(new Dimension(350, 520)); idCard.setBorder(new EmptyBorder(0, 0, 0, 0));
        populateIDCard(idCard); center.add(idCard);
        JScrollPane scroll = new JScrollPane(center); scroll.setBorder(null); scroll.getViewport().setBackground(Style.COLOR_BG); wrapper.add(scroll, BorderLayout.CENTER); return wrapper;
    }
    private JPanel createUploadPage() {
        JPanel wrapper = new JPanel(new BorderLayout()); wrapper.setBackground(Style.COLOR_BG);
        JPanel header = createHeaderBack(); wrapper.add(header, BorderLayout.NORTH);
        JPanel center = new JPanel(new GridBagLayout()); center.setOpaque(false);
        JPanel uploadPanel = createUploadPanel(); center.add(uploadPanel);
        JScrollPane scroll = new JScrollPane(center); scroll.setBorder(null); scroll.getViewport().setBackground(Style.COLOR_BG); wrapper.add(scroll, BorderLayout.CENTER); return wrapper;
    }
    private JPanel createHeaderBack() { JPanel header = new JPanel(new FlowLayout(FlowLayout.LEFT)); header.setOpaque(false); header.setBorder(new EmptyBorder(10,10,10,10)); JButton btnBack = new JButton("<< KEMBALI KE MENU"); Style.styleButton(btnBack); btnBack.setBackground(new Color(50,50,60)); btnBack.addActionListener(e -> cardLayout.show(mainContainer, "MENU")); header.add(btnBack); return header; }
    private void populateIDCard(JPanel idCard) {
        idCard.add(Box.createVerticalStrut(30));
        JLabel lblFoto = new JLabel(); lblFoto.setAlignmentX(Component.CENTER_ALIGNMENT); lblFoto.setPreferredSize(new Dimension(140, 140)); lblFoto.setMaximumSize(new Dimension(140, 140)); lblFoto.setBorder(BorderFactory.createLineBorder(Color.WHITE, 3)); 
        if (user.getFotoPath() != null) { try { ImageIcon icon = new ImageIcon(user.getFotoPath()); Image img = icon.getImage().getScaledInstance(140, 140, Image.SCALE_SMOOTH); lblFoto.setIcon(new ImageIcon(img)); } catch (Exception e) { lblFoto.setText("No Image"); lblFoto.setForeground(Color.WHITE); } } else { lblFoto.setText("No Image"); lblFoto.setForeground(Color.WHITE); lblFoto.setHorizontalAlignment(SwingConstants.CENTER); }
        idCard.add(lblFoto); idCard.add(Box.createVerticalStrut(20));
        JLabel lblNama = new JLabel(user.getNama().toUpperCase()); lblNama.setAlignmentX(Component.CENTER_ALIGNMENT); lblNama.setFont(new Font("Segoe UI", Font.BOLD, 22)); lblNama.setForeground(Color.WHITE); idCard.add(lblNama);
        JLabel lblRole = new JLabel("  " + user.getRole().toUpperCase() + "  "); lblRole.setAlignmentX(Component.CENTER_ALIGNMENT); lblRole.setFont(new Font("Consolas", Font.BOLD, 14)); lblRole.setForeground(Style.COLOR_ACCENT); lblRole.setOpaque(true); lblRole.setBackground(new Color(20, 20, 30)); lblRole.setBorder(BorderFactory.createLineBorder(Style.COLOR_ACCENT, 1)); idCard.add(Box.createVerticalStrut(5)); idCard.add(lblRole);
        idCard.add(Box.createVerticalStrut(20));
        JSeparator sep = new JSeparator(); sep.setMaximumSize(new Dimension(280, 2)); sep.setForeground(new Color(100, 100, 100)); idCard.add(sep); idCard.add(Box.createVerticalStrut(20));
        JPanel detailPnl = new JPanel(new GridLayout(4, 1, 5, 8)); detailPnl.setOpaque(false); detailPnl.setMaximumSize(new Dimension(280, 150));
        detailPnl.add(createDetailRow("ID MEMBER", user.getNomorAnggota())); detailPnl.add(createDetailRow("NIM", user.getNim()));
        if (user.getRole().equalsIgnoreCase("pengurus")) { detailPnl.add(createDetailRow("JABATAN", user.getJabatan())); } else { detailPnl.add(createDetailRow("KELAS", user.getKelas())); detailPnl.add(createDetailRow("DIVISI", user.getDivisi())); }
        idCard.add(detailPnl); idCard.add(Box.createVerticalGlue());
        JLabel lblStatus = new JLabel("STATUS: " + user.getStatus().toUpperCase(), SwingConstants.CENTER); lblStatus.setAlignmentX(Component.CENTER_ALIGNMENT); lblStatus.setForeground(new Color(50, 255, 100)); lblStatus.setFont(new Font("Segoe UI", Font.BOLD, 12)); idCard.add(lblStatus); idCard.add(Box.createVerticalStrut(20));
    }
    private JPanel createDetailRow(String label, String value) { 
        JPanel p = new JPanel(new BorderLayout()); p.setOpaque(false); 
        JLabel l1 = new JLabel(label); l1.setFont(new Font("Segoe UI", Font.PLAIN, 10)); l1.setForeground(Color.LIGHT_GRAY); 
        JLabel l2 = new JLabel(value == null ? "-" : value); l2.setFont(new Font("Segoe UI", Font.BOLD, 14)); l2.setForeground(Color.WHITE); p.add(l1, BorderLayout.NORTH); p.add(l2, BorderLayout.CENTER); return p; }
    
    private JPanel createUploadPanel() { 
        JPanel p = new JPanel(new GridBagLayout()); p.setOpaque(false); p.setBorder(BorderFactory.createTitledBorder(new LineBorder(new Color(80, 80, 90)), " Upload Karya ", 0, 0, new Font("Segoe UI", Font.PLAIN, 12), Color.LIGHT_GRAY)); GridBagConstraints g = new GridBagConstraints(); g.insets = new Insets(10, 10, 10, 10); g.gridx = 0; g.gridy = 0; g.fill = GridBagConstraints.HORIZONTAL; 
        JLabel lblInfo = new JLabel("<html><center>Simpan karya terbaikmu<br>ke dalam database.</center></html>", SwingConstants.CENTER); lblInfo.setForeground(Color.WHITE); lblInfo.setFont(new Font("Segoe UI", Font.PLAIN, 12)); p.add(lblInfo, g); g.gridy++; p.add(new JLabel("<html><font color='#aaaaaa'>Judul Karya:</font></html>"), g); g.gridy++; 
        JTextField txtJudul = new JTextField(20); txtJudul.setBackground(new Color(45, 49, 60)); txtJudul.setForeground(Color.WHITE); txtJudul.setCaretColor(Style.COLOR_ACCENT); txtJudul.setBorder(BorderFactory.createMatteBorder(0,0,2,0, Style.COLOR_ACCENT)); p.add(txtJudul, g); g.gridy++; 
        JButton btnUp = new JButton("PILIH FILE & UPLOAD"); Style.styleButton(btnUp); p.add(btnUp, g); btnUp.addActionListener(e -> { if (txtJudul.getText().isEmpty()) { JOptionPane.showMessageDialog(this, "Isi Judul!"); return; } JFileChooser fc = new JFileChooser(); if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { try { File file = fc.getSelectedFile(); File dest = new File("uploads/PORTO_" + System.currentTimeMillis() + "_" + file.getName()); dest.getParentFile().mkdirs(); Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING); new AnggotaDAO().uploadPortofolio(user.getId(), txtJudul.getText(), dest.getPath()); 
        JOptionPane.showMessageDialog(this, "Upload Sukses!"); txtJudul.setText(""); } catch (Exception ex) { ex.printStackTrace(); } } }); return p; }
    
    private JButton createBigButton(String title, String subtitle) { 
        JButton btn = new JButton("<html><center><h2>" + title + "</h2><p>" + subtitle + "</p></center></html>"); btn.setPreferredSize(new Dimension(300, 150)); Style.styleButton(btn); return btn; }
    
        class GradientCardPanel extends JPanel { @Override protected void paintComponent(Graphics g) { super.paintComponent(g); Graphics2D g2 = (Graphics2D) g; g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON); int w = getWidth(); int h = getHeight(); g2.setColor(new Color(35, 39, 50)); g2.fill(new RoundRectangle2D.Double(0, 0, w, h, 30, 30)); GradientPaint gp = new GradientPaint(0, 0, Style.COLOR_ACCENT, w, 0, new Color(114, 9, 183)); g2.setPaint(gp); g2.fill(new RoundRectangle2D.Double(0, 0, w, 80, 30, 30)); g2.fillRect(0, 40, w, 40); g2.setColor(new Color(60, 60, 70)); g2.draw(new RoundRectangle2D.Double(0, 0, w-1, h-1, 30, 30)); g2.setColor(Color.WHITE); g2.setFont(new Font("Segoe UI", Font.BOLD, 16)); FontMetrics fm = g2.getFontMetrics(); String title = "UKM MULTIMEDIA"; int x = (w - fm.stringWidth(title)) / 2; g2.drawString(title, x, 30); g2.setFont(new Font("Segoe UI", Font.PLAIN, 10)); String sub = "Official Member Card"; fm = g2.getFontMetrics(); int x2 = (w - fm.stringWidth(sub)) / 2; g2.drawString(sub, x2, 45); } }
}