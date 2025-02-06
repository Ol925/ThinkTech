package com.OL925.ThinkTech.Recipe.machineRecipe;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.TierEU.*;

import com.OL925.ThinkTech.common.Material.MaterialPool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import ic2.api.item.IC2Items;

public class MaterialsRecipePool {

    public void loadRecipes() {

        // AlkaneWaterMixture
        // GT++ mixer
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.NatruralGas.getGas(4000), GTModHandler.getSteam(20000))
            .fluidOutputs(MaterialPool.alkaneWaterMixture.getFluidOrGas(12000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 10)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // GT mixer
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NatruralGas.getCells(1))
            .itemOutputs(IC2Items.getItem("cell"))
            .fluidInputs(GTModHandler.getSteam(5000))
            .fluidOutputs(MaterialPool.alkaneWaterMixture.getFluidOrGas(3000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 4)
            .addTo(RecipeMaps.mixerRecipes);

        // alkaneWaterMixture
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.metaitem.98", 5))
            .itemOutputs(IC2Items.getItem("cell"))
            .fluidInputs(Materials.NatruralGas.getGas(1000))
            .fluidOutputs(MaterialPool.alkaneWaterMixture.getFluidOrGas(3000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 4)
            .addTo(RecipeMaps.mixerRecipes);

        // trinitrotoluene
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Toluene.getFluid(1000), Materials.NitricAcid.getFluid(3000))
            .fluidOutputs(MaterialPool.trinitrotoluene.getMolten(1000), Materials.Water.getFluid(3000))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 3)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.Toluene.getFluid(8000), Materials.NitricAcid.getFluid(24000))
            .fluidOutputs(MaterialPool.trinitrotoluene.getMolten(8000), Materials.Water.getFluid(24000))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
    }
}
