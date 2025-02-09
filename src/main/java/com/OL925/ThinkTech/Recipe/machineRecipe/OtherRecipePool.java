package com.OL925.ThinkTech.Recipe.machineRecipe;

import static gregtech.api.enums.TierEU.*;

import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

public class OtherRecipePool {

    public void loadRecipes() {

        // 炼油气离心生产液化石油气
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gas.getGas(600))
            .fluidOutputs(Materials.LPG.getFluid(1500))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 2)
            .addTo(RecipeMaps.centrifugeRecipes);

        // 三硝基甲苯用于生产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.trinitrotoluene.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 8),
                ThTMaterial.leadAzide.get(OrePrefixes.dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // PETN产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.PETN.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 16),
                ThTMaterial.leadAzide.get(OrePrefixes.dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 4))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // 硝化甘油产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Glyceryl.getFluid(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 16),
                ThTMaterial.leadAzide.get(OrePrefixes.dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 16))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // HMX产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.HMX.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene, 32),
                ThTMaterial.leadAzide.get(OrePrefixes.dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // HNIW产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.HNIW.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene, 64),
                ThTMaterial.leadAzide.get(OrePrefixes.dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 64), GTModHandler.getIC2Item("industrialTnt", 64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
    }
}
