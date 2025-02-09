package com.OL925.ThinkTech.common.Material;

import static bartworks.util.BWUtil.subscriptNumbers;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.TextureSet;

public class ThTMaterial implements Runnable {

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

    public static final Werkstoff trinitrotoluene = new Werkstoff(
        new short[] { 191, 104, 50 },
        "2,4,6-trinitrotoluene",
        subscriptNumbers("C7H5N3O6"),
        new Werkstoff.Stats().setMeltingPoint(354),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 1,
        TextureSet.SET_RUBY);

    public static final Werkstoff leadAzide = new Werkstoff(
        new short[]{233,238,232},
        "LeadAzide",
        subscriptNumbers("Pb(N3)2"),
        new Werkstoff.Stats().setMeltingPoint(350),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
        .onlyDust(),
        OffsetID + 2,
        TextureSet.SET_DULL
    );

    @Override
    public void run() {}
}
