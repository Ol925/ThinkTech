package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.Mods.IndustrialCraft2;
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

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.loaders.ItemRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
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

    private static ITexture SOLID_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));

    private final IStructureDefinition<ThT_ImplosionGenerator> multiDefinition = StructureDefinition
        .<ThT_ImplosionGenerator>builder()
        .addShape(
            mName,

            new String[][] { { "   B   ", "  BBB  ", "  BBB  ", "BGBBBGB", "BBB~BBB" },
                { "  FFF  ", " FBBBF ", " FBCBF ", "GFBBBFG", "BDDDDDB" },
                { "   F   ", "  EEE  ", "  ECE  ", "GFEEEFG", "BDDDDDB" },
                { "   F   ", "   A   ", "  A A  ", "GF A FG", "BDDDDDB" },
                { "   F   ", "   A   ", "  A A  ", "GF A FG", "BDDDDDB" },
                { "   F   ", "  EEE  ", "  ECE  ", "GFEEEFG", "BDDDDDB" },
                { "  FFF  ", " FBBBF ", " FBCBF ", "GFBBBFG", "BDDDDDB" },
                { "  BBB  ", " BBCBB ", " BCCCB ", "BBCCCBB", "BBBBBBB" } })
        .addElement('A', ofBlockAnyMeta(GameRegistry.findBlock(IndustrialCraft2.ID, "blockAlloy")))
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
        .addElement('G', ofBlockAnyMeta(iron_bars))
        .build();;

    public ThT_ImplosionGenerator(String name) {
        super(name);
    }

    @Override
    public String getMachineType() {
        return "";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 0;
    }

    public ThT_ImplosionGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IStructureDefinition<ThT_ImplosionGenerator> getStructureDefinition() {
        return multiDefinition;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    private boolean addGlass(Block block, int meta) {
        return block == ItemRegistry.bw_glasses[0];
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
        if(checkPiece(mName, 3, 4, 0)){
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
