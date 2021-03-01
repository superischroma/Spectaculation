package me.superischroma.spectaculation.item;

import me.superischroma.spectaculation.enchantment.Enchantment;
import me.superischroma.spectaculation.item.armor.ArmorSet;
import me.superischroma.spectaculation.item.orb.OrbBuff;
import me.superischroma.spectaculation.reforge.Reforge;
import me.superischroma.spectaculation.user.User;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ItemLore
{
    private static final String SPEC_ID = ChatColor.DARK_GRAY + "Spectaculation ID: %s";

    private SItem parent;

    public ItemLore(SItem parent)
    {
        this.parent = parent;
    }

    public List<String> asBukkitLore()
    {
        List<String> lore = new ArrayList<>();
        List<Enchantment> enchantments = parent.getEnchantments();
        SMaterial material = parent.getType();
        MaterialStatistics statistics = material.getStatistics();
        Reforge reforge = parent.getReforge() == null ? Reforge.blank() : parent.getReforge();
        if (statistics instanceof PlayerBoostStatistics)
        {
            PlayerBoostStatistics playerBoostStatistics = (PlayerBoostStatistics) material.getStatistics();
            boolean damage;
            Player player = null;
            try
            {
                player = Bukkit.getPlayer(UUID.fromString(parent.getDataString("owner")));
            }
            catch (IllegalArgumentException ignored) {}
            if (parent.getType() == SMaterial.EMERALD_BLADE && player != null &&
                    SUtil.roundTo(2.5 * SUtil.quadrt(User.getUser(player.getUniqueId()).getCoins()), 1) != 0.0)
            {
                damage = addPossiblePropertyDouble("Damage", playerBoostStatistics.getBaseDamage() +
                        SUtil.roundTo(2.5 * SUtil.quadrt(User.getUser(player.getUniqueId()).getCoins()), 1),
                        0, "", false, lore);
            }
            else
                damage = addPossiblePropertyInt("Damage", playerBoostStatistics.getBaseDamage(), "", false, lore);
            boolean strength = addPossiblePropertyInt("Strength", playerBoostStatistics.getBaseStrength(),
                    reforge.getStrength().getForRarity(parent.getRarity()), "", false, lore);
            boolean critChance = addPossiblePropertyInt("Crit Chance", (int) (playerBoostStatistics.getBaseCritChance() * 100),
                    (int) (reforge.getCritChance().getForRarity(parent.getRarity()) * 100), "%", false, lore);
            boolean critDamage = addPossiblePropertyInt("Crit Damage", (int) (playerBoostStatistics.getBaseCritDamage() * 100),
                    (int) (reforge.getCritDamage().getForRarity(parent.getRarity()) * 100), "%", false, lore);
            if (damage || strength || critChance || critDamage) lore.add("");
            boolean health = addPossiblePropertyInt("Health", playerBoostStatistics.getBaseHealth(), " HP", true, lore);
            boolean defense = addPossiblePropertyInt("Defense", playerBoostStatistics.getBaseDefense(), "", true, lore);
            boolean speed = addPossiblePropertyInt("Speed", (int) (playerBoostStatistics.getBaseSpeed() * 100), "", true, lore);
            boolean intelligence = addPossiblePropertyInt("Intelligence", playerBoostStatistics.getBaseIntelligence(),
                    reforge.getIntelligence().getForRarity(parent.getRarity()),"", true, lore);
            boolean magicFind = addPossiblePropertyInt("Magic Find", (int) (playerBoostStatistics.getBaseMagicFind() * 100), "", true, lore);
            if (health || defense || speed || intelligence || magicFind) lore.add("");
        }
        if (enchantments != null && enchantments.size() != 0)
        {
            int amount = enchantments.size();
            List<String> stringEnchantments = new ArrayList<>();
            for (Enchantment enchantment : enchantments)
                stringEnchantments.add(enchantment.getDisplayName());
            if (amount <= 5)
            {
                for (Enchantment enchantment : enchantments)
                {
                    lore.add(enchantment.getDisplayName());
                    for (String line : SUtil.splitByWordAndLength(enchantment.getDescription(), 30, "\\s"))
                        lore.add(ChatColor.GRAY + line);
                }
            }
            else if (amount <= 10)
            {
                for (Enchantment enchantment : enchantments)
                    lore.add(enchantment.getDisplayName());
            }
            else if (amount <= 25)
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 2));
            else
                lore.addAll(SUtil.combineElements(stringEnchantments, ", ", 3));
            lore.add("");
        }
        ArmorSet set = SMaterial.findArmorSet(material);
        if (set != null)
        {
            lore.add(ChatColor.GOLD + "Full Set Bonus: " + set.getName());
            for (String line : SUtil.splitByWordAndLength(set.getDescription(), 30, "\\s"))
                lore.add(ChatColor.GRAY + line);
            lore.add("");
        }
        Ability ability = material.getAbility();
        if (ability != null)
        {
            StringBuilder abilityTitle = new StringBuilder()
                    .append(ChatColor.GOLD).append("Item Ability: ").append(ability.getAbilityName());
            switch (ability.getAbilityActivation())
            {
                case RIGHT_CLICK:
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("RIGHT CLICK");
                    break;
                case LEFT_CLICK:
                    abilityTitle.append(" ").append(ChatColor.YELLOW).append(ChatColor.BOLD).append("LEFT CLICK");
                    break;
            }
            lore.add(abilityTitle.toString());
            for (String line : SUtil.splitByWordAndLength(ability.getAbilityDescription(), 30, "\\s"))
                lore.add(ChatColor.GRAY + line);
            if (ability.getManaCost() > 0)
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + ability.getManaCost());
            if (ability.getManaCost() == -1)
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "All");
            if (ability.getManaCost() == -2)
                lore.add(ChatColor.DARK_GRAY + "Mana Cost: " + ChatColor.DARK_AQUA + "Half");
            lore.add("");
        }
        OrbBuff buff = material.getOrbBuff();
        if (buff != null)
        {
            lore.add(parent.getRarity().getColor() + "Orb Buff: " + buff.getBuffName());
            for (String line : SUtil.splitByWordAndLength(buff.getBuffDescription(), 30, "\\s"))
                lore.add(ChatColor.GRAY + line);
            lore.add("");
        }
        int kills = parent.getDataInt("kills");
        if (statistics.displayKills())
        {
            lore.add(ChatColor.GOLD + "Kills: " + ChatColor.AQUA + kills);
            lore.add("");
        }
        String l = this.parent.getType().getStatistics().getLore();
        if (l != null)
        {
            for (String string : SUtil.splitByWordAndLength(l, 30, "\\s"))
                lore.add(ChatColor.GRAY + string);
            if (l.length() != 0)
                lore.add("");
        }
        List<String> ll = this.parent.getType().getStatistics().getListLore();
        if (ll != null)
        {
            for (String line : ll)
                lore.add(ChatColor.GRAY + line);
            if (ll.size() != 0)
                lore.add("");
        }
        if (parent.getDataInt("anvil") != 0)
            lore.add(ChatColor.GRAY + "Anvil Uses: " + ChatColor.RED + parent.getDataInt("anvil"));
        if (material.getItemData() != null)
        {
            NBTTagCompound compound = parent.getData();
            for (String key : compound.c())
            {
                List<String> dl = material.getItemData().getDataLore(key, SUtil.getObjectFromCompound(parent.getData(), key));
                if (dl != null)
                {
                    lore.addAll(dl);
                    lore.add("");
                }
            }
        }
        SpecificItemType type = statistics.getSpecificType();
        if (statistics.displayRarity())
        {
            lore.add((parent.isRecombobulated() ? parent.getRarity().getBoldedColor() + ChatColor.MAGIC + "D" + ChatColor.RESET + " " : "") +
                    parent.getRarity().getDisplay() + (type != SpecificItemType.NONE ? " " + type.getName() : "") +
                    (parent.isRecombobulated() ? parent.getRarity().getBoldedColor() + " " + ChatColor.MAGIC + "D" + ChatColor.RESET : ""));
        }
        //lore.add(String.format(SPEC_ID, parent.getType().name()));
        return lore;
    }

    private boolean addPossiblePropertyInt(String name, int i, int r, String succeeding, boolean green, List<String> list)
    {
        i += r;
        if (i == 0) return false;
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ")
                .append(green ? ChatColor.GREEN : ChatColor.RED)
                .append(i < 0 ? "" : "+").append(i).append(succeeding);
        if (r != 0)
        {
            builder.append(ChatColor.BLUE).append(" (").append(parent.getReforge().getName()).append(" ")
                    .append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
        }
        list.add(builder.toString());
        return true;
    }

    private boolean addPossiblePropertyInt(String name, int i, String succeeding, boolean green, List<String> list)
    {
        return addPossiblePropertyInt(name, i, 0, succeeding, green, list);
    }

    private boolean addPossiblePropertyDouble(String name, double d, int r, String succeeding, boolean green, List<String> list)
    {
        d += r;
        if (d == 0.0) return false;
        StringBuilder builder = new StringBuilder();
        builder.append(ChatColor.GRAY).append(name).append(": ")
                .append(green ? ChatColor.GREEN : ChatColor.RED)
                .append(d < 0.0 ? "" : "+").append(d).append(succeeding);
        if (r != 0)
        {
            builder.append(ChatColor.BLUE).append(" (").append(parent.getReforge().getName()).append(" ")
                    .append(r < 0 ? "" : "+").append(r).append(succeeding).append(")");
        }
        list.add(builder.toString());
        return true;
    }
}