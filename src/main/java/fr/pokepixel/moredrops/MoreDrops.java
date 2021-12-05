package fr.pokepixel.moredrops;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pixelmonmod.pixelmon.Pixelmon;
import fr.pokepixel.moredrops.commands.Drops;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;

import static fr.pokepixel.moredrops.gson.methods.readJson;

@Mod(
        modid = MoreDrops.MOD_ID,
        name = MoreDrops.MOD_NAME,
        version = MoreDrops.VERSION,
        serverSideOnly = true,
        acceptableRemoteVersions = "*"
)
public class MoreDrops {

    public static final String MOD_ID = "moredrops";
    public static final String MOD_NAME = "MoreDrops";
    public static final String VERSION = "5.0";

    public static Logger logger;
    public static File json;
    public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static ItemDrop itemDrop = new ItemDrop(new HashMap<>());


    @Mod.Instance(MOD_ID)
    public static MoreDrops INSTANCE;


    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent event) throws FileNotFoundException, UnsupportedEncodingException {
        logger = event.getModLog();
        File directory = new File(event.getModConfigurationDirectory(), MOD_NAME);
        directory.mkdir();
        json = new File(directory, "dropsdata.json");
        boolean check = json.exists();
        if (!check) {
            PrintWriter start = new PrintWriter(json,"UTF-8");
            start.write(gson.toJson(new ItemDrop(new HashMap<>())));
            start.close();
        }
        Pixelmon.EVENT_BUS.register(new PixelmonEvent());
        itemDrop = readJson();
    }


    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {

    }


    @Mod.EventHandler
    public void postinit(FMLPostInitializationEvent event) {

    }


    @Mod.EventHandler
    public void onServer(FMLServerStartingEvent event){
        event.registerServerCommand(new Drops());
    }

}
