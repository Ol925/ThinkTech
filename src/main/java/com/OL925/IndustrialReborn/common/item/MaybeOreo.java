package com.OL925.IndustrialReborn.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MaybeOreo extends ItemFood {

    public MaybeOreo() {
        super(16, 2.1F, false);
        this.setAlwaysEdible();
        this.setUnlocalizedName("superOreo");
        this.setTextureName("industrialreborn:superoreo");
        GameRegistry.registerItem(this, "superOreo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List toolTip,
        final boolean advancedToolTips) {
        toolTip.add("原料来自于还没倒掉的牛奶和烧剩下的蜂窝煤");
    }
}
