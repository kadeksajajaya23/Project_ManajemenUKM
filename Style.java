import java.awt.Color;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JComponent;

public class Style {
    public static final Color COLOR_BG = new Color(30, 30, 30);
    public static final Color COLOR_ACCENT = new Color(212, 175, 55); 
    public static final Color COLOR_TEXT = new Color(245, 245, 245);
    
    public static final Font FONT_HEADER = new Font("Segoe UI", Font.BOLD, 24);
    public static final Font FONT_NORMAL = new Font("Segoe UI", Font.PLAIN, 14);

    public static void styleButton(JButton btn) {
        btn.setBackground(COLOR_ACCENT);
        btn.setForeground(Color.BLACK);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setFocusPainted(false);
        btn.setBorderPainted(false);
    }
    
    public static void stylePanel(JComponent comp) {
        comp.setBackground(COLOR_BG);
    }
}