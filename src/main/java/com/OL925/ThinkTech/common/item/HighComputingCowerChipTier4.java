package com.OL925.ThinkTech.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class HighComputingCowerChipTier4 extends Item{
    public HighComputingCowerChipTier4() {
        super();
        this.setTextureName("thinktech:tier4chip");
        this.setUnlocalizedName("chipTier4");
        GameRegistry.registerItem(this,"chipTier4");
    }
}
