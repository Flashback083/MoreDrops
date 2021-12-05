package fr.pokepixel.moredrops;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.api.events.BeatWildPixelmonEvent;
import com.pixelmonmod.pixelmon.api.events.DropEvent;
import com.pixelmonmod.pixelmon.entities.npcs.registry.DropItemRegistry;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import com.pixelmonmod.pixelmon.entities.pixelmon.drops.DropItemQueryList;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static fr.pokepixel.moredrops.gson.methods.getItems;

public class PixelmonEvent {


    public static HashMap<UUID,List<ItemStack>> itemsdroplist = new HashMap<>();

    @SubscribeEvent
    public void onDrop(DropEvent event) {
        if (!event.isPokemon()) return;
        EntityPixelmon pixelmon = (EntityPixelmon) event.entity;
        List<ItemStack> drops;
        if (itemsdroplist.containsKey(pixelmon.getUniqueID())){
            drops = itemsdroplist.get(pixelmon.getUniqueID());
        }else {
            drops = haveDrop(pixelmon);
        }
        if (drops.size()>0){
            drops.forEach(event::addDrop);
        }
    }


    @SubscribeEvent
    public void onBeat(BeatWildPixelmonEvent event){
        EntityPixelmon pixelmon = (EntityPixelmon) event.wpp.getEntity();
        if (DropItemRegistry.getDropsForPokemon(pixelmon).size()==0){
            List<ItemStack> drops = haveDrop(pixelmon);
            if (drops.size()>0){
                itemsdroplist.put(pixelmon.getUniqueID(),haveDrop(pixelmon));
                DropItemQueryList.register(pixelmon,Lists.newArrayList(), event.player);
            }
        }
    }

    public static List<ItemStack> haveDrop(EntityPixelmon pixelmon){
        List<ItemStack> itemStacks = Lists.newArrayList();
        if (getItems(pixelmon).size() > 0) {
            getItems(pixelmon).forEach((nbt, d) -> {
                try {
                    Random r = new Random();
                    double choose = r.nextDouble() * 100;
                    double chance = d;
                    if (choose <= chance) {
                        ItemStack itemstack = new ItemStack(JsonToNBT.getTagFromJson(nbt));
                        itemStacks.add(itemstack);
                    }
                } catch (NBTException e) {
                    e.printStackTrace();
                }
            });
        }
        itemsdroplist.put(pixelmon.getUniqueID(),itemStacks);
        return itemStacks;
    }


}
