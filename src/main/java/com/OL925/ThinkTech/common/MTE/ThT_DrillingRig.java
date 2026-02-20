package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import javax.annotation.Nonnull;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.OL925.ThinkTech.common.init.ThTList;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
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
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import tectech.thing.casing.TTCasingsContainer;

public class ThT_DrillingRig extends MTEExtendedPowerMultiBlockBase<ThT_DrillingRig>
        implements ISurvivalConstructable, ISecondaryDescribable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final ITexture SOLID_STEEL_MACHINE_CASING = Textures.BlockIcons
            .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));

    // 机器等级 (1~5)，由管道方块(C)决定
    private int mMachineLevel = 0;
    // 芯片等级 (0=无芯片, 1~4)
    private int mChipTier = 0;
    private boolean mUpgraded = false;
    private double mSpeedBonus = 1.0;

    private static IStructureDefinition<ThT_DrillingRig> STRUCTURE_DEFINITION = null;

    // ==================== 结构字符串 ====================
    // A -> hatch放置位 + 脱氧钢机械方块
    // B -> 化学惰性机械方块 (gt.blockcasings8, 0)
    // C -> 分级管道方块（决定机器等级）
    // D -> 钢框架（固定）
    // E -> 空气
    // Offsets: 4 10 1
    private static final String[][] Shape = new String[][] {
            { // Layer 0
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "   AAA   ",
                    "   AAA   ",
                    "         ",
                    "         ",
                    "         "
            },
            { // Layer 1
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "  ABBBA  ",
                    "  A   A  ",
                    "   AAA   ",
                    "   A~A   ",
                    "   AAA   "
            },
            { // Layer 2
                    "    A    ",
                    "   AAA   ",
                    "   D D   ",
                    "   D D   ",
                    "   D D   ",
                    "   D D   ",
                    " DDD DDD ",
                    "DABBBBBAD",
                    "DA  D  AD",
                    "D A D A D",
                    "D A D A D",
                    "D AAAAA D"
            },
            { // Layer 3
                    "   AAA   ",
                    "   ACA   ",
                    "    C    ",
                    "    C    ",
                    "    C    ",
                    "    C    ",
                    "    C    ",
                    " ABBCBBA ",
                    " A DCD A ",
                    "  ADCDA  ",
                    "  ADCDA  ",
                    "  AAAAA  "
            },
            { // Layer 4
                    "    A    ",
                    "   AAA   ",
                    "   D D   ",
                    "   D D   ",
                    "   D D   ",
                    "   D D   ",
                    " DDD DDD ",
                    "DABBBBBAD",
                    "DA  D  AD",
                    "D A D A D",
                    "D A D A D",
                    "D AAAAA D"
            },
            { // Layer 5
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "  ABBBA  ",
                    "  A   A  ",
                    "   AAA   ",
                    "   AAA   ",
                    "   AAA   "
            },
            { // Layer 6
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "         ",
                    "   AAA   ",
                    "   AAA   ",
                    "         ",
                    "         ",
                    "         "
            }
    };

    // ==================== 构造方法 ====================
    public ThT_DrillingRig(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ThT_DrillingRig(String aName) {
        super(aName);
    }

    // ==================== 等级判定 ====================

    public static int getPipeTier(Block block, int meta) {
        if (block == GregTechAPI.sBlockCasings2 && meta == 13) return 1;
        if (block == GregTechAPI.sBlockCasings2 && meta == 14) return 2;
        if (block == GregTechAPI.sBlockCasings2 && meta == 15) return 3;
        if (block == GregTechAPI.sBlockCasings9 && meta == 0) return 4;
        if (block == TTCasingsContainer.sBlockCasingsTT && meta == 8) return 5;
        return 0;
    }

    // ==================== 结构定义 ====================
    @Override
    public IStructureDefinition<ThT_DrillingRig> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_DrillingRig>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, Shape)
                    .addElement('A',
                            buildHatchAdder(ThT_DrillingRig.class)
                                    .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus)
                                    .casingIndex(Textures.BlockIcons.getTextureIndex(SOLID_STEEL_MACHINE_CASING))
                                    .dot(1)
                                    .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                    // B -> 化学惰性机械方块
                    .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 0))
                    // C -> 分级管道方块（决定机器等级）
                    .addElement('C',
                            withChannel("pipe",
                                    ofBlocksTiered(
                                            ThT_DrillingRig::getPipeTier,
                                            ImmutableList.of(
                                                    Pair.of(GregTechAPI.sBlockCasings2, 13),  // Tier 1: 钢管道
                                                    Pair.of(GregTechAPI.sBlockCasings2, 14),  // Tier 2: 钛管道
                                                    Pair.of(GregTechAPI.sBlockCasings2, 15),  // Tier 3: 钨钢管道
                                                    Pair.of(GregTechAPI.sBlockCasings9, 0),   // Tier 4: PBI管道
                                                    Pair.of(TTCasingsContainer.sBlockCasingsTT, 8)   // Tier 5: TT管道
                                            ),
                                            0,
                                            (m, t) -> m.mMachineLevel = t,
                                            m -> m.mMachineLevel
                                    )))
                    // D -> 钢框架（固定）
                    .addElement('D', ofFrame(Materials.Steel))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // ==================== 检查结构 ====================
    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mMachineLevel = 0;
        if (checkPiece(STRUCTURE_PIECE_MAIN, 4, 10, 1)) {
            if (mMachineLevel > 0) {
                fixAllIssues();
                return true;
            }
        }
        return false;
    }

    // ==================== 处理逻辑 ====================
    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(4, 4); // 无损超频
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    /**
     * 并行数 = 8 + chipTier * 8
     */
    @Override
    public int getMaxParallelRecipes() {
        return 8 + mChipTier * 8;
    }

    /**
     * 速度加成：每级提升15%
     * SpeedBonus = 1.0 - (mMachineLevel - 1) * 0.15
     */
    protected float getSpeedBonus() {
        double sb = 1.0 - (mMachineLevel - 1) * 0.15;
        if (sb <= 0) sb = 0.01;
        mSpeedBonus = sb;
        return (float) mSpeedBonus;
    }

    // ==================== 芯片检测 ====================
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0) {
            ItemStack aGuiStack = this.getControllerSlot();
            if (aGuiStack != null) {
                if (GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER1.get(1))) {
                    this.mChipTier = 1;
                    this.mUpgraded = true;
                } else if (GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER2.get(1))) {
                    this.mChipTier = 2;
                    this.mUpgraded = true;
                } else if (GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER3.get(1))) {
                    this.mChipTier = 3;
                    this.mUpgraded = true;
                } else if (GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER4.get(1))) {
                    this.mChipTier = 4;
                    this.mUpgraded = true;
                } else {
                    this.mChipTier = 0;
                    this.mUpgraded = false;
                }
            } else {
                this.mChipTier = 0;
                this.mUpgraded = false;
            }
        }
    }

    // ==================== 配方表 ====================
    @Override
    public RecipeMap<?> getRecipeMap() {return ThTRecipeMap.DrillingRig;}

    // ==================== NBT ====================
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mMachineLevel", mMachineLevel);
        aNBT.setInteger("mChipTier", mChipTier);
        aNBT.setBoolean("mUpgraded", mUpgraded);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mMachineLevel = aNBT.getInteger("mMachineLevel");
        mChipTier = aNBT.getInteger("mChipTier");
        mUpgraded = aNBT.getBoolean("mUpgraded");
    }

    // ==================== 贴图：固定使用脱氧钢机械方块 ====================
    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
                                 boolean active, boolean redstone) {
        if (side == facing) {
            if (active) {
                return new ITexture[] {
                        SOLID_STEEL_MACHINE_CASING,
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                                .extFacing()
                                .build(),
                        TextureFactory.builder()
                                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                                .extFacing()
                                .glow()
                                .build()
                };
            }
            return new ITexture[] {
                    SOLID_STEEL_MACHINE_CASING,
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                            .extFacing()
                            .glow()
                            .build()
            };
        }
        return new ITexture[] { SOLID_STEEL_MACHINE_CASING };
    }

    // ==================== Tooltip ====================
    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.DR"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips1"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips2"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips3"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips4"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips5"))
                .addInfo(translateToLocalFormatted("mte.DR.tooltips6"))
                .addInfo(translateToLocalFormatted("mte.common.tooltips4"))
                .beginStructureBlock(9, 12, 7, false)
                .addController("见结构预览")
                .addCasingInfoExactly("脱氧钢机械方块", 0, false)
                .addCasingInfoExactly("化学惰性机械方块", 0, false)
                .addCasingInfoExactly("管道方块(决定等级)", 0, true)
                .addCasingInfoExactly("钢框架", 0, false)
                .addInputBus("任意脱氧钢机械方块")
                .addInputHatch("任意脱氧钢机械方块")
                .addOutputBus("任意脱氧钢机械方块")
                .addOutputHatch("任意脱氧钢机械方块")
                .addEnergyHatch("任意脱氧钢机械方块")
                .toolTipFinisher("§d§l§oThinkTech");

        return tt;
    }

    // ==================== 辅助输出 ====================
    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    // ==================== 基础方法 ====================
    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
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
        return new ThT_DrillingRig(this.mName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 10, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 10, 1, elementBudget, env, false, true);
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_STEAM_WASHER_LOOP;
    }

    // ==================== 免维护 ====================
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