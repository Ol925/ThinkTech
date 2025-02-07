package com.OL925.ThinkTech.common.MTE;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.init.Blocks.iron_bars;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public class eee extends GTPPMultiBlockBase<eee> {

    public eee(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public eee(String name) {
        super(name);
    }

    @Override
    public String getMachineType() {
        return "Explosive Generator | The art of Explosion";
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("mName", stackSize, hintsOnly, 3, 4, 0);
    }

    private final IStructureDefinition<eee> multiDefinition = StructureDefinition.<eee>builder()
        .addShape(
            mName,
            transpose(
                new String[][] { { "   B   ", "  BBB  ", "  BBB  ", "BGBBBGB", "BBB~BBB" },
                    { "  FFF  ", " FBBBF ", " FBCBF ", "GFBBBFG", "BDDDDDB" },
                    { "   F   ", "  EEE  ", "  ECE  ", "GFEEEFG", "BDDDDDB" },
                    { "   F   ", "   A   ", "  A A  ", "GF A FG", "BDDDDDB" },
                    { "   F   ", "   A   ", "  A A  ", "GF A FG", "BDDDDDB" },
                    { "   F   ", "  EEE  ", "  ECE  ", "GFEEEFG", "BDDDDDB" },
                    { "  FFF  ", " FBBBF ", " FBCBF ", "GFBBBFG", "BDDDDDB" },
                    { "  BBB  ", " BBCBB ", " BCCCB ", "BBCCCBB", "BBBBBBB" } }))
        .addElement('A', ofBlockAnyMeta(GameRegistry.findBlock(IndustrialCraft2.ID, "blockAlloy")))
        .addElement(
            'B',
            buildHatchAdder(eee.class).atLeast(Dynamo, InputHatch, Muffler)
                .casingIndex(49)
                .dot(1)
                .buildAndChain(GregTechAPI.sBlockCasings2, 0))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 13))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings5, 0))
        .addElement('F', ofFrame(Materials.StainlessSteel))
        .addElement('G', ofBlockAnyMeta(iron_bars))
        .build();

    @Override
    public IStructureDefinition<eee> getStructureDefinition() {
        return multiDefinition;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("聚爆发电机")
            .addInfo("§b爆炸向来都是艺术")
            .addInfo("§b现在我们要让这种艺术再次被发扬光大!")
            .addInfo("输入§b§n流体炸药§r§7以§4§n释放化学能§r§7")
            .beginStructureBlock(5, 5, 5, false)
            .addController("Front bottom")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return checkPiece(mName, 3, 4, 0);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ThT_ImplosionGenerator(this.mName);
    }

    @Nonnull
    @Override
    public CheckRecipeResult checkProcessing() {
        this.mMaxProgresstime = 20;
        setEnergyUsage(processingLogic);
        return CheckRecipeResultRegistry.GENERATING;
    }
}
