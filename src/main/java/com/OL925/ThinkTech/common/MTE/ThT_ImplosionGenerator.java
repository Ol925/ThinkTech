package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.init.Blocks.iron_bars;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.dreammaster.block.BlockList;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtnhlanth.api.recipe.LanthanidesRecipeMaps;
import gtnhlanth.util.DescTextLocalization;

public class ThT_ImplosionGenerator extends GTPPMultiBlockBase<ThT_ImplosionGenerator>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private int GeneratorTier = 0;
    private static IStructureDefinition<ThT_ImplosionGenerator> STRUCTURE_DEFINITION = null;
    private static ITexture SOLID_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));

    // Structure
    @Override
    public IStructureDefinition<ThT_ImplosionGenerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_ImplosionGenerator>builder()
                .addShape(
                    mName,

                    new String[][] { { "   B   ", "  BBB  ", "  BBB  ", "BHBBBHB", "BBB~BBB" },
                        { "  FFF  ", " FBBBF ", " FBCBF ", "HFBBBFH", "BDDDDDB" },
                        { "  AFA  ", " AEEEA ", " AECEA ", "HFEEEFH", "BDDDDDB" },
                        { "  AFA  ", " A G A ", " AG GA ", "HF G FH", "BDDDDDB" },
                        { "  AFA  ", " A G A ", " AG GA ", "HF G FH", "BDDDDDB" },
                        { "  AFA  ", " AEEEA ", " AECEA ", "HFEEEFH", "BDDDDDB" },
                        { "  FFF  ", " FBBBF ", " FBCBF ", "HFBBBFH", "BDDDDDB" },
                        { "  BBB  ", " BBCBB ", " BCCCB ", "BBCCCBB", "BBBBBBB" } })
                .addElement('A', ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true))
                //
                .addElement(
                    'B',
                    buildHatchAdder(ThT_ImplosionGenerator.class).atLeast(Dynamo, InputHatch, Muffler)
                        .casingIndex(Textures.BlockIcons.getTextureIndex(SOLID_STEEL_MACHINE_CASING))
                        .dot(1)
                        .buildAndChain(GregTechAPI.sBlockCasings2, 0))
                .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 13))
                .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('E', ofBlock(GregTechAPI.sBlockCasings5, 0))
                .addElement('F', ofFrame(Materials.StainlessSteel))
                .addElement(
                    'G',
                    withChannel(
                        "test",
                        ofBlocksTiered(
                            ThT_ImplosionGenerator::getGeneratorTier,
                            ImmutableList.of(
                                Pair.of(BlockList.BronzePlatedReinforcedStone.getBlock(), 0), // 硝化淀粉
                                Pair.of(BlockList.SteelPlatedReinforcedStone.getBlock(), 0), // 硝化甘油
                                Pair.of(BlockList.TitaniumPlatedReinforcedStone.getBlock(), 0), // 三硝基甲苯
                                Pair.of(BlockList.TungstensteelPlatedReinforcedStone.getBlock(), 0), // 黑索金
                                Pair.of(BlockList.NaquadahPlatedReinforcedStone.getBlock(), 0)// CL-20
                            ),
                            -1,
                            (m, t) -> m.GeneratorTier = t,
                            m -> m.GeneratorTier)))
                .addElement('H', ofBlockAnyMeta(iron_bars))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    public ThT_ImplosionGenerator(String name) {
        super(name);
    }

    //
    @Override
    public String getMachineType() {
        return "";
    }

    // 控制机器的等级
    public static int getGeneratorTier(Block block, int meta) {
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

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    public ThT_ImplosionGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return LanthanidesRecipeMaps.dissolutionTankRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                if (!checkRatio(recipe, Arrays.asList(inputFluids))) {
                    stopMachine(ShutDownReasonRegistry.CRITICAL_NONE);
                    return SimpleCheckRecipeResult.ofFailurePersistOnShutdown("dissolution_ratio");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

        };
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

    private boolean checkRatio(GTRecipe tRecipe, List<FluidStack> tFluidInputs) {
        FluidStack majorGenericFluid = tRecipe.mFluidInputs[0];
        FluidStack minorGenericFluid = tRecipe.mFluidInputs[1];

        int majorAmount;
        int minorAmount;

        FluidStack fluidInputOne = tFluidInputs.get(0);
        FluidStack fluidInputTwo = tFluidInputs.get(1);

        if (fluidInputOne.getUnlocalizedName()
            .equals(majorGenericFluid.getUnlocalizedName())) {
            if (fluidInputTwo.getUnlocalizedName()
                .equals(minorGenericFluid.getUnlocalizedName())) {
                // majorInput = fluidInputOne;
                majorAmount = fluidInputOne.amount;
                // minorInput = fluidInputTwo;
                minorAmount = fluidInputTwo.amount;
                // GTLog.out.print("in first IF");
            } else return false; // No valid other input

        } else if (fluidInputTwo.getUnlocalizedName()
            .equals(majorGenericFluid.getUnlocalizedName())) {
                if (fluidInputOne.getUnlocalizedName()
                    .equals(minorGenericFluid.getUnlocalizedName())) {
                    // majorInput = fluidInputTwo;
                    majorAmount = fluidInputTwo.amount;
                    // minorInput = fluidInputOne;
                    minorAmount = fluidInputOne.amount;
                    // GTLog.out.print("in second if");
                } else return false;

            } else return false;

        return majorAmount / tRecipe.mSpecialValue == minorAmount;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_ImplosionGenerator(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(mName, itemStack, b, 3, 4, 0);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(mName, 3, 4, 0) && mMufflerHatches.size() == 1) {
            fixAllIssues();
            return true;
        }
        return false;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("DissolutionTank.hint", 4);
    }

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
        tt.addMachineType(translateToLocalFormatted("mte.ImplosionGenerator"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.ImplosionGenerator.tooltips3"))
            .beginStructureBlock(7, 5, 8, true)
            .addController("Front bottom")
            .addInputHatch("Hint block with dot 1")
            .addDynamoHatch("Hint block with dot 1")
            .addMufflerHatch("Hint block with dot 1")
            .toolTipFinisher();

        return tt;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack arg0) {
        return false;
    }

    @Override
    public int getDamageToComponent(ItemStack arg0) {
        return 0;
    }

}
