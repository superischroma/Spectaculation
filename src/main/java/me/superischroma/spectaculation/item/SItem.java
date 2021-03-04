package me.superischroma.spectaculation.item;

import lombok.Getter;
import me.superischroma.spectaculation.enchantment.Enchantment;
import me.superischroma.spectaculation.enchantment.EnchantmentType;
import me.superischroma.spectaculation.item.armor.LeatherArmorStatistics;
import me.superischroma.spectaculation.potion.PotionEffect;
import me.superischroma.spectaculation.potion.PotionEffectType;
import me.superischroma.spectaculation.reforge.Reforge;
import me.superischroma.spectaculation.reforge.ReforgeType;
import me.superischroma.spectaculation.util.SLog;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.Item;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.*;

@Getter
public class SItem implements Cloneable
{
    private static final List<String> GLOBAL_NBT_TAGS = Arrays.asList("type", "rarity", "origin", "recombobulated");

    private final SMaterial type;
    private final short variant;
    private ItemStack stack;
    private ItemLore lore;
    private Rarity rarity;
    private ItemOrigin origin;
    private boolean recombobulated;
    private NBTTagCompound data;

    protected SItem(SMaterial type, short variant, ItemStack stack, Rarity rarity, ItemOrigin origin, boolean recombobulated, NBTTagCompound data, boolean overwrite)
    {
        this.type = type;
        this.variant = variant;
        this.stack = stack;
        this.rarity = rarity;
        this.data = data;
        this.lore = new ItemLore(this);
        this.origin = origin;
        this.recombobulated = recombobulated;
        if (overwrite)
        {
            ItemMeta meta = this.stack.getItemMeta();
            if (!(type.getStatistics() instanceof LoreableMaterialStatistics))
                meta.setLore(this.lore.asBukkitLore());
            else
                meta.setLore(((LoreableMaterialStatistics) type.getStatistics()).getCustomLore(this));
            meta.addItemFlags(ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS);
            meta.spigot().setUnbreakable(true);
            this.stack.setItemMeta(meta);
            update();
        }
    }

    public void enchant(boolean enchant)
    {
        if (enchant)
        {
            if (stack.getItemMeta().hasEnchants())
                return;
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
            stack.setItemMeta(meta);
            return;
        }
        if (!stack.getItemMeta().hasEnchants())
            return;
        ItemMeta meta = stack.getItemMeta();
        meta.removeEnchant(org.bukkit.enchantments.Enchantment.DURABILITY);
        stack.setItemMeta(meta);
    }

    public boolean addEnchantment(EnchantmentType type, int level)
    {
        if (!isEnchantable())
            return false;
        List<Enchantment> enchantments = getEnchantments();
        Enchantment enchantment = new Enchantment(type, level);
        removeEnchantment(type);
        enchantments.add(enchantment);
        if (type.getVanilla() != null)
        {
            ItemMeta meta = stack.getItemMeta();
            meta.addEnchant(type.getVanilla(), level, true);
            stack.setItemMeta(meta);
        }
        NBTTagCompound es = data.getCompound("enchantments");
        for (Enchantment e : enchantments)
            es.setInt(e.getType().getNamespace(), e.getLevel());
        data.set("enchantments", es);
        update();
        return true;
    }

    public boolean removeEnchantment(EnchantmentType type)
    {
        if (!isEnchantable())
            return false;
        List<Enchantment> enchantments = getEnchantments();
        boolean removeIf = enchantments.removeIf(e -> e.getType().equals(type));
        if (type.getVanilla() != null)
        {
            ItemMeta meta = stack.getItemMeta();
            meta.removeEnchant(type.getVanilla());
            stack.setItemMeta(meta);
        }
        NBTTagCompound es = new NBTTagCompound();
        for (Enchantment enchantment : enchantments)
            es.setInt(enchantment.getType().getNamespace(), enchantment.getLevel());
        data.set("enchantments", es);
        update();
        return removeIf;
    }

    public boolean hasEnchantment(EnchantmentType type)
    {
        if (!isEnchantable())
            return false;
        List<Enchantment> enchantments = getEnchantments();
        for (Enchantment enchantment : enchantments)
        {
            if (enchantment.getType() == type)
                return true;
        }
        return false;
    }

    public Enchantment getEnchantment(EnchantmentType type)
    {
        if (!isEnchantable())
            return null;
        List<Enchantment> enchantments = getEnchantments();
        for (Enchantment enchantment : enchantments)
        {
            if (enchantment.getType() == type)
                return enchantment;
        }
        return null;
    }

    public List<Enchantment> getEnchantments()
    {
        if (!isEnchantable())
            return null;
        NBTTagCompound es = data.hasKey("enchantments") ? data.getCompound("enchantments") : new NBTTagCompound();
        List<Enchantment> enchantments = new ArrayList<>();
        for (String key : es.c())
            enchantments.add(new Enchantment(EnchantmentType.getByNamespace(key), es.getInt(key)));
        return enchantments;
    }

