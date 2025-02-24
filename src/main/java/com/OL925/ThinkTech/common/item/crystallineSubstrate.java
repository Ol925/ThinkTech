package com.OL925.ThinkTech.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class crystallineSubstrate extends Item{
    public crystallineSubstrate() {
        super();
        this.setUnlocalizedName("crystallineSubstrate");
        this.setTextureName("thinktech:Crystalline_ubstrate");
        GameRegistry.registerItem(this, "crystallineSubstrate");
    }
}
