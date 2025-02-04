package com.OL925.IndustrialReborn.Recipe.machineRecipe;

import static com.OL925.IndustrialReborn.common.init.IRItemList.*;
import static gregtech.api.enums.TierEU.RECIPE_LV;
import static gregtech.api.enums.TierEU.RECIPE_MV;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.OL925.IndustrialReborn.common.Material.MaterialPool;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;

public class ItemRecipePool {

    public void loadRecipes() {

        // Briquette
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Saltpeter.getDust(4),
                new ItemStack(Items.coal, 8),
                new ItemStack(Items.coal, 2, 1),
                new ItemStack(Items.clay_ball, 1))
            .itemOutputs(BRIQUETTE.get(8))
            .eut(RECIPE_LV)
            .duration(60)
            .addTo(RecipeMaps.assemblerRecipes);

        // Half Briquette
        GTValues.RA.stdBuilder()
            .itemInputs(BRIQUETTE.get(1))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(HALF_BRIQUETTE.get(2))
            .eut(RECIPE_LV)
            .duration(60)
            .addTo(RecipeMaps.cutterRecipes);

        // MethaneClathrate
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ice.getDust(64))
            .fluidInputs(MaterialPool.alkaneWaterMixture.getFluidOrGas(2000))
            .itemOutputs(METHANE_CLATHRATE.get(16))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(60)
            .addTo(RecipeMaps.autoclaveRecipes);

    }
}
