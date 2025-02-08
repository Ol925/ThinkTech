package com.OL925.ThinkTech;

import com.OL925.ThinkTech.Config.Config;
import com.OL925.ThinkTech.Recipe.RecipeLoader;
import com.OL925.ThinkTech.Recipe.machineRecipe.MaterialsRecipePool;
import com.OL925.ThinkTech.Recipe.machineRecipe.implosionGeneratorFlueRecipe;
import com.OL925.ThinkTech.common.Material.ThTMaterial;
import com.OL925.ThinkTech.common.init.ThTItemLoader;
import com.OL925.ThinkTech.common.init.ThTMachineLoader;

import bartworks.API.WerkstoffAdderRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    // preInit "Run before anything else. Read your config, create blocks, items, etc, and register them with the
    // GameRegistry." (Remove if not needed)
    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        new ThTItemLoader().init();
        WerkstoffAdderRegistry.addWerkstoffAdder(new ThTMaterial());
        new bwLocalization().loader();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        ThTMachineLoader.loadMachine();

    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        new MaterialsRecipePool().loadRecipes();
        new RecipeLoader();
        new implosionGeneratorFlueRecipe().loadFuelRecipes();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
