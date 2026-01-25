import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AnggotaDAO {

    // GENERATE NOMOR NOMOR ANGGOTA OTOMATIS BERDASARKAN KELAS
    private String generateNomorAnggota(String kelas) {
        if (kelas == null || kelas.isEmpty()) return null;
        String newID = "MM-" + kelas + "-001";
        String sql = "SELECT nomor_anggota FROM anggota WHERE kelas=? ORDER BY id DESC LIMIT 1";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, kelas);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                String last = rs.getString("nomor_anggota");
                if (last != null && last.contains("-")) {
                    int nomor = Integer.parseInt(last.substring(last.lastIndexOf("-") + 1)) + 1;
                    newID = "MM-" + kelas + "-" + String.format("%03d", nomor);
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return newID;
    }

    // REGISTER 
    public void register(Anggota a) {
        String noAnggota = null;
        String initialStatus = "Active"; 
        
        if ("anggota".equalsIgnoreCase(a.getRole())) {
            noAnggota = generateNomorAnggota(a.getKelas());
            initialStatus = "Active";
        } else if ("pengurus".equalsIgnoreCase(a.getRole())) {
            initialStatus = "Pending"; 
        }

        String sql = "INSERT INTO anggota (nomor_anggota, nim, nama, kelas, divisi, jabatan, username, password, foto_path, role, status) VALUES (?,?,?,?,?,?,?,?,?,?,?)";
        
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, noAnggota); ps.setString(2, a.getNim()); ps.setString(3, a.getNama());
            if ("anggota".equalsIgnoreCase(a.getRole())) {
                ps.setString(4, a.getKelas()); ps.setString(5, a.getDivisi()); ps.setNull(6, Types.VARCHAR);
            } else if ("pengurus".equalsIgnoreCase(a.getRole())) {
                ps.setNull(4, Types.VARCHAR); ps.setNull(5, Types.VARCHAR); ps.setString(6, a.getJabatan());
            } else { 
                ps.setNull(4, Types.VARCHAR); ps.setNull(5, Types.VARCHAR); ps.setNull(6, Types.VARCHAR);
            }
            ps.setString(7, a.getUsername()); ps.setString(8, a.getPassword()); ps.setString(9, a.getFotoPath());
            ps.setString(10, a.getRole()); ps.setString(11, initialStatus);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // LOGIN 
    public Anggota login(String user, String pass) {
        Anggota a = null;
        String sql = "SELECT * FROM anggota WHERE username=? AND password=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, user); ps.setString(2, pass);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) a = mapResultSetToAnggota(rs);
        } catch (SQLException e) { e.printStackTrace(); }
        return a;
    }

    // APPROVE 
    public void approvePengurus(int id) {
        String sql = "UPDATE anggota SET status='Active' WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    // UPDATE DATA 
    public void update(Anggota a) {
        String sql = "UPDATE anggota SET nim=?, nama=?, kelas=?, divisi=?, jabatan=?, username=?, password=?, foto_path=? WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, a.getNim());
            ps.setString(2, a.getNama());
            
            if(a.getKelas() != null) ps.setString(3, a.getKelas()); else ps.setNull(3, Types.VARCHAR);
            if(a.getDivisi() != null) ps.setString(4, a.getDivisi()); else ps.setNull(4, Types.VARCHAR);
            if(a.getJabatan() != null) ps.setString(5, a.getJabatan()); else ps.setNull(5, Types.VARCHAR);
            
            ps.setString(6, a.getUsername());
            ps.setString(7, a.getPassword());
            ps.setString(8, a.getFotoPath()); //
            ps.setInt(9, a.getId());

            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Anggota> getAnggotaByRole(String role) {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE role=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, role);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSetToAnggota(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public List<Anggota> cariAnggota(String keyword) {
        List<Anggota> list = new ArrayList<>();
        String sql = "SELECT * FROM anggota WHERE nama LIKE ? OR nim LIKE ?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            String key = "%" + keyword + "%";
            ps.setString(1, key); ps.setString(2, key);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(mapResultSetToAnggota(rs));
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    public void delete(int id) {
        String sql = "DELETE FROM anggota WHERE id=?";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id); ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public void uploadPortofolio(int idAnggota, String judul, String filePath) {
        String sql = "INSERT INTO portofolio (anggota_id, judul_karya, file_path) VALUES (?,?,?)";
        try (Connection conn = Koneksi.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, idAnggota); ps.setString(2, judul); ps.setString(3, filePath);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }

    public List<Portofolio> getAllPortofolio() {
        List<Portofolio> list = new ArrayList<>();
        String sql = "SELECT p.id, p.judul_karya, p.file_path, a.nama " +
                     "FROM portofolio p JOIN anggota a ON p.anggota_id = a.id " +
                     "ORDER BY p.id DESC";
        try (Connection conn = Koneksi.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Portofolio p = new Portofolio();
                p.setId(rs.getInt("id")); p.setJudul(rs.getString("judul_karya"));
                p.setFilePath(rs.getString("file_path")); p.setNamaAnggota(rs.getString("nama"));
                list.add(p);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return list;
    }

    private Anggota mapResultSetToAnggota(ResultSet rs) throws SQLException {
        Anggota a = new Anggota();
        a.setId(rs.getInt("id")); a.setNomorAnggota(rs.getString("nomor_anggota"));
        a.setNim(rs.getString("nim")); a.setNama(rs.getString("nama"));
        a.setKelas(rs.getString("kelas")); a.setDivisi(rs.getString("divisi"));
        a.setJabatan(rs.getString("jabatan")); a.setUsername(rs.getString("username"));
        a.setPassword(rs.getString("password")); a.setFotoPath(rs.getString("foto_path"));
        a.setRole(rs.getString("role")); a.setStatus(rs.getString("status")); 
        return a;
    }
}