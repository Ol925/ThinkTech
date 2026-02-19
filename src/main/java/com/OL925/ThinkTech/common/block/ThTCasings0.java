package com.OL925.ThinkTech.common.block;

import com.OL925.ThinkTech.common.tile.TileThTCasingFacing;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.common.blocks.BlockCasingsAbstract;
import gregtech.common.blocks.ItemCasings;
import gregtech.common.blocks.MaterialCasings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.List;

public class ThTCasings0 extends BlockCasingsAbstract implements net.minecraft.block.ITileEntityProvider {


    private IIcon plascrete;

    private IIcon[] fronts = new IIcon[3];

    public ThTCasings0() {
        // 继承 GT casing 基类 -> wrench harvest tool、machine update、硬度抗爆、casing render 都自动具备
        super(ItemCasings.class, "thinktech.thtCasings0", MaterialCasings.INSTANCE, 16);
        setBlockName("thtCasings0");
    }

    private static int clampTier(int meta) {
        return switch (meta){
            case 0 -> 0;
            case 1 -> 1;
            case 2 -> 2;
            default -> meta;
        };
    }

    // --- TileEntity ---

    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    @Override
    public TileEntity createNewTileEntity(World world, int meta) {
        return new TileThTCasingFacing();
    }

    // --- Textures ---

    @Override
    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(net.minecraft.client.renderer.texture.IIconRegister reg) {
        plascrete = reg.registerIcon("ThinkTech:BLOCK_PLASCRETE");
        fronts[0] = reg.registerIcon("ThinkTech:CC_1");
        fronts[1] = reg.registerIcon("ThinkTech:CC_2");
        fronts[2] = reg.registerIcon("ThinkTech:CC_3");
    }

    // 物品/无世界坐标：默认用“side==3”为正面
    @Override
    public IIcon getIcon(int side, int meta) {
        int tier = clampTier(meta);
        return (side == 3) ? fronts[tier] : plascrete;
    }

    // 世界坐标：从 TE 读 facing
    @Override
    public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side) {
        int tier = clampTier(world.getBlockMetadata(x, y, z));
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileThTCasingFacing casingTE) {
            if (side == casingTE.getFacing()) return fronts[tier];
        } else {
            if (side == 3) return fronts[tier];
        }
        return plascrete;
    }

    // 放置时写入 TE 的 facing（meta 不再存朝向，只存 tier）
    @Override
    public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase placer, ItemStack stack) {
        super.onBlockPlacedBy(world, x, y, z, placer, stack);

        int facing = MathHelper.floor_double((placer.rotationYaw * 4.0F / 360.0F) + 0.5D) & 3;
        byte f = 2; // 北
        if (facing == 0) f = 2;
        if (facing == 1) f = 5;
        if (facing == 2) f = 3;
        if (facing == 3) f = 4;

        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileThTCasingFacing casingTE) {
            casingTE.setFacing(f);
        }
    }

    // 创造栏只显示 0/1/2
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubBlocks(Item item, CreativeTabs tab, List list) {
        list.add(new ItemStack(item, 1, 0));
        list.add(new ItemStack(item, 1, 1));
        list.add(new ItemStack(item, 1, 2));
    }

    // 给 GT casing 纹理页系统用（用一个“高位页码”避免撞 GT 原生 casing 的 0~63 等低位索引）
    @Override
    public int getTextureIndex(int aMeta) {
        int tier = clampTier(aMeta);
        return (16 << 7) | tier;
    }
}