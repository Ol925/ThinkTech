package com.OL925.ThinkTech.Recipe;

import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.Recipe.machineRecipe.FluidSolidifierRecipePool;
import com.OL925.ThinkTech.Recipe.machineRecipe.ItemRecipePool;
import com.OL925.ThinkTech.common.init.IRItemList;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeLoader {

    public RecipeLoader() {
        registerRecipe();
        registerSmelting();
        registerFuel();

        // load other recipe pool
        new ItemRecipePool().loadRecipes();
        new FluidSolidifierRecipePool().loadRecipes();

    }

    private void registerRecipe() {
        // Add your recipes here

    }

    private void registerSmelting() {
        // Add your smelting recipes here
    }

    private void registerFuel() {
        // Add your fuel recipes here
        GameRegistry.registerFuelHandler(new IFuelHandler() {

            @Override
            public int getBurnTime(ItemStack fuel) {
                return IRItemList.BRIQUETTE.getItem() == fuel.getItem() ? 12800 : 0;
            }
        });
    }
}
