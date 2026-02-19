package com.OL925.ThinkTech.common.block;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.ItemCasings;
import gregtech.common.blocks.MaterialCasings;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import java.util.List;
import net.minecraft.creativetab.CreativeTabs;

public class ThTCasings0 extends BlockCasingsAbstract {

    // 不要给字段加 @SideOnly，避免服务端裁剪导致 NoSuchFieldError
    private IIcon[] icons = new IIcon[3];

    public ThTCasings0() {
        super(ItemCasings.class, "thinktech.thtCasings0", MaterialCasings.INSTANCE, 16);
        setBlockName("thtCasings0");
    }

    private static int clampTier(int meta) {
        if (meta < 0) return 0;
        if (meta > 2) return 2;
        return meta;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister reg) {
        icons[0] = reg.registerIcon("ThinkTech:CC_1");
        icons[1] = reg.registerIcon("ThinkTech:CC_2");
        icons[2] = reg.registerIcon("ThinkTech:CC_3");
    }

    // 六面同贴图：不看 side，只看 meta
    @Override
    @SideOnly(Side.CLIENT)
    public IIcon getIcon(int side, int meta) {
        return icons[clampTier(meta)];
    }

    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    @Override
    public int getTextureIndex(int meta) {
        return (16 << 7) | clampTier(meta);
    }
}