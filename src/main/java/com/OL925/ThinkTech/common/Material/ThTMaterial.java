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
        new short[] { 233, 238, 232 },
        "LeadAzide",
        subscriptNumbers("Pb(N3)2"),
        new Werkstoff.Stats().setMeltingPoint(350),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust(),
        OffsetID + 2,
        TextureSet.SET_DULL);
    // 季戊四醇四硝酸酯(PETN)
    public static final Werkstoff PETN = new Werkstoff(
        new short[] { 221, 221, 221 },
        "PETN",
        subscriptNumbers("C5H8N4O12"),
        new Werkstoff.Stats().setMeltingPoint(139),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 3,
        TextureSet.SET_RUBY);
    // 环四亚甲基四硝胺(奥克托今)
    public static final Werkstoff HMX = new Werkstoff(
        new short[] { 226, 226, 228 },
        "HMX",
        subscriptNumbers("C4H8N8O8"),
        new Werkstoff.Stats().setMeltingPoint(275),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 4,
        TextureSet.SET_RUBY);
    // CL-20
    public static final Werkstoff HNIW = new Werkstoff(
        new short[] { 230, 230, 230 },
        "HNIW",
        subscriptNumbers("C6N12H6O12"),
        new Werkstoff.Stats().setMeltingPoint(275),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 5,
        TextureSet.SET_RUBY);

    @Override
    public void run() {}
}
