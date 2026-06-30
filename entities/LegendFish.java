package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class LegendFish extends Fish {
    public LegendFish()
    {
        super("Naga Emas", "Legend");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/emas.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar emas.png");
        }
    }
}
