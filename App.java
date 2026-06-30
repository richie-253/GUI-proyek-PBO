
import javax.swing.JFrame;
import menu_awal.GamePanel;


public class App {
    public static void main(String[] args) throws Exception {
        JFrame frame = new JFrame("Fish it");
        
        // frame menjadi full screen
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        GamePanel panel = new GamePanel();
        frame.add(panel);

        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        panel.requestFocusInWindow();

    }
}
