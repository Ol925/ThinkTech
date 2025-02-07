package com.OL925.ThinkTech;

import static gregtech.api.util.GTLanguageManager.addStringLocalization;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class bwLocalization {

    public void loader() {
        // fluid
        addStringLocalization("bw.werkstoff.18100.name", translateToLocalFormatted("fluid.AlkaneWaterMixture.name"));
        addStringLocalization("fluid.alkanewatermixture", translateToLocalFormatted("fluid.AlkaneWaterMixture.name"));
        // Material
        addStringLocalization("bw.werkstoff.18101.name", translateToLocalFormatted("item.2,4,6-trinitrotoluene.name"));
        // Molten Material
        addStringLocalization(
            "fluid.molten.2,4,6-trinitrotoluene",
            translateToLocalFormatted("item.2,4,6-trinitrotoluene.name"));
    }
}
