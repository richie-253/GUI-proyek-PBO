package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class CommonFish extends Fish{
    public CommonFish()
    {
        super("Sarden", "Common");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/sarden.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar sarden.png");
        }
    }
}
