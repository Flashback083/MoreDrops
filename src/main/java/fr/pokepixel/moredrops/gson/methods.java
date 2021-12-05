package fr.pokepixel.moredrops.gson;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.pixelmonmod.pixelmon.api.pokemon.PokemonSpec;
import com.pixelmonmod.pixelmon.entities.pixelmon.EntityPixelmon;
import fr.pokepixel.moredrops.ItemDrop;
import fr.pokepixel.moredrops.MoreDrops;

import java.io.*;
import java.nio.charset.StandardCharsets;

import static fr.pokepixel.moredrops.MoreDrops.itemDrop;

public class methods {

    public static void writeJson(ItemDrop object){
        try (PrintWriter writer = new PrintWriter(MoreDrops.json,"UTF-8")) {
            MoreDrops.gson.toJson(object, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ItemDrop readJson(){
        try (Reader reader = new InputStreamReader(new FileInputStream(MoreDrops.json), StandardCharsets.UTF_8)) {
            ItemDrop data = MoreDrops.gson.fromJson(reader, ItemDrop.class);
            return data;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addItem(String pokemonspec, String item,double rarity){
        ItemDrop json = readJson();
        assert json != null;
        ItemDrop.ItemInfo iteminfo = new ItemDrop.ItemInfo(item, rarity);
        if (json.getItemDropList().containsKey(pokemonspec)){
            //JsonObject iteminfo = new JsonObject();
            //iteminfo.addProperty("item",item);
            //iteminfo.addProperty("rarity",rarity);
            json.getItemDropList().get(pokemonspec).add(iteminfo);
            //json.get(pokemonspec).getAsJsonArray().add(iteminfo);
        }else {
            json.getItemDropList().put(pokemonspec, Lists.newArrayList(iteminfo));
        }
        writeJson(json);
    }

    public static boolean alreadyExist(String pokemonspec){
        ItemDrop json = readJson();
        assert json != null;
        return json.getItemDropList().containsKey(pokemonspec);
    }

    public static boolean removeSpec(String pokemonspec){
        ItemDrop json = readJson();
        assert json != null;
        if (json.getItemDropList().containsKey(pokemonspec)){
            json.getItemDropList().remove(pokemonspec);
            writeJson(json);
            return true;
        }
        return false;
    }

    public static boolean removeItem(String pokemonspec,String item,double rarity){
        ItemDrop json = readJson();
        assert json != null;
        if (json.getItemDropList().containsKey(pokemonspec)){
            ItemDrop.ItemInfo iteminfo = new ItemDrop.ItemInfo(item, rarity);
            if (json.getItemDropList().get(pokemonspec).contains(iteminfo)){
                json.getItemDropList().get(pokemonspec).remove(iteminfo);
                writeJson(json);
                return true;
            }
        }
        return false;
    }



    public static Multimap<String,Double> getItems(EntityPixelmon pixelmon){
        ItemDrop json = itemDrop;
        //ItemDrop json = readJson();
        Multimap<String, Double> items = ArrayListMultimap.create();
        assert json != null;
        json.getItemDropList().forEach((spec, itemInfos) -> {
            if (new PokemonSpec(spec).matches(pixelmon)){
                itemInfos.forEach(itemInfo -> {
                    items.put(itemInfo.getItem(), itemInfo.getRarity());
                });
            }
        });
        return items;
        /*json.entrySet().forEach(jsonElementEntry -> {
            if (new PokemonSpec(jsonElementEntry.getKey()).matches(pixelmon)){
                //jsonElementEntry.getValue().isJsonArray() true
                for (int i =0; i<jsonElementEntry.getValue().getAsJsonArray().size();i++){
                    String item = jsonElementEntry.getValue().getAsJsonArray().get(i).getAsJsonObject().get("item").getAsString();
                    double rarity = jsonElementEntry.getValue().getAsJsonArray().get(i).getAsJsonObject().get("rarity").getAsDouble();
                    items.put(item,rarity);
                }
            }
        });*/
    }


}
