package entities;

public class Bluevortex extends Vortex{

    private int speedX = 2;      
    private int speedY = 2;      
    private int dirX = 1;        // 1 = Kanan, -1 = Kiri
    private int dirY = 1;        // 1 = Bawah, -1 = Atas


    public Bluevortex(int x,int y,String fileName)
    {
        super(x, y, fileName);
    }

    // UPDATE: Sekarang menerima panelWidth dan panelHeight untuk deteksi 4 dinding
    public void update(int panelWidth, int panelHeight) {
        if (!isActive) return;

        // Gerakkan koordinat X dan Y secara bersamaan (jadi gerak diagonal)
        this.x += speedX * dirX;
        this.y += speedY * dirY;
        
        // 1. Pantulan Kanan-Kiri
        if (this.x <= 10 || this.x + this.width >= panelWidth - 10) {
            dirX *= -1; // Balik arah horizontal
        }
        
        // 2. Pantulan Atas-Bawah (Disesuaikan dengan area air dermaga kamu)
        // Batas atas air (270) agar pusaran tidak menabrak jembatan dermaga atas
        if (this.y <= 270 || this.y + this.height >= panelHeight - 40) {
            dirY *= -1; // Balik arah vertikal
        }
    }
}
