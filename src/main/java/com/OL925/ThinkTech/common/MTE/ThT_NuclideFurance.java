package com.OL925.ThinkTech.common.MTE;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrorRegistry;
import gregtech.api.util.MultiblockTooltipBuilder;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

public class ThT_NuclideFurance extends MTEExtendedPowerMultiBlockBase<ThT_NuclideFurance>
        implements ISurvivalConstructable, ISecondaryDescribable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static String[][] mainShape = new String[][]{
            {"                ", "                ", "           FFFFF", "           F   F", "           F   F", "           F   F"},
            {"                ", "                ", "           F   F", "            CCC ", "            D~D ", "            CHC "},
            {"                ", "                ", "    FFFFFFFF   F", "    F      FCCC ", "    F      FDDD ", "    F      FCHC "},
            {"                ", "     CGGGGGGGGC ", "FFFFFCCCCCCCCCCF", "F    C      CCC ", "F    CCCCCCCCCC ", "F     C     CCC "},
            {"     CGGGGGGGGC ", "  C   E      E  ", "FDAD  EAAAAAAE F", " DAD  EG    GE  ", " DAD  EGGGGGGE  ", " CCC CHHHHHHHHC "},
            {"     CCCCCCCCCC ", " CCC  EAAAAAAE  ", "FAID  E      E F", " AIBBBEAAAAAAE  ", " AID  E      E  ", " CCC CHHHHHHHHC "},
            {"     CGGGGGGGGC ", "  C   E      E  ", "FDAD  EAAAAAAE F", " DAD  EG    GE  ", " DAD  EGGGGGGE  ", " CCC CHHHHHHHHC "},
            {"                ", "     CGGGGGGGGC ", "FFFFFCCCCCCCCCCF", "F    C        C ", "F    CCCCCCCCCC ", "F     C      C  "},
            {"                ", "                ", "    FFFFFFFFFFFF", "    F          F", "    F          F", "    F          F"}};

    public ThT_NuclideFurance(int aID, String aName, String aNameRegional) {super(aID, aName, aNameRegional);}
    public ThT_NuclideFurance(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN,stackSize,hintsOnly,13,4,1);
    }

    @Override
    public IStructureDefinition<ThT_NuclideFurance> getStructureDefinition() {
        return null;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
//        tt.addController();
        return null;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 13, 4, 1, errors) && errors.isEmpty()) {
            errors.add(StructureErrorRegistry.UNKNOWN_STRUCTURE_ERROR);
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return null;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[0];
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
