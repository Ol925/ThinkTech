package com.OL925.ThinkTech.common.MTE;

import static com.OL925.ThinkTech.Recipe.implosionGeneratorFlueRecipe.IGFuelVoltage;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTRecipeConstants.LNG_BASIC_OUTPUT;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.init.Blocks.iron_bars;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import javax.annotation.Nonnull;

import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.dreammaster.block.BlockList;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class ThT_ImplosionGenerator extends GTPPMultiBlockBase<ThT_ImplosionGenerator>
    implements ISurvivalConstructable, ISecondaryDescribable {

    // 变量声明部分
    private int mBlockTier = 0;
    private HeatingCoilLevel mCoilLevel;
    private static IStructureDefinition<ThT_ImplosionGenerator> STRUCTURE_DEFINITION = null;
    private static ITexture SOLID_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));
    private static long displayGenerating;

    // 构造方法，三个形参分别是物品id,注册名,本地化名
    public ThT_ImplosionGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static String[][] Shape = new String[][] { { "   B   ", "  BBB  ", "  BBB  ", "BHBBBHB", "BBB~BBB" },
        { "  FFF  ", " FBBBF ", " FBCBF ", "HFBBBFH", "BDDDDDB" },
        { "  AFA  ", " AEEEA ", " AECEA ", "HFEEEFH", "BDDDDDB" },
        { "  AFA  ", " A G A ", " AG GA ", "HF G FH", "BDDDDDB" },
        { "  AFA  ", " A G A ", " AG GA ", "HF G FH", "BDDDDDB" },
        { "  AFA  ", " AEEEA ", " AECEA ", "HFEEEFH", "BDDDDDB" },
        { "  FFF  ", " FBBBF ", " FBCBF ", "HFBBBFH", "BDDDDDB" },
        { "  BBB  ", " BBCBB ", " BCCCB ", "BBCCCBB", "BBBBBBB" } };

    // 结构声明部分
    @Override
    public IStructureDefinition<ThT_ImplosionGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_ImplosionGenerator>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A', ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true))
                //
                .addElement(
                    'B',
                    buildHatchAdder(ThT_ImplosionGenerator.class)
                        .atLeast(Dynamo, InputHatch, Muffler)
                        .casingIndex(Textures.BlockIcons.getTextureIndex(SOLID_STEEL_MACHINE_CASING))
                        .dot(1)
                        .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 13))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement(
                    'E',
                    withChannel("coil", GTStructureUtility.ofCoil(ThT_ImplosionGenerator::setCoilLevel, ThT_ImplosionGenerator::getCoilLevel)))
                .addElement('F', ofFrame(Materials.StainlessSteel))
                .addElement(
                    'G',
                    withChannel("stone",
                        ofBlocksTiered(
                            ThT_ImplosionGenerator::getTierOfStone,
                            ImmutableList.of(
                                Pair.of(BlockList.BronzePlatedReinforcedStone.getBlock(), 0), // 三硝基甲苯HV
                                Pair.of(BlockList.SteelPlatedReinforcedStone.getBlock(), 0), // PETN EV
                                Pair.of(BlockList.TitaniumPlatedReinforcedStone.getBlock(), 0), // 硝化甘油 IV
                                Pair.of(BlockList.TungstensteelPlatedReinforcedStone.getBlock(), 0), // 奥克托今 LUV
                                Pair.of(BlockList.NaquadahPlatedReinforcedStone.getBlock(), 0)// CL-20 ZPM
                            ),
                            0,
                            (m, t) -> m.mBlockTier = t,
                            m -> m.mBlockTier)))
                .addElement('H', ofBlockAnyMeta(iron_bars))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    // 获取机器类型
    @Override
    public String getMachineType() {
        return "ImplosionGenerator";
    }

    // 音效持续时间
    @Override
    protected int getTimeBetweenProcessSounds() {
        return 20;
    }

    @Override
    public boolean shouldCheckMaintenance(){
        return false;
    }

    // 机器运行音效
    @Override
    protected SoundResource getProcessStartSound() {
        if (mMufflerHatches.isEmpty()) {
            return SoundResource.RANDOM_EXPLODE;
        }
        return null;
    }

    @Override
    public void doSound(byte aIndex, double aX, double aY, double aZ) {
        switch (aIndex) {
            case PROCESS_START_SOUND_INDEX -> {
                if (getProcessStartSound() != null)
                    GTUtility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
            }
            case INTERRUPT_SOUND_INDEX -> GTUtility
                .doSoundAtClient(SoundResource.IC2_MACHINES_INTERRUPT_ONE, 100, 1F, aX, aY, aZ);
        }
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        if (aIndex == PROCESS_START_SOUND_INDEX) {
            if (getProcessStartSound() != null)
                GTUtility.doSoundAtClient(getProcessStartSound(), getTimeBetweenProcessSounds(), 1.0F, aX, aY, aZ);
        }
    }

    // 控制机器的等级
    public static int getTierOfStone(Block block, int meta) {
        if (block == BlockList.BronzePlatedReinforcedStone.getBlock()) {
            return 1;
        } else if (block == BlockList.SteelPlatedReinforcedStone.getBlock()) {
            return 2;
        } else if (block == BlockList.TitaniumPlatedReinforcedStone.getBlock()) {
            return 3;
        } else if (block == BlockList.TungstensteelPlatedReinforcedStone.getBlock()) {
            return 4;
        } else if (block == BlockList.NaquadahPlatedReinforcedStone.getBlock()) {
            return 5;
        }
        return 0;
    }

    // 获取最大平行配方
    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    // 设置配方反射
    @Override
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.implosionGeneratorFuels;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    public ThT_ImplosionGenerator(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_ImplosionGenerator(this.mName);
    }

    // 创造模式自动构建
    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 4, 0);
    }

    // 生存模式自动构建
    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
    }

    // 设置机器主方块贴图
    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
        boolean active, boolean redstone) {

        if (side == facing) {
            if (active) return new ITexture[] { SOLID_STEEL_MACHINE_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { SOLID_STEEL_MACHINE_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0)) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.common.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips5"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips6"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips7"))
            .addInfo(translateToLocalFormatted("mte.common.tooltips1"))
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(7, 5, 8, false)
            .addController("正面底部中央")
            .addCasingInfoExactly("脱氧钢机械方块",66,false)
            .addCasingInfoExactly("不锈钢框架",30,false)
            .addCasingInfoExactly("铁栏杆",14,false)
            .addCasingInfoExactly("任意线圈方块",16,true)
            .addCasingInfoExactly("洁净不锈钢机械方块",30,false)
            .addCasingInfoExactly("钢管道方块",11,false)
            .addCasingInfoExactly("防爆玻璃",24,false)
            .addCasingInfoExactly("镀层防爆石",8,true)
            .addInputHatch("任意脱氧钢机械方块")
            .addDynamoHatch("任意脱氧钢机械方块")
            .addMufflerHatch("任意脱氧钢机械方块")
            .toolTipFinisher("§d§l§oThinkTech");

        return tt;
    }

//    @Override
//    public int getPollutionPerSecond(ItemStack aStack) {
//        return 0721;
//    }

    @Override
    public int getDamageToComponent(ItemStack arg0) {
        return 0;
    }

    // 检查逻辑
    @Override
    public CheckRecipeResult checkProcessing() {
        setEnergyUsage(processingLogic);
        return super.checkProcessing();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Nonnull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                int power = recipe.getMetadataOrDefault(LNG_BASIC_OUTPUT, 0);
                lEUt = power;
                if ((mBlockTier >= 1 && mBlockTier <= 5) && power <= IGFuelVoltage[mBlockTier - 1]) {
                    return CheckRecipeResultRegistry.GENERATING;
                } else {
                    return CheckRecipeResultRegistry.NO_FUEL_FOUND;
                }
            }
        };
    }

    @Override
    protected void setEnergyUsage(ProcessingLogic processingLogic) {
        lEUt = (long) (lEUt * (1 + 0.25 * (mCoilLevel.getTier() - 1)));
    }

    @Override
    public boolean addEnergyOutput(long aEU) {
        displayGenerating = aEU;
        if (aEU <= 0) {
            return true;
        }
        if (!this.mAllDynamoHatches.isEmpty()) {
            return addEnergyOutputMultipleDynamos(aEU, true);
        }
        return false;
    }

    @Override
    public boolean addEnergyOutputMultipleDynamos(long aEU, boolean aAllowMixedVoltageDynamos) {
        int injected = 0;
        long aFirstVoltageFound = -1;
        for (MTEHatch aDynamo : validMTEList(mAllDynamoHatches)) {
            long aVoltage = aDynamo.maxEUOutput();
            // Check against voltage to check when hatch mixing
            if (aFirstVoltageFound == -1) {
                aFirstVoltageFound = aVoltage;
            }
        }

        long leftToInject;
        long aVoltage;
        int aAmpsToInject;
        int aRemainder;
        int ampsOnCurrentHatch;
        for (MTEHatch aDynamo : validMTEList(mAllDynamoHatches)) {
            leftToInject = aEU - injected;
            aVoltage = aDynamo.maxEUOutput();
            aAmpsToInject = (int) (leftToInject / aVoltage);
            aRemainder = (int) (leftToInject - (aAmpsToInject * aVoltage));
            ampsOnCurrentHatch = (int) Math.min(aDynamo.maxAmperesOut(), aAmpsToInject);

            // add full amps
            aDynamo.getBaseMetaTileEntity()
                .increaseStoredEnergyUnits(aVoltage * ampsOnCurrentHatch, false);
            injected += aVoltage * ampsOnCurrentHatch;

            // add reminder
            if (aRemainder > 0 && ampsOnCurrentHatch < aDynamo.maxAmperesOut()) {
                aDynamo.getBaseMetaTileEntity()
                    .increaseStoredEnergyUnits(aRemainder, false);
                injected += aRemainder;
            }
        }
        return injected > 0;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) {
            fixAllIssues();
            return true;
        }
        return false;
    }

    @Override
    protected void drawTexts(DynamicPositionedColumn screenElements, SlotWidget inventorySlot) {
        super.drawTexts(screenElements, inventorySlot);
        screenElements
            .widget(
                new TextWidget().setStringSupplier(() -> "Last EU generating(eu/tick):" + numberFormat.format(displayGenerating))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setEnabled(widget -> getErrorDisplayID() == 0)
            )
            .widget(new FakeSyncWidget.LongSyncer(() ->displayGenerating,val -> displayGenerating = val));
    }
}
