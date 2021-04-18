package me.superischroma.spectaculation.item;

import me.superischroma.spectaculation.Spectaculation;
import me.superischroma.spectaculation.collection.ItemCollection;
import me.superischroma.spectaculation.entity.*;
import me.superischroma.spectaculation.entity.caverns.CreeperFunction;
import me.superischroma.spectaculation.event.CreeperIgniteEvent;
import me.superischroma.spectaculation.item.storage.Storage;
import me.superischroma.spectaculation.listener.PListener;
import me.superischroma.spectaculation.potion.PotionEffect;
import me.superischroma.spectaculation.region.Region;
import me.superischroma.spectaculation.region.RegionType;
import me.superischroma.spectaculation.user.*;
import me.superischroma.spectaculation.util.SUtil;
import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.*;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.*;

public class ItemListener extends PListener
{
    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e)
    {
        if (!SItem.isSpecItem(e.getItem())) return;
        SItem sItem = SItem.find(e.getItem());
        if (sItem == null) return;
        updateStatistics(e.getPlayer());
        Action action = e.getAction();
        if (sItem.getType().getStatistics().getSpecificType() == SpecificItemType.HELMET &&
            action == Action.RIGHT_CLICK_AIR && isAir(e.getPlayer().getInventory().getHelmet()))
        {
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
        }
        Player player = e.getPlayer();
        Ability ability = sItem.getType().getAbility();
        if (ability != null)
        {
            AbilityActivation activation = ability.getAbilityActivation();
            if (activation == AbilityActivation.LEFT_CLICK || activation == AbilityActivation.RIGHT_CLICK)
            {
                if (activation == AbilityActivation.LEFT_CLICK ?
                        action == Action.LEFT_CLICK_AIR || action == Action.LEFT_CLICK_BLOCK :
                        action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK)
                {
                    PlayerUtils.useAbility(player, sItem);
                }
            }
        }
        MaterialFunction function = sItem.getType().getFunction();
        if (function != null)
            function.onInteraction(e);
    }

    @EventHandler
    public void onInventoryClose(InventoryCloseEvent e) // todo: rework storage
    {
        if (!(e.getPlayer() instanceof Player)) return;
        Player player = (Player) e.getPlayer();
        Inventory storage = Storage.getCurrentStorageOpened(player);
        if (storage == null) return;
        Inventory inventory = e.getInventory();
        SItem hand = SItem.find(player.getItemInHand());
        if (hand == null) return;
        NBTTagCompound storageData = new NBTTagCompound();
        for (int i = 0; i < inventory.getSize(); i++)
        {
            SItem sItem = SItem.find(inventory.getItem(i));
            if (sItem == null)
            {
                SItem equiv = SItem.of(inventory.getItem(i));
                if (equiv != null)
                {
                    storageData.setByteArray(String.valueOf(i), SUtil.gzipCompress(equiv.toCompound().toString().getBytes()));
                    continue;
                }
                storageData.remove(String.valueOf(i));
                continue;
            }
            storageData.setByteArray(String.valueOf(i), SUtil.gzipCompress(sItem.toCompound().toString().getBytes()));
        }
        hand.getData().set("storage_data", storageData);
        hand.update();
        Storage.closeCurrentStorage(player);
    }

    @EventHandler
    public void onPlayerFlight(PlayerToggleFlightEvent e)
    {
        Player player = e.getPlayer();
        GameMode gameMode = player.getGameMode();
        if (gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR) return;
        for (ItemStack stack : player.getInventory().getArmorContents())
        {
            SItem sItem = SItem.find(stack);
            if (sItem != null)
            {
                Ability ability = sItem.getType().getAbility();
                if (ability != null)
                {
                    if (e.isFlying() && ability.getAbilityActivation() == AbilityActivation.FLIGHT)
                    {
                        e.setCancelled(true);
                        PlayerUtils.useAbility(player, sItem);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e)
    {
        if (e.getView().getTopInventory().getType() != InventoryType.CRAFTING) return;
        if (e.getSlotType() != InventoryType.SlotType.CONTAINER && e.getSlotType() != InventoryType.SlotType.QUICKBAR) return;
        if (e.getAction() != InventoryAction.MOVE_TO_OTHER_INVENTORY) return;
        Inventory inventory = e.getClickedInventory();
        if (inventory == null) return;
        if (inventory.getType() != InventoryType.PLAYER) return;
        ItemStack current = e.getCurrentItem();
        if (current == null) return;
        SItem sItem = SItem.find(current);
        if (sItem == null)
            sItem = SItem.of(current);
        updateStatistics((Player) e.getWhoClicked());
        if (sItem.getType().getStatistics().getSpecificType() == null || sItem.getType().getStatistics().getSpecificType() != SpecificItemType.HELMET) return;
        PlayerInventory playerInventory = (PlayerInventory) inventory;
        if (!isAir(playerInventory.getHelmet())) return;
        e.setCancelled(true);
        e.setCurrentItem(new ItemStack(Material.AIR));
        playerInventory.setHelmet(current);
    }

    @EventHandler
    public void onArmorChange(InventoryClickEvent e)
    {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType() != InventoryType.PLAYER && e.getClickedInventory().getType() != InventoryType.CRAFTING) return;
        updateStatistics((Player) e.getWhoClicked());
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onItemClick(InventoryClickEvent e)
    {
        ItemStack stack = e.getCurrentItem();
        if (stack == null) return;
        SItem sItem = SItem.find(stack);
        if (sItem == null) return;
        if (sItem.getType().getFunction() == null) return;
        sItem.getType().getFunction().onInventoryClick(sItem, e);
    }

    @EventHandler
    public void onItemMove(InventoryClickEvent e)
    {
        if (e.getClickedInventory() == null) return;
        if (e.getClickedInventory().getType() != InventoryType.PLAYER) return;
        if (e.getSlot() != 8) return;
        e.setCancelled(true);
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e)
    {
        SItem sItem = SItem.find(e.getItemInHand());
        if (sItem == null) return;
        if (sItem.getType().getStatistics().getSpecificType() == SpecificItemType.HELMET && isAir(e.getPlayer().getInventory().getHelmet()))
        {
            e.setCancelled(true);
            e.getPlayer().getInventory().setHelmet(sItem.getStack());
            e.getPlayer().setItemInHand(null);
            return;
        }
        if (!sItem.getType().isCraft())
        {
            if (sItem.getType().getStatistics().getType() != GenericItemType.BLOCK)
                e.setCancelled(true);
            else
                new SBlock(e.getBlockPlaced().getLocation(), sItem.getType(), sItem.getData()).save();
        }
    }

    @EventHandler
    public void onFrameInteract(PlayerInteractEvent e)
    {
        if (e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        Player player = e.getPlayer();
        Block block = e.getClickedBlock();
        ItemStack hand = e.getItem();
        if (hand == null) return;
        SItem item = SItem.find(hand);
        if (item == null) return;
        if (block.getType() != Material.ENDER_PORTAL_FRAME) return;
        SBlock sBlock = SBlock.getBlock(block.getLocation());
        if (sBlock == null)
        {
            e.setCancelled(true);
            return;
        }
        if (sBlock.getType() != SMaterial.SUMMONING_FRAME)
        {
            e.setCancelled(true);
            return;
        }
        if (!block.hasMetadata("placer"))
        {
            if (item.getType() != SMaterial.SUMMONING_EYE) return;
            block.setMetadata("placer", new FixedMetadataValue(plugin, player.getUniqueId()));
            BlockState state = block.getState();
            state.setRawData((byte) 4);
            state.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SLEEPING_EYE).getStack());
            List<Location> locations = StaticDragonManager.EYES.containsKey(player.getUniqueId()) ?
                    StaticDragonManager.EYES.get(player.getUniqueId()) : new ArrayList<>();
            locations.add(block.getLocation());
            StaticDragonManager.EYES.remove(player.getUniqueId());
            StaticDragonManager.EYES.put(player.getUniqueId(), locations);
            int quantity = 0;
            for (List<Location> ls : StaticDragonManager.EYES.values())
                quantity += ls.size();
            Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.GREEN + player.getName() + ChatColor.LIGHT_PURPLE +
                    " placed a Summoning Eye! " + (quantity == 8 ? "Brace yourselves! " : "") +
                    ChatColor.GRAY + "(" +
                    (quantity == 8 ? ChatColor.GREEN : ChatColor.YELLOW) + quantity +
                    ChatColor.GRAY + "/" + ChatColor.GREEN + "8" + ChatColor.GRAY + ")");
            if (quantity != 8) return;
            List<UUID> cleared = new ArrayList<>();
            for (List<Location> ls : StaticDragonManager.EYES.values())
            {
                for (Location location : ls)
                {
                    Block b = location.getBlock();
                    List<MetadataValue> values = b.getMetadata("placer");
                    Player p = Bukkit.getPlayer((UUID) values.get(0).value());
                    if (p == null) continue;
                    if (cleared.contains(p.getUniqueId())) continue;
                    PlayerInventory inventory = p.getInventory();
                    for (int i = 0; i < inventory.getSize(); i++)
                    {
                        SItem si = SItem.find(inventory.getItem(i));
                        if (si == null) continue;
                        if (si.getType() == SMaterial.SLEEPING_EYE)
                            inventory.setItem(i, SItem.of(SMaterial.REMNANT_OF_THE_EYE).getStack());
                    }
                    p.sendMessage(ChatColor.DARK_PURPLE + "Your Sleeping Eyes have been awoken by the magic of the Dragon." +
                            " They are now Remnants of the Eye!");
                    cleared.add(p.getUniqueId());
                }
            }
            StaticDragonManager.ACTIVE = true;
            block.getWorld().playSound(block.getLocation(), Sound.ENDERMAN_STARE, 50f, -2f);
            new BukkitRunnable()
            {
                public void run()
                {
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_DEATH, 50f, -2f);
                }
            }.runTaskLater(plugin, 90);
            new BukkitRunnable()
            {
                public void run()
                {
                    for (int i = 0; i < 3; i++)
                        block.getWorld().playSound(block.getLocation(), Sound.EXPLODE, 50f, -2f);
                    SEntityType dragonType = SEntityType.PROTECTOR_DRAGON;
                    int chance = SUtil.random(0, 100);
                    if (chance >= 16)
                        dragonType = SEntityType.OLD_DRAGON;
                    if (chance >= 32)
                        dragonType = SEntityType.WISE_DRAGON;
                    if (chance >= 48)
                        dragonType = SEntityType.UNSTABLE_DRAGON;
                    if (chance >= 64)
                        dragonType = SEntityType.YOUNG_DRAGON;
                    if (chance >= 80)
                        dragonType = SEntityType.STRONG_DRAGON;
                    if (chance >= 96)
                        dragonType = SEntityType.SUPERIOR_DRAGON;
                    SEntity entity = new SEntity(block.getLocation().clone().add(0, 53, 0), dragonType);
                    for (Player p : Bukkit.getOnlinePlayers())
                    {
                        Region area = Region.getRegionOfEntity(p);
                        if (area == null) continue;
                        if (area.getType() != RegionType.DRAGONS_NEST) continue;
                        Vector vector = p.getLocation().clone().subtract(new Vector(-670.5, 58.0, -275.5)).toVector();
                        p.setVelocity(vector.normalize().multiply(40.0).setY(100.0));
                    }
                    StaticDragonManager.DRAGON = entity;
                    block.getWorld().playSound(block.getLocation(), Sound.ENDERDRAGON_GROWL, 50f, 1f);
                    Bukkit.broadcastMessage(ChatColor.DARK_PURPLE + "☬ " + ChatColor.LIGHT_PURPLE + ChatColor.BOLD +
                            "The " + ChatColor.RED + ChatColor.BOLD + entity.getStatistics().getEntityName() +
                            ChatColor.LIGHT_PURPLE + ChatColor.BOLD + " has spawned!");
                }
            }.runTaskLater(plugin, 180);
            return;
        }
        List<MetadataValue> values = block.getMetadata("placer");
        Player p = Bukkit.getPlayer((UUID) values.get(0).value());
        if (p == null) return;
        if (item.getType() == SMaterial.SLEEPING_EYE)
        {
            if (!p.getUniqueId().equals(player.getUniqueId()))
            {
                player.sendMessage(ChatColor.RED + "You can only recover Summoning Eyes that you placed!");
                return;
            }
            if (StaticDragonManager.ACTIVE)
            {
                player.sendMessage(ChatColor.RED + "You cannot recover Summoning Eyes after the dragon has been summoned!");
                return;
            }
            block.removeMetadata("placer", plugin);
            BlockState state = block.getState();
            state.setRawData((byte) 0);
            state.update();
            player.getInventory().setItemInHand(SItem.of(SMaterial.SUMMONING_EYE).getStack());
            StaticDragonManager.EYES.get(p.getUniqueId()).remove(block.getLocation());
            player.sendMessage(ChatColor.DARK_PURPLE + "You recovered a Summoning Eye!");
            return;
        }
    }

    @EventHandler
    public void onItemPickup(PlayerPickupItemEvent e)
    {
        Item item = e.getItem();
        Player player = e.getPlayer();
        updateStatistics(player);
        NBTTagCompound compound = CraftItemStack.asNMSCopy(item.getItemStack()).getTag();
        if (compound == null)
            compound = new NBTTagCompound();
        if (!compound.hasKey("type"))
            item.getItemStack().setItemMeta(SItem.of(item.getItemStack()).getStack().getItemMeta());
        if (item.hasMetadata("owner"))
        {
            List<MetadataValue> o = item.getMetadata("owner");
            if (o.size() != 0)
            {
                if (!o.get(0).asString().equals(e.getPlayer().getUniqueId().toString()))
                {
                    e.setCancelled(true);
                    return;
                }
            }
        }
        User user = User.getUser(player.getUniqueId());
        ItemStack stack = item.getItemStack();
        SItem sItem = SItem.find(stack);
        if (sItem == null)
            throw new NullPointerException("AYOOOO SOMETHING GOT FUCKED UP BRUH");
        if (item.hasMetadata("obtained"))
            Bukkit.broadcastMessage(ChatColor.GREEN + player.getName() + ChatColor.YELLOW + " has obtained " + sItem.getFullName() + ChatColor.YELLOW + "!");
        if (sItem.getOrigin() == ItemOrigin.NATURAL_BLOCK || sItem.getOrigin() == ItemOrigin.MOB)
        {
            sItem.setOrigin(ItemOrigin.UNKNOWN);
            ItemCollection collection = ItemCollection.getByMaterial(sItem.getType(), stack.getDurability());
            if (collection != null)
            {
                int prev = user.getCollection(collection);
                user.addToCollection(collection, stack.getAmount());
                user.save();
                if (prev == 0)
                {
                    player.sendMessage(ChatColor.GOLD + "" + ChatColor.BOLD + "  COLLECTION UNLOCKED " + ChatColor.RESET +
                            ChatColor.YELLOW + collection.getName());
                    player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 2f);
                }
            }
        }
    }

    @EventHandler
    public void onItemDrop(PlayerDropItemEvent e)
    {
        SItem sItem = SItem.find(e.getItemDrop().getItemStack());
        if (sItem != null && (sItem.getType() == SMaterial.SKYBLOCK_MENU || sItem.getType() == SMaterial.QUIVER_ARROW))
            e.setCancelled(true);
        updateStatistics(e.getPlayer());
    }

    @EventHandler
    public void onFishingRodReel(PlayerFishEvent e)
    {
        SItem rod = SItem.find(e.getPlayer().getItemInHand());
        if (rod == null) return;
        e.getHook().setMetadata("owner", new FixedMetadataValue(Spectaculation.getPlugin(), e.getPlayer()));
        MaterialFunction function = rod.getType().getFunction();
        if (function == null) return;
        if (function instanceof FishingRodFunction)
            ((FishingRodFunction) function).onFish(rod, e);
    }

    @EventHandler
    public void onPotionSplash(PotionSplashEvent e)
    {
        SItem item = SItem.find(e.getPotion().getItem());
        if (item == null) return;
        if (!item.isPotion()) return;
        e.setCancelled(true);
        for (LivingEntity entity : e.getAffectedEntities())
        {
            if (!(entity instanceof Player)) continue;
            User user = User.getUser(entity.getUniqueId());
            if (user == null) continue;
            for (PotionEffect effect : item.getPotionEffects())
            {
                PlayerUtils.updatePotionEffects(user, PlayerUtils.STATISTICS_CACHE.get(user.getUuid()));
                if (effect.getType().getOnDrink() != null)
                    effect.getType().getOnDrink().accept(effect, (Player) entity);
                long ticks = (long) (effect.getDuration() * e.getIntensity(entity));
                if ((!user.hasPotionEffect(effect.getType())) || (user.hasPotionEffect(effect.getType()) &&
                        ticks > user.getPotionEffect(effect.getType()).getRemaining()))
                {
                    user.removePotionEffect(effect.getType());
                    user.addPotionEffect(new PotionEffect(effect.getType(), effect.getLevel(), ticks));
                }
                entity.sendMessage((effect.getType().isBuff() ? ChatColor.GREEN + "" + ChatColor.BOLD + "BUFF!" :
                        ChatColor.RED + "" + ChatColor.BOLD + "DEBUFF!") +
                        ChatColor.RESET + ChatColor.WHITE + " You " + (e.getPotion().getShooter().equals(entity) ? "splashed yourself" : "were splashed") +
                        " with " + effect.getDisplayName() + ChatColor.WHITE + "!");
            }
        }
    }

    private static void updateStatistics(Player player)
    {
        PlayerInventory inv = player.getInventory();
        ItemStack beforeHelmet = inv.getHelmet(),
                beforeChestplate = inv.getChestplate(),
                beforeLeggings = inv.getLeggings(),
                beforeBoots = inv.getBoots();
        new BukkitRunnable()
        {
            @Override
            public void run()
            {
                PlayerStatistics statistics = PlayerUtils.STATISTICS_CACHE.get(player.getUniqueId());
                ItemStack afterHelmet = inv.getHelmet(),
                        afterChestplate = inv.getChestplate(),
                        afterLeggings = inv.getLeggings(),
                        afterBoots = inv.getBoots();
                boolean helmetSimilar = similar(beforeHelmet, afterHelmet),
                        chestplateSimilar = similar(beforeChestplate, afterChestplate),
                        leggingsSimilar = similar(beforeLeggings, afterLeggings),
                        bootsSimilar = similar(beforeBoots, afterBoots);
                SItem helmet = null, chestplate = null, leggings = null, boots = null;
                if (!helmetSimilar)
                    PlayerUtils.updateArmorStatistics((helmet = SItem.find(afterHelmet)), statistics, PlayerStatistic.HELMET);
                if (!chestplateSimilar)
                    PlayerUtils.updateArmorStatistics((chestplate = SItem.find(afterChestplate)), statistics, PlayerStatistic.CHESTPLATE);
                if (!leggingsSimilar)
                    PlayerUtils.updateArmorStatistics((leggings = SItem.find(afterLeggings)), statistics, PlayerStatistic.LEGGINGS);
                if (!bootsSimilar)
                    PlayerUtils.updateArmorStatistics((boots = SItem.find(afterBoots)), statistics, PlayerStatistic.BOOTS);
                PlayerUtils.updateInventoryStatistics(player, statistics);
            }
        }.runTaskLater(Spectaculation.getPlugin(), 1);
    }

    private static boolean similar(ItemStack is, ItemStack is1)
    {
        if (is == null && is1 == null) return true;
        if (is != null && is1 == null) return false;
        if (is == null) return false;
        return is.isSimilar(is1);
    }

    private static boolean isAir(ItemStack is)
    {
        if (is == null) return true;
        return is.getType() == Material.AIR;
    }

}