package entities;

import java.awt.Rectangle;
import java.util.Random;

public class Wave {
    public int x, y;
    public int speedX, speedY;
    public int width = 90;  
    public int height = 70;

    private int minY = 270;
    public Wave(int screenWidth, int screenHeight) {
        Random rand = new Random();
        
        // LOGIKA BARU: Pilih sisi kemunculan (0=Kiri, 1=Kanan, 2=Atas, 3=Bawah)
        int sisi = rand.nextInt(4); 
        
        if (sisi == 0) {
            // Sisi Kiri: koordinat X di paling kiri (0), Y random dari atas ke bawah
            this.x = 0;
            this.y = rand.nextInt(screenHeight - this.height);
        } 
        else if (sisi == 1) {
            // Sisi Kanan: koordinat X di mentok kanan, Y random
            this.x = screenWidth - this.width;
            this.y = rand.nextInt(screenHeight - this.height);
        } 
        else if (sisi == 2) {
            // Sisi Atas: koordinat X random, Y di paling atas (0)
            this.x = rand.nextInt(screenWidth - this.width);
            this.y = 0;
        } 
        else if (sisi == 3) {
            // Sisi Bawah: koordinat X random, Y di mentok bawah
            this.x = rand.nextInt(screenWidth - this.width);
            this.y = screenHeight - this.height;
        }

        // Mengatur arah kecepatan gerak secara acak (-3 sampai 3)
        this.speedX = 2;
        this.speedY = 2;
        
        // Validasi darurat: jika speed bernilai 0, paksa jadi 2 agar ombak tidak diam
        if (this.speedX == 0) this.speedX = 2;
        if (this.speedY == 0) this.speedY = 2;
    }

    public void update(int screenWidth, int screenHeight) {
        x += speedX;
        y += speedY;

        // Logika memantul di dinding window game
        if (x < 0 || x > screenWidth - width) {
            speedX = -speedX;
        }
        
        if(y < minY)
        {
            y = minY;
            speedY = -speedY;
        }
        else if(y>screenHeight - height)
        {
            y = screenHeight - height;
            speedY = -speedY;   
        }
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getSpeedX() {
        return speedX;
    }

    public void setSpeedX(int speedX) {
        this.speedX = speedX;
    }

    public int getSpeedY() {
        return speedY;
    }

    public void setSpeedY(int speedY) {
        this.speedY = speedY;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}