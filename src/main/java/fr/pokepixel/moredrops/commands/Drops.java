package fr.pokepixel.moredrops.commands;

import com.google.common.collect.Lists;
import com.pixelmonmod.pixelmon.enums.EnumSpecies;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static fr.pokepixel.moredrops.MoreDrops.itemDrop;
import static fr.pokepixel.moredrops.gson.methods.*;


public class Drops extends CommandBase implements ICommand {

    private final List<String> aliases;


    public Drops(){
        aliases = Lists.newArrayList("moredrops","md");
    }

    @Override
    public String getName() {
        return "moredrops";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return  "§a[MoreDrops]\n"
                + "§a/moredrops additem <percent drop> <[pokemon spec]>\n"
                + "§a/moredrops reload\n"
                + "§a/moredrops removeitem <percent drop> <[pokemon spec]>";
    }

    @Override
    public List<String> getAliases() {
        return aliases;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) {
        if (sender instanceof EntityPlayerMP){
            EntityPlayerMP player = (EntityPlayerMP) sender;
            if (args.length == 0) {
                sender.sendMessage((new TextComponentString(getUsage(sender))));
            }else if (args.length >= 3 && args[0].equalsIgnoreCase("additem")){
                    if (player.inventory.getCurrentItem().equals(ItemStack.EMPTY)){
                        sender.sendMessage(new TextComponentString("§cYou dont have item in your hand"));
                    }else {
                        double percent = Double.parseDouble(args[1]);
                        if (percent > 0.0 && percent <=100.0){
                            ArrayList<String> arg = Lists.newArrayList(args);
                            arg.remove(0);
                            arg.remove(0);
                            addItem(String.join(" ",arg),player.inventory.getCurrentItem().serializeNBT().toString(),percent);
                            sender.sendMessage(new TextComponentString("§aItem added!"));
                            itemDrop = readJson();
                        }else {
                            sender.sendMessage(new TextComponentString("§cIncorrect value of the percent!"));
                        }
                    }
            }else if (args.length >= 3 && args[0].equalsIgnoreCase("removeitem")){
                if (player.inventory.getCurrentItem().equals(ItemStack.EMPTY)){
                    sender.sendMessage(new TextComponentString("§cYou dont have item in your hand"));
                }else {
                    double percent = Double.parseDouble(args[1]);
                    if (percent > 0.0 && percent <=100.0){
                        ArrayList<String> arg = Lists.newArrayList(args);
                        arg.remove(0);
                        arg.remove(0);
                        if (removeItem(String.join(" ",arg),player.inventory.getCurrentItem().serializeNBT().toString(),percent)){
                            sender.sendMessage(new TextComponentString("§aItem removed!"));
                            itemDrop = readJson();
                            return;
                        }
                        sender.sendMessage(new TextComponentString("§cItem not found!"));
                    }else {
                        sender.sendMessage(new TextComponentString("§cIncorrect value of the percent!"));
                    }
                }
            }else if (args.length == 1 && args[0].equalsIgnoreCase("reload")){
                itemDrop = readJson();
                sender.sendMessage(new TextComponentString("§aConfig reloaded"));
            }
            else {
                sender.sendMessage((new TextComponentString(getUsage(sender))));
            }
        }
    }



    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }

    @Override
    @Nonnull
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 1)
        {
            return getListOfStringsMatchingLastWord(args, "additem","removeitem");
        }else if (args.length == 3 && (args[0].equalsIgnoreCase("additem") ||args[0].equalsIgnoreCase("removeitem"))){
            return getListOfStringsMatchingLastWord(args, EnumSpecies.getNameList());
        }
        {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return false;
    }

    @Override
    public int compareTo(ICommand o) {
        return 0;
    }
}
