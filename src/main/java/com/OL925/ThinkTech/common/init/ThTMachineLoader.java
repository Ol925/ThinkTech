package com.OL925.ThinkTech.common.init;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.common.MTE.*;
import com.OL925.ThinkTech.common.hatch.ThT_HatchFuelBus;
import net.minecraft.item.ItemStack;

public class ThTMachineLoader {

    public static ItemStack ExplosiveGenerator;
    public static ItemStack CzochralskiSingleCrystalFurnace;
    public static ItemStack GeneralFactory;
    public static ItemStack Kiln;
    public static ItemStack NobleGasEnrichmentSystem;
    public static ItemStack DrillingRig;
    public static ItemStack SteelCrucible;
    public static ItemStack FuelInputBus;
    public static ItemStack InvarCrucible;
    public static ItemStack StainlessCrucible;
    public static ItemStack TitaniumCrucible;
    public static ItemStack TungstenSteelCrucible;

    public static void loadMachine() {
        ExplosiveGenerator = new ThT_ImplosionGenerator(
            27000,
            "NameThTImplosionGenerator",
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

        DrillingRig = new ThT_DrillingRig(
            27005,
            "NameDrillingRig",
            translateToLocalFormatted("mte.DR")).getStackForm(1);
        ThTList.DrillingRig.set(DrillingRig);

        SteelCrucible = new ThT_SteelCrucible(
            27006,
            "NameSteelCrucible",
            translateToLocalFormatted("mte.steelCrucible")).getStackForm(1);
        ThTList.SteelCrucible.set(SteelCrucible);

        FuelInputBus = new ThT_HatchFuelBus(
            27007,
            "NameThTFuelInputBus",
            translateToLocalFormatted("mte.FuelInputBus")).getStackForm(1);
        ThTList.FuelInputBus.set(FuelInputBus);

        InvarCrucible = new ThT_InvarCrucible(
            27008,
            "NameInvarCrucible",
            translateToLocalFormatted("mte.invarCrucible")).getStackForm(1);
        ThTList.InvarCrucible.set(InvarCrucible);

        StainlessCrucible = new ThT_StainlessCrucible(
            27009,
            "NameStainlessCrucible",
            translateToLocalFormatted("mte.stainlessCrucible")).getStackForm(1);
        ThTList.StainlessCrucible.set(StainlessCrucible);

        TitaniumCrucible = new ThT_TitaniumCrucible(
            27010,
            "NameTitaniumCrucible",
            translateToLocalFormatted("mte.titaniumCrucible")).getStackForm(1);
        ThTList.TitaniumCrucible.set(TitaniumCrucible);

        TungstenSteelCrucible = new ThT_TungstenSteelCrucible(
            27011,
            "NameTungstenSteelCrucible",
            translateToLocalFormatted("mte.tungstenSteelCrucible")).getStackForm(1);
        ThTList.TungstenSteelCrucible.set(TungstenSteelCrucible);
    }
}
