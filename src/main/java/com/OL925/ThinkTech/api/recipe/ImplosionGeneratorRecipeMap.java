package com.OL925.ThinkTech.api.recipe;

import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMapBackend;
import gregtech.api.recipe.RecipeMapBuilder;
import gregtech.nei.formatter.SimpleSpecialValueFormatter;

import static com.OL925.ThinkTech.gui.ThTUITexture.PICTURE_Implosion_Generator;

public class ImplosionGeneratorRecipeMap {

    public static final RecipeMap<RecipeMapBackend> implosionGeneratorFuels = RecipeMapBuilder
        .of("tht.recipe.implosion_generator")
        .maxIO(0, 0, 1, 1)
        .minInputs(0, 1)
        .neiSpecialInfoFormatter(new SimpleSpecialValueFormatter("value.implosion_generator"))
        .progressBar(PICTURE_Implosion_Generator)
        .progressBarPos(65,16)
        .progressBarSize(44,41)
        //.addSpecialTexture(65, 16, 44, 41, PICTURE_IG_PB)
        .build();
}
