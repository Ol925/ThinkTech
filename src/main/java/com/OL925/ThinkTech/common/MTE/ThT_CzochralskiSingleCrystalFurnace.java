package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.init.Blocks.hopper;
import static net.minecraft.init.Blocks.stone_slab;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTRecipe;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

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
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

public class ThT_CzochralskiSingleCrystalFurnace extends MTEExtendedPowerMultiBlockBase<ThT_CzochralskiSingleCrystalFurnace>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private static IStructureDefinition<ThT_CzochralskiSingleCrystalFurnace> STRUCTURE_DEFINITION = null;
    private int mMaxParallel = 4 * GTUtility.getTier(this.getMaxInputVoltage());
    private static ITexture STAINLESS_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1));


    public ThT_CzochralskiSingleCrystalFurnace(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static String[][] Shape = new String[][]{
        {"       ","       ","  DDD  ","  DAD  ","  DAD  ","  DDD  ","  D~D  ","  DDD  "},
        {"  F F  "," FDDDF ","FD   DF","FD   DF","FD   DF","FD   DF","FC G CF","FDBBBDF"},
        {"  F F  ","  DDD  ","FDEEEDF"," A J A "," A   A "," D H D "," CGBGC "," DBBBD "},
        {"  F F  "," FDDDF ","FD   DF","FD   DF","FD   DF","FD   DF","FC G CF","FDBBBDF"},
        {"       ","  F F  ","  DDD  ","  DAD  ","  DAD  ","  DDD  ","  CCC  ","  DDD  "},
        {"       ","       ","  FFF  ","  F F  ","  F F  ","  F F  ","  F F  ","  F F  "}};

    @Override
    public IStructureDefinition<ThT_CzochralskiSingleCrystalFurnace> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_CzochralskiSingleCrystalFurnace>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A',ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true))
                .addElement('B',ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement('C',ofBlock(GregTechAPI.sBlockCasings3, 10))
                .addElement('D',buildHatchAdder(ThT_CzochralskiSingleCrystalFurnace.class).atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputBus, OutputHatch)
                    .casingIndex(Textures.BlockIcons.getTextureIndex(STAINLESS_STEEL_MACHINE_CASING))
                    .dot(1)
                    .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement('E',ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('F',ofFrame(Materials.Steel))
                .addElement('G',ofFrame(Materials.StainlessSteel))
                .addElement('H',ofBlockAnyMeta(stone_slab))
                .addElement('J',ofBlockAnyMeta(hopper))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.CzochralskiSingleCrystalFurnace;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {
            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                maxParallel = mMaxParallel;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        };
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack itemStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public ThT_CzochralskiSingleCrystalFurnace(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_CzochralskiSingleCrystalFurnace(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 6, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 0, elementBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstone) {

        if (side == facing) {
            if (active) return new ITexture[] { STAINLESS_STEEL_MACHINE_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { STAINLESS_STEEL_MACHINE_CASING, TextureFactory.builder()
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
            Textures.BlockIcons.getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1)) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.CSCFType"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips5"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips6"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips7"))
            .toolTipFinisher("§d§l§oOL925 & _fantasque_");

        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 0)) {
            fixAllIssues();
            return true;
        }
        return false;
    }


}
