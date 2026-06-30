package game_menu;

import entities.Boat;
import entities.Inventory;
import entities.KapalP2; 
import entities.KapalP3;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PortMenu {
    private int selectedSlot = 1; // 1 = Fisher Boat (Kiri), 2 = Dark Reaper (Kanan)
    private boolean showFailPopUp = false;
    
    private BufferedImage bgPort;
    private BufferedImage imgNotifGagal;

    public PortMenu() {
        try {
            File fBg = new File("../assets/port.png");
            if (!fBg.exists()) fBg = new File("assets/port.png");
            if (fBg.exists()) bgPort = ImageIO.read(fBg);

            File fNotif = new File("../assets/notif_gagal.png");
            if (!fNotif.exists()) fNotif = new File("assets/notif_gagal.png");
            if (fNotif.exists()) imgNotifGagal = ImageIO.read(fNotif);
            
        } catch (Exception e) {
            System.out.println("STATUS ERROR: Gagal memuat gambar aset di PortMenu!");
            e.printStackTrace();
        }
    }

    public void navigate(int key) {
        if (showFailPopUp) {
            if (key == KeyEvent.VK_ENTER || key == KeyEvent.VK_ESCAPE) {
                showFailPopUp = false;
            }
            return;
        }

        //  SEKARANG MENGGUNAKAN TOMBOL A DAN D UNTUK NAVIGASI HORIZONTAL
        if (key == KeyEvent.VK_A) {
            selectedSlot = 1; // Geser ke kiri (Fisher Boat)
        } else if (key == KeyEvent.VK_D) {
            selectedSlot = 2; // Geser ke kanan (Dark Reaper)
        }
    }

    public Boat triggerBuy(Inventory globalInventory, int currentX, int currentY) {
        if (showFailPopUp) return null;

        int commonQty    = globalInventory.getFishCountByRarity("Common");
        int rareQty      = globalInventory.getFishCountByRarity("Rare");
        int specialQty   = globalInventory.getFishCountByRarity("Special");
        int epicQty      = globalInventory.getFishCountByRarity("Epic");
        int legendQty    = globalInventory.getFishCountByRarity("Legend");
        int collectorQty = globalInventory.getFishCountByRarity("Collector");

        if (selectedSlot == 1) {
            if (commonQty >= 7 && rareQty >= 5 && specialQty >= 3 && epicQty >= 1) {
                globalInventory.removeFishByRarity("Common", 7);
                globalInventory.removeFishByRarity("Rare", 5);
                globalInventory.removeFishByRarity("Special", 3);
                globalInventory.removeFishByRarity("Epic", 1);
                return new KapalP2(currentX, currentY); 
            } else {
                showFailPopUp = true; 
            }
        } 
        else if (selectedSlot == 2) {
            if (commonQty >= 3 && rareQty >= 3 && specialQty >= 3 && epicQty >= 2 && legendQty >= 1 && collectorQty >= 1) {
                globalInventory.removeFishByRarity("Common", 3);
                globalInventory.removeFishByRarity("Rare", 3);
                globalInventory.removeFishByRarity("Special", 3);
                globalInventory.removeFishByRarity("Epic", 2);
                globalInventory.removeFishByRarity("Legend", 1);
                globalInventory.removeFishByRarity("Collector", 1);
                return new KapalP3(currentX, currentY); 
            } else {
                showFailPopUp = true;
            }
        }
        return null;
    }

    // METHOD DRAW DIUBAH AGAR MENERIMA PARAMETER WIDTH (w) DAN HEIGHT (h) LAYAR
    public void draw(Graphics2D g2, int w, int h) {
        // [1] Draw Background Utama Port - Dipaksa melar mengikuti resolusi window (w, h)
        if (bgPort != null) {
            g2.drawImage(bgPort, 0, 0, w, h, null);
        }

        // [2] Draw Tanda Panah Kuning Penunjuk Pilihan
        g2.setColor(Color.YELLOW);
        g2.setFont(new Font("Arial", Font.BOLD, 40)); 

        // Angka 680 adalah koordinat Y perkiraan agar pas di layar beresolusi tinggi 768. 
        // Silakan ubah angka 680 ini naik/turun menyesuaikan letak button pada gambar port.png milikmu.
        if (selectedSlot == 1) {
            g2.drawString(">", 370, 865); 
        } else if (selectedSlot == 2) {
            g2.drawString(">", 990, 895); 
        }

        // [3] Draw Pop-up Overlay Gagal (Mengikuti ukuran window game secara presisi)
        if (showFailPopUp) {
            g2.setColor(new Color(0, 0, 0, 160)); 
            g2.fillRect(0, 0, w, h); // Blackout memenuhi seluruh layar game

            if (imgNotifGagal != null) {
                // Kalkulasi otomatis agar gambar pop-up gagal berada tepat di tengah window
                int x = (w - imgNotifGagal.getWidth()) / 2;
                int y = (h - imgNotifGagal.getHeight()) / 2;
                g2.drawImage(imgNotifGagal, x, y, null);
            }
        }
    }
}