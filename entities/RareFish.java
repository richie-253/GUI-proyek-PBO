package entities;

import java.io.File;
import javax.imageio.ImageIO;

public class RareFish extends Fish{
    public RareFish()
    {
        super("Tuna", "Rare");
        try {
            this.setFisImage(ImageIO.read(new File("../assets/tuna.png")));
        } catch (Exception e) {
            System.out.println("ERROR: Gagal memuat gambar tuna.png");
        }
    }
}
