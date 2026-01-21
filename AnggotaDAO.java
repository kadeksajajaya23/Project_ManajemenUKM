import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    private String generateNomorAnggota(String kelas) {
        String newID = "MM-" + kelas + "-001"; 
        String sql = "SELECT id FROM anggota ORDER BY id DESC LIMIT 1";
        try (Connection conn = Koneksi.getConnection(); Statement st = conn.createStatement(); ResultSet rs = st.executeQuery(sql)) {
            if (rs.next()) {
                int lastId = rs.getInt("id");
                newID = "MM-" + kelas + "-" + String.format("%03d", (lastId + 1));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return newID;
    }

    public void register(Anggota a) {
        String noAnggota = generateNomorAnggota(a.getKelas());
        String sql = "INSERT INTO anggota (nomor_anggota, nim, nama, kelas, divisi, username, password, foto_path, role) VALUES (?,?,?,?,?,?,?,?,?)";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noAnggota);
            ps.setString(2, a.getNim());
            ps.setString(3, a.getNama());
            ps.setString(4, a.getKelas());
            ps.setString(5, a.getDivisi());
            ps.setString(6, a.getUsername());
            ps.setString(7, a.getPassword());
            ps.setString(8, a.getFotoPath());
            ps.setString(9, "Anggota");
            ps.executeUpdate();
            System.out.println("Berhasil Register!");
        } catch (SQLException e) { 
            System.out.println("GAGAL REGISTER: " + e.getMessage());
            e.printStackTrace(); 
        }
    }

    
}