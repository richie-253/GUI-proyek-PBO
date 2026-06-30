package game_menu;

import entities.Fish;
import entities.Inventory;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class ShopMenu {
    private BufferedImage shopBg;
    private BufferedImage failBg;
    
    // State internal Toko
    private int selectedSlot = 1; // 1: Rare, 2: Special, 3: Epic, 4: Legend, 5: Collector
    private boolean showFailPopUp = false;

    public ShopMenu() {
        // --- AUTO PATH LOADING ASSETS ---
        try {
            File fShop = new File("assets/shop.png");
            if(!fShop.exists()) fShop = new File("../assets/shop.png");
            if(fShop.exists()) this.shopBg = ImageIO.read(fShop);

            File fFail = new File("assets/notif_gagal.png");
            if(!fFail.exists()) fFail = new File("../assets/notif_gagal.png");
            if(fFail.exists()) this.failBg = ImageIO.read(fFail);
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal memuat asset Shop!");
            e.printStackTrace();
        }
    }

    // Mengatur navigasi panah lewat keyboard
    public void navigate(int keyCode) {
        // Jika pop-up gagal sedang muncul, tekan tombol apa saja (Enter/Esc) untuk menutupnya
        if (showFailPopUp) {
            if (keyCode == java.awt.event.KeyEvent.VK_ENTER || keyCode == java.awt.event.KeyEvent.VK_ESCAPE) {
                showFailPopUp = false;
            }
            return;
        }

        // Jalur navigasi panah normal
        if (keyCode == java.awt.event.KeyEvent.VK_LEFT || keyCode == java.awt.event.KeyEvent.VK_A) {
            if (selectedSlot > 1) selectedSlot--;
        } 
        else if (keyCode == java.awt.event.KeyEvent.VK_RIGHT || keyCode == java.awt.event.KeyEvent.VK_D) {
            if (selectedSlot < 5) selectedSlot++;
        } 
        else if (keyCode == java.awt.event.KeyEvent.VK_UP || keyCode == java.awt.event.KeyEvent.VK_W) {
            if (selectedSlot > 3) selectedSlot -= 3; // Dari bawah lompat ke baris atas
        } 
        else if (keyCode == java.awt.event.KeyEvent.VK_DOWN || keyCode == java.awt.event.KeyEvent.VK_S) {
            if (selectedSlot <= 3) {
                selectedSlot += 3;
                if (selectedSlot > 5) selectedSlot = 5; // Mentok di Collector
            }
        }
    }

    // Fungsi inti eksekusi barter resep barter ikan
    // Update fungsi ini di dalam ShopMenu.java
    public Fish triggerTrade(Inventory globalInventory) {
        if (showFailPopUp) return null; 

        int commonQty  = globalInventory.getFishCountByRarity("Common");
        int rareQty    = globalInventory.getFishCountByRarity("Rare");
        int specialQty = globalInventory.getFishCountByRarity("Special");
        int epicQty    = globalInventory.getFishCountByRarity("Epic");
        int legendQty  = globalInventory.getFishCountByRarity("Legend");

        Fish fishGained = null;

        try {
            if (selectedSlot == 1) { // 1. RARE (Butuh 5 Common)
                if (commonQty >= 5) {
                    globalInventory.removeFishByRarity("Common", 5);
                    
                    // Buat objek ikan
                    fishGained = new Fish("Tuna", "Rare");
                    
                    // LOAD GAMBARNYA DI SINI 
                    File f = new File("../assets/tuna.png");
                    if(!f.exists()) f = new File("assets/tuna.png");
                    if(f.exists()) {
                        fishGained.setFisImage(ImageIO.read(f)); // Gunakan setter gambar milik class Fish kamu
                    }
                } else { showFailPopUp = true; }
            } 
            else if (selectedSlot == 2) { // 2. SPECIAL
                if (commonQty >= 5 && rareQty >= 5) {
                    globalInventory.removeFishByRarity("Common", 5);
                    globalInventory.removeFishByRarity("Rare", 5);
                    
                    fishGained = new Fish("Ikan Badut", "Special");
                    
                    //  LOAD GAMBARNYA 
                    File f = new File("../assets/badut.png");
                    if(!f.exists()) f = new File("assets/badut.png");
                    if(f.exists()) fishGained.setFisImage(ImageIO.read(f));
                } else { showFailPopUp = true; }
            } 
            else if (selectedSlot == 3) { // 3. EPIC
                if (commonQty >= 3 && rareQty >= 3 && specialQty >= 3) {
                    globalInventory.removeFishByRarity("Common", 3);
                    globalInventory.removeFishByRarity("Rare", 3);
                    globalInventory.removeFishByRarity("Special", 3);
                    
                    fishGained = new Fish("Aura Purple", "Epic");
                    
                    // LOAD GAMBARNYA DI SINI
                    File f = new File("../assets/purple.png");
                    if(!f.exists()) f = new File("assets/purple.png");
                    if(f.exists()) fishGained.setFisImage(ImageIO.read(f));
                } else { showFailPopUp = true; }
            } 
            else if (selectedSlot == 4) { // 4. LEGEND
                if (epicQty >= 5) {
                    globalInventory.removeFishByRarity("Epic", 5);
                    
                    fishGained = new Fish("Naga Emas", "Legend");
                    
                    //  LOAD GAMBARNYA DI SINI
                    File f = new File("../assets/emas.png");
                    if(!f.exists()) f = new File("assets/emas.png");
                    if(f.exists()) fishGained.setFisImage(ImageIO.read(f));
                } else { showFailPopUp = true; }
            } 
            else if (selectedSlot == 5) { // 5. COLLECTOR
                if (legendQty >= 5) {
                    globalInventory.removeFishByRarity("Legend", 5);
                    
                    fishGained = new Fish("Fosil Purba", "Collector");
                    
                    //  LOAD GAMBARNYA DI SINI
                    File f = new File("../assets/fosil.png");
                    if(!f.exists()) f = new File("assets/fosil.png");
                    if(f.exists()) fishGained.setFisImage(ImageIO.read(f));
                } else { showFailPopUp = true; }
            }
        } catch (Exception e) {
            System.out.println("Gagal memuat gambar ikan hasil barter!");
            e.printStackTrace();
        }

        return fishGained; // mengembalikan objek ikan 
    }

    // Menggambar tampilan toko beserta panah indikator
    public void draw(Graphics2D g2, int w, int h) {
        // 1. Gambarkan background toko utama
        if (shopBg != null) {
            g2.drawImage(shopBg, 0, 0, w, h, null);
        }

        // 2. Tentukan koordinat Tanda Panah Selektor 
        int arrowX = 0;
        int arrowY = 0;

        if (selectedSlot == 1) { arrowX = 160; arrowY = 540; } // Tombol Trade Slot 1
        else if (selectedSlot == 2) { arrowX = 670; arrowY = 540; } // Tombol Trade Slot 2
        else if (selectedSlot == 3) { arrowX = 1170; arrowY = 540; } // Tombol Trade Slot 3
        else if (selectedSlot == 4) { arrowX = 335; arrowY = 915; } // Tombol Trade Slot 4
        else if (selectedSlot == 5) { arrowX = 965; arrowY = 915; } // Tombol Trade Slot 5

        // 3. Menggambar bentuk fisik Tanda Panah (Warna Merah Segitiga Retro / Teks Berkedip)
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 50));
        g2.drawString(">", arrowX - 30, arrowY); // Menggambar panah penunjuk tepat di kiri tombol

        // 4. JIKA GAGAL: Gambar Overlay Background Pop-Up Gagal di tengah layar
        if (showFailPopUp && failBg != null) {
            g2.drawImage(failBg, 0, 0, w, h, null);
        }
    }


    //  public void draw(Graphics2D g2, int w, int h) {
    //     // 1. Gambarkan background toko utama
    //     if (shopBg != null) {
    //         g2.drawImage(shopBg, 0, 0, w, h, null);
    //     }

    //     // 2. Tentukan koordinat Tanda Panah Selektor 
    //     int arrowX = 0;
    //     int arrowY = 0;

    //     if (selectedSlot == 1) { arrowX = 170; arrowY = 540; } // Tombol Trade Slot 1
    //     else if (selectedSlot == 2) { arrowX = 680; arrowY = 540; } // Tombol Trade Slot 2
    //     else if (selectedSlot == 3) { arrowX = 1170; arrowY = 520; } // Tombol Trade Slot 3
    //     else if (selectedSlot == 4) { arrowX = 335; arrowY = 915; } // Tombol Trade Slot 4
    //     else if (selectedSlot == 5) { arrowX = 965; arrowY = 915; } // Tombol Trade Slot 5

    //     // 3. Menggambar bentuk fisik Tanda Panah (Warna Merah Segitiga Retro / Teks Berkedip)
    //     g2.setColor(Color.YELLOW);
    //     g2.setFont(new Font("Arial", Font.BOLD, 50));
    //     g2.drawString(">", arrowX - 30, arrowY); // Menggambar panah penunjuk tepat di kiri tombol

    //     // 4. JIKA GAGAL: Gambar Overlay Background Pop-Up Gagal di tengah layar
    //     if (showFailPopUp && failBg != null) {
    //         g2.drawImage(failBg, 0, 0, w, h, null);
    //     }
    // }
    
    
    public boolean isShowFailPopUp() { return showFailPopUp; }

    
    



    
}
