package entities;

import java.io.File;
import java.util.Random;
import javax.imageio.ImageIO;

public class KapalAwal extends Boat {
    private Random rand = new Random();

    public KapalAwal(int x, int y) {
        super(x, y); // Menjalankan blueprint posisi di class Boat
        try {
            // 🔥 Kode load kapal 1 sekarang resmi pindah ke sini!
            this.setImgUp(ImageIO.read(new File("../assets/kapal1_up.png")));
            this.setImgDown(ImageIO.read(new File("../assets/kapal1_down.png")));
            this.setImgLeft(ImageIO.read(new File("../assets/kapal1_left.png")));
            this.setImgRight(ImageIO.read(new File("../assets/kapal1_rigt.png"))); 
            System.out.println("STATUS: Aset gambar Kapal Awal (Level 1) berhasil dimuat!");
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal memuat aset gambar Kapal Awal!");
            e.printStackTrace();
        }
    }

    @Override
    public Fish gachaIkan() {
        int roll = rand.nextInt(100);
        if (roll < 70) {
            return new CommonFish(); // 70% dapat Sarden
        } else {
            return new RareFish();   // 30% dapat Tuna
        }
    }
}