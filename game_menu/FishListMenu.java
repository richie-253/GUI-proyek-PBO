// FishList Menus

package game_menu;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class FishListMenu {
    private BufferedImage bgImage;

    public FishListMenu()
    {
        try {
            File file = new File("../assets/fish_list.png");
            if (file.exists())
            {
                bgImage = ImageIO.read(file);
                System.out.println("STATUS: FishListMenu berhasil memuat fish_list.png");
            }
            else 
            {
                System.out.println("STATUS ERROR: File fish_list.png tidak ditemukan di " + file.getAbsolutePath());
            }
        } catch (IOException e) {
            System.out.println("STATUS ERROR: Gagal membaca file fish_list.png");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, int width, int height) {
        if (bgImage != null) {
            g2.drawImage(bgImage, 0, 0, width, height, null);
        }
    }
}
