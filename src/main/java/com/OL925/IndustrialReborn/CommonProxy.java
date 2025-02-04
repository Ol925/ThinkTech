package com.OL925.IndustrialReborn;

import com.OL925.IndustrialReborn.Config.Config;
import com.OL925.IndustrialReborn.Recipe.RecipeLoader;
import com.OL925.IndustrialReborn.Recipe.machineRecipe.MaterialsRecipePool;
import com.OL925.IndustrialReborn.common.Material.MaterialPool;
import com.OL925.IndustrialReborn.common.init.IRItemLoader;

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

        // IndustrialReborn.LOG.info(Config.greeting);
        IndustrialReborn.LOG.info("Industrial Reborn at version " + "0.1.0");

        new IRItemLoader().init();
        WerkstoffAdderRegistry.addWerkstoffAdder(new MaterialPool());
        new localize().loader();
    }

    // load "Do your mod setup. Build whatever data structures you care about. Register recipes." (Remove if not needed)
    public void init(FMLInitializationEvent event) {
        new RecipeLoader();
    }

    // postInit "Handle interaction with other mods, complete your setup based on this." (Remove if not needed)
    public void postInit(FMLPostInitializationEvent event) {
        new MaterialsRecipePool().loadRecipes();
    }

    // register server commands in this event handler (Remove if not needed)
    public void serverStarting(FMLServerStartingEvent event) {}
}
