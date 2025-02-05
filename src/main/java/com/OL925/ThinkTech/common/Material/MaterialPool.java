package com.OL925.ThinkTech.common.Material;

import static bartworks.util.BWUtil.subscriptNumbers;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.TextureSet;

public class MaterialPool implements Runnable {

    protected static final int OffsetID = 18_100;

    public static final Werkstoff alkaneWaterMixture = new Werkstoff(
        new short[] { 178, 176, 176 },
        "AlkaneWaterMixture",
        subscriptNumbers("CH4·xH2O"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID,
        TextureSet.SET_FLUID);

    @Override
    public void run() {}
}
