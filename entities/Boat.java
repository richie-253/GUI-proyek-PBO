package entities;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

public class Boat {
    // Semua variabel private demi Encapsulation total
    private int x, y, speed;
    private BufferedImage imgUp, imgDown, imgLeft, imgRight;
    private int direction = 0; 
    private double animationAngle = 0;
    private int bobbingY = 0;

    public Boat(int x, int y) {
        this.x = x;
        this.y = y;
        this.speed = 8; // Kecepatan gerak standar kapal meluncur
    }

    public void update() {
        // Efek ombak mengayun halus
        animationAngle += 0.05;
        bobbingY = (int) (Math.sin(animationAngle) * 3);
    }

    public void moveUp() {
        direction = 0;
        if (y > 180) { 
            y -= speed;
        }
    }

    public void moveDown(int screenHeight) {
        direction = 1;
        if (y < screenHeight - 140) { 
            y += speed;
        }
    }

    public void moveLeft() {
        direction = 2;
        if (x > 10) { 
            x -= speed;
        }
    }

    public void moveRight(int screenWidth) {
        direction = 3;
        if (x < screenWidth - 100) { 
            x += speed;
        }
    }

    public void draw(Graphics2D g2) {
        BufferedImage currentImg = null;
        
        if (direction == 0) currentImg = imgUp;
        else if (direction == 1) currentImg = imgDown;
        else if (direction == 2) currentImg = imgLeft;
        else if (direction == 3) currentImg = imgRight;

        if (currentImg != null) {
            int drawW = (direction == 0 || direction == 1) ? 55 : 100;
            int drawH = (direction == 0 || direction == 1) ? 110 : 65;
            
            g2.drawImage(currentImg, x, y + bobbingY, drawW, drawH, null);
        } else {
            // Backup darurat kotak merah jika anak kapal belum berhasil load gambar
            g2.setColor(Color.RED);
            g2.fillRect(x, y + bobbingY, 60, 60);
        }
    }

    // TARGET POLYMORPHISM: Untuk di-override oleh tiap tingkat kapal
    public Fish gachaIkan() {
        return null;
    }

    // =========================================================================
    // KUMPULAN GETTER & SETTER (Untuk diakses GamePanel & Class Anak)
    // =========================================================================
    public int getX() { return x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return y; }
    public void setY(int y) { this.y = y; }

    public int getSpeed() { return speed; }
    public void setSpeed(int speed) { this.speed = speed; }

    public int getDirection() { return direction; }
    public void setDirection(int direction) { this.direction = direction; }

    public void setImgUp(BufferedImage imgUp) { this.imgUp = imgUp; }
    public void setImgDown(BufferedImage imgDown) { this.imgDown = imgDown; }
    public void setImgLeft(BufferedImage imgLeft) { this.imgLeft = imgLeft; }
    public void setImgRight(BufferedImage imgRight) { this.imgRight = imgRight; }
}