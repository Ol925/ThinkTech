package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import static net.minecraft.init.Blocks.stone_slab;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;

import gregtech.api.enums.StructureError;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.MTESteamMultiBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;

import java.util.Collection;


public class ThT_Kiln extends MTESteamMultiBase<ThT_Kiln> implements ISurvivalConstructable {

    private static IStructureDefinition<ThT_Kiln> STRUCTURE_DEFINITION = null;
    private static ITexture BRICK = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 15));


    public ThT_Kiln(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    public ThT_Kiln(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Kiln";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static String[][] Shape = new String[][]{{
        "     ",
        "  A  ",
        " AAA ",
        " A~A ",
        " AAA "
    },{
        "  A  ",
        " A A ",
        "A   A",
        "AB BA",
        "AAAAA"
    },{
        "  A  ",
        " A A ",
        "A   A",
        "AB BA",
        "AAAAA"
    },{
        "  A  ",
        " A A ",
        "A   A",
        "AB BA",
        "AAAAA"
    },{
        "  A  ",
        " A A ",
        "A   A",
        "AB BA",
        "AAAAA"
    },{
        "     ",
        "  A  ",
        " AAA ",
        " AAA ",
        " AAA "
    }};

    @Override
    public IStructureDefinition<ThT_Kiln> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null){
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_Kiln>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A',
                    ofChain(
                        buildSteamInput(ThT_Kiln.class)
                            .casingIndex(Textures.BlockIcons.getTextureIndex(BRICK))
                            .dot(1)
                            .allowOnly(ForgeDirection.NORTH)
                            .build(),
                        buildHatchAdder(ThT_Kiln.class)
                            .atLeast(
                                SteamHatchElement.InputBus_Steam,
                                SteamHatchElement.OutputBus_Steam)
                            .casingIndex(Textures.BlockIcons.getTextureIndex(BRICK))
                            .dot(1)
                            .allowOnly(ForgeDirection.NORTH)
                            .buildAndChain(),
                        ofBlock(GregTechAPI.sBlockCasings4, 15))
                            )
                .addElement('B',ofBlockAnyMeta(stone_slab))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return ThTRecipeMap.Kiln;
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
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {}

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_EBF_LOOP;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_Kiln(this.mName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 2, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
                                 boolean active, boolean redstone) {

        if (side == facing) {
            if (active) return new ITexture[] { BRICK, TextureFactory.builder()
                .addIcon(MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(MACHINE_CASING_BRICKEDBLASTFURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build()};

            return new ITexture[] {BRICK, TextureFactory.builder()
                .addIcon(MACHINE_CASING_BRICKEDBLASTFURNACE_INACTIVE)
                .extFacing()
                .build()};
        }
        return new ITexture[] {BRICK};
    }

    @Override
    protected ITexture getFrontOverlay() {
        return null;
    }

    @Override
    protected ITexture getFrontOverlayActive() {
        return null;
    }

    @Override
    public int getTierRecipes() {
        return 1;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.common.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.kiln.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.kiln.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.kiln.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.Kiln.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.Kiln.tooltips5"))
            .beginStructureBlock(6, 5, 5, false)
            .addController("正面中央")
            .addCasingInfoExactly("耐火砖",64,false)
            .addCasingInfoExactly("石台阶",8,false)
            .addCasingInfoMin("输入总线(蒸汽)",1,false)
            .addCasingInfoMin("输出总线(蒸汽)",1,false)
            .toolTipFinisher("§d§l§oThinkTech");
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 2, 3, 0)) {
            fixAllIssues();
            return true;
        }
        return false;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

}
