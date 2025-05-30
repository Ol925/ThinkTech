package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.block.ControllerTier1;
import com.OL925.ThinkTech.common.block.ControllerTier2;
import com.OL925.ThinkTech.common.block.ControllerTier3;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class ThTBlockLoader {

    public static Block controllerTier1 = new ControllerTier1();
    public static Block controllerTier2 = new ControllerTier2();
    public static Block controllerTier3 = new ControllerTier3();

    public static void init()
    {
        GameRegistry.registerBlock(controllerTier1, "controllerTier1");
        ThTList.controllerTier1.set(Item.getItemFromBlock(controllerTier1));

        GameRegistry.registerBlock(controllerTier2, "controllerTier2");
        ThTList.controllerTier2.set(Item.getItemFromBlock(controllerTier2));

        GameRegistry.registerBlock(controllerTier3, "controllerTier3");
        ThTList.controllerTier3.set(Item.getItemFromBlock(controllerTier3));
    }
}
