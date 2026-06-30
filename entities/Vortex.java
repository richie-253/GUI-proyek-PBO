package entities;

import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class Vortex {
    public int x, y;
    public int width, height;
    public BufferedImage image;
    public boolean isActive = true;

    // Constructor diganti agar hanya menerima nama file saja
    public Vortex(int x, int y, String fileName) {
        this.x = x;
        this.y = y;
        this.width = 75;  // Ukuran disesuaikan agar pas di bawah dermaga
        this.height = 75;
        
        // --- LOGIC DETEKSI PATH OTOMATIS ---
        File file = new File("assets/" + fileName); // Mencari jika terminal di root project
        if (!file.exists()) {
            file = new File("../assets/" + fileName); // Mencari jika terminal di dalam folder src/
        }

        try {
            if (file.exists()) {
                this.image = ImageIO.read(file);
                System.out.println("STATUS: Berhasil memuat " + fileName + " dari " + file.getPath());
            } else {
                System.out.println("STATUS ERROR: File " + fileName + " TIDAK DITEMUKAN di assets/ maupun ../assets/!");
                System.out.println("Pastikan file tersebut sudah ada di folder assets kamu.");
            }
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal membaca file gambar " + fileName);
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2) {
        if (isActive && image != null) {
            g2.drawImage(image, x, y, width, height, null);
        }
    }

    public boolean isNear(Boat boat) {
        // Membuat area kotak interaksi di sekeliling portal
        Rectangle interactionZone = new Rectangle(x - 15, y - 15, width + 30, height + 30);
        
        // 🔥 PERBAIKAN: Ganti 'b.x' jadi 'boat.getX()' dan 'b.y' jadi 'boat.getY()'
        return interactionZone.contains(boat.getX() + 50, boat.getY() + 30); 
    }
}