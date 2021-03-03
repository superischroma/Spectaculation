package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.collection.ItemCollection;
import me.superischroma.spectaculation.collection.ItemCollectionReward;
import me.superischroma.spectaculation.collection.ItemCollectionRewards;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ItemCollectionGUI extends GUI
{
    private final ItemCollection collection;

    public ItemCollectionGUI(ItemCollection collection)
    {
        super(collection.getName() + " Collection", 54);
        this.collection = collection;
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        int amount = user.getCollection(collection);
        int tier = collection.getTier(amount);
        set(4, SUtil.getStack(ChatColor.YELLOW + collection.getName() + " " + SUtil.toRomanNumeral(tier),
                collection.getMaterial().getCraftMaterial(), collection.getData(), 1,
                ChatColor.GRAY + "View all your " + collection.getName() + " Collection",
                ChatColor.GRAY + "progress and rewards!", " ",
                ChatColor.GRAY + "Total Collected: " + ChatColor.YELLOW + SUtil.commaify(amount)));
        set(GUIClickableItem.getCloseItem(49));
        set(GUIClickableItem.createGUIOpenerItem(new CategoryCollectionGUI(collection.getCategory()), player,
                ChatColor.GREEN + "Go Back", 48, Material.ARROW, (short) 0,
                ChatColor.GRAY + "To " + collection.getCategory().getName() + " Collection"));
        for (int i = 0, slot = 18; i < collection.getRewards().size(); i++, slot++)
        {
            int t = i + 1;
            if (t == 28)
                break;
            ItemCollectionRewards rewards = collection.getRewards().get(i);
            if (rewards == null)
                continue;
            int finalSlot = slot;
            ChatColor color = ChatColor.RED;
            short data = 14;
            if (amount >= rewards.getRequirement())
            {
                color = ChatColor.GREEN;
                data = 5;
            }
            if (tier + 1 == t)
            {
                color = ChatColor.YELLOW;
                data = 4;
            }
            ChatColor finalColor = color;
            short finalData = data;
            List<String> lore = new ArrayList<>(Arrays.asList(" ",
                    SUtil.createProgressText("Progress", amount, rewards.getRequirement()),
                    SUtil.createLineProgressBar(20, ChatColor.DARK_GREEN, amount, rewards.getRequirement()), " "));
            if (rewards.size() != 0)
            {
                lore.add(ChatColor.GRAY + "Reward" + (rewards.size() != 1 ? "s" : "") + ":");
                for (ItemCollectionReward reward : rewards)
                    lore.add(ChatColor.GRAY + " " + reward.toRewardString());
                lore.add(" ");
            }
            lore.add(ChatColor.YELLOW + "Click to view rewards!");
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    // todo: rewards gui
                }

                @Override
                public int getSlot()
                {
                    return finalSlot;
                }

                @Override
                public ItemStack getItem()
                {
                    return SUtil.getStack(finalColor + collection.getName() + " " + SUtil.toRomanNumeral(t),
                            Material.STAINED_GLASS_PANE, finalData, t, lore);
                }
            });
        }
    }
}
