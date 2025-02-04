package com.OL925.IndustrialReborn;

import static gregtech.api.util.GTLanguageManager.addStringLocalization;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class localize {

    public void loader() {
        addStringLocalization("bw.werkstoff.18100.name", translateToLocalFormatted("fluid.AlkaneWaterMixture.name"));
        addStringLocalization("fluid.alkanewatermixture", translateToLocalFormatted("fluid.AlkaneWaterMixture.name"));
    }
}
