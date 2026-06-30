package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class EpicFish extends Fish {
    public EpicFish()
    {
        super("Aura Purple", "Epic");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/purple.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar purple.png");
        }
    }
}
