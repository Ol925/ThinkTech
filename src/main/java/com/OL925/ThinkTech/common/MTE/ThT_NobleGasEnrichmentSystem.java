package com.OL925.ThinkTech.common.MTE;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.OL925.ThinkTech.common.block.ControllerTier1;
import com.OL925.ThinkTech.common.init.ThTBlockLoader;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
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
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import net.minecraft.block.Block;
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
    private static ITexture STAINLESS_STEEL_MACHINE_CASING = getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1));

    private int MTier = 0;
    private int controllerTier = 0;
    private Block CT1 = new ControllerTier1();

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
                .addElement('C',
                    ofBlock(GregTechAPI.sBlockCasings3, 10))//
                .addElement('D',buildHatchAdder(ThT_NobleGasEnrichmentSystem.class).atLeast(Energy.or(ExoticEnergy), InputBus, OutputHatch)
                    .casingIndex(getTextureIndex(STAINLESS_STEEL_MACHINE_CASING))
                    .dot(1)
                    .buildAndChain(GregTechAPI.sBlockCasings4, 1))
                .addElement('E',ofBlock(GregTechAPI.sBlockCasings8, 1))
                .addElement('F',ofFrame(Materials.Steel))
                .addElement('G',ofChain(
                    ofBlock(GregTechAPI.sBlockReinforced,2),
                    onElementPass(ThT_NobleGasEnrichmentSystem::setT1,ofBlock(ThTBlockLoader.controllerTier1,0)),
                    onElementPass(ThT_NobleGasEnrichmentSystem::setT2,ofBlock(ThTBlockLoader.controllerTier2,0)),
                    onElementPass(ThT_NobleGasEnrichmentSystem::setT3,ofBlock(ThTBlockLoader.controllerTier3,0))
                ))
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

    @Override
    public int getMaxParallelRecipes() {
        if(MTier == 0){
            switch (controllerTier){
                case 1:
                    return 16;
                case 2:
                    return 32;
                case 3:
                    return 64;
            }
        }
        return 1;
    }

    private void setT1() {controllerTier = 1;}

    private void setT2() {controllerTier = 2;}

    private void setT3() {controllerTier = 3;}

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
            .addDynamoHatch("任意洁净不锈钢机械方块",1)
            .addOutputHatch("输出稀有气体,任意洁净不锈钢机械方块",1)
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
    public boolean supportsVoidProtection(){
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
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
        return new ITexture[] {TextureFactory.of(BLOCK_PLASCRETE)};
    }
}
