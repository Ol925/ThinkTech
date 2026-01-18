package com.OL925.ThinkTech.common.MTE;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.*;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.minecraft.BlockPos;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.config.ASMConfiguration;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.nbthandlers.MTEHatchCatalysts;
import gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.chemplant.MTEChemicalPlant;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static com.OL925.ThinkTech.Recipe.ThTRecipeMap.GeneralChemicalFactory;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTUtility.validMTEList;
import static net.minecraft.init.Blocks.*;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class ThT_GeneralChemicalFactory extends MTEExtendedPowerMultiBlockBase<ThT_GeneralChemicalFactory>
        implements ISurvivalConstructable, ISecondaryDescribable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private HeatingCoilLevel mCoilLevel = HeatingCoilLevel.None;
    private double mSpeedBonus;
    private final ArrayList<MTEHatchCatalysts> mCatalystHatches = new ArrayList<>();

    private static ITexture SOLID_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings2, 0));
    private static IStructureDefinition<ThT_GeneralChemicalFactory> STRUCTURE_DEFINITION = null;


    private static final int MACHINEMODE_MTECR = 0;
    private static final int MACHINEMODE_GCF = 1;
    private static final int MACHINEMODE_GTPP = 2;
    private static String[][] Shape = new String[][]{{
        "                    ",
        "                    ",
        "                    ",
        "   HHHHH            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   HHHHH            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            "
    },{
        "                    ",
        "                    ",
        "                    ",
        "   HBBBH            ",
        "    BBB             ",
        "    GGG             ",
        "    GGG             ",
        "    GGG             ",
        "    BBB             ",
        "   HBBBH    HHHHHHH ",
        "            H     H ",
        "            H     H ",
        "            H     H "
    },{
        "                    ",
        "    BBB             ",
        "    BBB             ",
        "  HB I BH           ",
        "   B   B            ",
        "   G   G            ",
        "   G C G            ",
        "   GDDDG            ",
        "   B   B     BBBBB  ",
        "  HB   BH   HB   BH ",
        "    BBB      B   B  ",
        "    B~B      B   B  ",
        "    BBB      BBBBB  "
    },{
        "                    ",
        "   B   B            ",
        "   BGGGB            ",
        "HHB  I  BHH         ",
        "H B     B H         ",
        "H G     G H         ",
        "H G  C  G H         ",
        "H GD   DG H  BBBBB  ",
        "H B FFF B H BBBBBBB ",
        "HHB     BHHHBGGGGGBH",
        "H  BFFFB  H BGAAAGB ",
        "H  B C B  H BGGGGGB ",
        "H  BEEEB  H BEEEEEB "
    },{
        "                    ",
        "  B     B           ",
        "  BGGGGGB           ",
        "HB   I   BH         ",
        " B   I   B          ",
        " G   I   G          ",
        " G   C   G          ",
        " GD  I  DG   BAAAB  ",
        " B F I F B  BBJJJBB ",
        "HB       BH  G   G H",
        "  BF I FB    G   G  ",
        "  B  C  B    GKKKG  ",
        "  BEEEEEB   BEEEEEB "
    },{
        "     CCCCCCC        ",
        "  B  C  B  C        ",
        "  BGGCGGB  C        ",
        "HBIIICIIIBHC        ",
        " B  ICI  B C        ",
        " G  ICI  G C        ",
        " GCCCCCCCG C        ",
        " GD ICI DG C BAAAB  ",
        " B FICIF B CBBJJJBB ",
        "HB   C   BHCCD   G H",
        "  BFICIFB    G   G  ",
        "  BCCCCCB    GKKKG  ",
        "  BEEEEEB   BEEEEEB "
    },{
        "                    ",
        "  B     B           ",
        "  BGGGGGB           ",
        "HB   I   BH         ",
        " B   I   B          ",
        " G   I   G          ",
        " G   C   G          ",
        " GD  I  DG   BBBBB  ",
        " B F I F B  BBBBBBB ",
        "HB       BH BGGGGGBH",
        "  BF I FB   BGGGGGB ",
        "  B  C  B   BGGGGGB ",
        "  BEEEEEB   BEEEEEB "
    },{
        "                    ",
        "   B   B            ",
        "   BGGGB            ",
        "HHB  I  BHH         ",
        "H B     B H         ",
        "H G     G H         ",
        "H G  C  G H         ",
        "H GD   DG H         ",
        "H B FFF B H  BBBBB  ",
        "HHB     BHHHHB   BH ",
        "H  BFFFB  H  B   B  ",
        "H  B C B  H  B   B  ",
        "H  BEEEB  H  BBBBB  "
    },{
        "                    ",
        "    BBB             ",
        "    BBB             ",
        "  HB I BH           ",
        "   B   B            ",
        "   G   G            ",
        "   G C G            ",
        "   GDDDG            ",
        "   B   B            ",
        "  HB   BH   HHHHHHH ",
        "    BBB     H     H ",
        "    BBB     H     H ",
        "    BBB     H     H "
    },{
        "                    ",
        "                    ",
        "                    ",
        "   HBBBH            ",
        "    BBB             ",
        "    GGG             ",
        "    GGG             ",
        "    GGG             ",
        "    BBB             ",
        "   HBBBH            ",
        "                    ",
        "                    ",
        "                    "
    },{
        "                    ",
        "                    ",
        "                    ",
        "   HHHHH            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            ",
        "   HHHHH            ",
        "   H   H            ",
        "   H   H            ",
        "   H   H            "
    }};

    protected ThT_GeneralChemicalFactory(String aName) {
        super(aName);
    }

    public ThT_GeneralChemicalFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IStructureDefinition<ThT_GeneralChemicalFactory> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_GeneralChemicalFactory>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, Shape)
                    .addElement('A',ofBlockUnlocalizedName("IC2", "blockAlloyGlass", 0, true))
                    .addElement('B',
                            ofChain(buildHatchAdder(ThT_GeneralChemicalFactory.class)
                                    .atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputHatch, OutputBus)
                                    .casingIndex(Textures.BlockIcons.getTextureIndex(SOLID_STEEL_MACHINE_CASING))
                                    .dot(1)
                                    .buildAndChain(GregTechAPI.sBlockCasings2, 0),
                                buildHatchAdder(ThT_GeneralChemicalFactory.class).hatchClass(MTEHatchCatalysts.class)
                                    .shouldReject(t -> !t.mCatalystHatches.isEmpty())
                                    .casingIndex(Textures.BlockIcons.getTextureIndex(SOLID_STEEL_MACHINE_CASING))
                                    .adder(ThT_GeneralChemicalFactory::addChemicalPlantList)
                                    .dot(2)
                                    .buildAndChain(GregTechAPI.sBlockCasings2, 0)))
                    .addElement('C',ofBlock(GregTechAPI.sBlockCasings2, 13))
                    .addElement('D',ofBlock(GregTechAPI.sBlockCasings3, 10))
                    .addElement('E',ofBlock(GregTechAPI.sBlockCasings4, 1))
                    .addElement('F', GTStructureUtility
                            .ofCoil(ThT_GeneralChemicalFactory::setCoilLevel, ThT_GeneralChemicalFactory::getCoilLevel))
                    .addElement('G',ofBlock(GregTechAPI.sBlockCasings8, 0))
                    .addElement('H',ofFrame(Materials.Steel))
                    .addElement('I',ofFrame(Materials.StainlessSteel))
                    .addElement('J',ofBlock(GregTechAPI.sBlockMetal6,13))
                    .addElement('K',ofBlockAnyMeta(piston))
                    .build();
        }
        return STRUCTURE_DEFINITION;
    }


    public boolean addChemicalPlantList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof MTEHatchCatalysts hatchCatalysts) {
            hatchCatalysts.updateTexture(aBaseCasingIndex);
            return mCatalystHatches.add(hatchCatalysts);
        }
        return false;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mCatalystHatches.clear();
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        for (MTEHatchCatalysts h : validMTEList(mCatalystHatches)) {
            h.updateSlots();
            h.tryFillUsageSlots();
        }
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);

        return addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_STEAM_WASHER_LOOP;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        if(machineMode == MACHINEMODE_MTECR){
            return RecipeMaps.multiblockChemicalReactorRecipes;
        }else if(machineMode==MACHINEMODE_GTPP){
            return GTPPRecipeMaps.chemicalPlantRecipes;
        }
        return GeneralChemicalFactory;
    }

    @NotNull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(GeneralChemicalFactory,RecipeMaps.multiblockChemicalReactorRecipes,GTPPRecipeMaps.chemicalPlantRecipes);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_MTECR) {
            return MACHINEMODE_GTPP;
        } else if (machineMode == MACHINEMODE_GTPP) {
            return MACHINEMODE_GCF;
        } else {
            return MACHINEMODE_MTECR;
        }
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
    public void setMachineModeIcons() {
        machineModeIcons.clear();
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_CHEMBATH);
    }

    @Override
    public String getMachineModeName() {
        return StatCollector.translateToLocal("mte.SAF.common.mode." + machineMode);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            ItemStack catalyst;

            //no need to catalysts and check the tier of recipes.
            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                RecipeMap<?> recipeMap = getCurrentRecipeMap();
                if (recipeMap == GTPPRecipeMaps.chemicalPlantRecipes){
                    ItemStack catalystInRecipe = null;
                    for (ItemStack item : recipe.mInputs) {
                        if (MTEChemicalPlant.isCatalyst(item)) {
                            catalystInRecipe = item;
                            break;
                        }
                    }
                    if (catalystInRecipe != null) {
                        catalyst = findCatalyst(getCatalystInputs().toArray(new ItemStack[0]), catalystInRecipe);
                        if (catalyst == null) {
                            return SimpleCheckRecipeResult.ofFailure("no_catalyst");
                        }
                    } else {
                        // remove reference to the old catalyst if our new recipe doesn't use it
                        catalyst = null;
                    }
                    return CheckRecipeResultRegistry.SUCCESSFUL;
                }
                return super.validateRecipe(recipe);
            }

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                if (getCurrentRecipeMap() == GTPPRecipeMaps.chemicalPlantRecipes) {
                    ItemStack[] baseInputs = (inputItems == null) ? new ItemStack[0] : inputItems;
                    ArrayList<ItemStack> inputItemsList = new ArrayList<>(Arrays.asList(baseInputs));
                    inputItemsList.addAll(getCatalystInputs());
                    inputItems = inputItemsList.toArray(new ItemStack[0]);
                }
                return super.process();
            }

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@Nonnull GTRecipe recipe) {
                if (getCurrentRecipeMap() == GTPPRecipeMaps.chemicalPlantRecipes){
                    if (!GTUtility.isStackValid(catalyst)){
                        catalyst = null;
                    }
                }
                return super.onRecipeStart(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    private ItemStack findCatalyst(ItemStack[] aItemInputs, ItemStack catalyst) {
        if (aItemInputs != null) {
            for (ItemStack item : aItemInputs) {
                if (GTUtility.areStacksEqual(item, catalyst, true)) {
                    return item;
                }
            }
        }
        return null;
    }

    public ArrayList<ItemStack> getCatalystInputs() {
        ArrayList<ItemStack> tItems = new ArrayList<>();
        for (MTEHatchCatalysts tHatch : validMTEList(mCatalystHatches)) {
            ArrayList<ItemStack> aHatchContent = tHatch.getContentUsageSlots();
            if (!aHatchContent.isEmpty()) {
                tItems.addAll(aHatchContent);
            }
        }
        return tItems;
    }

    @Override
    public int getMaxParallelRecipes() {
        return 8 * mCoilLevel.getTier();
    }

    protected float getSpeedBonus() {
        mSpeedBonus = 0.0;
        countSpeedBonus();
        return (float) mSpeedBonus;
    }

    private void countSpeedBonus(){
        double mSB = 1.0 - ((mCoilLevel.getTier() - 1) * 0.1);
        if (mSB <=0){
            mSpeedBonus = 0.01;
        }else mSpeedBonus = mSB;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 5, 11, 2);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 5, 11, 2, elementBudget, env, false, true);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.common.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips5"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips6"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips7"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips8"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips9"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips10"))
            .addInfo(translateToLocalFormatted("mte.SAF.tooltips11"))
            .beginStructureBlock(11, 13, 20, false)
            .addController("见结构预览")
            .addCasingInfoExactly("洁净不锈钢机械方块",41,false)
            .addCasingInfoExactly("钢管道方块",48,false)
            .addCasingInfoExactly("脱氧钢机械方块",242,false)
            .addCasingInfoExactly("格栅机械方块",17,false)
            .addCasingInfoExactly("任意线圈方块",24,true)
            .addCasingInfoExactly("不锈钢框架",32,false)
            .addCasingInfoExactly("化学惰性机械方块",118,false)
            .addCasingInfoExactly("活塞",6,false)
            .addCasingInfoExactly("钢块",6,false)
            .addCasingInfoExactly("防爆玻璃",9,false)
            .addCasingInfoExactly("钢框架",164,false)
            .addInputBus("任意脱氧钢机械方块")
            .addInputHatch("任意脱氧钢机械方块")
            .addOutputBus("任意脱氧钢机械方块")
            .addOutputHatch("任意脱氧钢机械方块")
            .addEnergyHatch("任意脱氧钢机械方块")
            .toolTipFinisher("§d§l§oThinkTech");

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 5, 11, 2)) {
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

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        mCoilLevel = aCoilLevel;
    }

    public HeatingCoilLevel getCoilLevel() {
        return mCoilLevel;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ThT_GeneralChemicalFactory(this.mName);
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
