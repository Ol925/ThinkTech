package com.OL925.ThinkTech.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class HighComputingCowerChipTier1 extends Item {
    public HighComputingCowerChipTier1(){
        super();
        this.setTextureName("thinktech:tier1chip");
        this.setUnlocalizedName("chipTier1");
        GameRegistry.registerItem(this,"chipTier1");
    }
}
