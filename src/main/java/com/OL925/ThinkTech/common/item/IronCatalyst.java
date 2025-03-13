package com.OL925.ThinkTech.common.item;

import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class IronCatalyst extends Item{
    public IronCatalyst() {
        super();
        this.setUnlocalizedName("ironCatalyst");
        this.setTextureName("thinktech:Iron_catalyst");
        GameRegistry.registerItem(this, "ironCatalyst");
    }
}
