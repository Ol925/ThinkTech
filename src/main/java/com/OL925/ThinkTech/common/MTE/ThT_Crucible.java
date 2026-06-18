package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
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
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaConfigHandler;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ThT_Crucible extends MTEExtendedPowerMultiBlockBase<ThT_Crucible>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private long mHeat = 0;
    private final ArrayList<ThT_HatchFuelBus> mFuelBusses = new ArrayList<>();
    private static final long HEAT_CAPACITY = 20_000_000L;
    private static final long HEAT_BOOST_THRESHOLD = 10_000_000L;

    private static IStructureDefinition<ThT_Crucible> STRUCTURE_DEFINITION = null;
    private static final ITexture DEOXIDIZED_STEEL_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));

    public ThT_Crucible(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public ThT_Crucible(String aName) {
        super(aName);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] Shape = new String[][]{
        {"AAAA", "AAA~", "AAAA"},
        {"A  A", "A  A", "AAAA"},
        {"A  A", "A  A", "AAAA"},
        {"AAAA", "AAAA", "AAAA"}
    };

    @Override
    public IStructureDefinition<ThT_Crucible> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_Crucible>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A',
                    buildHatchAdder(ThT_Crucible.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                        .casingIndex(Textures.BlockIcons.getTextureIndex(DEOXIDIZED_STEEL_CASING))
                        .hint(1)
                        .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 1, 0, errors)) return;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 1, 0, elementBudget, env, false, true);
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
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.Crucible;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {
            @NotNull
            @Override
            public CheckRecipeResult process() {
                setOverclock(2, 4);
                if (mHeat >= HEAT_BOOST_THRESHOLD) {
                    setSpeedBonus(0.7f);
                }
                return super.process();
            }
        }.setMaxParallelSupplier(() -> 4);
    }

    @Override
    public int getMaxParallelRecipes() {
        return 4;
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
        int percent = (int) (heat * 100 / HEAT_CAPACITY);
        String color = heat >= HEAT_BOOST_THRESHOLD ? "§a" : "§e";
        currentTip.add(color + translateToLocalFormatted("waila.crucible.heat", heat, HEAT_CAPACITY, percent));
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (!aBaseMetaTileEntity.isServerSide()) return;

        if (aTick % 80 == 0 && mMaxProgresstime > 0 && mHeat < HEAT_CAPACITY) {
            for (ThT_HatchFuelBus tHatch : mFuelBusses) {
                for (int i = 0; i < tHatch.getSizeInventory(); i++) {
                    ItemStack stack = tHatch.getStackInSlot(i);
                    if (stack == null) continue;
                    int burnTime = GTModHandler.getFuelValue(stack);
                    if (burnTime <= 0) continue;
                    long heatToAdd = (long) burnTime * 10;
                    if (mHeat + heatToAdd > HEAT_CAPACITY) continue;
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
                if (mHeat >= HEAT_CAPACITY) break;
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
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
                                 boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] { DEOXIDIZED_STEEL_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { DEOXIDIZED_STEEL_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { DEOXIDIZED_STEEL_CASING };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.crucibleType"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips5"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips6"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips7"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips8"))
            .addInfo(translateToLocalFormatted("mte.crucible.tooltips9"))
            .addInfo("添加者：§4§nOL925")
            .beginStructureBlock(4, 4, 3, false)
            .addController("正面底层右侧")
            .addCasingInfoExactly("脱氧钢机械方块", 40, false)
            .addInputBus("任意脱氧钢机械方块")
            .addInputHatch("任意脱氧钢机械方块")
            .addOutputBus("任意脱氧钢机械方块")
            .addOutputHatch("任意脱氧钢机械方块")
            .toolTipFinisher("§d§l§oThinkTech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_Crucible(this.mName);
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
