package com.OL925.ThinkTech.Config;

import codechicken.nei.api.IConfigureNEI;
import gtnhlanth.Tags;

public class NEIConfig implements IConfigureNEI {

    public static boolean isAdded = true;

    @Override
    public String getName() {
        return "ThinkTech NEI";
    }

    @Override
    public String getVersion() {
        return Tags.VERSION;
    }

    @Override
    public void loadConfig() {
        isAdded = true;
    }
}
