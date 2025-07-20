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
import com.OL925.ThinkTech.common.init.ThTList;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTRecipe;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
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

    private double mSpeedBonus;
    private int mMachineTier = 0;
    private boolean mUpgraded = false;
    private static IStructureDefinition<ThT_CzochralskiSingleCrystalFurnace> STRUCTURE_DEFINITION = null;
    private static ITexture STAINLESS_STEEL_MACHINE_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 1));


    public ThT_CzochralskiSingleCrystalFurnace(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public ThT_CzochralskiSingleCrystalFurnace(String aName) {
        super(aName);
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
                .addElement('D',buildHatchAdder(ThT_CzochralskiSingleCrystalFurnace.class).atLeast(Energy.or(ExoticEnergy), InputHatch, InputBus, OutputBus, OutputHatch,Muffler)
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

    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            public CheckRecipeResult process() {
                setSpeedBonus(getSpeedBonus());
                setOverclock(isEnablePerfectOverclock() ? 4 : 2, 4);
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
        return 1 + 4 * mMachineTier;
    };

    protected float getSpeedBonus() {
        mSpeedBonus = 0.0;
        countSpeedBonus();
        return (float) mSpeedBonus;
    }

    private void countSpeedBonus(){
        if(this.mUpgraded){
            mSpeedBonus = 1.0 - mMachineTier * 0.2;
        }else mSpeedBonus = 1.0;
    }

    protected boolean isEnablePerfectOverclock() {
        return mSpeedBonus == 0.6;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aTick % 20 == 0 && !mUpgraded) {
            ItemStack aGuiStack = this.getControllerSlot();
            if (aGuiStack != null) {
                if (GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER1.get(1))) {
                    this.mUpgraded = true;
                    this.mMachineTier = 1;
                }else if(GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER2.get(1))){
                    this.mUpgraded = true;
                    this.mMachineTier = 2;
                }else if(GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER3.get(1))){
                    this.mUpgraded = true;
                    this.mMachineTier = 3;
                }else if(GTUtility.areStacksEqual(aGuiStack, ThTList.CHIPTIER4.get(1))){
                    this.mUpgraded = true;
                    this.mMachineTier = 4;
                }
            }
        }
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
    //STAINLESS_STEEL_MACHINE_CASING  OVERLAY_FRONT_OIL_CRACKER_ACTIVE  OVERLAY_FRONT_OIL_CRACKER_GLOW
    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
                                 boolean active, boolean redstone) {

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
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips8"))
            .addInfo(translateToLocalFormatted("mte.CSCF.tooltips9"))
            .addInfo(translateToLocalFormatted("mte.common.tooltips1"))
            .beginStructureBlock(6, 8, 7, false)
            .addController("正面第二层中央")
            .addCasingInfoExactly("洁净不锈钢机械方块",57,false)
            .addCasingInfoExactly("格栅机械方块",9,false)
            .addCasingInfoExactly("钢框架",51,false)
            .addCasingInfoExactly("脱氧钢机械方块",10,false)
            .addCasingInfoExactly("不锈钢框架",4,false)
            .addCasingInfoExactly("防爆玻璃",8,false)
            .addCasingInfoExactly("聚四氟乙烯管道方块",3,false)
            .addCasingInfoExactly("漏斗",1,false)
            .addCasingInfoExactly("石台阶",1,false)
            .addMufflerHatch("任意洁净不锈钢机械方块")
            .addInputBus("任意洁净不锈钢机械方块")
            .addInputHatch("任意洁净不锈钢机械方块")
            .addOutputHatch("任意洁净不锈钢机械方块")
            .addOutputBus("任意洁净不锈钢机械方块")
            .addEnergyHatch("任意洁净不锈钢机械方块")
            .toolTipFinisher("§d§l§oThinkTech");

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

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getProcessStartSound() {
        if (mMufflerHatches.isEmpty()){
            return SoundResource.GT_MACHINES_EBF_LOOP;
        }
        return null;
    }

    @Override
    protected int getTimeBetweenProcessSounds() {
        return 20;
    }
}
