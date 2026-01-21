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


    public Anggota login(String user, String pass) {
        System.out.println("Sedang mencoba login...");
        System.out.println("Username input: " + user);
        System.out.println("Password input: " + pass);

        Anggota a = null;
        String sql = "SELECT * FROM anggota WHERE username=? AND password=?";
        
        try (Connection conn = Koneksi.getConnection()) {
            if(conn == null) {
                System.out.println("KONEKSI DATABASE GAGAL! Cek file Koneksi.java atau XAMPP.");
                return null;
            }

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user); 
            ps.setString(2, pass);
            
            ResultSet rs = ps.executeQuery();
            
            if (rs.next()) {
                System.out.println("User DITEMUKAN di database!");
                a = new Anggota();
                a.setId(rs.getInt("id"));
                a.setNomorAnggota(rs.getString("nomor_anggota"));
                a.setNim(rs.getString("nim"));
                a.setNama(rs.getString("nama"));
                a.setDivisi(rs.getString("divisi"));
                a.setFotoPath(rs.getString("foto_path"));
                a.setRole(rs.getString("role"));
            } else {
                System.out.println("User TIDAK DITEMUKAN. Username/Password salah.");
            }
        } catch (SQLException e) { 
            System.out.println("ERROR SQL: " + e.getMessage());
            e.printStackTrace(); 
        }
        return a;
    }

    public void uploadPortofolio(int idAnggota, String judul, String filePath) {
        String sql = "INSERT INTO portofolio (anggota_id, judul_karya, file_path) VALUES (?,?,?)";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAnggota); ps.setString(2, judul); ps.setString(3, filePath); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Anggota> cariAnggota(String keyword) {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? OR nim LIKE ? OR divisi LIKE ?";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key); ps.setString(2, key); ps.setString(3, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Anggota a = new Anggota();
                a.setId(rs.getInt("id")); a.setNomorAnggota(rs.getString("nomor_anggota"));
                a.setNim(rs.getString("nim")); a.setNama(rs.getString("nama"));
                a.setDivisi(rs.getString("divisi")); a.setRole(rs.getString("role"));
                list.add(a);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM anggota WHERE id=?";
        try (Connection conn = Koneksi.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}