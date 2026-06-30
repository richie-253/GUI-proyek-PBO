package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class SpecialFish extends Fish{
    public SpecialFish()
    {
        super("Ikan Badut", "Special");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/badut.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar badut.png");
        }
    }
}
