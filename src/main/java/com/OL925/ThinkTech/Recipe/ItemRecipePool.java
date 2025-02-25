package com.OL925.ThinkTech.Recipe.machineRecipe;

import static com.OL925.ThinkTech.common.init.ThTList.*;
import static gregtech.api.enums.TierEU.*;

import com.OL925.ThinkTech.common.init.ThTList;
import gregtech.api.util.GTModHandler;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTOreDictUnificator;

public class ItemRecipePool {

    public void loadRecipes() {

        // Briquette
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.Saltpeter.getDust(4),
                new ItemStack(Items.coal, 8),
                new ItemStack(Items.coal, 2, 1),
                new ItemStack(Items.clay_ball, 1))
            .itemOutputs(BRIQUETTE.get(8))
            .eut(RECIPE_LV)
            .duration(60)
            .addTo(RecipeMaps.assemblerRecipes);

        // Half Briquette
        GTValues.RA.stdBuilder()
            .itemInputs(BRIQUETTE.get(1))
            .fluidInputs(Materials.Water.getFluid(1000))
            .itemOutputs(HALF_BRIQUETTE.get(2))
            .eut(RECIPE_LV)
            .duration(60)
            .addTo(RecipeMaps.cutterRecipes);

        // MethaneClathrate
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ice.getDust(64))
            .fluidInputs(ThTMaterial.alkaneWaterMixture.getFluidOrGas(2000))
            .itemOutputs(METHANE_CLATHRATE.get(16))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 25)
            .addTo(RecipeMaps.autoclaveRecipes);

        // SuperOreo
        GTValues.RA.stdBuilder()
            .itemInputs(HALF_BRIQUETTE.get(2))
            .fluidInputs(Materials.Milk.getFluid(1000))
            .itemOutputs(SUPEROREO.get(4))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20)
            .addTo(RecipeMaps.assemblerRecipes);

        // implosion Generator
        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Electric_Motor_HV.get(4),
                ItemList.Electric_Piston_HV.get(8),
                ItemList.Hull_HV.get(1),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.StainlessSteel, 4),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.StainlessSteel, 16))
            .fluidInputs(Materials.SolderingAlloy.getMolten(2304))
            .itemOutputs(ExplosiveGenerator.get(1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 16)
            .addTo(RecipeMaps.assemblerRecipes);

        //结晶基底
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 8),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.StainlessSteel, 64),
                GTModHandler.getIC2Item("carbonFiber", 8L))
            .fluidInputs(Materials.SiliconSG.getMolten(1296))
            .itemOutputs(CRYSTALLINESUBSTRATE.get(1))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 16)
            .addTo(RecipeMaps.assemblerRecipes);

        //单晶炉 CSCF
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Electric_Motor_HV.get(8),
                ItemList.Electric_Pump_HV.get(8),
                ItemList.Electric_Piston_HV.get(8),
                ItemList.Robot_Arm_HV.get(1),
                ItemList.Machine_Multi_BlastFurnace.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8))
            .fluidInputs(Materials.StainlessSteel.getMolten(1152))
            .itemOutputs(CzochralskiSingleCrystalFurnace.get(1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 20)
            .addTo(RecipeMaps.assemblerRecipes);
    }
}


