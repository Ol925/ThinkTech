package com.OL925.IndustrialReborn.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class Briquette extends Item {

    public Briquette() {
        super();
        this.setUnlocalizedName("briquette");
        this.setTextureName("industrialreborn:briquette");
        GameRegistry.registerItem(this, "briquette");
    }
}
