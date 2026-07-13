package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ThT_TitaniumCrucible extends ThT_CrucibleBase<ThT_TitaniumCrucible>
    implements ISurvivalConstructable, ISecondaryDescribable {

    private static IStructureDefinition<ThT_TitaniumCrucible> STRUCTURE_DEFINITION = null;
    private static final ITexture TITANIUM_CASING = Textures.BlockIcons
        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings4, 2));

    public ThT_TitaniumCrucible(int id, String name, String nameRegional) { super(id, name, nameRegional); }
    public ThT_TitaniumCrucible(String aName) { super(aName); }

    @Override public int getCrucibleTier() { return 4; }
    @Override public long getHeatCapacity() { return 160_000_000L; }
    @Override public int getMaxParallelRecipes() { return 32; }
    @Override protected float getSpeedBonus() { return 0.75f; }
    @Override protected double getHeatBoostThresholdRatio() { return 0.65; }
    @Override protected int getFuelBurnInterval() { return 10; }

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String[][] Shape = new String[][]{
        {"AAAA", "AAA~", "AAAA"},
        {"A  A", "A  A", "AAAA"},
        {"A  A", "A  A", "AAAA"},
        {"AAAA", "AAAA", "AAAA"}
    };

    @Override
    public IStructureDefinition<ThT_TitaniumCrucible> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<ThT_TitaniumCrucible>builder()
                .addShape(STRUCTURE_PIECE_MAIN, Shape)
                .addElement('A',
                    buildHatchAdder(ThT_TitaniumCrucible.class)
                        .atLeast(InputBus, OutputBus, InputHatch, OutputHatch)
                        .casingIndex(Textures.BlockIcons.getTextureIndex(TITANIUM_CASING))
                        .hint(1)
                        .buildAndChain(GregTechAPI.sBlockCasings4, 2))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 1, 0, errors)) return;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        buildPiece(STRUCTURE_PIECE_MAIN, itemStack, b, 3, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity te, ForgeDirection side, ForgeDirection facing, int colorIndex,
                                 boolean active, boolean redstone) {
        if (side == facing) {
            if (active) return new ITexture[] { TITANIUM_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW).extFacing().glow().build() };
            return new ITexture[] { TITANIUM_CASING, TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER).extFacing().build(),
                TextureFactory.builder().addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW).extFacing().glow().build() };
        }
        return new ITexture[] { TITANIUM_CASING };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocalFormatted("mte.crucibleType"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips1"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips2"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips3"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips4"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips5"))
            .addInfo(translateToLocalFormatted("mte.titaniumCrucible.tooltips6"))
            .addInfo("添加者：§4§nOL925")
            .beginStructureBlock(4, 4, 3, false)
            .addController("正面底层右侧")
            .addCasing("40", "稳定钛机械方块", false)
            .addInputBus("1", "任意稳定钛机械方块")
            .addInputHatch("1", "任意稳定钛机械方块")
            .addOutputBus("1", "任意稳定钛机械方块")
            .addOutputHatch("1", "任意稳定钛机械方块")
            .toolTipFinisher("§d§l§oThinkTech");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity arg0) {
        return new ThT_TitaniumCrucible(this.mName);
    }
}
