package me.superischroma.spectaculation.util;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.MaxChangedBlocksException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.extent.clipboard.io.ClipboardFormat;
import com.sk89q.worldedit.function.operation.Operation;
import com.sk89q.worldedit.function.operation.Operations;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldedit.world.registry.WorldData;
import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.enchantment.Enchantment;
import me.superischroma.spectaculation.gui.GUI;
import me.superischroma.spectaculation.item.*;
import me.superischroma.spectaculation.potion.PotionColor;
import me.superischroma.spectaculation.potion.PotionEffect;
import net.minecraft.server.v1_8_R3.*;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.craftbukkit.v1_8_R3.util.UnsafeList;
import org.bukkit.entity.*;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.NPC;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.io.*;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class SUtil
{
    public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM/dd/yy");

    private static final NumberFormat COMMA_FORMAT = NumberFormat.getInstance();
    private static final List<ChatColor> CRIT_SPECTRUM = Arrays.asList(ChatColor.WHITE, ChatColor.WHITE, ChatColor.YELLOW, ChatColor.GOLD,
            ChatColor.RED, ChatColor.RED);
    private static final List<ChatColor> VISIBLE_COLOR_SPECTRUM = Arrays.asList(ChatColor.DARK_GREEN, ChatColor.DARK_AQUA, ChatColor.DARK_RED,
            ChatColor.DARK_PURPLE, ChatColor.GOLD, ChatColor.GREEN, ChatColor.AQUA, ChatColor.RED, ChatColor.LIGHT_PURPLE, ChatColor.YELLOW, ChatColor.WHITE);

    static
    {
        COMMA_FORMAT.setGroupingUsed(true);
    }

    public static String commaify(int i)
    {
        return COMMA_FORMAT.format(i);
    }

    public static String commaify(double d)
    {
        return COMMA_FORMAT.format(d);
    }

    public static String commaify(long l)
    {
        return COMMA_FORMAT.format(l);
    }

    public static List<String> getPlayerNameList()
    {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            names.add(player.getName());
        return names;
    }

    public static int random(int min, int max)
    {
        return new Random().nextInt((max - min) + 1) + min;
    }

    public static double random(double min, double max)
    {
        return Math.random() * (max - min) + min;
    }

    public static ItemStack getSkull(String texture, ItemStack stack, SMaterial material)
    {
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        Spectaculation plugin = Spectaculation.getPlugin();
        String stringUUID;
        if (material != null)
        {
            if (!plugin.heads.contains(material.name().toLowerCase()))
            {
                plugin.heads.set(material.name().toLowerCase(), UUID.randomUUID().toString());
                plugin.heads.save();
            }
            stringUUID = plugin.heads.getString(material.name().toLowerCase());
        }
        else
            stringUUID = UUID.randomUUID().toString();
        GameProfile profile = new GameProfile(UUID.fromString(stringUUID), null);
        byte[] ed = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", texture).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(ed)));
        Field f;
        try
        {
            f = meta.getClass().getDeclaredField("profile");
            f.setAccessible(true);
            f.set(meta, profile);
        }
        catch (NoSuchFieldException | IllegalArgumentException | IllegalAccessException ex) {}
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getSkull(String texture, SMaterial material)
    {
        return getSkull(texture, new ItemStack(Material.SKULL_ITEM, 1, (short) 3), material);
    }

    public static List<String> splitByWordAndLength(String string, int splitLength, String separator)
    {
        List<String> result = new ArrayList<>();
        Pattern pattern = Pattern.compile("\\G" + separator + "*(.{1," + splitLength + "})(?=\\s|$)", Pattern.DOTALL);
        Matcher matcher = pattern.matcher(string);
        while (matcher.find())
            result.add(matcher.group(1));
        return result;
    }

    public static ItemStack applyColorToLeatherArmor(ItemStack stack, Color color)
    {
        if (!(stack.getItemMeta() instanceof LeatherArmorMeta)) return stack;
        LeatherArmorMeta meta = (LeatherArmorMeta) stack.getItemMeta();
        meta.setColor(color);
        stack.setItemMeta(meta);
        return stack;
    }

    public static String color(String string)
    {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    // not my code; found on stack overflow
    public static String toRomanNumeral(int num)
    {
        StringBuilder sb = new StringBuilder();
        int times;
        String[] romans = new String[] { "I", "IV", "V", "IX", "X", "XL", "L",
                "XC", "C", "CD", "D", "CM", "M" };
        int[] ints = new int[] { 1, 4, 5, 9, 10, 40, 50, 90, 100, 400, 500,
                900, 1000 };
        for (int i = ints.length - 1; i >= 0; i--)
        {
            times = num / ints[i];
            num %= ints[i];
            while (times > 0)
            {
                sb.append(romans[i]);
                times--;
            }
        }
        return sb.toString();
    }

    public static String rainbowize(String string)
    {
        StringBuilder builder = new StringBuilder();
        int i = 0;
        for (String c : string.split(""))
        {
            if (i > CRIT_SPECTRUM.size() - 1) i = 0;
            builder.append(CRIT_SPECTRUM.get(i)).append(c);
            i++;
        }
        return builder.toString();
    }

    public static String getMaterialDisplayName(Material material, short variant)
    {
        if (variant != 0)
            return SMaterial.getSpecEquivalent(material, variant).getBaseName();
        net.minecraft.server.v1_8_R3.ItemStack nmsStack = CraftItemStack.asNMSCopy(new ItemStack(material));
        if (nmsStack == null) return material.name();
        if (nmsStack.getItem() == null) return material.name();
        return nmsStack.getName();
    }

    public static void sendActionBar(Player player, String message)
    {
        PacketPlayOutChat packet = new PacketPlayOutChat(IChatBaseComponent.ChatSerializer.a("{\"text\":\"" + message + "\"}"), (byte) 2);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    public static GenericItemType getItemType(Material material)
    {
        if (material == Material.BOW) return GenericItemType.RANGED_WEAPON;
        if (Groups.SWORDS.contains(material)) return GenericItemType.WEAPON;
        if (Groups.PICKAXES.contains(material) ||
            Groups.HOES.contains(material) ||
            Groups.AXES.contains(material) ||
            Groups.SHOVELS.contains(material)) return GenericItemType.TOOL;
        if (Groups.LEATHER_ARMOR.contains(material) ||
            Groups.IRON_ARMOR.contains(material) ||
            Groups.GOLD_ARMOR.contains(material) ||
            Groups.DIAMOND_ARMOR.contains(material)) return GenericItemType.ARMOR;
        return material.isBlock() ? GenericItemType.BLOCK : GenericItemType.ITEM;
    }

    public static ItemStack createNamedItemStack(Material material, String name)
    {
        ItemStack stack = new ItemStack(material);
        if (name != null)
        {
            ItemMeta meta = stack.getItemMeta();
            meta.setDisplayName(name);
            stack.setItemMeta(meta);
        }
        return stack;
    }

    public static ItemStack createColoredStainedGlassPane(short data, String name)
    {
        ItemStack stack = createNamedItemStack(Material.STAINED_GLASS_PANE, name);
        stack.setDurability(data);
        return stack;
    }

    public static void border(Inventory inventory, GUI gui, ItemStack stack, int cornerSlot, int cornerSlot2, boolean overwrite, boolean pickup)
    {
        if (cornerSlot < 0 || cornerSlot > inventory.getSize())
            throw new IllegalArgumentException("Corner 1 of the border described is out of bounds");
        if (cornerSlot2 < 0 || cornerSlot2 > inventory.getSize())
            throw new IllegalArgumentException("Corner 2 of the border described is out of bounds");
        int topLeft = Math.min(cornerSlot, cornerSlot2);
        int bottomRight = Math.max(cornerSlot, cornerSlot2);
        int topRight;
        for (topRight = bottomRight; topRight > topLeft; topRight -= 9);
        int bottomLeft;
        for (bottomLeft = topLeft; bottomLeft < bottomRight; bottomLeft += 9);
        topRight += 9;
        bottomLeft -= 9;
        for (int y = topLeft; y <= bottomLeft; y += 9)
        {
            for (int x = y; x <= topRight - topLeft + y; x++)
            {
                int f = x;
                if (gui.getItems().stream().filter(item -> item.getSlot() == f).toArray().length != 0 && !overwrite) continue;
                if (y == topLeft || y == bottomLeft)
                {
                    gui.set(x, stack, pickup);
                    inventory.setItem(x, stack);
                }
                if (x == y || x == topRight - topLeft + y)
                {
                    gui.set(x, stack, pickup);
                    inventory.setItem(x, stack);
                }
            }
        }
    }

    public static void sendTypedTitle(Player player, String message, PacketPlayOutTitle.EnumTitleAction type)
    {
        IChatBaseComponent chatTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + message + "\"}");

        PacketPlayOutTitle title = new PacketPlayOutTitle(type, chatTitle);
        PacketPlayOutTitle length = new PacketPlayOutTitle(5, 20, 5);


        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(title);
        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(length);
    }

    public static void sendTitle(Player player, String message)
    {
        sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.TITLE);
    }

    public static void sendSubtitle(Player player, String message)
    {
        sendTypedTitle(player, message, PacketPlayOutTitle.EnumTitleAction.SUBTITLE);
    }

    public static void lightningLater(Location location, boolean effect, long delay)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                if (effect)
                    location.getWorld().strikeLightningEffect(location);
                else
                    location.getWorld().strikeLightning(location);
            }
        }.runTaskLater(Spectaculation.getPlugin(), delay);
    }

    public static void runIntervalForTicks(Runnable runnable, long interval, long end)
    {
        AtomicBoolean stop = new AtomicBoolean(false);
        new BukkitRunnable()
        {
            public void run()
            {
                if (stop.get())
                {
                    cancel();
                    return;
                }
                runnable.run();
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 0, interval);
        new BukkitRunnable()
        {
            public void run()
            {
                stop.set(true);
            }
        }.runTaskLater(Spectaculation.getPlugin(), end);
    }

    public static String getDate()
    {
        return DATE_FORMAT.format(new Date());
    }

    public static Item spawnPersonalItem(ItemStack stack, Location location, Player player)
    {
        Item item = location.getWorld().dropItem(location, stack);
        item.setMetadata("owner", new FixedMetadataValue(Spectaculation.getPlugin(), player.getUniqueId().toString()));
        return item;
    }

    // stackoverflow code lol
    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map)
    {
        List<Map.Entry<K, V>> list = new ArrayList<>(map.entrySet());
        list.sort(Map.Entry.comparingByValue());

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list)
        {
            result.put(entry.getKey(), entry.getValue());
        }

        return result;
    }

    public static <T> boolean addIf(T t, List<T> list, boolean test)
    {
        if (test)
            list.add(t);
        return test;
    }

    public static ItemStack setStackAmount(ItemStack stack, int amount)
    {
        stack.setAmount(amount);
        return stack;
    }

    public static SItem setSItemAmount(SItem item, int amount)
    {
        item.getStack().setAmount(amount);
        return item;
    }

    public static double roundTo(double d, int decimalPlaces)
    {
        if (decimalPlaces < 1)
            throw new IllegalArgumentException();
        StringBuilder builder = new StringBuilder()
                .append("#.");
        for (int i = 0; i < decimalPlaces; i++)
            builder.append("#");
        DecimalFormat df = new DecimalFormat(builder.toString());
        df.setRoundingMode(RoundingMode.CEILING);
        return Double.parseDouble(df.format(d));
    }

    public static void toggleAllowFlightNoCreative(UUID uuid, boolean flight)
    {
        Player player = Bukkit.getPlayer(uuid);
        if (player == null) return;
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;
        player.setAllowFlight(flight);
    }

    // not my code
    public static List<Block> getNearbyBlocks(Location location, int radius, Material type)
    {
        List<Block> blocks = new ArrayList<>();
        for (int x = location.getBlockX() - radius; x <= location.getBlockX() + radius; x++)
        {
            for (int y = location.getBlockY() - radius; y <= location.getBlockY() + radius; y++)
            {
                for (int z = location.getBlockZ() - radius; z <= location.getBlockZ() + radius; z++)
                {
                    Block block = location.getWorld().getBlockAt(x, y, z);
                    if (block.getType() == type || type == null)
                        blocks.add(block);
                }
            }
        }
        return blocks;
    }

    public static void markAimingArrow(Projectile projectile, Enchantment aiming)
    {
        if (aiming == null) return;
        AtomicReference<LivingEntity> target = new AtomicReference<>(null);
        new BukkitRunnable()
        {
            public void run()
            {
                if (projectile.isDead())
                {
                    cancel();
                    return;
                }
                if (target.get() == null)
                {
                    LivingEntity setTarget;
                    List<LivingEntity> possible = new ArrayList<>();
                    for (Entity entity : projectile.getNearbyEntities(2 * aiming.getLevel(), 2 * aiming.getLevel(), 2 * aiming.getLevel()))
                    {
                        if (entity instanceof Player)
                            continue;
                        if (entity instanceof LivingEntity && !(entity instanceof ArmorStand) && !(entity instanceof NPC) && !entity.isDead())
                            possible.add((LivingEntity) entity);
                    }
                    setTarget = SUtil.getRandom(possible);
                    if (setTarget == null) return;
                    target.set(setTarget);
                }
                Location location = projectile.getLocation().clone();
                Vector vector = location.clone().toVector().subtract(target.get().getLocation().clone().add(0, 1, 0).toVector());
                location.setYaw((float) Math.atan2(vector.getX(), vector.getZ()));
                projectile.teleport(location);
                projectile.setVelocity(vector.clone().multiply(-1.0).multiply(0.2));
            }
        }.runTaskTimer(Spectaculation.getPlugin(), 0, 1);
        new BukkitRunnable()
        {
            public void run()
            {
                projectile.remove();
            }
        }.runTaskLater(Spectaculation.getPlugin(), 140);
    }

    public static ItemStack getStack(String name, Material material, short data, int amount, List<String> lore)
    {
        ItemStack stack = new ItemStack(material, data);
        stack.setDurability(data);
        ItemMeta meta = stack.getItemMeta();
        if (name != null)
            meta.setDisplayName(name);
        stack.setAmount(amount);
        meta.setLore(lore);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_UNBREAKABLE, ItemFlag.HIDE_ATTRIBUTES);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getStack(String name, Material material, short data, int amount, String... lore)
    {
        return getStack(name, material, data, amount, Arrays.asList(lore));
    }

    public static ItemStack getSkullStack(String name, String skullName, int amount, String... lore)
    {
        ItemStack stack = getStack(name, Material.SKULL_ITEM, (short) 3, amount, lore);
        SkullMeta meta = (SkullMeta) stack.getItemMeta();
        meta.setOwner(skullName);
        stack.setItemMeta(meta);
        return stack;
    }

    public static ItemStack getSkullURLStack(String name, String url, int amount, String... lore)
    {
        return getSkull(url, getStack(name, Material.SKULL_ITEM, (short) 3, amount, lore), null);
    }

    public static ItemStack getSingleLoreStack(String name, Material material, short data, int amount, String lore)
    {
        List<String> l = new ArrayList<>();
        for (String line : SUtil.splitByWordAndLength(lore, 30, "\\s"))
            l.add(ChatColor.GRAY + line);
        return getStack(name, material, data, amount, l.toArray(new String[]{}));
    }

    public static boolean isEnchantable(SItem sItem)
    {
        if (sItem.getType() == SMaterial.ENCHANTED_BOOK) return true;
        GenericItemType type = sItem.getType().getStatistics().getType();
        return type == GenericItemType.WEAPON || type == GenericItemType.TOOL ||
                type == GenericItemType.RANGED_WEAPON || type == GenericItemType.ARMOR;
    }

    public static boolean isAir(ItemStack is)
    {
        if (is == null) return true;
        return is.getType() == Material.AIR;
    }

    public static List<String> combineElements(List<String> list, String separator, int perElement)
    {
        List<String> n = new ArrayList<>();
        for (int i = 0; i < list.size(); i += perElement)
        {
            StringBuilder builder = new StringBuilder();
            for (int j = 0; j < perElement; j++)
            {
                if (i + j > list.size() - 1)
                    break;
                builder.append(j != 0 ? separator : "").append(list.get(i + j));
            }
            n.add(builder.toString());
        }
        return n;
    }

    // not my code
    public static boolean pasteSchematic(File schematicFile, Location location, boolean withAir)
    {
        try
        {
            com.sk89q.worldedit.Vector pasteLocation = new com.sk89q.worldedit.Vector(
                    location.getX(), location.getY(), location.getZ());
            World pasteWorld = new BukkitWorld(location.getWorld());
            WorldData pasteWorldData = pasteWorld.getWorldData();
            Clipboard clipboard = ClipboardFormat.SCHEMATIC.getReader(new FileInputStream(schematicFile)).read(pasteWorldData);
            ClipboardHolder clipboardHolder = new ClipboardHolder(clipboard, pasteWorldData);
            EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(pasteWorld, -1);
            Operation operation = clipboardHolder
                    .createPaste(editSession, pasteWorldData)
                    .to(pasteLocation)
                    .ignoreAirBlocks(!withAir)
                    .build();
            Operations.complete(operation);
            return true;
        }
        catch (IOException | WorldEditException ex)
        {
            ex.printStackTrace();
            return false;
        }
    }

    public static void setBlocks(Location c1, Location c2, Material material, boolean applyPhysics)
    {
        if (!c1.getWorld().getName().equals(c1.getWorld().getName()))
            return;
        int sy = Math.min(c1.getBlockY(), c2.getBlockY()),
                ey = Math.max(c1.getBlockY(), c2.getBlockY()),
                sx = Math.min(c1.getBlockX(), c2.getBlockX()),
                ex = Math.max(c1.getBlockX(), c2.getBlockX()),
                sz = Math.min(c1.getBlockZ(), c2.getBlockZ()),
                ez = Math.max(c1.getBlockZ(), c2.getBlockZ());
        org.bukkit.World world = c1.getWorld();
        for (int y = sy; y <= ey; y++)
        {
            for (int x = sx; x <= ex; x++)
            {
                for (int z = sz; z <= ez; z++)
                {
                    world.getBlockAt(x, y, z).setType(material, applyPhysics);
                }
            }
        }
    }

    public static <T> T instance(Class<T> clazz, Object... params)
    {
        Class<?>[] paramClasses = new Class[params.length];
        for (int i = 0; i < paramClasses.length; i++)
            paramClasses[i] = params[i].getClass();
        try
        {
            Constructor<T> constructor = clazz.getConstructor(paramClasses);
            constructor.setAccessible(true);
            return constructor.newInstance(params);
        }
        catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex)
        {
            return null;
        }
    }

    public static <C, T> T getDeclaredField(C instance, String name, Class<T> type)
    {
        try
        {
            Field f = instance.getClass().getDeclaredField(name);
            f.setAccessible(true);
            return type.cast(f.get(instance));
        }
        catch (NoSuchFieldException | IllegalAccessException ex)
        {
            return null;
        }
    }

    public static Object getObjectFromCompound(NBTTagCompound compound, String key)
    {
        Object o;
        switch (compound.get(key).getTypeId())
        {
            case 1:
                o = compound.getByte(key);
                break;
            case 2:
                o = compound.getShort(key);
                break;
            case 3:
                o = compound.getInt(key);
                break;
            case 4:
                o = compound.getLong(key);
                break;
            case 5:
                o = compound.getFloat(key);
                break;
            case 6:
                o = compound.getDouble(key);
                break;
            case 7:
                o = compound.getByteArray(key);
                break;
            case 10:
                o = compound.getCompound(key);
                break;
            case 11:
                o = compound.getIntArray(key);
                break;
            default:
                o = compound.getString(key);
                break;
        }
        return o;
    }

    public static NBTBase getBaseFromObject(Object o)
    {
        if (o instanceof Byte)
            return new NBTTagByte((byte) o);
        else if (o instanceof Short)
            return new NBTTagShort((short) o);
        else if (o instanceof Integer)
            return new NBTTagInt((int) o);
        else if (o instanceof Long)
            return new NBTTagLong((long) o);
        else if (o instanceof Float)
            return new NBTTagFloat((float) o);
        else if (o instanceof Double)
            return new NBTTagDouble((double) o);
        else if (o instanceof String)
            return new NBTTagString((String) o);
        return null;
    }

    public static NBTBase getBaseFromObject(ConfigurationSection cs, String key)
    {
        return getBaseFromObject(cs.get(key));
    }

    public static ChatColor getRandomVisibleColor()
    {
        return VISIBLE_COLOR_SPECTRUM.get(random(0, VISIBLE_COLOR_SPECTRUM.size() - 1));
    }

    public static <T> T getRandom(List<T> list)
    {
        if (list.size() == 0)
            return null;
        return list.get(SUtil.random(0, list.size() - 1));
    }

    public static void broadcastExcept(String message, Player player)
    {
        for (Player p : Bukkit.getOnlinePlayers())
        {
            if (p.getUniqueId().equals(player.getUniqueId()))
                continue;
            p.sendMessage(message);
        }
        SLog.info(message);
    }

    public static ItemStack enchant(ItemStack stack)
    {
        ItemMeta meta = stack.getItemMeta();
        meta.addEnchant(org.bukkit.enchantments.Enchantment.DURABILITY, 1, true);
        stack.setItemMeta(meta);
        return stack;
    }

    public static SItem enchant(SItem item, Enchantment... enchantments)
    {
        for (Enchantment enchantment : enchantments)
            item.addEnchantment(enchantment.getType(), enchantment.getLevel());
        return item;
    }

    // NOT MY CODE
    public static byte[] gzipCompress(byte[] uncompressedData)
    {
        byte[] result = new byte[]{};
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream(uncompressedData.length);
             GZIPOutputStream gzipOS = new GZIPOutputStream(bos))
        {
            gzipOS.write(uncompressedData);
            // You need to close it before using bos
            gzipOS.close();
            result = bos.toByteArray();
        }
        catch (IOException ignored) {}
        return result;
    }

    // NOT MY CODE
    public static byte[] gzipUncompress(byte[] compressedData)
    {
        byte[] result = new byte[]{};
        try (ByteArrayInputStream bis = new ByteArrayInputStream(compressedData);
             ByteArrayOutputStream bos = new ByteArrayOutputStream();
             GZIPInputStream gzipIS = new GZIPInputStream(bis))
        {
            byte[] buffer = new byte[1024];
            int len;
            while ((len = gzipIS.read(buffer)) != -1)
            {
                bos.write(buffer, 0, len);
            }
            result = bos.toByteArray();
        }
        catch (IOException ignored) {}
        return result;
    }

    public static double midpoint(int x, int y)
    {
        return (double) (x + y) / 2.0;
    }

    public static double midpoint(double x, double y)
    {
        return (x + y) / 2.0;
    }

    public static void clearGoalSelector(PathfinderGoalSelector goalSelector)
    {
        try
        {
            Field b = PathfinderGoalSelector.class.getDeclaredField("b");
            Field c = PathfinderGoalSelector.class.getDeclaredField("c");
            b.setAccessible(true);
            c.setAccessible(true);
            ((UnsafeList) b.get(goalSelector)).clear();
            ((UnsafeList) c.get(goalSelector)).clear();
        }
        catch (IllegalAccessException | NoSuchFieldException e)
        {
            e.printStackTrace();
        }
    }

    public static <T> T getOrDefault(List<T> list, int index, T def)
    {
        if (index < 0 || index >= list.size())
            return def;
        return list.get(index);
    }

    public static <T> T getOrDefault(T[] array, int index, T def)
    {
        if (index < 0 || index >= array.length)
            return def;
        return array[index];
    }

    public static String zeroed(long l)
    {
        return l > 9 ? "" + l : "0" + l;
    }

    public static String getFormattedTime(long t, int div)
    {
        long seconds = t / div; // 86400
        long hours = seconds / 3600; // 24
        seconds -= hours * 3600; // 86400 - 84600 = 0
        long minutes = seconds / 60; // 0
        seconds -= minutes * 60; // 59 * 60 = 3540
        return (hours != 0 ? hours + ":" : "") + zeroed(minutes) + ":" + zeroed(seconds);
    }

    public static String getFormattedTime(long ticks)
    {
        return getFormattedTime(ticks, 20);
    }

    public static String getSlayerFormattedTime(long millis)
    {
        long seconds = millis / 1000; // 86400
        long hours = seconds / 3600; // 24
        seconds -= hours * 3600; // 86400 - 84600 = 0
        long minutes = seconds / 60; // 0
        seconds -= minutes * 60; // 59 * 60 = 3540
        return (hours != 0 ? zeroed(hours) + "h" : "") + zeroed(minutes) + "m" + zeroed(seconds) + "s";
    }

    public static double quadrt(double d)
    {
        return Math.pow(d, 1.0 / 4.0);
    }

    public static void delay(Runnable runnable, long delay)
    {
        new BukkitRunnable()
        {
            public void run()
            {
                runnable.run();
            }
        }.runTaskLater(Spectaculation.getPlugin(), delay);
    }

    public static GameProfile createGameProfile(String url)
    {
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] ed = Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"http://textures.minecraft.net/texture/%s\"}}}", url).getBytes());
        profile.getProperties().put("textures", new Property("textures", new String(ed)));
        return profile;
    }

    // not my code
    public static float getLookAtYaw(Vector motion)
    {
        double dx = motion.getX();
        double dz = motion.getZ();
        double yaw = 0.0D;
        if (dx != 0.0D)
        {
            if (dx < 0.0D)
                yaw = 4.71238898038469D;
            else
                yaw = 1.5707963267948966D;
            yaw -= Math.atan(dz / dx);
        }
        else if (dz < 0.0D)
        {
            yaw = Math.PI;
        }
        return (float) (-yaw * 180.0D / Math.PI - 90.0D);
    }

    public static String ntify(int i)
    {
        if (i == 11 || i == 12 || i == 13)
            return i + "th";
        String s = String.valueOf(i);
        char last = s.charAt(s.length() - 1);
        switch (last)
        {
            case '1': return i + "st";
            case '2': return i + "nd";
            case '3': return i + "rd";
            default: return i + "th";
        }
    }

    public static String pad(String s, int length)
    {
        return String.format("%-" + length + "s", s);
    }

    // not my code cuz i was lazy lol
    public static <T> List<T> shuffle(List<T> list)
    {
        Random rnd = ThreadLocalRandom.current();
        for (int i = list.size() - 1; i > 0; i--)
        {
            int index = rnd.nextInt(i + 1);
            T t = list.get(index);
            list.set(index, list.get(i));
            list.set(i, t);
        }
        return list;
    }

    public static <T> int deepLength(T[][] array2d)
    {
        int c = 0;
        for (T[] array : array2d)
            c += array.length;
        return c;
    }

    public static <T> T[] unnest(T[][] array2d, Class<T> clazz)
    {
        T[] array = (T[]) Array.newInstance(clazz, deepLength(array2d));
        for (int i = 0, c = 0; i < array2d.length; i++)
        {
            for (int j = 0; j < array2d[i].length; j++, c++)
                array[c] = array2d[i][j];
        }
        return array;
    }

    public static String createProgressText(String text, int current, int max)
    {
        double percent;
        if (max != 0)
            percent = ((double) current / (double) max) * 100.0;
        else
            percent = 0.0;
        percent = roundTo(percent, 1);
        return ChatColor.GRAY + text + ": " + (percent < 100.0 ? ChatColor.YELLOW + "" + SUtil.commaify(percent)
                + ChatColor.GOLD + "%" : ChatColor.GREEN + "100.0%");
    }

    public static String createProgressText(String text, double current, double max)
    {
        double percent;
        if (max != 0)
            percent = (current / max) * 100.0;
        else
            percent = 0.0;
        percent = roundTo(percent, 1);
        return ChatColor.GRAY + text + ": " + (percent < 100.0 ? ChatColor.YELLOW + "" + SUtil.commaify(percent)
                + ChatColor.GOLD + "%" : ChatColor.GREEN + "100.0%");
    }

    public static String createLineProgressBar(int length, ChatColor progressColor, int current, int max)
    {
        double percent = Math.min(current, (double) max) / (double) max;
        long completed = Math.round((double) length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; i++)
            builder.append("-");
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; i++)
            builder.append("-");
        builder.append(" ").append(ChatColor.YELLOW).append(SUtil.commaify(current)).append(ChatColor.GOLD).append("/")
                .append(ChatColor.YELLOW).append(SUtil.commaify(max));
        return builder.toString();
    }

    public static String createLineProgressBar(int length, ChatColor progressColor, double current, double max)
    {
        double percent = Math.min(current, max) / max;
        long completed = Math.round((double) length * percent);
        StringBuilder builder = new StringBuilder().append(progressColor);
        for (int i = 0; i < completed; i++)
            builder.append("-");
        builder.append(ChatColor.WHITE);
        for (int i = 0; i < length - completed; i++)
            builder.append("-");
        builder.append(" ").append(ChatColor.YELLOW).append(SUtil.commaify(current)).append(ChatColor.GOLD).append("/")
                .append(ChatColor.YELLOW).append(SUtil.commaify(max));
        return builder.toString();
    }

    public static <T> T[] toArray(List<T> list, Class<T> clazz)
    {
        T[] array = (T[]) Array.newInstance(clazz, list.size());
        for (int i = 0; i < list.size(); i++)
            array[i] = list.get(i);
        return array;
    }

    public static Rarity findPotionRarity(int level)
    {
        switch (level)
        {
            case 0:
            case 1:
            case 2:
                return Rarity.COMMON;
            case 3:
            case 4:
                return Rarity.UNCOMMON;
            case 5:
            case 6:
                return Rarity.RARE;
            case 7:
            case 8:
                return Rarity.EPIC;
            case 9:
            case 10:
                return Rarity.LEGENDARY;
            case 11:
            case 12:
                return Rarity.MYTHIC;
            case 13:
            case 14:
                return Rarity.SUPREME;
            case 15:
            case 16:
                return Rarity.SPECIAL;
            default:
                return Rarity.VERY_SPECIAL;
        }
    }

    public static PotionColor getTopColor(SItem item)
    {
        if (!item.isPotion()) return null;
        int topLevel = 0;
        PotionColor color = null;
        for (PotionEffect effect : item.getPotionEffects())
        {
            if (effect.getLevel() > topLevel)
            {
                topLevel = effect.getLevel();
                color = effect.getType().getColor();
            }
        }
        return color;
    }

    public static boolean canFitStack(Inventory inventory, ItemStack fit)
    {
        for (ItemStack stack : inventory)
        {
            if (stack == null)
                continue;
            if (!fit.equals(stack))
                continue;
            if (stack.getAmount() + fit.getAmount() > 64)
                continue;
            return true;
        }
        return false;
    }

    public static int blackMagic(double d)
    {
        return ((Double) d).intValue();
    }

    public static String prettify(Object obj)
    {
        Class<?> clazz = obj.getClass();
        if (clazz == Location.class)
        {
            Location location = (Location) obj;
            return location.getX() + ", " + location.getY() + ", " + location.getZ() + ", " + location.getWorld().getName() + ", " +
                    location.getYaw() + ", " + location.getPitch();
        }
        return "No pretty!";
    }

    public static String toNormalCase(String string)
    {
        string = string.replaceAll("_", " ");
        String[] spl = string.split(" ");
        for (int i = 0; i < spl.length; i++)
        {
            String s = spl[i];
            if (s.length() == 0)
                continue;
            if (s.length() == 1)
            {
                spl[i] = s.toUpperCase();
                continue;
            }
            spl[i] = s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
        }
        return StringUtils.join(spl, " ");
    }

    public static String getAuctionFormattedTime(long millis)
    {
        if (millis == 0)
            return "Ended!";
        if (millis >= 8.64E7)
            return Math.round(millis / 8.64E7) + "d";
        if (millis >= 2.16E7)
            return Math.round(millis / 3.6E6) + "h";
        long seconds = millis / 1000; // 86400
        long hours = seconds / 3600; // 24
        seconds -= hours * 3600; // 86400 - 84600 = 0
        long minutes = seconds / 60; // 0
        seconds -= minutes * 60; // 59 * 60 = 3540
        StringBuilder builder = new StringBuilder();
        if (hours > 0)
            builder.append(hours).append("h ");
        builder.append(minutes).append("m ").append(seconds).append("s");
        return builder.toString();
    }

    public static String getAuctionSetupFormattedTime(long millis)
    {
        String dur;
        if (millis >= 8.64E7)
        {
            long days = Math.round(millis / 8.64E7);
            dur = days + " Day";
            if (days != 1) dur += "s";
        }
        else if (millis >= 3600000)
        {
            long hours = Math.round(millis / 3600000.0);
            dur = hours + " Hour";
            if (hours != 1) dur += "s";
        }
        else
        {
            long minutes = Math.round(millis / 60000.0);
            dur = minutes + " Minute";
            if (minutes != 1) dur += "s";
        }
        return dur;
    }
}