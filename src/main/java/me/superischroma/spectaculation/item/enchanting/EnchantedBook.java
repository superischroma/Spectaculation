package me.superischroma.spectaculation.item.enchanting;

import me.superischroma.spectaculation.enchantment.Enchantment;
import me.superischroma.spectaculation.item.*;

public class EnchantedBook implements MaterialStatistics, MaterialFunction, Enchantable
{
    private static final MaterialQuantifiable PAPER_16 = new MaterialQuantifiable(SMaterial.PAPER, 16);

    @Override
    public String getDisplayName()
    {
        return "Enchanted Book";
    }

    @Override
    public Rarity getRarity()
    {
        return Rarity.COMMON;
    }

    @Override
    public String getLore()
    {
        return "Use this on an item in an Anvil to apply it!";
    }

    @Override
    public GenericItemType getType()
    {
        return GenericItemType.ITEM;
    }

    @Override
    public void onInstanceUpdate(SItem instance)
    {
        int max = 1;
        for (Enchantment enchantment : instance.getEnchantments())
        {
            if (enchantment.getLevel() > max)
                max = enchantment.getLevel();
        }
        switch (max)
        {
            case 1:
            case 2:
            case 3:
            case 4:
                instance.setRarity(Rarity.COMMON, false);
                break;
            case 5:
                instance.setRarity(Rarity.UNCOMMON, false);
                break;
            case 6:
                instance.setRarity(Rarity.RARE, false);
                break;
            case 7:
                instance.setRarity(Rarity.EPIC, false);
                break;
            case 8:
                instance.setRarity(Rarity.LEGENDARY, false);
                break;
            case 9:
                instance.setRarity(Rarity.MYTHIC, false);
                break;
            default: // 10 and on
                instance.setRarity(Rarity.SUPREME, false);
                break;
        }
    }
}