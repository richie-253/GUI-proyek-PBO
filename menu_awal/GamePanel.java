package menu_awal;

import entities.Bluevortex;
import entities.Boat;
import entities.Fish;
import entities.Inventory;
import entities.KapalAwal;
import entities.Vortex;
import entities.Wave;
import game_menu.FishListMenu; 
import game_menu.GameplayMenu;
import game_menu.PortMenu;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Random;    
import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements KeyListener {
    
    int STATE_MENU = 0;
    int STATE_DOCK_MENU = 1; 
    int STATE_PLAYING = 2;   
    int STATE_SHOP = 3;      
    int STATE_INVENTORY = 4; 
    int STATE_FISH_LIST = 5; 
    int STATE_PORT_MENU = 6;

    int currentState = STATE_MENU;
    int mainSelector = 0; 
    
    BufferedImage menuBg, playBg;
    GameplayMenu dockMenu;
    FishListMenu fishListMenu;
    private Boat playerBoat;
    private Inventory globalInventory = new Inventory();
    private game_menu.InventoryMenu inventoryMenu = new game_menu.InventoryMenu();
    private game_menu.ShopMenu shopMenu = new game_menu.ShopMenu();
    private PortMenu portMenu = new PortMenu();
    Timer gameTimer;

    ArrayList<Vortex> yellowVortexes = new ArrayList<>(); 
    Bluevortex blueVortex; 
    
    long lastBlueVortexSpawnTime;
    Random rand = new Random();

    // VARIABEL BARU UNTUK POP-UP NOTIFIKASI GAMBAR IKAN
    private Fish ikanTerakhirDapat = null;
    private int notificationTimer = 0; // Mengatur durasi muncul pop-up

    private ArrayList<Wave> waveList = new ArrayList<>();
    private BufferedImage imgOmbak = null;
    private BufferedImage imgKapalRusak = null;
    private long waveTimer = 0;        // pengukur waktu 10 detik ombak
    private long destructionTimer = 0; // jeda waktu kapal hancur
    private boolean isDestroyed = false; // status hidup/hancurnya kapal

    public GamePanel() {
        this.setPreferredSize(new Dimension(1024, 768));
        this.setFocusable(true);
        this.addKeyListener(this);

        System.out.println("--- MEMULAI GAME DENGAN KOORDINAT BARU ---");
        fishListMenu = new FishListMenu();
        dockMenu = new GameplayMenu();
        playerBoat = new KapalAwal(480, 450); 

        yellowVortexes.add(new Vortex(270, 290, "pus_portal.png")); // Shop
        yellowVortexes.add(new Vortex(620, 290, "pus_portal.png")); // Inventory
        yellowVortexes.add(new Vortex(970, 290, "pus_portal.png")); // Fish List
        yellowVortexes.add(new Vortex(1325, 290, "pus_portal.png")); // Port

        lastBlueVortexSpawnTime = System.currentTimeMillis();

        try {
            menuBg = ImageIO.read(new File("../assets/menu_awal.png"));
            File playFile = new File("../assets/play_bg.png");
            if(playFile.exists()) playBg = ImageIO.read(playFile);

            File fOmbak = new File("../assets/ombak.png");
            if (!fOmbak.exists()) fOmbak = new File("assets/ombak.png");
            if (fOmbak.exists()) imgOmbak = ImageIO.read(fOmbak);

            File fRusak = new File("../assets/kapal_rusak.png");
            if (!fRusak.exists()) fRusak = new File("assets/kapal_rusak.png");
            if (fRusak.exists()) imgKapalRusak = ImageIO.read(fRusak);
        } catch (Exception e) {}

        // LOOP TIMER (berjalan setiap 20 milidetik)
        // LOOP TIMER (berjalan setiap 20 milidetik)
        gameTimer = new Timer(20, e -> {
            if (currentState == STATE_PLAYING || currentState == STATE_DOCK_MENU) {
                
                // JIKA KAPAL SEDANG HANCUR (JEDA 2 DETIK)
                if (isDestroyed) {
                    if (System.currentTimeMillis() - destructionTimer > 2000) { 
                        isDestroyed = false;
                        
                        // Kembalikan ke KapalAwal dengan koordinat semula (480, 450)
                        this.playerBoat = new KapalAwal(480, 450); 
                        resetSistemOmbak();
                    }
                    repaint();
                    return; // Skip update pergerakan di bawah jika sedang hancur
                }

                playerBoat.update(); 

                if (currentState == STATE_DOCK_MENU) {
                    processBlueVortexSpawn();

                    if (blueVortex != null) {
                        //blueVortex.update(getWidth(), getHeight()); 
                    }
                }

                // timer ombak (10 dtk) & maks 20 ombak
                if (System.currentTimeMillis() - waveTimer >= 10000) {
                    if (waveList.size() < 20) {
                        waveList.add(new Wave(1024, 768));
                    }
                    // if (waveList.size() < 20) {
                    //     waveList.add(new Wave(1024, 768)); // tambah 2 ombak sekaligus
                    // }
                    waveTimer = System.currentTimeMillis(); // reset waktu ke 0 lagi
                }

                // UPDATE GERAK OMBAK & DETEKSI COLLISION (TABRAKAN)
                Rectangle playerBounds = new Rectangle(playerBoat.getX(), playerBoat.getY(), 80, 80); 
                
                for (int i = 0; i < waveList.size(); i++) {
                    Wave w = waveList.get(i);
                    w.update(getWidth(), getHeight()); // Jalankan pergerakan ombak

                    // cek jika area ombak bersinggungan dengan area kapal
                    if (w.getBounds().intersects(playerBounds)) {
                        isDestroyed = true;
                        destructionTimer = System.currentTimeMillis(); // catat waktu tabrakan
                        break; // Keluar dari loop ombak
                    }
                }

                // HITUNG MUNDUR TIMER NOTIFIKASI
                if (notificationTimer > 0) {
                    notificationTimer--;
                }

                repaint();
            }
        });
        gameTimer.start();
    }

    //  FUNGSI UNTUK MENYIMPAN DAFTAR IKAN KE FILE TEKS
    // public void saveGame() {
    //     try {
    //         // Membuat atau menimpa file bernama "saveData.txt"
    //         java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter("saveData.txt"));
            
    //         // Ambil list ikan dari inventory (Pastikan di class Inventory ada method getFishList())
    //         ArrayList<Fish> listIkan = globalInventory.getFishList(); 
            
    //         // Catat semua ikan satu per satu menggunakan loop biasa
    //         for (int i = 0; i < listIkan.size(); i++) {
    //             Fish f = listIkan.get(i);
    //             // Format simpan: NamaIkan,RarityIkan
    //             writer.write(f.getName() + "," + f.getRarity());
    //             writer.newLine(); // Pindah baris baru
    //         }
            
    //         writer.close();
    //         System.out.println(" [SYSTEM]: Progres berhasil disimpan ke saveData.txt!");
    //     } catch (Exception e) {
    //         System.out.println("Gagal menyimpan data game: " + e.getMessage());
    //     }
    // }

    // // 🔥 FUNGSI UNTUK MEMBACA KEMBALI DATA DARI FILE TEKS
    // public void loadGame() {
    //     try {
    //         java.io.File file = new java.io.File("saveData.txt");
            
    //         // Jika file belum ada (misal baru pertama kali main), jangan load apa-apa
    //         if (!file.exists()) {
    //             System.out.println("👉 [SYSTEM]: File save tidak ditemukan. Memulai game baru.");
    //             return; 
    //         }
            
    //         java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.FileReader(file));
    //         String baris;
            
    //         // Baca file baris demi baris sampai habis
    //         while ((baris = reader.readLine()) != null) {
    //             String[] data = baris.split(","); // Pisahkan berdasarkan tanda koma
    //             if (data.length == 2) {
    //                 String namaIkan = data[0];
    //                 String rarityIkan = data[1];
                    
    //                 // Bungkus jadi objek Fish baru, lalu masukkan ke inventory
    //                 Fish f = new Fish(namaIkan, rarityIkan);
    //                 globalInventory.addFish(f);
    //             }
    //         }
            
    //         reader.close();
    //         System.out.println("👉 [SYSTEM]: Progres lama berhasil dimuat!");
    //     } catch (Exception e) {
    //         System.out.println("Gagal memuat data game: " + e.getMessage());
    //     }
    // }

    private void resetSistemOmbak() {
        waveList.clear();
        waveTimer = System.currentTimeMillis();
        // 2 ombak awal sebagai pemanasan pertama
        waveList.add(new Wave(1024, 768));
        waveList.add(new Wave(1024, 768));
    }
    
    private void processBlueVortexSpawn() {
        long currentTime = System.currentTimeMillis();
        
        if (currentTime - lastBlueVortexSpawnTime >= 10000) {
            int randomX = rand.nextInt(getWidth() - 100) + 50;
            int randomY = rand.nextInt(330) + 270;
            
            blueVortex = new Bluevortex(randomX, randomY, "pus_ikan.png");
            lastBlueVortexSpawnTime = currentTime;
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        int w = getWidth();
        int h = getHeight();

        if (currentState == STATE_MENU) {
            if (menuBg != null) g2.drawImage(menuBg, 0, 0, w, h, null);
            g2.setColor(Color.YELLOW); 
            g2.setFont(new Font("Arial", Font.BOLD, 100)); 
            int arrowX = (int) (w * 0.32); 
            int arrowY = (mainSelector == 0) ? (int) (h * 0.68) : (int) (h * 0.83);
            g2.drawString(">", arrowX, arrowY);
        } 
        else if (currentState == STATE_DOCK_MENU) {
            dockMenu.draw(g2, w, h); 

            for (int i = 0; i < yellowVortexes.size(); i++) {
                yellowVortexes.get(i).draw(g2); 
            }
            
            if (blueVortex != null) {
                blueVortex.draw(g2);
            }

            if (imgOmbak != null) {
                for (int i = 0; i < waveList.size(); i++) {
                    Wave waveObj = waveList.get(i);
                    g2.drawImage(imgOmbak, waveObj.getX(), waveObj.getY(), waveObj.getWidth(), waveObj.getHeight(), null);
                }
            }

            if (isDestroyed) {
                if (imgKapalRusak != null) {
                    g2.drawImage(imgKapalRusak, playerBoat.getX(), playerBoat.getY(), 90, 90, null);
                }
            } else {
                playerBoat.draw(g2); 
            }
    
        }
        else if (currentState == STATE_PLAYING) {
            if (playBg != null) g2.drawImage(playBg, 0, 0, w, h, null);
            else { g2.setColor(new Color(30, 144, 255)); g2.fillRect(0, 0, w, h); }
            
            // DRAW OMBAK DI STATE PLAYING JUGA
            if (imgOmbak != null) {
                for (int i = 0; i < waveList.size(); i++) {
                    Wave waveObj = waveList.get(i);
                    g2.drawImage(imgOmbak, waveObj.getX(), waveObj.getY(), waveObj.getWidth(), waveObj.getHeight(), null);
                }
            }

            if (isDestroyed) {
                if (imgKapalRusak != null) {
                    g2.drawImage(imgKapalRusak, playerBoat.getX(), playerBoat.getY(), 90, 90, null);
                }
            } else {
                playerBoat.draw(g2); 
            } 
        }
        else if(currentState == STATE_FISH_LIST){
            fishListMenu.draw(g2, w, h);
        }
        else if(currentState == STATE_INVENTORY)
        {
            inventoryMenu.draw(g2, w, h, globalInventory);
        }
        else if(currentState == STATE_SHOP)
        {
            shopMenu.draw(g2, w, h);
        }
        else if(currentState == STATE_PORT_MENU)
        {
            portMenu.draw(g2,w,h);
        }
        else {
            drawPlaceholderScreen(g2, w, h);
        }

        // MENGGAMBAR POP-UP GAMBAR DI ATAS LAYAR
        if (notificationTimer > 0 && ikanTerakhirDapat != null) {
            BufferedImage imgPopUp = ikanTerakhirDapat.getFisImage(); // Panggil Getter fisImage
            if (imgPopUp != null) {
                int imgW = imgPopUp.getWidth();
                int posX = (w - imgW) / 2; // Otomatis rata tengah horizontal
                int posY = 30;             // Diletakkan sedikit ke bawah dari batas atas layar
                
                g2.drawImage(imgPopUp, posX, posY, null);
            }
        }
    }

    private void drawPlaceholderScreen(Graphics2D g2, int w, int h) {
        String title = "";
        Color c = Color.BLACK;
        if (currentState == STATE_SHOP) { title = "HALAMAN SHOP"; c = new Color(139, 69, 19); }
        else if (currentState == STATE_INVENTORY) { title = "HALAMAN INVENTORY"; c = new Color(47, 79, 79); }
        else if (currentState == STATE_FISH_LIST) { title = "HALAMAN FISH LIST"; c = new Color(34, 139, 34); }

        g2.setColor(c);
        g2.fillRect(0, 0, w, h);
        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 40));
        g2.drawString(title, w / 4, h / 2);
        g2.setFont(new Font("Arial", Font.PLAIN, 20));
        g2.drawString("Tekan [ESC] untuk kembali ke Dermaga", w / 4, (h / 2) + 50);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        // Taruh di paling atas method keyPressed(KeyEvent e) sebelum IF lainnya
        if (key == KeyEvent.VK_P) {
            System.out.println("MEMPROSES CHEAT TOMBOL P...");
            String[] rarities = {"Common", "Rare", "Special", "Epic", "Legend", "Collector"};
            
            // [1] Load gambar placeholder agar InventoryMenu tidak NullPointerException
            BufferedImage dummyImg = null;
            try {
                File f = new File("../assets/pus_ikan.png");
                if (!f.exists()) f = new File("assets/pus_ikan.png");
                if (f.exists()) dummyImg = ImageIO.read(f);
            } catch (Exception ex) {
                System.out.println("Peringatan Cheat: Gambar dummy gagal dimuat!");
            }

            // [2] Isi masing-masing kelangkaan dengan 10 ikan
            for (int i = 0; i < rarities.length; i++) {
                for (int j = 0; j < 10; j++) {
                    Fish f = new Fish("Cheat " + rarities[i], rarities[i]);
                    
                    // Suntik gambar dummy ke objek fish agar aman saat di-draw
                    if (dummyImg != null) {
                        f.setFisImage(dummyImg); 
                    }
                    
                    globalInventory.addFish(f);
                }
            }
            
            System.out.println("CHEAT BERHASIL: Masing-masing 10 ikan tiap jenis masuk ke inventory!");
            repaint();
            return; // Langsung keluar dari fungsi agar input tidak bocor ke logika lain
        }
        
        if (currentState == STATE_MENU) {
            if (key == KeyEvent.VK_W) mainSelector = 0;
            if (key == KeyEvent.VK_S) mainSelector = 1;
            if (key == KeyEvent.VK_ENTER) {
                if (mainSelector == 0) 
                {
                    currentState = STATE_DOCK_MENU; 
                    resetSistemOmbak();
                }
                else System.exit(0);
            }
        }
        else if (currentState == STATE_SHOP) {
            // Jalankan navigasi panah internal milik toko
            shopMenu.navigate(key);

            // Jika menekan ENTER untuk deal barter
            if (key == KeyEvent.VK_ENTER) {
                Fish hasilBarter = shopMenu.triggerTrade(globalInventory);
                
                // Jika sukses mendapatkan ikan monster baru
                if (hasilBarter != null) {
                    globalInventory.addFish(hasilBarter);
                    this.ikanTerakhirDapat = hasilBarter;
                    this.notificationTimer = 100; // Munculkan pop-up tangkapan ikan selama 2 detik
                    System.out.println("BARTER BERHASIL: Mendapatkan " + hasilBarter.getName());
                }
            }
            
            // Keluar dari Toko kembali ke Dermaga menggunakan ESCAPE
            if (key == KeyEvent.VK_ESCAPE) {
                // Hanya izinkan keluar jika pop-up gagal sedang tidak mengunci layar
                if (!shopMenu.isShowFailPopUp()) {
                    currentState = STATE_DOCK_MENU;
                    playerBoat.setY(350); 
                    playerBoat.setDirection(1); 
                    resetSistemOmbak();
                }
            }
        }
        else if (currentState == STATE_PORT_MENU) {
            //  FIX 3: Navigasi & Aksi Khusus di Halaman Port Menu
            portMenu.navigate(key);

            if (key == KeyEvent.VK_ENTER) {
                int currentX = playerBoat.getX();
                int currentY = playerBoat.getY();
                
                Boat kapalBaru = portMenu.triggerBuy(globalInventory, currentX, currentY);
                if (kapalBaru != null) {
                    this.playerBoat = kapalBaru; // Ganti kapal player secara real-time
                    currentState = STATE_DOCK_MENU; // Kembali ke dermaga setelah berhasil beli
                    playerBoat.setY(350);
                    playerBoat.setDirection(1);
                    resetSistemOmbak();
                }
            }

            if (key == KeyEvent.VK_ESCAPE) {
                currentState = STATE_DOCK_MENU;
                playerBoat.setY(350); 
                playerBoat.setDirection(1); 
                resetSistemOmbak();
            }
        } 
        else if (currentState == STATE_DOCK_MENU) {
            if (key == KeyEvent.VK_W) playerBoat.moveUp();
            if (key == KeyEvent.VK_S) playerBoat.moveDown(getHeight());
            if (key == KeyEvent.VK_A) playerBoat.moveLeft();
            if (key == KeyEvent.VK_D) playerBoat.moveRight(getWidth());
            
            if (key == KeyEvent.VK_K) {
                handleInteractions();
            }

            if (key == KeyEvent.VK_ESCAPE) {
                currentState = STATE_MENU; 
            }
        }
        else {
            if (key == KeyEvent.VK_ESCAPE) {
                currentState = STATE_DOCK_MENU;
                
                // 🔥 PERBAIKAN ERROR: Sekarang aman menggunakan setter resmi
                playerBoat.setY(350); 
                playerBoat.setDirection(1); 
                resetSistemOmbak();
            }
            
            if (currentState == STATE_PLAYING) {
                if (key == KeyEvent.VK_A) playerBoat.moveLeft();
                if (key == KeyEvent.VK_D) playerBoat.moveRight(getWidth());
            }
        }
        repaint(); 
    }

    private void handleInteractions() {
        if (blueVortex != null && blueVortex.isActive) {
            if (blueVortex.isNear(playerBoat)) {
                blueVortex.isActive = false; 
                
                // TRIGGER GACHA POLYMORPHISM SAAT BERHASIL MENANGKAP IKAN
                ikanTerakhirDapat = playerBoat.gachaIkan(); 
                if (ikanTerakhirDapat != null) {
                    notificationTimer = 100; // Aktifkan gambar selama 100 frame (2 detik)
                    globalInventory.addFish(ikanTerakhirDapat);
                    System.out.println("STATUS: Kamu mendapatkan " + ikanTerakhirDapat.getName() + "!");
                }
                
                repaint();
                return; 
            }
        }

        for (int i = 0; i < yellowVortexes.size(); i++) {
            Vortex v = yellowVortexes.get(i);
            if (v.isNear(playerBoat)) {
                if (i == 0) currentState = STATE_SHOP;
                else if (i == 1) currentState = STATE_INVENTORY;
                else if (i == 2) currentState = STATE_FISH_LIST;
                else if (i == 3) currentState = STATE_PORT_MENU;
                resetSistemOmbak();

                
                System.out.println("STATUS: Pindah ke halaman " + currentState);
                break; 
            }
        }
    }

    

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}
}