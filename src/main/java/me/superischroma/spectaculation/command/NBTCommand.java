package me.superischroma.spectaculation.command;

import net.minecraft.server.v1_8_R3.ItemStack;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

@CommandParameters(description = "Gets the NBT of your current item.", permission = "spt.item")
public class NBTCommand extends SCommand
{
    @Override
    public void run(CommandSource sender, String[] args)
    {
        if (args.length != 0) throw new CommandArgumentException();
        if (sender instanceof ConsoleCommandSender) throw new CommandFailException("Console senders cannot use this command!");
        Player player = sender.getPlayer();
        PlayerInventory inv = player.getInventory();
        ItemStack stack = CraftItemStack.asNMSCopy(inv.getItemInHand());
        if (inv.getItemInHand() == null)
            throw new CommandFailException("Get an item in your hand!");
        NBTTagCompound compound = stack.getTag();
        if (compound == null)
            throw new CommandFailException("This item does not have any NBT data!");
        send(ChatColor.GREEN + "NBT >");
        for (String key : compound.c())
            send(ChatColor.YELLOW + key + ChatColor.GREEN + ": " + ChatColor.RESET + compound.get(key).toString().replaceAll("§", "&"));
    }
}