    public boolean addPotionEffect(PotionEffect effect)
    {
        if (!isPotion())
            return false;
        List<PotionEffect> effects = getPotionEffects();
        removePotionEffect(effect.getType());
        effects.add(effect);
        NBTTagCompound es = data.getCompound("effects");
        for (PotionEffect e : effects)
            es.set(e.getType().getNamespace(), e.toCompound());
        data.set("effects", es);
        update();
        return true;
    }

    public boolean removePotionEffect(PotionEffectType type)
    {
        if (!isPotion())
            return false;
        List<PotionEffect> effects = getPotionEffects();
        boolean removeIf = effects.removeIf(e -> e.getType().equals(type));
        PotionMeta meta = (PotionMeta) stack.getItemMeta();
        meta.removeCustomEffect(type.getColor());
        stack.setItemMeta(meta);
        NBTTagCompound es = data.getCompound("effects");
        for (PotionEffect e : effects)
            es.set(e.getType().getNamespace(), e.toCompound());
        data.set("effects", es);
        update();
        return removeIf;
    }

    public PotionEffect getPotionEffect(PotionEffectType type)
    {
        if (!isPotion())
            return null;
        List<PotionEffect> effects = getPotionEffects();
        for (PotionEffect effect : effects)
        {
            if (effect.getType() == type)
                return effect;
        }
        return null;
    }

    public List<PotionEffect> getPotionEffects()
    {
        if (!isPotion())
            return null;
        NBTTagCompound es = data.hasKey("effects") ? data.getCompound("effects") : new NBTTagCompound();
        List<PotionEffect> effects = new ArrayList<>();
        for (String key : es.c())
            effects.add(PotionEffect.ofCompound(key, es.getCompound(key)));
        return effects;
    }

    public boolean isEnchantable()
    {
        return type.getGenericInstance() instanceof Enchantable;
    }

    public boolean isReforgable()
    {
        return type.getGenericInstance() instanceof Reforgable;
    }

    public boolean isPotion()
    {
        return type == SMaterial.WATER_BOTTLE;
    }

    public void setAnvilUses(int anvilUses)
    {
        if (!(type.getGenericInstance() instanceof Enchantable))
            throw new UnsupportedOperationException("You cannot set the anvil uses on an unenchantable item");
        data.setInt("anvil", anvilUses);
        update();
    }

    public void setKills(Integer kills)
    {
        if (!type.getStatistics().displayKills())
            throw new UnsupportedOperationException("You cannot display kills on this item");
        data.setInt("kills", kills);
        update();
    }

    public void setRarity(Rarity rarity, boolean instanceUpdate)
    {
        this.rarity = rarity;
        update(instanceUpdate);
    }

    public void setRarity(Rarity rarity)
    {
        setRarity(rarity, true);
    }

    public void setAmount(int amount)
    {
        this.stack.setAmount(amount);
    }

    public void upgradeRarity()
    {
        this.rarity = this.rarity.upgrade();
        update();
    }

    public void downgradeRarity()
    {
        this.rarity = this.rarity.downgrade();
        update();
    }

    public void setReforge(Reforge reforge)
    {
        if (!(type.getGenericInstance() instanceof Reforgable))
            throw new UnsupportedOperationException("You cannot set the reforge of an unreforgable item");
        data.setString("reforge", ReforgeType.getByClass(reforge.getClass()).name());
        update();
    }

    public void setOrigin(ItemOrigin origin)
    {
        this.origin = origin;
        update();
    }

    public void setRecombobulated(boolean recombobulated)
    {
        this.recombobulated = recombobulated;
        if (recombobulated)
            setRarity(type.getStatistics().getRarity().upgrade());
        else
            setRarity(type.getStatistics().getRarity());
        update();
    }

    public Reforge getReforge()
    {
        if (!(type.getGenericInstance() instanceof Reforgable))
            return null;
        if (!data.hasKey("reforge"))
            return null;
        return ReforgeType.getReforgeType(data.getString("reforge")).getReforge();
    }

    public String getFullName()
    {
        return this.rarity.getColor() +
                (data.hasKey("reforge") ? ReforgeType.getReforgeType(data.getString("reforge")).getReforge().getName() + " " : "") +
                this.getType().getDisplayName(variant);
    }

    public String getDataString(String key)
    {
        return data.getString(key);
    }

    public int getDataInt(String key)
    {
        return data.getInt(key);
    }

    public long getDataLong(String key)
    {
        return data.getLong(key);
    }

    public NBTTagCompound getDataCompound(String key)
    {
        return data.getCompound(key);
    }

    public void setDataString(String key, String value)
    {
        data.setString(key, value);
        update();
    }

