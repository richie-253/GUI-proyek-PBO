package game_menu;

import entities.Inventory;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class InventoryMenu {
    private BufferedImage inventoryBg;

    public InventoryMenu()
    {
        File file = new File("../assets/inventory.png");

        try {
            if (file.exists())
            {
                this.inventoryBg = ImageIO.read(file);
                System.out.println("STATUS: Berhasil memuat gambar inventory dari " + file.getPath());
            }
            else
            {
                System.out.println("STATUS ERROR: File inventory.png TIDAK DITEMUKAN!");
            }
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal membaca file gambar inventory");
            e.printStackTrace();
        }
    }

    public void draw(Graphics2D g2, int w, int h, Inventory globalInventory) {
        // 1. Gambar Background UI yang di-load tadi
        if (inventoryBg != null) {
            g2.drawImage(inventoryBg, 0, 0, w, h, null);
        }

        // 2. Set font dan warna angka (Hijau Neon)
        g2.setColor(new Color(57, 255, 20)); 
        g2.setFont(new Font("Arial", Font.BOLD, 22));

        // 3. Ambil data jumlah ikan dari objek inventory global
        int commonCount    = globalInventory.getFishCountByRarity("Common");
        int rareCount      = globalInventory.getFishCountByRarity("Rare");
        int specialCount   = globalInventory.getFishCountByRarity("Special");
        int epicCount      = globalInventory.getFishCountByRarity("Epic");
        int legendCount    = globalInventory.getFishCountByRarity("Legend");
        int collectorCount = globalInventory.getFishCountByRarity("Collector");

        // 4. Gambar angkanya di kotak hitam masing-masing slot
        // --- BARIS ATAS (Slot 1, 2, 3) ---
        g2.drawString(String.valueOf(commonCount), 330, 472);   // Slot 1: Common
        g2.drawString(String.valueOf(rareCount), 850, 472);     // Slot 2: Rare
        g2.drawString(String.valueOf(specialCount), 1355, 472);  // Slot 3: Special

        // --- BARIS BAWAH (Slot 4, 5, 6) ---
        g2.drawString(String.valueOf(epicCount), 330, 843);      // Slot 4: Epic
        g2.drawString(String.valueOf(legendCount), 850, 843);    // Slot 5: Legend
        g2.drawString(String.valueOf(collectorCount), 1355, 843     ); // Slot 6: Collector
    }

}
