package com.OL925.ThinkTech.Recipe;

import static gregtech.api.enums.TierEU.RECIPE_MV;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.recipe.RecipeMaps;

public class FluidSolidifierRecipePool {

    public void loadRecipes() {

        // add crashed ice to recipe
        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Ice.getDust(64))
            .fluidInputs(Materials.Water.getFluid(8000))
            .eut(RECIPE_MV)
            .duration(20 * 5)
            .addTo(RecipeMaps.fluidSolidifierRecipes);
    }
}
