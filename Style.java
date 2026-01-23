import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;

public class Style {
    // --- PALET WARNA MODERN (DARK & COLORFUL) ---
    public static final Color COLOR_BG = new Color(25, 28, 36);       
    public static final Color COLOR_PANEL = new Color(35, 39, 50);    
    
    // WARNA UTAMA
    public static final Color COLOR_ACCENT = new Color(67, 97, 238);  // BLUE (Default)
    
    // VARIASI WARNA DASHBOARD (Modern Flat Colors)
    public static final Color COLOR_PURPLE = new Color(114, 9, 183);  // Ungu Deep
    public static final Color COLOR_TEAL   = new Color(0, 184, 148);  // Hijau Tosca/Teal
    public static final Color COLOR_ORANGE = new Color(255, 118, 117); // Salmon/Oranye Soft
    public static final Color COLOR_GREEN  = new Color(85, 239, 196); // Mint Green
    
    public static final Color COLOR_TEXT_MAIN = new Color(255, 255, 255); 
    public static final Color COLOR_TEXT_DIM = new Color(160, 160, 160);
    public static final Color COLOR_INPUT_BG = new Color(45, 49, 60);

    public static final Color COLOR_PRIMARY = COLOR_ACCENT; 
    public static final Color COLOR_TEXT = COLOR_TEXT_MAIN; 

    // FONTS
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 26);
    public static final Font FONT_SUBHEADER = new Font("Segoe UI", Font.BOLD, 18);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);
    public static final Font FONT_BOLD = new Font("Segoe UI", Font.BOLD, 14);

    public static void styleButton(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.WHITE);
        btn.setFont(FONT_BOLD);
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }
    
    public static void styleButtonDanger(JButton btn) {
        styleButton(btn);
        btn.setBackground(new Color(214, 48, 49)); // Merah Modern
    }
    
    public static void stylePanel(JComponent comp) {
        comp.setBackground(COLOR_BG);
    }

    public static void styleTable(JTable table) {
        table.setBackground(new Color(45, 49, 60));
        table.setForeground(Color.WHITE);
        table.setGridColor(new Color(70, 75, 90));
        table.setSelectionBackground(COLOR_ACCENT);
        table.setSelectionForeground(Color.WHITE);
        table.setRowHeight(30);
        table.setFont(FONT_NORMAL);
        
        JTableHeader header = table.getTableHeader();
        header.setBackground(new Color(30, 33, 42));
        header.setForeground(COLOR_ACCENT);
        header.setFont(FONT_BOLD);
        
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(javax.swing.JLabel.CENTER);
        table.setDefaultRenderer(Object.class, centerRenderer);
    }
}