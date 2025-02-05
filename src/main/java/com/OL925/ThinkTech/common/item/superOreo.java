package com.OL925.ThinkTech.common.item;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class superOreo extends ItemFood {

    public superOreo() {
        super(16, 2.2F, false);
        this.setUnlocalizedName("superOreo");
        this.setTextureName("thinktech:superoreo");
        GameRegistry.registerItem(this, "superOreo");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(final ItemStack itemStack, final EntityPlayer player, final List toolTip,
        final boolean advancedToolTips) {
        toolTip.add("使用没倒掉的牛奶和烧剩下的蜂窝煤制作");
    }
}
