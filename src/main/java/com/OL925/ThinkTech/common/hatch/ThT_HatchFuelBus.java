package com.OL925.ThinkTech.common.hatch;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInputBus;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class ThT_HatchFuelBus extends MTEHatchInputBus {

    private static final int FUEL_SLOTS = 16;

    public ThT_HatchFuelBus(int id, String name, String nameRegional) {
        super(id, name, nameRegional, 1, FUEL_SLOTS + 1);
    }

    public ThT_HatchFuelBus(String aName, int aTier, int aSlots, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ThT_HatchFuelBus(mName, mTier, mInventory.length, mDescriptionArray, mTextures);
    }

    @Override
    public int getCircuitSlot() {
        return FUEL_SLOTS;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex,
                                  ForgeDirection side, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex,
                                 ForgeDirection side, ItemStack aStack) {
        return aIndex < FUEL_SLOTS;
    }
}
