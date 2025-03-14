package com.OL925.ThinkTech.common.init;

import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.Recipe.*;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

public class RecipeLoader {

    public RecipeLoader() {
        registerRecipe();
        registerSmelting();
        registerFuel();

        // load other recipe pool
        new MaterialsRecipePool().loadRecipes();
        new implosionGeneratorFlueRecipe().loadFuelRecipes();
        new ItemRecipePool().loadRecipes();
        new FluidSolidifierRecipePool().loadRecipes();
        new OtherRecipePool().loadRecipes();
        new MachineRecipePool().loadRecipes();
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
                return ThTList.BRIQUETTE.getItem() == fuel.getItem() ? 12800 : 0;
            }
        });

        GameRegistry.registerFuelHandler(new IFuelHandler() {

            @Override
            public int getBurnTime(ItemStack fuel) {
                return ThTList.METHANE_CLATHRATE.getItem() == fuel.getItem() ? 30000 : 0;
            }
        });
    }
}
