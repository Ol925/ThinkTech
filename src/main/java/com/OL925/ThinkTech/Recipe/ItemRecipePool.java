package com.OL925.ThinkTech.Recipe;

import static com.OL925.ThinkTech.common.init.ThTList.*;
import static gregtech.api.enums.TierEU.*;

import com.OL925.ThinkTech.common.init.ThTList;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import net.minecraft.init.Items;
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

        //chipTier1
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.EV, 8),
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 2),
            ItemList.Tool_DataStick.get(2))
            .itemOutputs(CHIPTIER1.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RecipeMaps.assemblerRecipes);

        //chipTier2
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8),
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 2),
                ItemList.Tool_DataStick.get(8))
            .itemOutputs(CHIPTIER2.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 20)
            .addTo(RecipeMaps.assemblerRecipes);

        //chipTier3
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 8),
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 2),
                ItemList.Tool_DataStick.get(16))
            .itemOutputs(CHIPTIER3.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .noOptimize()
            .eut(RECIPE_LuV)
            .duration(20 * 20)
            .addTo(RecipeMaps.assemblerRecipes);

        //chipTier4
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 8),
                GTUtility.getIntegratedCircuit(24),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 2),
                ItemList.Tool_DataStick.get(32))
            .itemOutputs(CHIPTIER4.get(1))
            .fluidInputs(Materials.SolderingAlloy.getMolten(9216))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 *20)
            .addTo(RecipeMaps.assemblerRecipes);

        //铁触媒催化剂
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnetite.getDust(64),
                Materials.Aluminiumoxide.getDust(10),
                Materials.Quicklime.getDust(6),
                Materials.Magnesia.getDust(5),
                Materials.CobaltOxide.getDust(3))
            .itemOutputs(IRON_CATALYST.get(1))
            .noOptimize()
            .eut(RECIPE_HV)
            .duration(20 * 10)
            .addTo(RecipeMaps.multiblockChemicalReactorRecipes);

        //通用化工厂
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Tool_DataStick.get(4),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.HV, 8),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Polytetrafluoroethylene, 6),
                ItemList.Hull_EV.get(1),
                ItemList.Electric_Piston_HV.get(2))
            .itemOutputs(GeneralFactory.get(1))
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(2304))
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 * 15)
            .addTo(RecipeMaps.assemblerRecipes);

        //稀有气体富集装置
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 32),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 8),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.TungstenSteel, 4),
                ItemList.Hull_IV.get(1),
                ItemList.Electric_Piston_EV.get(16),
                ItemList.Electric_Piston_EV.get(16))
            .fluidInputs()
            .itemOutputs(NobleGasEnrichmentSystem.get(1))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 114)
            .addTo(RecipeMaps.assemblerRecipes);

        //控制中心T1
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Block_Plascrete.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.IV, 24),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 48),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.StainlessSteel, 16),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.StainlessSteel, 16),
                ItemList.Tool_DataStick.get(16),
                ItemList.Cover_Screen.get(4)
                )
            .itemOutputs(controllerTier1.get(1))
            .noOptimize()
            .eut(RECIPE_IV)
            .duration(20 * 32)
            .addTo(RecipeMaps.assemblerRecipes);

        //控制中心T2
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Block_Plascrete.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.LuV, 24),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 64),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnergeticAlloy, 16),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.EnergeticAlloy, 16),
                ItemList.Tool_DataOrb.get(32),
                ItemList.Cover_Screen.get(4)
            )
            .fluidInputs(Materials.PulsatingIron.getMolten(1152))
            .itemOutputs(controllerTier2.get(1))
            .noOptimize()
            .eut(RECIPE_LuV)
            .duration(20 * 32)
            .addTo(RecipeMaps.assemblerRecipes);

        //控制中心T3
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Block_Plascrete.get(1),
                GTOreDictUnificator.get(OrePrefixes.circuit, Materials.ZPM, 24),
                GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.AnnealedCopper, 64),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.EnergeticAlloy, 16),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.Sunnarium, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Sunnarium, 8),
                ItemList.Tool_DataOrb.get(48),
                ItemList.Cover_Screen.get(4)
            )
            .itemOutputs(controllerTier3.get(1))
            .fluidInputs(Materials.Sunnarium.getMolten(144))
            .noOptimize()
            .eut(RECIPE_ZPM)
            .duration(20 * 32)
            .addTo(RecipeMaps.assemblerRecipes);

        //凝固灭菌蛋白块
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Salt.getDust(64),Materials.MeatRaw.getDust(64))
            .itemOutputs(PROTEIN_BLOCK.get(16))
            .fluidInputs(GTModHandler.getDistilledWater(16000))//蒸馏水
            .noOptimize()
            .eut(RECIPE_EV)
            .duration(20 *25)
            .addTo(RecipeMaps.autoclaveRecipes);
    }
}
//GTValues.RA.stdBuilder()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);

