package com.OL925.ThinkTech.common.MTE;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class ThT_FutureDrilling extends MTEExtendedPowerMultiBlockBase<ThT_FutureDrilling>
        implements ISurvivalConstructable, ISecondaryDescribable {

    protected ThT_FutureDrilling(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ThT_FutureDrilling(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {

    }


    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static String[][] Shape = new String[][]{{
            "        ",
            "        ",
            "BBBBB   ",
            "B   B   ",
            "B   B   ",
            "B   B   "
    },{
            "        ",
            "        ",
            "B A B   ",
            "  A   A ",
            "  A  A~A",
            " AAA AAA"
    },{
            "  A     ",
            "  A     ",
            "BACAB   ",
            " ACA  A ",
            " ACA AAA",
            " AAA AAA"
    },{
            "        ",
            "        ",
            "B A B   ",
            "  A   A ",
            "  A  AAA",
            " AAA AAA"
    },{
            "        ",
            "        ",
            "BBBBB   ",
            "B   B   ",
            "B   B   ",
            "B   B   "
    }};

    @Override
    public IStructureDefinition<ThT_FutureDrilling> getStructureDefinition() {
        return null;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return null;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        if (checkPiece(STRUCTURE_PIECE_MAIN, 6, 4, 1)) {
            fixAllIssues();
            return true;
        }
        return false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new ThT_FutureDrilling(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity iGregTechTileEntity, ForgeDirection forgeDirection, ForgeDirection forgeDirection1, int i, boolean b, boolean b1) {
        return new ITexture[0];
    }
}
