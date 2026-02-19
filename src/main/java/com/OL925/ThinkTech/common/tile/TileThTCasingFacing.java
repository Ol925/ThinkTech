package com.OL925.ThinkTech.common.tile;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public class TileThTCasingFacing extends TileEntity {

    // 2=北 3=南 4=西 5=东
    private byte facing = 3;

    public byte getFacing() {
        return facing;
    }

    public void setFacing(byte facing) {
        this.facing = facing;
        if (worldObj != null) {
            worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
            markDirty();
        }
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        facing = nbt.getByte("Facing");
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setByte("Facing", facing);
    }
}