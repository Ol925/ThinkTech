package com.OL925.ThinkTech;

import com.OL925.ThinkTech.common.init.ThTBlockLoader;
import net.minecraftforge.common.MinecraftForge;

import com.OL925.ThinkTech.Config.Config;
import com.OL925.ThinkTech.common.init.RecipeLoader;
import com.OL925.ThinkTech.common.Material.ThTMaterial;
import com.OL925.ThinkTech.common.event.ExplosionEventHandler;
import com.OL925.ThinkTech.common.init.ThTItemLoader;
import com.OL925.ThinkTech.common.init.ThTMachineLoader;

import bartworks.API.WerkstoffAdderRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        Config.synchronizeConfiguration(event.getSuggestedConfigurationFile());
        ThTItemLoader.init();
        ThTBlockLoader.init();
        WerkstoffAdderRegistry.addWerkstoffAdder(new ThTMaterial());
        MinecraftForge.EVENT_BUS.register(new ExplosionEventHandler());
        new bwLocalization().loader();
    }

    public void init(FMLInitializationEvent event) {
        ThTMachineLoader.loadMachine();

    }

    public void postInit(FMLPostInitializationEvent event) {

        new RecipeLoader();

    }

    public void serverStarting(FMLServerStartingEvent event) {}
}
