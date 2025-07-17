package com.OL925.ThinkTech.Recipe;

import static com.OL925.ThinkTech.common.init.ThTList.CHIPTIER3;
import static com.OL925.ThinkTech.common.init.ThTList.CRYSTALLINESUBSTRATE;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.TierEU.*;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.core.item.chemistry.RocketFuels.Formaldehyde;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import bartworks.system.material.WerkstoffLoader;
import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.*;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tectech.thing.CustomItemList;

public class OtherRecipePool {

    public void loadRecipes() {

        // 炼油气离心生产液化石油气
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gas.getGas(600))
            .fluidOutputs(Materials.LPG.getFluid(1500))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 2)
            .addTo(RecipeMaps.centrifugeRecipes);

        // 三硝基甲苯用于生产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.trinitrotoluene.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 8),
                ThTMaterial.leadAzide.get(dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // PETN产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.PETN.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 16),
                ThTMaterial.leadAzide.get(dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 4))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // 硝化甘油产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Glyceryl.getFluid(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Plastic, 16),
                ThTMaterial.leadAzide.get(dust, 1))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 16))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // HMX产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.HMX.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene, 32),
                ThTMaterial.leadAzide.get(dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // HNIW产itnt
        GTValues.RA.stdBuilder()
            .fluidInputs(ThTMaterial.HNIW.getMolten(200))
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polytetrafluoroethylene, 64),
                ThTMaterial.leadAzide.get(dust, 4))
            .itemOutputs(GTModHandler.getIC2Item("industrialTnt", 64), GTModHandler.getIC2Item("industrialTnt", 64))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // 大化反合成甲醛
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Methanol.getFluid(32000), Materials.Oxygen.getGas(32000))
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidOutputs(FluidUtils.getFluidStack(Formaldehyde, 32000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //大化反合成乙酸酐
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MethylAcetate.getFluid(8000),Materials.CarbonMonoxide.getGas(8000))
            .fluidOutputs(new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 8000))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 10)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //五氯化磷生产磷酸
        GTValues.RA.stdBuilder()
            .itemInputs(ThTMaterial.pentachloride.get(dust,5))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(5000),Materials.PhosphoricAcid.getFluid(1000))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 2)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //特氟龙大化反配方
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(4),
                GTOreDictUnificator.get(dust, Materials.Polytetrafluoroethylene, 15),
                Materials.Carbon.getDust(1),
                Materials.Plastic.getDust(3))
            .fluidInputs(getFluidStack("liquid_sodium", 1000))
            .fluidOutputs(getFluidStack("molten.teflon", 2880))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 9)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //特氟龙产胶带
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(plate, Materials.Polytetrafluoroethylene, 1),
                ItemUtils.getItemStackWithMeta(true, "IC2:itemPartCarbonMesh", "RawCarbonMesh", 0, 2))
            .fluidInputs(getFluidStack("molten.teflon", 288))
            .itemOutputs(ItemList.Duct_Tape.get(6))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 5)
            .addTo(RecipeMaps.assemblerRecipes);

        //特氟龙产化学惰性方块
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Casing_SolidSteel.get(1), GTUtility.getIntegratedCircuit(6))
            .itemOutputs(ItemList.Casing_Chemically_Inert.get(1))
            .fluidInputs(getFluidStack("molten.teflon", 72))
            .duration(20)
            .eut(RECIPE_LV)
            .addTo(RecipeMaps.assemblerRecipes);

        //特氟龙产生碳纤维
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(6))
            .fluidInputs(getFluidStack("molten.teflon", 9))
            .itemOutputs(ItemUtils.getItemStackWithMeta(true, "IC2:itemCarbonFiber", "RawCarbonFibre", 0, 10))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20)
            .addTo(RecipeMaps.assemblerRecipes);

        //特氟龙生产光缆&激光管
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Parts_GlassFiber.get(8),//硼玻璃线圈
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8))
            .itemOutputs(CustomItemList.DATApipe.get(16L))
            .fluidInputs(getFluidStack("molten.teflon", 144))
            .noOptimize()
            .requiresCleanRoom()
            .eut(RECIPE_LuV)
            .duration(20 * 10)
            .addTo(assemblerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CustomItemList.DATApipe.get(8),
                GTModHandler.getIC2Item("reinforcedGlass", 12L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 4))
            .itemOutputs(CustomItemList.LASERpipe.get(16))
            .fluidInputs(getFluidStack("molten.teflon", 1440))
            .requiresCleanRoom()
            .duration(20 * 12)
            .eut(RECIPE_UV)
            .addTo(assemblerRecipes);

        //硒用于增产三种橡胶
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.RawRubber.getDust(12))
            .fluidOutputs(Materials.Rubber.getMolten(3240))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 6)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.Polydimethylsiloxane.getDust(12))
            .fluidOutputs(Materials.Silicone.getMolten(3240))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 36)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.RawStyreneButadieneRubber.getDust(12))
            .fluidOutputs(Materials.StyreneButadieneRubber.getMolten(3240))
            .noOptimize()
            .eut(RECIPE_MV)
            .duration(20 * 36)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //钛铁矿酸洗
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Ilmenite.getDust(15),
                Materials.Salt.getDust(1),
                ThTMaterial.seleniumDioxide.get(dust,1))
            .itemOutputs(Materials.Rutile.getDust(9),Materials.WroughtIron.getIngots(3))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1500))
            .fluidOutputs(Materials.SaltWater.getFluid(400))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 173)
            .specialValue((int) HeatingCoilLevel.MV.getHeat())//炉温
            .addTo(RecipeMaps.blastFurnaceRecipes);

        //白钨矿酸洗
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Scheelite.getDust(18),
                Materials.Salt.getDust(2),
                ThTMaterial.seleniumDioxide.get(dust,1))
            .itemOutputs(BotWerkstoffMaterialPool.TungstenTrioxide.get(dust,12),
                WerkstoffLoader.CalciumChloride.get(dust,9))//三氧化钨
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3000))
            .fluidOutputs(Materials.Water.getFluid(1000))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
    }
}
//GTValues.RA.stdBuilder()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);
