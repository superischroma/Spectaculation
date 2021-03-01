package me.superischroma.spectaculation.user;

import me.superischroma.spectaculation.item.SMaterial;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class ItemCollectionRecipeReward extends ItemCollectionReward
{
    private final SMaterial material;

    public ItemCollectionRecipeReward(SMaterial material)
    {
        super(Type.RECIPE);
        this.material = material;
    }

    @Override
    public void onAchieve(Player player)
    {
        // TODO
    }

    public String toString()
    {
        return ChatColor.GRAY + material.getDisplayName(material.getData()) + ChatColor.GRAY + " Recipe";
    }
}