package com.OL925.ThinkTech.common.init;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTModHandler;
import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.Recipe.*;

import cpw.mods.fml.common.IFuelHandler;
import cpw.mods.fml.common.registry.GameRegistry;


public class RecipeLoader {

    public RecipeLoader() {
        new MaterialsRecipePool().loadRecipes();
        new implosionGeneratorFlueRecipe().loadFuelRecipes();
        new ItemRecipePool().loadRecipes();
        new OtherRecipePool().loadRecipes();
        new MachineRecipePool().loadRecipes();
    }

    public static void registerRecipe() {
        GTModHandler.addCraftingRecipe(
            ThTList.Kiln.get(1),
            new Object[] { "CCC", "CMC", "CCC", 'M', ItemList.Hull_Bronze_Bricks, 'C', ItemList.Firebrick.get(1) });
    }

    public static void registerSmelting() {
    }

    public static void registerFuel() {
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
