package com.OL925.ThinkTech.common.MTE;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
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
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class ThT_NobleGasEnrichmentSystem extends MTEExtendedPowerMultiBlockBase<ThT_NobleGasEnrichmentSystem>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private static ITexture CASING = TextureFactory.of(BLOCK_PLASCRETE);

    private int MTier = 0;
    private int controllerTier = 0;

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
                .addElement('A',ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true))
                .addElement('B',ofBlock(GregTechAPI.sBlockCasings2, 0))
                .addElement('C',ofBlock(GregTechAPI.sBlockCasings3, 10))
                .addElement('D',ofBlock(GregTechAPI.sBlockCasings4, 1))
                .addElement('E',ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('F',ofFrame(Materials.Steel))
                .addElement('G',buildHatchAdder(ThT_NobleGasEnrichmentSystem.class).atLeast(Energy.or(ExoticEnergy),InputBus, OutputHatch)
                    .casingIndex(Textures.BlockIcons.getTextureIndex(CASING))
                    .dot(1)
                    .buildAndChain(GregTechAPI.sBlockReinforced,2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }//结构还没写完

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private int getMaxParallelRecipes() {
        if(MTier == 0){
            switch (controllerTier){
                case 1:
                    return 16;
                case 2:
                    return 24;
                case 3:
                    return 32;
            }
        }
        return 4;//固定提供4并行
    }

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
            .addInfo(translateToLocalFormatted("mte.NGES.tt3"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt4"))
            .addInfo(translateToLocalFormatted("mte.NGES.tt5"))
            .toolTipFinisher("§d§l§oThinkTech");

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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 1)) {
            fixAllIssues();
            return true;
        }
        return false;
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
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ThT_NobleGasEnrichmentSystem(this.mName);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 1, elementBudget, env, false, true);
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
                .build(),};
        }
        return new ITexture[] {TextureFactory.of(BLOCK_PLASCRETE)};
    }
}
