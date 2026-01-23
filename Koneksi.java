import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            // 1. PENTING: Paksa load driver MySQL
            // Ini akan memancing error yang lebih jelas jika library tidak ketemu
            Class.forName("com.mysql.cj.jdbc.Driver"); 

            // 2. Koneksi ke Database
            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ukm_multimedia", "root", "");
            
        } catch (ClassNotFoundException e) {
            System.err.println("ERROR: Driver MySQL tidak ditemukan!");
            System.err.println("Pastikan file .jar sudah dimasukkan ke Library.");
            e.printStackTrace();
        } catch (SQLException e) {
            System.err.println("ERROR: Gagal Konek ke Database!");
            e.printStackTrace();
        }
        return conn;
    }
}