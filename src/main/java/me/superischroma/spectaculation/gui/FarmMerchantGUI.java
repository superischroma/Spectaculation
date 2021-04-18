package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.item.SItem;
import me.superischroma.spectaculation.item.SMaterial;
import me.superischroma.spectaculation.util.SUtil;

public class FarmMerchantGUI extends ShopGUI
{
    private static final SItem[] ITEMS = new SItem[]
    {
            SUtil.setSItemAmount(SItem.of(SMaterial.WHEAT), 3),
            SUtil.setSItemAmount(SItem.of(SMaterial.CARROT_ITEM), 3),
            SUtil.setSItemAmount(SItem.of(SMaterial.POTATO_ITEM), 3),
            SUtil.setSItemAmount(SItem.of(SMaterial.MELON), 10),
            SUtil.setSItemAmount(SItem.of(SMaterial.SUGAR_CANE), 3),
            SItem.of(SMaterial.PUMPKIN),
            SItem.of(SMaterial.COCOA_BEANS),
            SItem.of(SMaterial.RED_MUSHROOM),
            SItem.of(SMaterial.BROWN_MUSHROOM),
            SUtil.setSItemAmount(SItem.of(SMaterial.SAND), 2),
            SItem.of(SMaterial.CACTUS)
    };

    public FarmMerchantGUI(int page)
    {
        super("Farm Merchant", page, ITEMS);
    }

    public FarmMerchantGUI()
    {
        this(1);
    }
}