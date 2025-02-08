package com.OL925.ThinkTech.common.init;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.common.MTE.ThT_ImplosionGenerator;

public class ThTMachineLoader {

    public static ItemStack ExplosiveGenerator;

    public static void loadMachine() {
        ExplosiveGenerator = new ThT_ImplosionGenerator(
            21000,
            "NameExplosiveGenerator",
            translateToLocalFormatted("mte.ImplosionGenerator")).getStackForm(1);
        // ThTList.ExplosiveGenerator.set(ExplosiveGenerator);
    }
}
