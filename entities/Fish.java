package entities;

import java.awt.image.BufferedImage;

public class Fish {
    private String name;
    private String rarity;
    private BufferedImage fisImage;
    
    public Fish(String name,String rarity)
    {
        this.name = name;
        this.rarity = rarity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRarity() {
        return rarity;
    }

    public void setRarity(String rarity) {
        this.rarity = rarity;
    }

    public BufferedImage getFisImage() {
        return fisImage;
    }

    public void setFisImage(BufferedImage fisImage) {
        this.fisImage = fisImage;
    }

    
}
