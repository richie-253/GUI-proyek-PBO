package entities;

import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class KapalP3 extends Boat{
    private Random rand = new Random();
    public KapalP3(int x,int y)
    {
        super(x, y);
        try {
            // 🔥 Ganti 4 arah gambarnya khusus untuk Kapal Level 3
            this.setImgUp(ImageIO.read(new File("../assets/kapal3_up.png")));
            this.setImgDown(ImageIO.read(new File("../assets/kapal3_down.png")));
            this.setImgLeft(ImageIO.read(new File("../assets/kapal3_left.png")));
            this.setImgRight(ImageIO.read(new File("../assets/kapal3_right.png")));
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal memuat aset gambar Kapal P3!");
        }
    }

    @Override
    public Fish gachaIkan() {
        int roll = rand.nextInt(100);
        if (roll < 20) {
            return new CommonFish();  // 20%
        } else if (roll < 40) {
            return new RareFish();    // 20%
        } else if (roll < 70) {
            return new SpecialFish(); // 30%
        } else if(roll<85){
            return new EpicFish(); // 15%
        } else if(roll < 93){
            return new LegendFish();// 8%
        }else {
            return new CollectorFish(); // 7%
        }
    }
}
