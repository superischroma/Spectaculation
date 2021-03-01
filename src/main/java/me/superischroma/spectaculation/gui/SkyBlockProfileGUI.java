package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.user.PlayerStatistics;
import me.superischroma.spectaculation.user.PlayerUtils;
import me.superischroma.spectaculation.util.SUtil;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class SkyBlockProfileGUI extends GUI
{
    public SkyBlockProfileGUI()
    {
        super("Your SkyBlock Profile", 54);
    }

    @Override
    public void onOpen(GUIOpenEvent e)
    {
        fill(BLACK_STAINED_GLASS_PANE);
        set(GUIClickableItem.createGUIOpenerItem(GUIType.SKYBLOCK_MENU, e.getPlayer(), ChatColor.GREEN + "Go Back", 48, Material.ARROW,
                ChatColor.GRAY + "To SkyBlock Menu"));
        set(GUIClickableItem.getCloseItem(49));
        Player player = e.getPlayer();
        PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
        set(4, SUtil.getSkullStack(ChatColor.GREEN + "Your SkyBlock Profile", player.getName(), 1,
                ChatColor.RED + "  ❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll()) + " HP",
                ChatColor.GREEN + "  ❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll()),
                ChatColor.RED + "  ❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll()),
                ChatColor.WHITE + "  ✦ Speed " + SUtil.commaify(((Double) (statistics.getSpeed().addAll() * 100.0)).intValue()),
                ChatColor.BLUE + "  ☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritChance().addAll() * 100.0)).intValue()) + "%",
                ChatColor.BLUE + "  ☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritDamage().addAll() * 100.0)).intValue()) + "%",
                ChatColor.AQUA + "  ✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll()),
                ChatColor.YELLOW + "  ⚔ Bonus Attack Speed " + ChatColor.RED + "✗",
                ChatColor.DARK_AQUA + "  α Sea Creature Chance " + ChatColor.RED + "✗",
                ChatColor.LIGHT_PURPLE + "  ♣ Pet Luck " + ChatColor.RED + "✗"));
        set(19, SUtil.getStack(ChatColor.RED + "❤ Health " + ChatColor.WHITE + SUtil.commaify(statistics.getMaxHealth().addAll()) + " HP", Material.GOLDEN_APPLE,
                (short) 0, 1,
                ChatColor.GRAY + "Health is your total maximum",
                ChatColor.GRAY + "health. Your natural",
                ChatColor.GRAY + "regeneration gives " + ChatColor.GREEN + SUtil.commaify(SUtil.roundTo((double) statistics.getMaxHealth().addAll() * 0.01, 1)) + " HP",
                ChatColor.GRAY + "every " + ChatColor.GREEN + "2s" + ChatColor.GRAY + "."));
        set(20, SUtil.getStack(ChatColor.GREEN + "❈ Defense " + ChatColor.WHITE + SUtil.commaify(statistics.getDefense().addAll()), Material.IRON_CHESTPLATE,
                (short) 0, 1,
                ChatColor.GRAY + "Defense reduces the damage that",
                ChatColor.GRAY + "you take from enemies.",
                " ",
                ChatColor.GRAY + "Damage Reduction: " + ChatColor.GREEN + SUtil.roundTo(((double) statistics.getDefense().addAll() / ((double) statistics.getDefense().addAll() + 100.0)) * 100.0,
                        1) + "%",
                ChatColor.GRAY + "Effective Health: " + ChatColor.RED +
                        SUtil.commaify(SUtil.roundTo((double) statistics.getMaxHealth().addAll() * (((double) statistics.getDefense().addAll() + 100.0) / 100.0), 1)) + "❤"));
        set(21, SUtil.getStack(ChatColor.RED + "❁ Strength " + ChatColor.WHITE + SUtil.commaify(statistics.getStrength().addAll()), Material.BLAZE_POWDER,
                (short) 0, 1,
                ChatColor.GRAY + "Strength increases your base",
                ChatColor.GRAY + "melee damage, including punching",
                ChatColor.GRAY + "and weapons.",
                " ",
                ChatColor.GRAY + "Base Damage: " + ChatColor.GREEN + SUtil.commaify((int) (5.0 + ((double) statistics.getStrength().addAll() / 5.0)))));
        set(22, SUtil.getStack(ChatColor.WHITE + "✦ Speed " + SUtil.commaify(((Double) (statistics.getSpeed().addAll() * 100.0)).intValue()), Material.SUGAR,
                (short) 0, 1,
                ChatColor.GRAY + "Speed increases your walk speed.",
                " ",
                ChatColor.GRAY + "You are " + ChatColor.GREEN + (((Double) (statistics.getSpeed().addAll() * 100.0)).intValue() - 100) + "% " + ChatColor.GRAY + "faster!"));
        set(23, SUtil.getSkullURLStack(ChatColor.BLUE + "☣ Crit Chance " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritChance().addAll() * 100.0)).intValue()) + "%",
                "3e4f49535a276aacc4dc84133bfe81be5f2a4799a4c04d9a4ddb72d819ec2b2b", 1,
                ChatColor.GRAY + "Crit Chance is your chance to",
                ChatColor.GRAY + "deal extra damage on enemies."));
        set(24, SUtil.getSkullURLStack(ChatColor.BLUE + "☠ Crit Damage " + ChatColor.WHITE + SUtil.commaify(((Double) (statistics.getCritDamage().addAll() * 100.0)).intValue()) + "%",
                "ddafb23efc57f251878e5328d11cb0eef87b79c87b254a7ec72296f9363ef7c", 1,
                ChatColor.GRAY + "Crit Damage is the amount of",
                ChatColor.GRAY + "extra damage you deal when",
                ChatColor.GRAY + "landing a critical hit."));
        set(25, SUtil.getStack(ChatColor.AQUA + "✎ Intelligence " + ChatColor.WHITE + SUtil.commaify(statistics.getIntelligence().addAll()), Material.ENCHANTED_BOOK,
                (short) 0, 1,
                ChatColor.GRAY + "Intelligence increases both your",
                ChatColor.GRAY + "Mana Pool and the damage of your",
                ChatColor.GRAY + "magical items.",
                " ",
                ChatColor.GRAY + "Magic Damage: +" + ChatColor.AQUA + SUtil.commaify(statistics.getIntelligence().addAll()) + "%",
                ChatColor.GRAY + "Mana Pool: " + ChatColor.AQUA + SUtil.commaify(statistics.getIntelligence().addAll() + 100)));
        set(29, SUtil.getStack(ChatColor.YELLOW + "⚔ Bonus Attack Speed " + ChatColor.RED + "✗", Material.GOLD_AXE,
                (short) 0, 1,
                ChatColor.GRAY + "Bonus Attack Speed decreases the",
                ChatColor.GRAY + "time between hits on your",
                ChatColor.GRAY + "opponent."));
        set(30, SUtil.getStack(ChatColor.DARK_AQUA + "α Sea Creature Chance " + ChatColor.RED + "✗", Material.PRISMARINE_CRYSTALS,
                (short) 0, 1,
                ChatColor.GRAY + "Sea Creature Chance is your",
                ChatColor.GRAY + "chance to catch Sea Creatures",
                ChatColor.GRAY + "while fishing."));
        set(31, SUtil.getSkullURLStack(ChatColor.LIGHT_PURPLE + "♣ Pet Luck " + ChatColor.RED + "✗",
                "93c8aa3fde295fa9f9c27f734bdbab11d33a2e43e855accd7465352377413b", 1,
                ChatColor.GRAY + "Pet Luck increases how many pets",
                ChatColor.GRAY + "you find and gives you better",
                ChatColor.GRAY + "luck when crafting pets."));
    }
}