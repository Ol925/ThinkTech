package com.OL925.ThinkTech.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class HighComputingCowerChipTier3 extends Item{
    public HighComputingCowerChipTier3() {
        super();
        this.setTextureName("thinktech:tier3chip");
        this.setUnlocalizedName("chipTier3");
        GameRegistry.registerItem(this,"chipTier3");
    }
}
