import java.awt.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import javax.swing.*;

public class EditDialog extends JDialog {
    JTextField txtNim, txtNama, txtUser, txtPass;
    JTextField txtKelas, txtDivisi, txtJabatan;
    JButton btnSimpan, btnPilihFoto; // Tambah tombol foto
    JLabel lblFotoStatus;
    
    Anggota anggota;
    boolean isSaved = false;
    String newFotoPath = null; // Menyimpan path foto baru jika ada

    public EditDialog(Frame owner, Anggota a) {
        super(owner, "Edit Data: " + a.getNama(), true);
        this.anggota = a;
        this.newFotoPath = a.getFotoPath(); // Default path lama
        setSize(450, 650);
        setLocationRelativeTo(owner);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        Style.stylePanel(panel);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0; gbc.gridy = 0;

        Runnable nextRow = () -> { gbc.gridy++; };

        JLabel lblInfo = new JLabel("EDIT DATA (" + anggota.getRole().toUpperCase() + ")");
        lblInfo.setForeground(Style.COLOR_ACCENT);
        lblInfo.setFont(new Font("Segoe UI", Font.BOLD, 16));
        panel.add(lblInfo, gbc); nextRow.run();

        // --- FIELD GANTI FOTO (BARU) ---
        panel.add(newLabel("Foto Profil:"), gbc); nextRow.run();
        
        JPanel photoPanel = new JPanel(new BorderLayout(5, 0));
        photoPanel.setOpaque(false);
        
        btnPilihFoto = new JButton("Ganti Foto...");
        Style.styleButton(btnPilihFoto);
        btnPilihFoto.setBackground(Color.DARK_GRAY);
        btnPilihFoto.setPreferredSize(new Dimension(120, 30));
        
        lblFotoStatus = new JLabel((anggota.getFotoPath() != null ? "Ada Foto" : "Tidak Ada Foto"));
        lblFotoStatus.setForeground(Color.GRAY);
        
        photoPanel.add(btnPilihFoto, BorderLayout.WEST);
        photoPanel.add(lblFotoStatus, BorderLayout.CENTER);
        
        panel.add(photoPanel, gbc); nextRow.run();
        // -------------------------------

        panel.add(newLabel("NIM:"), gbc); nextRow.run();
        txtNim = new JTextField(anggota.getNim()); panel.add(txtNim, gbc); nextRow.run();

        panel.add(newLabel("Nama Lengkap:"), gbc); nextRow.run();
        txtNama = new JTextField(anggota.getNama()); panel.add(txtNama, gbc); nextRow.run();

        if (anggota.getRole().equalsIgnoreCase("anggota")) {
            panel.add(newLabel("Kelas (A/B1/B2):"), gbc); nextRow.run();
            txtKelas = new JTextField(anggota.getKelas()); panel.add(txtKelas, gbc); nextRow.run();
            panel.add(newLabel("Divisi:"), gbc); nextRow.run();
            txtDivisi = new JTextField(anggota.getDivisi()); panel.add(txtDivisi, gbc); nextRow.run();
        } else {
            panel.add(newLabel("Jabatan:"), gbc); nextRow.run();
            txtJabatan = new JTextField(anggota.getJabatan()); panel.add(txtJabatan, gbc); nextRow.run();
        }

        panel.add(newLabel("Username:"), gbc); nextRow.run();
        txtUser = new JTextField(anggota.getUsername()); panel.add(txtUser, gbc); nextRow.run();

        panel.add(newLabel("Password:"), gbc); nextRow.run();
        txtPass = new JTextField(anggota.getPassword()); panel.add(txtPass, gbc); nextRow.run();

        btnSimpan = new JButton("SIMPAN PERUBAHAN");
        Style.styleButton(btnSimpan);
        nextRow.run(); panel.add(btnSimpan, gbc);

        add(panel);

        // --- EVENT HANDLER ---
        
        // Logic Upload Foto Baru
        btnPilihFoto.addActionListener(e -> {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                try {
                    File file = fc.getSelectedFile();
                    String newName = "PROFILE_" + System.currentTimeMillis() + "_" + file.getName();
                    File dest = new File("uploads/" + newName);
                    
                    if(!dest.getParentFile().exists()) dest.getParentFile().mkdir();
                    Files.copy(file.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    
                    newFotoPath = dest.getPath(); // Simpan path baru sementara
                    lblFotoStatus.setText("Terpilih: " + file.getName());
                    lblFotoStatus.setForeground(Style.COLOR_ACCENT);
                } catch (Exception ex) { ex.printStackTrace(); }
            }
        });

        // Logic Simpan
        btnSimpan.addActionListener(e -> {
            anggota.setNim(txtNim.getText());
            anggota.setNama(txtNama.getText());
            anggota.setUsername(txtUser.getText());
            anggota.setPassword(txtPass.getText());
            anggota.setFotoPath(newFotoPath); // Update path foto di objek

            if (txtKelas != null) anggota.setKelas(txtKelas.getText());
            if (txtDivisi != null) anggota.setDivisi(txtDivisi.getText());
            if (txtJabatan != null) anggota.setJabatan(txtJabatan.getText());

            new AnggotaDAO().update(anggota); // Update ke Database
            JOptionPane.showMessageDialog(this, "Data Berhasil Diupdate!");
            isSaved = true;
            dispose();
        });
    }

    private JLabel newLabel(String text) {
        JLabel l = new JLabel(text); l.setForeground(Color.WHITE); return l;
    }

    public boolean isSaved() { return isSaved; }
}