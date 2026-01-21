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

   
}