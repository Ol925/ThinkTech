package com.OL925.ThinkTech.common.init;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import com.OL925.ThinkTech.common.MTE.ThT_CzochralskiSingleCrystalFurnace;
import com.OL925.ThinkTech.common.MTE.ThT_GeneralChemicalFactory;
import com.OL925.ThinkTech.common.MTE.ThT_Kiln;
import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.common.MTE.ThT_ImplosionGenerator;

public class ThTMachineLoader {

    public static ItemStack ExplosiveGenerator;
    public static ItemStack CzochralskiSingleCrystalFurnace;
    public static ItemStack GeneralFactory;
    public static ItemStack Kiln;

    public static void loadMachine() {
        ExplosiveGenerator = new ThT_ImplosionGenerator(
            21000,
            "NameExplosiveGenerator",
            translateToLocalFormatted("mte.ImplosionGenerator")).getStackForm(1);
        ThTList.ExplosiveGenerator.set(ExplosiveGenerator);

        CzochralskiSingleCrystalFurnace = new ThT_CzochralskiSingleCrystalFurnace(
            21001,
            "NameIndustrialVaporDeposition",
            translateToLocalFormatted("mte.CSCF")).getStackForm(1);
        ThTList.CzochralskiSingleCrystalFurnace.set(CzochralskiSingleCrystalFurnace);

        GeneralFactory = new ThT_GeneralChemicalFactory(
            21002,
            "NameGeneralFactory",
            translateToLocalFormatted("mte.SAF")).getStackForm(1);
        ThTList.GeneralFactory.set(GeneralFactory);

//        Kiln = new ThT_Kiln(
//            21003,
//            "NameKiln",
//            translateToLocalFormatted("mte.kiln")).getStackForm(1);
//        ThTList.Kiln.set(Kiln);
    }
}
