package com.OL925.ThinkTech.Recipe.machineRecipe;

import static com.OL925.ThinkTech.api.recipe.ImplosionGeneratorRecipeMap.implosionGeneratorFuels;
import static gregtech.api.util.GTRecipeConstants.LNG_BASIC_OUTPUT;

import net.minecraftforge.fluids.FluidStack;

import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.GTValues;

public class implosionGeneratorFlueRecipe {

    public static void loadFuelRecipes() {
        FluidStack[] inputs = new FluidStack[] { ThTMaterial.trinitrotoluene.getMolten(100) };
        int[] IGFuelTime = new int[] { 100 };
        int[] IGFuelVoltage = new int[] { 114514 };
        for (int i = 0; i < 1; i++) {
            GTValues.RA.stdBuilder()
                .fluidInputs(inputs[i])
                .fluidOutputs()
                .noOptimize()
                .eut(0)
                .metadata(LNG_BASIC_OUTPUT, IGFuelVoltage[i])
                .duration(IGFuelTime[i])
                .addTo(implosionGeneratorFuels);
        }
    }
}
