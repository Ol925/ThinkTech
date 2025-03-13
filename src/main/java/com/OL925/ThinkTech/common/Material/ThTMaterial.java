package com.OL925.ThinkTech.common.Material;

import static bartworks.util.BWUtil.subscriptNumbers;

import bartworks.system.material.Werkstoff;
import gregtech.api.enums.TextureSet;

public class ThTMaterial implements Runnable {

    protected static final int OffsetID = 18100;

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
    //HMT(乌洛托品)
    public static final Werkstoff HMT = new Werkstoff(
        new short[] { 230, 230, 230 },
        "HMT",
        subscriptNumbers("C6H12N4"),
        new Werkstoff.Stats().setMeltingPoint(280),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 6,
        TextureSet.SET_RUBY);
    //乙二醛
    public static final Werkstoff ethanedial = new Werkstoff(
        new short[] { 182, 186, 44 },//颜色要改成黄色的
        "Ethanedial",
        subscriptNumbers("C2H2O2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 7,
        TextureSet.SET_FLUID);
    //苄胺
    public static final Werkstoff phenylmethanamine = new Werkstoff(
        new short[] { 178, 176, 176 },
        "Phenylmethanamine",
        subscriptNumbers("C6H5CH2NH2"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 8,
        TextureSet.SET_FLUID);
    //苯甲醛
    public static final Werkstoff benzaldehyd = new Werkstoff(
        new short[] { 178, 176, 176 },
        "Benzaldehyd",
        subscriptNumbers("C7H6O"),
        new Werkstoff.Stats(),
        Werkstoff.Types.MIXTURE,
        new Werkstoff.GenerationFeatures().disable()
            .addCells(),
        OffsetID + 9,
        TextureSet.SET_FLUID);
    //五氯化磷
    public static final Werkstoff pentachloride = new Werkstoff(
        new short[] { 190, 196, 70 },
        "Pentachloride",
        subscriptNumbers("PCl5"),
        new Werkstoff.Stats().setMeltingPoint(440),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 10,
        TextureSet.SET_RUBY);
    //叠氮化钠
    public static final Werkstoff sodiumAzide = new Werkstoff(
        new short[] { 221, 221, 221 },
        "SodiumAzide",
        subscriptNumbers("NaN3"),
        new Werkstoff.Stats().setMeltingPoint(548),
        Werkstoff.Types.MATERIAL,
        new Werkstoff.GenerationFeatures().disable()
            .onlyDust()
            .addMolten(),
        OffsetID + 11,
        TextureSet.SET_DULL);
    //
    @Override
    public void run() {}
}
