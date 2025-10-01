package com.OL925.ThinkTech.Recipe;

import static bartworks.system.material.WerkstoffLoader.SodiumNitrate;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.TierEU.*;
import static gtPlusPlus.core.fluids.GTPPFluids.*;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import bartworks.system.material.WerkstoffLoader;
import com.OL925.ThinkTech.common.Material.ThTMaterial;

import com.OL925.ThinkTech.common.init.ThTList;
import goodgenerator.items.GGMaterial;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import ic2.api.item.IC2Items;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class MaterialsRecipePool {

    public void loadRecipes() {

        // AlkaneWaterMixture
        // GT++ mixer
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.NatruralGas.getGas(4000), GTModHandler.getSteam(20000))
            .fluidOutputs(ThTMaterial.alkaneWaterMixture.getFluidOrGas(12000))
            .eut(RECIPE_MV)
            .duration(20 * 10)
            .addTo(GTPPRecipeMaps.mixerNonCellRecipes);

        // GT mixer
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.NatruralGas.getCells(1))
            .itemOutputs(IC2Items.getItem("cell"))
            .fluidInputs(GTModHandler.getSteam(5000))
            .fluidOutputs(ThTMaterial.alkaneWaterMixture.getFluidOrGas(3000))
            .eut(RECIPE_MV)
            .duration(20 * 4)
            .addTo(RecipeMaps.mixerRecipes);

        // alkaneWaterMixture
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(GregTech.ID, "gt.metaitem.98", 5))
            .itemOutputs(IC2Items.getItem("cell"))
            .fluidInputs(Materials.NatruralGas.getGas(1000))
            .fluidOutputs(ThTMaterial.alkaneWaterMixture.getFluidOrGas(3000))
            .eut(RECIPE_MV)
            .duration(20 * 4)
            .addTo(RecipeMaps.mixerRecipes);

        // trinitrotoluene
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Toluene.getFluid(1000), Materials.NitricAcid.getFluid(3000))
            .fluidOutputs(ThTMaterial.trinitrotoluene.getMolten(1000), Materials.Water.getFluid(3000))
            .eut(RECIPE_HV)
            .duration(20 * 3)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidInputs(Materials.Toluene.getFluid(8000), Materials.NitricAcid.getFluid(24000))
            .fluidOutputs(ThTMaterial.trinitrotoluene.getMolten(8000), Materials.Water.getFluid(24000))
            .eut(RECIPE_EV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //PETN
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.Pentaerythritol, 4))
            .fluidInputs(Materials.NitricAcid.getFluid(4000))
            .fluidOutputs(ThTMaterial.PETN.getMolten(1000))
            .eut(RECIPE_EV)
            .duration(20 * 8)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //HNIW
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 0,false))
            .fluidInputs(ThTMaterial.phenylmethanamine.getFluidOrGas(6000),ThTMaterial.ethanedial.getFluidOrGas(3000),
                Materials.Hydrogen.getGas(2000),Materials.NitricAcid.getFluid(6000))
            .fluidOutputs(ThTMaterial.HNIW.getMolten(1000))
            .eut(RECIPE_ZPM)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //HMT
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Ammonia.getGas(4000), FluidUtils.getFluidStack(Formaldehyde, 6000))
            .fluidOutputs(ThTMaterial.HMT.getMolten(1000),Materials.Water.getFluid(6000))
            .eut(RECIPE_EV)
            .duration(20 * 2)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //HMX
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.HMT.getMolten(1000),Materials.NitricAcid.getFluid(4000), WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(2000),
        new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 6000))
            .fluidOutputs(ThTMaterial.HMX.getMolten(1500))
            .eut(RECIPE_LuV)
            .duration(20 * 2)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //乙二醛
        GTValues.RA.stdBuilder()
            .fluidInputs(MaterialsKevlar.Ethyleneglycol.getFluid(8000),Materials.Oxygen.getGas(8000))
            .fluidOutputs(ThTMaterial.ethanedial.getFluidOrGas(8000),Materials.Water.getFluid(8000))
            .eut(RECIPE_MV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //苯甲醛
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Toluene.getFluid(4000),Materials.Oxygen.getGas(4000))
            .fluidOutputs(ThTMaterial.benzaldehyd.getFluidOrGas(4000))
            .eut(RECIPE_MV)
            .duration(20 * 3)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //苄胺
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.benzaldehyd.getFluidOrGas(4000),Materials.Ammonia.getGas(4000) )
            .fluidOutputs(ThTMaterial.phenylmethanamine.getFluidOrGas(4000))
            .eut(RECIPE_HV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //五氯化磷
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(5),Materials.Phosphorus.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(5000))
            .itemOutputs(ThTMaterial.pentachloride.get(dust,1))
            .eut(RECIPE_MV)
            .duration(30)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //叠氮化铅合成配方   还未注册叠氮化钠 NaN₃ + Pb (NO₃)₂ → Pb (N₃)₂ + NaNO₃
        GTValues.RA.stdBuilder()
            .itemInputs(ThTMaterial.sodiumAzide.get(OrePrefixes.dust,64),Materials.Lead.getDust(64))
            .itemOutputs(SodiumNitrate.get(dust, 64),ThTMaterial.leadAzide.get(OrePrefixes.dust,64))
            .fluidInputs(Materials.NitricAcid.getFluid(128000))
            .fluidOutputs(Materials.Hydrogen.getGas(128000))
            .eut(RECIPE_MV)
            .duration(20 * 3)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //叠氮化钠
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(32))
            .itemOutputs(ThTMaterial.sodiumAzide.get(OrePrefixes.dust,32))
            .fluidInputs(Materials.Ammonia.getGas(32000),Materials.Nitrogen.getGas(16000))
            .fluidOutputs(Materials.Hydrogen.getGas(48000))
            .eut(RECIPE_MV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //预培养维生冷藏细菌液
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.PreculturedBacterialSolution.getFluidOrGas(1000))
            .fluidOutputs(ThTMaterial.FreezedPreculturedBacterialSolution.getFluidOrGas(1000))
            .eut(RECIPE_EV)
            .duration(20 *25)
            .addTo(RecipeMaps.vacuumFreezerRecipes);


        //二氧化硒
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().SELENIUM.getDust(1))
            .fluidInputs(Materials.Oxygen.getGas(2000))
            .itemOutputs(ThTMaterial.seleniumDioxide.get(dust,1))
            .eut(RECIPE_LV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //营养液
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RockSalt.getDust(4),
                Materials.Phosphorus.getDust(1),
                MaterialsElements.getInstance().IODINE.getDust(1))
            .fluidInputs(Materials.Nitrogen.getGas(2000))
            .fluidOutputs(ThTMaterial.nutrientSolution.getFluidOrGas(1000))
            .eut(RECIPE_MV)
            .duration(20 * 3)
            .addTo(RecipeMaps.mixerRecipes);

        //碘粉
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RockSalt.getDust(16))
            .itemOutputs(Materials.Potassium.getDust(4),
                MaterialsElements.getInstance().IODINE.getDust(3))
            .fluidInputs(getFluidStack("sodiumpotassium",144),
                GTModHandler.getDistilledWater(8000))
            .fluidOutputs(Materials.Chlorine.getGas(1000))
            .eut(RECIPE_HV)
            .duration(20 * 30)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //营养液产肥料
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcite.getDust(1))
            .fluidInputs(ThTMaterial.nutrientSolution.getFluidOrGas(1000))
            .itemOutputs(GTModHandler.getModItem(IndustrialCraft2.ID,  "itemFertilizer", 40))
            .eut(30)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //焙烧锌-铟线
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.SulfuricAcid.getFluid(16000))
            .fluidOutputs(Materials.Hydrogen.getGas(32000))
            .itemInputs(Materials.RoastedZinc.getDust(16))
            .itemOutputs(WerkstoffLoader.ZincSulfate.get(dust,16))
            .eut(RECIPE_HV)
            .duration(20 * 6)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
                .fluidInputs(GTModHandler.getDistilledWater(8000),
                        GGMaterial.ether.getFluidOrGas(4000),
                        Materials.Acetone.getFluid(4000))
                .fluidOutputs(ThTMaterial.Sxcqyry.getFluidOrGas(16000))
                .itemInputs(WerkstoffLoader.ZincSulfate.get(dust,24))
                .eut(RECIPE_EV)
                .duration(20 * 30)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
                .fluidInputs(GTModHandler.getDistilledWater(6000),
                        ThTMaterial.Dndcq.getFluidOrGas(10000))
                .fluidOutputs(ThTMaterial.Sxcqyry.getFluidOrGas(16000))
                .itemInputs(WerkstoffLoader.ZincSulfate.get(dust,24))
                .eut(RECIPE_EV)
                .duration(20 * 15)
                .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
                .fluidInputs(ThTMaterial.Sxcqyry.getFluidOrGas(4000))
                .fluidOutputs(ThTMaterial.Dndcq.getFluidOrGas(2500))
                .itemInputs(ThTList.IRON_CATALYST.get(0))
                .itemOutputs(Materials.Indium.getDust(6))
                .eut(RECIPE_HV)
                .duration(20 * 45)
                .addTo(ThTRecipeMap.GeneralChemicalFactory);
    }
}
//        GTValues.RA.stdBuilder()
//            .fluidInputs()
//            .fluidOutputs()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);
