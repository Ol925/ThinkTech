package com.OL925.ThinkTech.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class pWafer extends Item{
    public pWafer(){
        super();
        this.setUnlocalizedName("pWafer");
        this.setTextureName("thinktech:wafer");
        GameRegistry.registerItem(this, "pWafer");
    }
}
