package com.OL925.ThinkTech.Recipe;

import static com.OL925.ThinkTech.gui.ThTUITexture.PICTURE_Implosion_Generator;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.api.recipe.maps.LargeNEIFrontend;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

public class ThTRecipeMap {

    public static final RecipeMap<RecipeMapBackend> implosionGeneratorFuels = RecipeMapBuilder
        .of("tht.recipe.implosion_generator")
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.implosion_generator"))
        .progressBar(PICTURE_Implosion_Generator)
        .progressBarPos(65, 16)
        .progressBarSize(44, 41)
        .build();

    public static final RecipeMap<RecipeMapBackend> CzochralskiSingleCrystalFurnace = RecipeMapBuilder
        .of("tht.recipe.CSCF")
        .maxIO(3, 2, 4, 1)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .frontend(LargeNEIFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> GeneralChemicalFactory = RecipeMapBuilder
        .of("tht.recipe.SAF")
        .maxIO(6, 6, 6, 6)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .frontend(LargeNEIFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> Kiln = RecipeMapBuilder
        .of("tht.recipe.kiln")
        .maxIO(6, 6, 0, 0)
        .progressBar(GTUITextures.PROGRESSBAR_ARROW_MULTIPLE)
        .disableOptimize()
        .frontend(LargeNEIFrontend::new)
        .build();

    public static final RecipeMap<RecipeMapBackend> NobleGasEnrichmentSystem = RecipeMapBuilder
        .of("tht.recipe.NGES")
        .maxIO(3, 0, 9, 9)
        .progressBar(GTUITextures.PROGRESSBAR_BATH)
        .disableOptimize()
        .frontend(LargeNEIFrontend::new)
        .build();
}
