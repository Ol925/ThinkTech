package com.OL925.ThinkTech.common.MTE;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.OL925.ThinkTech.common.hatch.ThT_HatchFuelBus;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.SoundResource;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchOutputBus;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaConfigHandler;

import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;

import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public abstract class ThT_CrucibleBase<T extends ThT_CrucibleBase<T>> extends MTEExtendedPowerMultiBlockBase<T> {

    protected long mHeat = 0;
    protected final ArrayList<ThT_HatchFuelBus> mFuelBusses = new ArrayList<>();

    public abstract int getCrucibleTier();
    public abstract long getHeatCapacity();
    protected abstract float getSpeedBonus();
    protected abstract double getHeatBoostThresholdRatio();
    protected abstract int getFuelBurnInterval();

    public ThT_CrucibleBase(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public ThT_CrucibleBase(String aName) {
        super(aName);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.Crucible;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {
            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                int required = recipe.mSpecialValue;
                if (required <= 0) required = 1;
                if (getCrucibleTier() < required) {
                    return SimpleCheckRecipeResult.ofFailure("crucible_insufficient_tier");
                }
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setOverclock(2, 4);
                if (mHeat >= (long) (getHeatCapacity() * getHeatBoostThresholdRatio())) {
                    setSpeedBonus(getSpeedBonus());
                }
                return super.process();
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTValues.V[4]);
        logic.setAvailableAmperage(1L);
        logic.setAmperageOC(false);
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        if (aEU <= 0) return true;
        if (mHeat < aEU) return false;
        mHeat -= aEU;
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (lEUt > 0) {
            addEnergyOutput(((long) lEUt * mEfficiency) / 10000);
            return true;
        }
        if (lEUt < 0) {
            long aHeatVal = ((-lEUt * 10000) / Math.max(1000, mEfficiency));
            if (mHeat < aHeatVal) {
                return false;
            }
            mHeat -= aHeatVal;
        }
        return true;
    }

    @Override
    public boolean addInputBusToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null && aTileEntity.getMetaTileEntity() instanceof ThT_HatchFuelBus fuelBus) {
            fuelBus.updateTexture(aBaseCasingIndex);
            return mFuelBusses.add(fuelBus);
        }
        return super.addInputBusToMachineList(aTileEntity, aBaseCasingIndex);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;

        long cap = getHeatCapacity();
        if (aTick % getFuelBurnInterval() == 0 && mMaxProgresstime > 0 && mHeat < cap) {
            for (ThT_HatchFuelBus tHatch : mFuelBusses) {
                for (int i = 0; i < tHatch.getSizeInventory(); i++) {
                    ItemStack stack = tHatch.getStackInSlot(i);
                    if (stack == null) continue;
                    int burnTime = GTModHandler.getFuelValue(stack);
                    if (burnTime <= 0) continue;
                    long heatToAdd = (long) burnTime * 10;
                    if (mHeat + heatToAdd > cap) continue;
                    mHeat += heatToAdd;
                    ItemStack container = GTUtility.getContainerItem(stack, true);
                    stack.stackSize--;
                    if (stack.stackSize <= 0) {
                        tHatch.setInventorySlotContents(i, null);
                    }
                    if (container != null) {
                        for (MTEHatchOutputBus outHatch : mOutputBusses) {
                            for (int j = 0; j < outHatch.getSizeInventory(); j++) {
                                ItemStack outStack = outHatch.getStackInSlot(j);
                                if (outStack == null) {
                                    outHatch.setInventorySlotContents(j, GTUtility.copy(container));
                                    container = null;
                                    break;
                                }
                                if (GTUtility.areStacksEqual(outStack, container)
                                    && outStack.stackSize + container.stackSize <= outStack.getMaxStackSize()) {
                                    outStack.stackSize += container.stackSize;
                                    container = null;
                                    break;
                                }
                            }
                            if (container == null) break;
                        }
                    }
                    tHatch.updateSlots();
                    break;
                }
                if (mHeat >= cap) break;
            }
        }

        if (aTick % 20 == 0 && mProgresstime <= 0 && mHeat > 0) {
            mHeat -= mHeat / 10000;
        }
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mFuelBusses.clear();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("mHeat", mHeat);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mHeat = aNBT.getLong("mHeat");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag,
                                World world, int x, int y, int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setLong("heat", mHeat);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
                             IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        long heat = tag.getLong("heat");
        long cap = getHeatCapacity();
        int percent = (int) (heat * 100 / cap);
        String color = heat >= cap / 2 ? "§a" : "§e";
        currentTip.add(color + translateToLocalFormatted("waila.crucible.heat", heat, cap, percent));
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_EBF_LOOP;
    }

    @Override
    public void checkMaintenance() {}

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean shouldCheckMaintenance() {
        return false;
    }
}