    public void setDataInt(String key, int value)
    {
        data.setInt(key, value);
        update();
    }

    public void setDataDouble(String key, double value)
    {
        data.setDouble(key, value);
        update();
    }

    public void setDataFloat(String key, float value)
    {
        data.setFloat(key, value);
        update();
    }

    public void setDataLong(String key, long value)
    {
        data.setLong(key, value);
        update();
    }

    public void setDataBoolean(String key, boolean value)
    {
        data.setBoolean(key, value);
        update();
    }

    public void setDataCompound(String key, NBTTagCompound value)
    {
        data.set(key, value);
        update();
    }

    public void removeData(String key)
    {
        data.remove(key);
        update();
    }

    public boolean hasDataFor(String key)
    {
        return data.hasKey(key);
    }

    public void setDisplayName(String name)
    {
        Reforge reforge = null;
        if (data.hasKey("reforge"))
            reforge = ReforgeType.getReforgeType(data.getString("reforge")).getReforge();
        ItemMeta meta = stack.getItemMeta();
        if (reforge == null)
            meta.setDisplayName(this.rarity.getColor() + name);
        else
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + name);
        stack.setItemMeta(meta);
    }

    public void update(boolean instanceUpdate)
    {
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(this.stack);
        if (nmsStack == null)
            SLog.info(stack);
        NBTTagCompound compound = nmsStack.getTag() != null ? nmsStack.getTag() : new NBTTagCompound();
        compound.remove("type");
        compound.remove("variant");
        compound.remove("enchantments");
        compound.remove("anvil");
        compound.remove("rarity");
        compound.remove("kills");
        compound.remove("reforge");
        compound.remove("origin");
        compound.remove("recombobulated");

        for (String key : data.c())
        {
            compound.remove(key);
            if (!data.get(key).isEmpty())
                compound.set(key, data.get(key));
        }
        compound.remove("amount");
        compound.setString("type", type.name());
        if (variant != 0)
            compound.setShort("variant", variant);
        if (this.rarity != type.getStatistics().getRarity())
            compound.setString("rarity", rarity.name());
        if (origin != ItemOrigin.UNKNOWN)
            compound.setString("origin", origin.name());
        if (this.recombobulated)
            compound.setBoolean("recombobulated", true);
        if (!this.getType().getStatistics().isStackable() && !compound.hasKey("uuid"))
            compound.setString("uuid", UUID.randomUUID().toString());
        nmsStack.setTag(compound);
        stack.setItemMeta(CraftItemStack.getItemMeta(nmsStack));
        ItemMeta meta = stack.getItemMeta();
        MaterialStatistics statistics = type.getStatistics();
        Reforge reforge = null;
        if (data.hasKey("reforge"))
            reforge = ReforgeType.getReforgeType(data.getString("reforge")).getReforge();
        if (reforge == null)
            meta.setDisplayName(this.rarity.getColor() + type.getDisplayName(variant));
        else
            meta.setDisplayName(this.rarity.getColor() + reforge.getName() + " " + type.getDisplayName(variant));
        if (isPotion())
        {
            for (PotionEffect effect : getPotionEffects())
            {
                PotionEffectType type = effect.getType();
                PotionMeta potionMeta = (PotionMeta) stack.getItemMeta();
                potionMeta.addCustomEffect(type.getColor().createEffect((int) effect.getDuration(), effect.getLevel() - 1), true);
            }
        }
        if (!(statistics instanceof LoreableMaterialStatistics))
            meta.setLore(lore.asBukkitLore());
        else
            meta.setLore(((LoreableMaterialStatistics) statistics).getCustomLore(this));
        stack.setItemMeta(meta);
        if (type.getGenericInstance() instanceof Enchantable || statistics.isEnchanted())
            enchant(data.getCompound("enchantments").c().size() != 0 || statistics.isEnchanted());
        MaterialFunction function = type.getFunction();
        if (function != null && instanceUpdate)
            type.getFunction().onInstanceUpdate(this);
    }

    public void update()
    {
        update(true);
    }

    public static SItem of(SMaterial specMaterial, short variant, ItemOrigin origin)
    {
        ItemStack stack = new ItemStack(specMaterial.getCraftMaterial(), 1, variant);
        MaterialStatistics statistics = specMaterial.getStatistics();
        if (specMaterial.getCraftMaterial() == Material.SKULL_ITEM &&
                statistics instanceof SkullStatistics)
        {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, specMaterial);
        }
        if (statistics instanceof LeatherArmorStatistics)
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(statistics.getRarity().getColor() + specMaterial.getDisplayName(variant));
        stack.setItemMeta(meta);
        return new SItem(specMaterial, variant, stack,
                statistics.getRarity(), origin, false,
                specMaterial.getItemData() != null ? specMaterial.getItemData().getData() : new NBTTagCompound(),
                true);
    }

    public static SItem of(SMaterial specMaterial, ItemOrigin origin)
    {
        return of(specMaterial, specMaterial.getData(), origin);
    }

    public static SItem of(SMaterial specMaterial, short variant)
    {
        return of(specMaterial, variant, ItemOrigin.UNKNOWN);
    }

    public static SItem of(SMaterial specMaterial)
    {
        return of(specMaterial, specMaterial.getData());
    }

    public static SItem of(ItemStack stack, ItemOrigin origin)
    {
        if (stack == null)
            return null;
        SMaterial material = SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability());
        if (material == null)
            return null;
        if (Item.getById(material.getCraftMaterial().getId()) == null)
            return null;
        SItem n = of(material, stack.getDurability(), origin);
        n.getStack().setAmount(stack.getAmount());
        return n;
    }

    public static SItem of(ItemStack stack)
    {
        return of(stack, ItemOrigin.UNKNOWN);
    }

    public static boolean isSpecItem(ItemStack stack)
    {
        if (stack == null) return false;
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        if (nmsStack == null) return false;
        if (!nmsStack.hasTag()) return false;
        NBTTagCompound compound = nmsStack.getTag();
        return compound.hasKey("type");
    }

    @Override
    public SItem clone()
    {
        return new SItem(type, variant, stack.clone(), rarity, origin, recombobulated, data, true);
    }

    public NBTTagCompound toCompound()
    {
        NBTTagCompound compound = new NBTTagCompound();
        for (String key : data.c())
        {
            compound.remove(key);
            compound.set(key, data.get(key));
        }
        compound.setString("type", type.name());
        if (variant != 0)
            compound.setShort("variant", variant);
        compound.setInt("amount", stack.getAmount());
        if (this.rarity != type.getStatistics().getRarity())
            compound.setString("rarity", rarity.name());
        if (origin != ItemOrigin.UNKNOWN)
            compound.setString("origin", origin.name());
        if (this.recombobulated)
            compound.setBoolean("recombobulated", true);
        if (!this.getType().getStatistics().isStackable() && !compound.hasKey("uuid"))
            compound.setString("uuid", UUID.randomUUID().toString());
        return compound;
    }

    public static SItem find(ItemStack stack)
    {
        if (stack == null) return null;
        if (!isSpecItem(stack)) return null;
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(stack);
        NBTTagCompound compound = nmsStack.getTag();
        if (compound == null) return null;
        return of(compound, stack);
    }

    public static SItem of(NBTTagCompound compound)
    {
        SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        ItemStack stack = new ItemStack(type.getCraftMaterial(), compound.hasKey("amount") ? compound.getInt("amount") : 1, type.getData());
        MaterialStatistics statistics = type.getStatistics();
        if (type.getCraftMaterial() == Material.SKULL_ITEM &&
                statistics instanceof SkullStatistics)
        {
            stack.setDurability((short) 3);
            SUtil.getSkull(((SkullStatistics) statistics).getURL(), stack, type);
        }
        if (statistics instanceof LeatherArmorStatistics)
            SUtil.applyColorToLeatherArmor(stack, Color.fromRGB(((LeatherArmorStatistics) statistics).getColor()));
        ItemMeta meta = stack.getItemMeta();
        short variant = compound.hasKey("variant") ? compound.getShort("variant") : 0;
        meta.setDisplayName(statistics.getRarity().getColor() + type.getDisplayName(variant));
        stack.setItemMeta(meta);
        NBTTagCompound data = new NBTTagCompound();
        for (String key : compound.c())
        {
            if (GLOBAL_NBT_TAGS.contains(key))
                continue;
            data.set(key, compound.get(key));
        }
        return new SItem(type, variant, stack,
                compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : statistics.getRarity(),
                compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN,
                compound.getBoolean("recombobulated"),
                data,
                true);
    }

    public static SItem convert(ItemStack stack)
    {
        return SUtil.setSItemAmount(SItem.of(SMaterial.getSpecEquivalent(stack.getType(), stack.getDurability())), stack.getAmount());
    }

    private static SItem of(NBTTagCompound compound, ItemStack stack)
    {
        SMaterial type = SMaterial.getMaterial(compound.getString("type"));
        NBTTagCompound data = new NBTTagCompound();
        for (String key : compound.c())
        {
            if (GLOBAL_NBT_TAGS.contains(key))
                continue;
            data.set(key, compound.get(key));
        }
        return new SItem(type, compound.hasKey("variant") ? compound.getShort("variant") : 0, stack,
                (compound.hasKey("rarity") ? Rarity.getRarity(compound.getString("rarity")) : type.getStatistics().getRarity()),
                compound.hasKey("origin") ? ItemOrigin.valueOf(compound.getString("origin")) : ItemOrigin.UNKNOWN,
                compound.hasKey("recombobulated"), data, false);
    }
}