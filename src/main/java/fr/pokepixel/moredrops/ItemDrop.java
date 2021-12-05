package fr.pokepixel.moredrops;

import java.util.HashMap;
import java.util.List;

public class ItemDrop {

    private final HashMap<String, List<ItemInfo>> itemDropList;

    public ItemDrop(HashMap<String, List<ItemInfo>> itemDropList){
        this.itemDropList = itemDropList;
    }

    public HashMap<String, List<ItemInfo>> getItemDropList() {
        return itemDropList;
    }

    public static class ItemInfo{

        private final String item;
        private final double rarity;

        public ItemInfo(String item, double rarity){
            this.item = item;
            this.rarity = rarity;
        }

        public String getItem() {
            return item;
        }

        public double getRarity() {
            return rarity;
        }
    }

}
