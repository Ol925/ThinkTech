package com.OL925.ThinkTech.common.item;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.item.Item;

public class HighComputingCowerChipTier2 extends Item {


    public HighComputingCowerChipTier2() {
        super();
        this.setTextureName("thinktech:tier2chip");
        this.setUnlocalizedName("chipTier2");
        GameRegistry.registerItem(this,"chipTier2");
    }

}
