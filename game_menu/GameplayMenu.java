package game_menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GameplayMenu {
    private BufferedImage bgImage;

    public GameplayMenu() {
        try {
            bgImage = ImageIO.read(new File("../assets/menu_main.png"));
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gambar menu_main.png tidak ditemukan!");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, int screenWidth, int screenHeight) {
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, screenWidth, screenHeight, null);
        }
        // Pointer penunjuk warna kuning "v" resmi dihapus bro!
    }
}