package com.OL925.ThinkTech;

import static gregtech.api.util.GTLanguageManager.addStringLocalization;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

public class bwLocalization {

    public void loader() {
        // Material & Molten
        String[] akey1 = new String[] { "bw.werkstoff.18101.name", "fluid.molten.2,4,6-trinitrotoluene",
            "bw.werkstoff.18102.name",
            "bw.werkstoff.18103.name", "fluid.molten.petn",
            "bw.werkstoff.18104.name", "fluid.molten.hmx",
            "bw.werkstoff.18105.name", "fluid.molten.hniw",
            "bw.werkstoff.18106.name", "fluid.molten.hmt",
            "bw.werkstoff.18110.name", "fluid.molten.pentachloride",
            "bw.werkstoff.18111.name", "fluid.molten.sodiumazide"};
        String[] aenglish1 = new String[] { "item.2,4,6-trinitrotoluene.name", "item.2,4,6-trinitrotoluene.name",
            "item.leadAzide.name",
            "item.PETN.name","item.PETN.name",
            "item.HMX.name","item.HMX.name",
            "item.cl20.name","item.cl20.name",
            "item.HMT.name","item.HMT.name",
            "item.pentachloride.name","item.pentachloride.name",
            "item.sodiumAzide.name", "item.sodiumAzide.name"};
        for (int i = 0; i < akey1.length; i++) {
            addStringLocalization(akey1[i], translateToLocalFormatted(aenglish1[i]));
        }
        // fluid
        String[] akey2 = new String[] {
            "bw.werkstoff.18100.name","fluid.alkanewatermixture",
            "bw.werkstoff.18107.name","fluid.ethanedial",
            "bw.werkstoff.18108.name","fluid.phenylmethanamine",
            "bw.werkstoff.18109.name","fluid.benzaldehyd" };
        String[] aenglish2 = new String[] {
            "fluid.AlkaneWaterMixture.name", "fluid.AlkaneWaterMixture.name",
            "fluid.Ethanedial.name","fluid.Ethanedial.name",
            "fluid.Phenylmethanamine.name","fluid.Phenylmethanamine.name",
            "fluid.Benzaldehyd.name","fluid.Benzaldehyd.name"};
        for (int i = 0; i < akey2.length; i++) {
            addStringLocalization(akey2[i], translateToLocalFormatted(aenglish2[i]));
        }
    }
}
