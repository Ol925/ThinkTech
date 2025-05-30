package com.OL925.ThinkTech.common.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;


public class ControllerTier1 extends Block{

    private IIcon frontIcon;
    private IIcon sideIcon;

    public ControllerTier1() {
        super(Material.rock);
        this.setBlockName("ControllerTier1");
        this.setHardness(5.0F);
        this.setResistance(30.0F);
        this.setStepSound(soundTypeStone);
        this.setHarvestLevel("pickaxe", 2);
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.frontIcon = iconRegister.registerIcon("ThinkTech:CC_1");
        this.sideIcon = iconRegister.registerIcon("ThinkTech:BLOCK_PLASCRETE");
    }

    @Override
    public IIcon getIcon(int side, int meta) {
        // 默认放置时正面朝北（面向玩家）
        return side == 3 ? this.frontIcon : this.sideIcon;
    }

    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int meta = world.getBlockMetadata(x, y, z);
        // 这里假设meta用来存储朝向
        // 2=北 3=南 4=西 5=东
        if (side == meta) {
            return this.frontIcon;
        }
        return this.sideIcon;
    }

    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        int facing = MathHelper.floor_double((double)(placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        int meta = 2;
        if (facing == 0) meta = 2;
        if (facing == 1) meta = 5;
        if (facing == 2) meta = 3;
        if (facing == 3) meta = 4;
        world.setBlockMetadataWithNotify(x, y, z, meta, 2);
    }
}
