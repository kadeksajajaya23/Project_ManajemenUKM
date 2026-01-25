import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Koneksi {
    public static Connection getConnection() {
        Connection conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); 
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