package com.OL925.ThinkTech.Recipe;

import static com.OL925.ThinkTech.common.init.ThTList.CHIPTIER3;
import static com.OL925.ThinkTech.common.init.ThTList.CRYSTALLINESUBSTRATE;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.plate;
import static gregtech.api.enums.TierEU.*;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gtPlusPlus.core.fluids.GTPPFluids.*;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import bartworks.system.material.WerkstoffLoader;
import com.OL925.ThinkTech.common.Material.ThTMaterial;

import gregtech.api.enums.*;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtnhlanth.common.register.BotWerkstoffMaterialPool;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import tectech.thing.CustomItemList;

public class OtherRecipePool {

    public void loadRecipes() {

        // 炼油气离心生产液化石油气
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Gas.getGas(600))
            .fluidOutputs(Materials.LPG.getFluid(1500))
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
            .eut(RECIPE_EV)
            .duration(20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        // 大化反合成甲醛
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Methanol.getFluid(32000), Materials.Oxygen.getGas(32000))
            .itemInputs(GTUtility.getIntegratedCircuit(24))
            .fluidOutputs(FluidUtils.getFluidStack(Formaldehyde, 32000))
            .eut(RECIPE_MV)
            .duration(20 * 20)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //大化反合成乙酸酐
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.MethylAcetate.getFluid(8000),Materials.CarbonMonoxide.getGas(8000))
            .fluidOutputs(MaterialMisc.ACETIC_ANHYDRIDE.getFluidStack(8000))
            .eut(RECIPE_HV)
            .duration(20 * 10)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);
        //五氯化磷生产磷酸
        GTValues.RA.stdBuilder()
            .itemInputs(ThTMaterial.pentachloride.get(dust,5))
            .fluidInputs(Materials.Water.getFluid(4000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(5000),Materials.PhosphoricAcid.getFluid(1000))
            .eut(RECIPE_MV)
            .duration(20 * 2)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);


        //硒用于增产三种橡胶
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6),
                Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.RawRubber.getDust(12))
            .fluidOutputs(Materials.Rubber.getMolten(3240))
            .eut(RECIPE_MV)
            .duration(20 * 6)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6),
                Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.Polydimethylsiloxane.getDust(12))
            .fluidOutputs(Materials.Silicone.getMolten(3240))
            .eut(RECIPE_MV)
            .duration(20 * 36)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(6),
                Materials.Sulfur.getDust(1),
                MaterialsElements.getInstance().SELENIUM.getDust(1),
                Materials.RawStyreneButadieneRubber.getDust(12))
            .fluidOutputs(Materials.StyreneButadieneRubber.getMolten(3240))
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
            .eut(RECIPE_EV)
            .duration(20 * 5)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //冰箱冻碎冰
        GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(24))
                .itemOutputs(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Ice, 8L))
                .fluidInputs(Materials.Water.getFluid(8000))
                .eut(RECIPE_MV)
                .duration(20 * 3)
                .addTo(RecipeMaps.vacuumFreezerRecipes);
    }
}
//GTValues.RA.stdBuilder()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);
