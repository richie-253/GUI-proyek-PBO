package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class CollectorFish extends Fish{
    public CollectorFish()
    {
        super("Fosil Purba", "Collector");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/fosil.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar Fosil.png");
        }
    }
}
