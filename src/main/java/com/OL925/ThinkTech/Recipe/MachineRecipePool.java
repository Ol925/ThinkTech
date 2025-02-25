package com.OL925.ThinkTech.Recipe;

import com.OL925.ThinkTech.Recipe.ThTRecipeMap;
import com.OL925.ThinkTech.common.Material.ThTMaterial;
import com.OL925.ThinkTech.common.init.ThTList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;

import static goodgenerator.items.GGMaterial.naquadahine;
import static gregtech.api.enums.TierEU.RECIPE_LuV;
import static gregtech.api.enums.TierEU.RECIPE_IV;
import static gregtech.api.enums.TierEU.RECIPE_EV;
import static gregtech.api.enums.TierEU.RECIPE_HV;

public class MachineRecipePool {
    public void loadRecipes(){
        //单晶硅
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 0,false))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot.get(16))
            .fluidInputs(Materials.SiliconTetrachloride.getFluid(32000),
                Materials.Hydrogen.getGas(64000),
                Materials.GalliumArsenide.getMolten(2304))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(64000))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 *450)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //磷掺杂单晶硅
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot2.get(16))
            .fluidInputs(Materials.SiliconTetrachloride.getFluid(64000),
                Materials.Hydrogen.getGas(128000),
                Materials.GalliumArsenide.getMolten(4608),
                ThTMaterial.pentachloride.getMolten(8000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(64000))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 *250)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //硅岩掺杂
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(16))
            .fluidInputs(Materials.Naquadah.getMolten(2304),
                Materials.GalliumArsenide.getMolten(4608),
                Materials.SiliconTetrachloride.getFluid(128000),
                Materials.Hydrogen.getGas(256000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(128000))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*400)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);

        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0),naquadahine.get(OrePrefixes.dust, 48))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot3.get(16))
            .fluidInputs(Materials.GalliumArsenide.getMolten(4608),
                Materials.SiliconTetrachloride.getFluid(128000),
                Materials.Hydrogen.getGas(256000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(128000))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*125)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //铕掺杂
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0),GTOreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 0,false))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(16))
            .fluidInputs(Materials.Europium.getMolten(4608),
                Materials.GalliumArsenide.getMolten(9216),
                Materials.SiliconTetrachloride.getFluid(128000),
                Materials.Hydrogen.getGas(256000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(128000))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20*690)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //镅掺杂
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0),GTOreDictUnificator.get(OrePrefixes.dust, Materials.Americium, 0,false))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(16))
            .fluidInputs(Materials.Americium.getMolten(9216),
                Materials.GalliumArsenide.getMolten(18432),
                Materials.SiliconTetrachloride.getFluid(256000),
                Materials.Hydrogen.getGas(512000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(256000))
            .noOptimize()
            .eut(RECIPE_LuV)
            .duration(20*1000)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
    }
}
//GTValues.RA.stdBuilder()
//            .fluidInputs()
//            .fluidOutputs()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);
