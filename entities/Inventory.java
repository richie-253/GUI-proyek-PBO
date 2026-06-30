package entities;

import java.util.ArrayList;

public class Inventory {
    private ArrayList<Fish> listIkan;

    public Inventory()
    {
        this.listIkan = new ArrayList<>();
    }

    public void addFish(Fish fish)
    {
        if (fish!=null)
        {
            this.listIkan.add(fish);
        }
    }

    public int getFishCountByRarity(String targetRarity) {
        int count = 0;
        for (int i = 0; i < listIkan.size(); i++) {
            // Membandingkan string tanpa peduli huruf besar/kecil
            if (listIkan.get(i).getRarity().equalsIgnoreCase(targetRarity)) {
                count++;
            }
        }
        return count;
    }

    public void removeFishByRarity(String rarity, int amount)
    {
        int countRemoved = 0;

        for (int i = this.listIkan.size()-1;i>=0;i--)
        {
            if (this.listIkan.get(i).getRarity().equalsIgnoreCase(rarity))
            {
                this.listIkan.remove(i);
                countRemoved++;
            }

            if (countRemoved == amount)
            {
                break;
            }
        }
    }
}
