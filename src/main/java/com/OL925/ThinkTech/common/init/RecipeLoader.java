package com.OL925.ThinkTech.common.init;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.Recipe.*;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;

import static gtPlusPlus.core.recipe.common.CI.*;

public class RecipeLoader {

    public RecipeLoader() {
        registerRecipe();
        registerSmelting();
        registerFuel();

        // load other recipe pool
        new MaterialsRecipePool().loadRecipes();
        new implosionGeneratorFlueRecipe().loadFuelRecipes();
        new ItemRecipePool().loadRecipes();
        new OtherRecipePool().loadRecipes();
        new MachineRecipePool().loadRecipes();
    }

    private void registerRecipe() {
        // Add your recipes here
        GTModHandler.addCraftingRecipe(
            ThTList.Kiln.get(1),
            bitsd,
            new Object[] { "CCC", "CMC", "CCC", 'M', ItemList.Hull_Bronze_Bricks,'C', ItemList.Firebrick.get(1)});
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
                return ThTList.METHANE_CLATHRATE.getItem() == fuel.getItem() ? 100000 : 0;
            }
        });
    }
}
