package com.OL925.IndustrialReborn.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class MethaneClathrate extends Item {

    public MethaneClathrate() {
        super();
        this.setUnlocalizedName("methaneClathrate");
        this.setTextureName("industrialreborn:Methane_clathrate");
        GameRegistry.registerItem(this, "methaneClathrate");
    }
}
