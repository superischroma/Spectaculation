package me.superischroma.spectaculation.gui;

import me.superischroma.spectaculation.util.SUtil;

public enum GUIType
{
    CRAFTING_TABLE(CraftingTableGUI.class),
    ITEM_BROWSE(ItemBrowserGUI.class),
    ANVIL(AnvilGUI.class),
    REFORGE_ANVIL(ReforgeAnvilGUI.class),
    BANKER(BankerGUI.class),
    BANKER_DEPOSIT(DepositGUI.class),
    BANKER_WITHDRAWAL(WithdrawalGUI.class),
    SKYBLOCK_MENU(SkyBlockMenuGUI.class),
    SKYBLOCK_PROFILE(SkyBlockProfileGUI.class),
    QUIVER(QuiverGUI.class),
    LIFT(LiftGUI.class),
    SLAYER(SlayerGUI.class),
    REVENANT_HORROR(RevenantHorrorGUI.class),
    TARANTULA_BROODFATHER(TarantulaBroodfatherGUI.class),
    SVEN_PACKMASTER(SvenPackmasterGUI.class),
    COLLECTION_MENU(CollectionMenuGUI.class),
    SKILL_MENU(SkillMenuGUI.class),
    PETS(PetsGUI.class);
    ;

    private final Class<? extends GUI> gui;

    GUIType(Class<? extends GUI> gui)
    {
        this.gui = gui;
    }

    public GUI getGUI()
    {
        try
        {
            return gui.newInstance();
        }
        catch (IllegalAccessException | InstantiationException e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public GUI getGUI(Object... params)
    {
        return SUtil.instance(GUI.class, params);
    }

    public static GUI getGUI(String title)
    {
        for (GUIType type : values())
        {
            if (type.getGUI().getTitle().contains(title))
                return type.getGUI();
        }
        return null;
    }
}