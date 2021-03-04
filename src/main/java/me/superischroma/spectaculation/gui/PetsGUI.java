package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.pet.Pet;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.PaginationList;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class PetsGUI extends GUI
{
    private static final int[] INTERIOR = new int[]{
            10, 11, 12, 13, 14, 15, 16,
            19, 20, 21, 22, 23, 24, 25,
            28, 29, 30, 31, 32, 33, 34,
            37, 38, 39, 40, 41, 42, 43
    };

    private int page;
    private boolean pickup;

    public PetsGUI(int page, boolean pickup)
    {
        super("Pets", 54);
        this.page = page;
        this.pickup = pickup;
    }

    public PetsGUI(boolean pickup)
    {
        this(1, pickup);
    }

    public PetsGUI()
    {
        this(false);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        Player player = e.getPlayer();
        User user = User.getUser(player.getUniqueId());
        border(BLACK_STAINED_GLASS_PANE);
        PaginationList<Pet.PetItem> paged = new PaginationList<>(28);
        paged.addAll(user.getPets());
        if (paged.size() == 0) page = 0;
        int finalPage = page;
        if (page > 1)
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new PetsGUI(finalPage - 1, false).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 45;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "<-");
                }
            });
        }
        if (page != paged.getPageCount())
        {
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    new PetsGUI(finalPage + 1, false).open((Player) e.getWhoClicked());
                }
                @Override
                public int getSlot()
                {
                    return 53;
                }
                @Override
                public ItemStack getItem()
                {
                    return SUtil.createNamedItemStack(Material.ARROW, ChatColor.GRAY + "->");
                }
            });
        }
        Pet.PetItem active = user.getActivePet();
        String name;
        if (active == null)
            name = ChatColor.RED + "None";
        else
            name = active.getRarity().getColor() + active.getType().getDisplayName(active.getType().getData());
        set(4, SUtil.getStack(ChatColor.GREEN + "Pets", Material.BONE, (short) 0, 1,
            ChatColor.GRAY + "View and manage all of your",
            ChatColor.GRAY + "Pets.",
            " ",
            ChatColor.GRAY + "Level up your pets faster by",
            ChatColor.GRAY + "gaining XP in their favorite",
            ChatColor.GRAY + "skill!",
            " ",
            ChatColor.GRAY + "Selected pet: " + name));
        set(47, SUtil.getStack(ChatColor.GREEN + "Pet Score Rewards", Material.DIAMOND, (short) 0, 1,
                ChatColor.GRAY + "Pet score is calculated based",
                ChatColor.GRAY + "on how many " + ChatColor.GREEN + "unique" + ChatColor.GRAY + " pets you",
                ChatColor.GRAY + "have and the " + ChatColor.GREEN + "rarity" + ChatColor.GRAY + " of these",
                ChatColor.GRAY + "pets.",
                " ",
                ChatColor.GOLD + "10 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "1 Magic Find",
                ChatColor.GOLD + "25 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "2 Magic Find",
                ChatColor.GOLD + "50 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "3 Magic Find",
                ChatColor.GOLD + "75 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "4 Magic Find",
                ChatColor.GOLD + "100 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "5 Magic Find",
                ChatColor.GOLD + "130 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "6 Magic Find",
                ChatColor.GOLD + "175 Score: " + ChatColor.GRAY + "+" + ChatColor.AQUA + "7 Magic Find",
                " ",
                ChatColor.BLUE + "Your Pet Score: " + ChatColor.RED + "Coming soon!"));
        set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, player, ChatColor.GREEN + "Go Back", 48,
                Material.ARROW, ChatColor.GRAY + "To SkyBlock Menu"));
        set(GUIClickableItem.getCloseItem(49));
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                new PetsGUI(page, !pickup).open(player);
            }

            @Override
            public int getSlot()
            {
                return 50;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.GREEN + "Convert Pet to an Item", Material.INK_SACK, pickup ? (short) 10 : (short) 8, 1,
                        ChatColor.GRAY + "Enable this setting and",
                        ChatColor.GRAY + "click any pet to convert it",
                        ChatColor.GRAY + "to an item.",
                        " ",
                        pickup ? ChatColor.GREEN + "Enabled" : ChatColor.RED + "Disabled");
            }
        });
        set(new GUIClickableItem()
        {
            @Override
            public void run(InventoryClickEvent e)
            {
                // todo: add pet visibility toggle
            }

            @Override
            public int getSlot()
            {
                return 51;
            }

            @Override
            public ItemStack getItem()
            {
                return SUtil.getStack(ChatColor.RED + "Hide Pets", Material.STONE_BUTTON, (short) 0, 1,
                        ChatColor.GRAY + "Hide all pets which are",
                        ChatColor.GRAY + "little heads from being",
                        ChatColor.GRAY + "visible in the world.",
                        " ",
                        ChatColor.GRAY + "Pet effects remain active.",
                        " ",
                        ChatColor.GRAY + "Currently: " + ChatColor.RED + "Pets hidden!",
                        ChatColor.GRAY + "Selected pet: " + name,
                        " ",
                        ChatColor.RED + "Coming soon!");
            }
        });
        List<Pet.PetItem> p = paged.getPage(page);
        if (p == null) return;
        for (int i = 0; i < p.size(); i++)
        {
            int slot = INTERIOR[i];
            Pet.PetItem pet = p.get(i);
            String n = pet.getRarity().getColor() + pet.getType().getDisplayName(pet.getType().getData());
            SItem item = SItem.of(pet.getType());
            item.setRarity(pet.getRarity());
            item.setDataDouble("xp", pet.getXp());
            item.getData().setBoolean("equipped", true);
            item.update();
            if (!pickup)
            {
                ItemMeta meta = item.getStack().getItemMeta();
                List<String> lore = meta.getLore();
                lore.add(" ");
                if (pet.isActive())
                    lore.add(ChatColor.RED + "Click to despawn");
                else
                    lore.add(ChatColor.YELLOW + "Click to summon");
                meta.setLore(lore);
                item.getStack().setItemMeta(meta);
            }
            set(new GUIClickableItem()
            {
                @Override
                public void run(InventoryClickEvent e)
                {
                    if (pickup)
                    {
                        SItem n = SItem.of(pet.getType());
                        n.setRarity(pet.getRarity());
                        n.setDataDouble("xp", pet.getXp());
                        player.getInventory().addItem(n.getStack());
                        pet.setActive(false);
                        user.removePet(pet);
                        new PetsGUI(page, false).open(player);
                        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1f, 1f);
                        return;
                    }
                    if (pet.isActive())
                    {
                        pet.setActive(false);
                        player.closeInventory();
                        player.sendMessage(ChatColor.GREEN + "You despawned your " + n + ChatColor.GREEN + "!");
                        return;
                    }
                    user.equipPet(pet);
                    player.closeInventory();
                    player.sendMessage(ChatColor.GREEN + "You spawned your " + n + ChatColor.GREEN + "!");
                }

                @Override
                public int getSlot()
                {
                    return slot;
                }

                @Override
                public ItemStack getItem()
                {
                    return item.getStack();
                }
            });
        }
    }
}