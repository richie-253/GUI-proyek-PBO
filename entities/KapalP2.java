package entities;

import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class KapalP2 extends Boat {
    private Random rand = new Random();

    public KapalP2(int x, int y) {
        super(x, y); // Jalankan constructor induk dulu
        try {
            // 🔥 Ganti 4 arah gambarnya khusus untuk Kapal Level 2
            this.setImgUp(ImageIO.read(new File("../assets/kapal2_up.png")));
            this.setImgDown(ImageIO.read(new File("../assets/kapal2_down.png")));
            this.setImgLeft(ImageIO.read(new File("../assets/kapal2_left.png")));
            this.setImgRight(ImageIO.read(new File("../assets/kapal2_right.png")));
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal memuat aset gambar Kapal P2!");
        }
    }

    @Override
    public Fish gachaIkan() {
        int roll = rand.nextInt(100);
        if (roll < 30) {
            return new CommonFish();  // 30%
        } else if (roll < 70) {
            return new RareFish();    // 40%
        } else if (roll < 85) {
            return new SpecialFish(); // 15%
        } else if(roll<95){
            return new EpicFish(); // 10%
        }
        else {
            return new LegendFish();    // 5%
        }
    }
}