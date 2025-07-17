package com.OL925.ThinkTech.common.item;


import net.minecraft.item.Item;

import cpw.mods.fml.common.registry.GameRegistry;

public class ProteinBlock extends Item{

    public ProteinBlock(){
        super();
        this.setUnlocalizedName("proteinBlock");
        this.setTextureName("thinktech:proteinBlock");
        GameRegistry.registerItem(this, "proteinBlock");
    }

}
