package com.OL925.IndustrialReborn.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class HalfBriquette extends Item {

    public HalfBriquette() {
        super();
        this.setUnlocalizedName("halfBriquette");
        this.setTextureName("industrialreborn:half_briquette");
        GameRegistry.registerItem(this, "halfBriquette");
    }

}
