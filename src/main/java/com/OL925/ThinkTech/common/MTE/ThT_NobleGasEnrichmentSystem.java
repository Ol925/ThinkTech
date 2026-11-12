package com.OL925.ThinkTech.common.MTE;

import com.OL925.ThinkTech.Tags;
import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.OL925.ThinkTech.common.init.ThTBlockLoader;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;

import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;

import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.List;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class ThT_NobleGasEnrichmentSystem extends MTEExtendedPowerMultiBlockBase<ThT_NobleGasEnrichmentSystem>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private static ITexture CASING = TextureFactory.of(BLOCK_PLASCRETE);
    private static ITexture STAINLESS_STEEL_MACHINE_CASING = getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1));

    private int controllerTier = 0;

    private static final double CORE_EU_DISCOUNT_PER_TIER = 0.5;

    private long mLastRecipeEUt = -1L;
    private long mSyncedFullParallelEUt = -1L;
    private long mSyncedPowerBudget = -1L;
    private int mSyncedParallelCap = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static IStructureDefinition<ThT_NobleGasEnrichmentSystem> STRUCTURE_DEFINITION = null;
    private static String[][] Shape = new String[][]{{
        "FFFFFFF",
        "F DDD F",
        "F     F",
        "F     F",
        "F     F",
        "F     F",
        "FFDDDFF"
    },{
        " F   F ",
        " DBBBD ",
        "  GGG  ",
        "  GGG  ",
        "  G~G  ",
        "  GGG  ",
        "FDBBBDF"
    },{
        " F   F ",
        "DBCCCBD",
        " G   G ",
        " G   G ",
        " G   G ",
        " G   G ",
        "DBBBBBD"
    },{
        " F   F ",
        "DBCCCBD",
        " G E G ",
        " A E A ",
        " A E A ",
        " G E G ",
        "DBBBBBD"
    },{
        " F   F ",
        "DBCCCBD",
        " G   G ",
        " G   G ",
        " G   G ",
        " G   G ",
        "DBBBBBD"
    },{
        " F   F ",
        " DBBBD ",
        "  GGG  ",
        "  GCG  ",
        "  GCG  ",
        "  GCG  ",
        "FDBBBDF"
    },{
        "FFFFFFF",
        "F DDD F",
        "F     F",
        "F     F",
        "F     F",
        "F     F",
        "FFDDDFF"
    }};

    public ThT_NobleGasEnrichmentSystem(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ThT_NobleGasEnrichmentSystem(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 1);
    }

    @Override
    public IStructureDefinition<ThT_NobleGasEnrichmentSystem> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_NobleGasEnrichmentSystem>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A',Casings.ReinforcedGlass.asElement())
                .addElement('B',ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement('C',
                    ofBlock(GregTechAPI.sBlockCasings3, 10))//
                .addElement('D',
                     buildHatchAdder(ThT_NobleGasEnrichmentSystem.class)
                     .atLeast(Energy.or(ExoticEnergy), InputBus, OutputBus, InputHatch, OutputHatch)
                    .casingIndex(getTextureIndex(STAINLESS_STEEL_MACHINE_CASING))
                    .hint(1)
                    .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement('E',ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('F',ofFrame(Materials.Steel))
                .addElement('G',ofChain(
                    ofBlock(GregTechAPI.sBlockReinforced,2),
                        onElementPass(ThT_NobleGasEnrichmentSystem::setT1, ofBlock(ThTBlockLoader.thtCasings0, 0)),
                        onElementPass(ThT_NobleGasEnrichmentSystem::setT2, ofBlock(ThTBlockLoader.thtCasings0, 1)),
                        onElementPass(ThT_NobleGasEnrichmentSystem::setT3, ofBlock(ThTBlockLoader.thtCasings0, 2))
                ))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setOverclock(4,4);
                setEuModifier(getCoreEuModifier());
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                mLastRecipeEUt = recipe.mEUt;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return controllerTier <= 0 ? 1 : 16 << (controllerTier - 1);
    }

    protected double getCoreEuModifier() {
        return Math.pow(CORE_EU_DISCOUNT_PER_TIER, Math.max(controllerTier, 0));
    }

    private long perParallelEnergy(long aRecipeEUt, int aCoreTier) {
        if (aRecipeEUt <= 0) return 0L;
        return (long) Math.ceil(aRecipeEUt * Math.pow(CORE_EU_DISCOUNT_PER_TIER, Math.max(aCoreTier, 0)));
    }

    public long getFullParallelEnergy() {
        if (mLastRecipeEUt <= 0) return -1L;
        return getMaxParallelRecipes() * perParallelEnergy(mLastRecipeEUt, controllerTier);
    }

    public long getPowerBudget() {
        boolean singleAmp = !debugEnergyPresent && mEnergyHatches.size() == 1 && mExoticEnergyHatches.isEmpty();
        return getAverageInputVoltage() * (singleAmp ? 1L : getMaxInputAmps());
    }

    public int getPowerLimitedParallel() {
        long per = perParallelEnergy(mLastRecipeEUt, controllerTier);
        if (per <= 0) return getMaxParallelRecipes();
        return (int) Math.min(getMaxParallelRecipes(), getPowerBudget() / per);
    }

    private static String parallelHint(long aRequiredEUt, long aBudget, int aCap) {
        if (aRequiredEUt <= 0) return translateToLocal("mte.NGES.ui.parallel_unknown");
        if (aBudget >= aRequiredEUt) return translateToLocalFormatted("mte.NGES.ui.parallel_ok", aRequiredEUt, aBudget);
        return translateToLocalFormatted("mte.NGES.ui.parallel_short", aRequiredEUt, aBudget, aCap);
    }

    private static final String WAILA_FULL_PARALLEL_EU = "thtNgesFullParallelEUt";
    private static final String WAILA_POWER_BUDGET = "thtNgesPowerBudget";
    private static final String WAILA_PARALLEL_CAP = "thtNgesParallelCap";

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
            int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (!mMachine) return; // 结构未成型时不显示电力提示
        tag.setLong(WAILA_FULL_PARALLEL_EU, getFullParallelEnergy());
        tag.setLong(WAILA_POWER_BUDGET, getPowerBudget());
        tag.setInteger(WAILA_PARALLEL_CAP, getPowerLimitedParallel());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
            IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (!tag.hasKey(WAILA_POWER_BUDGET)) return;
        currentTip.add(
                parallelHint(
                        tag.getLong(WAILA_FULL_PARALLEL_EU),
                        tag.getLong(WAILA_POWER_BUDGET),
                        tag.getInteger(WAILA_PARALLEL_CAP)));
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);

        screenElements
                .widget(new FakeSyncWidget.LongSyncer(this::getFullParallelEnergy, v -> mSyncedFullParallelEUt = v))
                .widget(new FakeSyncWidget.LongSyncer(this::getPowerBudget, v -> mSyncedPowerBudget = v))
                .widget(new FakeSyncWidget.IntegerSyncer(this::getPowerLimitedParallel, v -> mSyncedParallelCap = v))
                .widget(
                        TextWidget
                                .dynamicString(
                                        () -> parallelHint(
                                                mSyncedFullParallelEUt,
                                                mSyncedPowerBudget,
                                                mSyncedParallelCap))
                                .setTextAlignment(Alignment.CenterLeft)
                                .setEnabled(widget -> mMachine));
    }

    // [改动-修正] 混放 T1/T2/T3 核心时取最高等级
    //（原来是谁在结构扫描顺序里最后被扫到就用谁；配合 checkMachine 里的清零，语义 = 结构内存在的最高核心等级）
    private void setT1() {controllerTier = Math.max(controllerTier, 1);}

    private void setT2() {controllerTier = Math.max(controllerTier, 2);}

    private void setT3() {controllerTier = Math.max(controllerTier, 3);}

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.NGES"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt1"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt2"))
            .addInfo(translateToLocalFormatted("mte.common.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt4"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt5"))
            .addInfo(translateToLocalFormatted("mte.common.tooltips5"))
            .addInfo("添加者：§4§nOL925")
            .addInfo("§d§l§o" + Tags.MODNAME)
            .beginStructureBlock(7, 7, 7, false)
            .addController("见结构预览")
            .addCasingInfoExactly("塑料混凝土方块",40,false)
            .addCasingInfoExactly("钢框架",56,false)
            .addCasingInfoExactly("防爆玻璃",4,false)
            .addCasingInfoExactly("格栅机械方块",12,false)
            .addCasingInfoExactly("洁净不锈钢机械方块",29,false)
            .addCasingInfoExactly("脱氧钢机械方块",33,false)
            .addCasingInfoExactly("聚四氟乙烯管道方块",4,false)
            .addInputBus("任意洁净不锈钢机械方块",1)
            .addInputHatch("任意洁净不锈钢机械方块",1)
            .addDynamoHatch("任意洁净不锈钢机械方块",1)
            //.addOutputBus("任意洁净不锈钢机械方块",1)
            .addOutputHatch("输出稀有气体,任意洁净不锈钢机械方块",1)
            .toolTipFinisher();

        return tt;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.NobleGasEnrichmentSystem;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        controllerTier = 0; // [改动-修正] 检查前清零，避免拆掉核心后仍保留旧并行等级（对齐 ThT_DrillingRig 的 mMachineLevel = 0）
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 1, errors)) return;
    }

    @Override
    public boolean supportsVoidProtection(){
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
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ThT_NobleGasEnrichmentSystem(this.mName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 1, elementBudget, env, false, true);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getProcessStartSound() {
            return SoundResource.GT_MACHINES_PURIFICATION_PLASMA_LOOP;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            if (active) return new ITexture[] { CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FUSION1_GLOW)
                    .extFacing()
                    .glow()
                    .build() };

            return new ITexture[] { CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FUSION1)
                .extFacing()
                .build() };
        }
        return new ITexture[] { CASING };
    }

    //maintenance
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
