package com.OL925.ThinkTech.Recipe;

import static gregtech.api.util.GTRecipeConstants.LNG_BASIC_OUTPUT;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import net.minecraftforge.fluids.FluidStack;

import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;

public class implosionGeneratorFlueRecipe {

    FluidStack[] inputs = new FluidStack[] { ThTMaterial.trinitrotoluene.getMolten(1000),
        ThTMaterial.PETN.getMolten(1000), Materials.Glyceryl.getFluid(1000), ThTMaterial.HMX.getMolten(1000),
        ThTMaterial.HNIW.getMolten(1000) };
    int[] IGFuelTime = new int[] { 150, 80, 80, 100, 100 };
    public static int[] IGFuelVoltage = new int[] { 8192, 32768, 131072, 524288, 2097152 };

    public void loadFuelRecipes() {

        for (int i = 0; i < 5; i++) {
            GTValues.RA.stdBuilder()
                .fluidInputs(inputs[i])
                .fluidOutputs()
                .eut(0)
                .metadata(LNG_BASIC_OUTPUT, IGFuelVoltage[i])
                .duration(IGFuelTime[i])
                .addTo(ThTRecipeMap.implosionGeneratorFuels);
        }
    }
}
