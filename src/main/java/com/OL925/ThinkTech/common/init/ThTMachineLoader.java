package com.OL925.ThinkTech.common.init;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.common.MTE.*;
import net.minecraft.item.ItemStack;

public class ThTMachineLoader {

    public static ItemStack ExplosiveGenerator;
    public static ItemStack CzochralskiSingleCrystalFurnace;
    public static ItemStack GeneralFactory;
    public static ItemStack Kiln;
    public static ItemStack NobleGasEnrichmentSystem;

    public static void loadMachine() {
        ExplosiveGenerator = new ThT_ImplosionGenerator(
            27000,
            "NameExplosiveGenerator",
            translateToLocalFormatted("mte.ImplosionGenerator")).getStackForm(1);
        ThTList.ExplosiveGenerator.set(ExplosiveGenerator);

        CzochralskiSingleCrystalFurnace = new ThT_CzochralskiSingleCrystalFurnace(
            27001,
            "NameIndustrialVaporDeposition",
            translateToLocalFormatted("mte.CSCF")).getStackForm(1);
        ThTList.CzochralskiSingleCrystalFurnace.set(CzochralskiSingleCrystalFurnace);

        GeneralFactory = new ThT_GeneralChemicalFactory(
            27002,
            "NameGeneralFactory",
            translateToLocalFormatted("mte.SAF")).getStackForm(1);
        ThTList.GeneralFactory.set(GeneralFactory);

        Kiln = new ThT_Kiln(
            27003,
            "NameKiln",
            translateToLocalFormatted("mte.kiln")).getStackForm(1);
        ThTList.Kiln.set(Kiln);

        NobleGasEnrichmentSystem = new ThT_NobleGasEnrichmentSystem(
            27004,
            "NameNobleGasEnrichmentSystem",
            translateToLocalFormatted("mte.NGES")).getStackForm(1);
        ThTList.NobleGasEnrichmentSystem.set(NobleGasEnrichmentSystem);
    }
}
