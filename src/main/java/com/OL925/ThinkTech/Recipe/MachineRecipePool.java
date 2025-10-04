package com.OL925.ThinkTech.Recipe;

import bartworks.system.material.WerkstoffLoader;
import com.OL925.ThinkTech.common.Material.ThTMaterial;
import com.OL925.ThinkTech.common.init.ThTList;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.fluids.GTPPFluids;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import static com.OL925.ThinkTech.common.init.ThTList.*;
import static goodgenerator.items.GGMaterial.naquadahine;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.enums.TierEU.*;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

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
            .eut(RECIPE_IV)
            .duration(20*125)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //铕掺杂
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Europium, 0,false))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot4.get(16))
            .fluidInputs(Materials.Europium.getMolten(4608),
                Materials.GalliumArsenide.getMolten(9216),
                Materials.SiliconTetrachloride.getFluid(128000),
                Materials.Hydrogen.getGas(256000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(128000))
            .eut(RECIPE_IV)
            .duration(20*690)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //镅掺杂
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.CRYSTALLINESUBSTRATE.get(0),
                GTOreDictUnificator.get(OrePrefixes.dust, Materials.Americium, 0,false))
            .itemOutputs(ItemList.Circuit_Silicon_Ingot5.get(16))
            .fluidInputs(Materials.Americium.getMolten(9216),
                Materials.GalliumArsenide.getMolten(18432),
                Materials.SiliconTetrachloride.getFluid(256000),
                Materials.Hydrogen.getGas(512000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(256000))
            .eut(RECIPE_LuV)
            .duration(20*1000)
            .addTo(ThTRecipeMap.CzochralskiSingleCrystalFurnace);
        //合成氨
        GTValues.RA.stdBuilder()
            .itemInputs(ThTList.IRON_CATALYST.get(0))
            .fluidInputs(Materials.Hydrogen.getGas(240000),Materials.Nitrogen.getGas(80000))
            .fluidOutputs(Materials.Ammonia.getGas(80000))
            .eut(RECIPE_EV)
            .duration(20 * 120)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //叠氮化钠
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sodium.getDust(64),Materials.Sodium.getDust(64))
            .itemOutputs(GTUtility.copyAmountUnsafe(64 * 2,ThTMaterial.sodiumAzide.get(OrePrefixes.dust)))
            .fluidInputs(Materials.Ammonia.getGas(32000 * 4),Materials.Nitrogen.getGas(16000 *4))
            .fluidOutputs(Materials.Hydrogen.getGas(48000 * 4))
            .eut(RECIPE_EV)
            .duration(20 * 8)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //耐火砖 一块砖要6个耐火砖2个石膏粉,即3砖粉3粘土粉2石膏粉
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Gypsum.getDust(64),
                Materials.Brick.getDust(64),
                Materials.Brick.getDust(32),
                Materials.Clay.getDust(32))
            .itemOutputs(ItemList.Casing_Firebricks.get(32))
            .eut(0)
            .duration(20 * 44)
            .addTo(ThTRecipeMap.Kiln);

        //红砖 Lignite
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Clay.getDust(64),Materials.Clay.getDust(64),Materials.Coal.getGems(64))
            .itemOutputs(Materials.Brick.getIngots(64),Materials.Brick.getIngots(64))
            .eut(0)
            .duration(20 * 48)
            .addTo(ThTRecipeMap.Kiln);


        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Clay.getDust(64),Materials.Clay.getDust(64),Materials.Coal.getDust(64))
            .itemOutputs(Materials.Brick.getIngots(64),Materials.Brick.getIngots(64))
            .eut(0)
            .duration(20 * 30)
            .addTo(ThTRecipeMap.Kiln);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Clay.getDust(64),Materials.Clay.getDust(64),Materials.Charcoal.getGems(64))
            .itemOutputs(Materials.Brick.getIngots(64),Materials.Brick.getIngots(64))
            .eut(0)
            .duration(20 * 48)
            .addTo(ThTRecipeMap.Kiln);


        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Clay.getDust(64),Materials.Clay.getDust(64),Materials.Lignite.getGems(64))
            .itemOutputs(Materials.Brick.getIngots(64),Materials.Brick.getIngots(64))
            .eut(0)
            .duration(20 * 60)
            .addTo(ThTRecipeMap.Kiln);

        //被动稀有气体富集 EV
//        GTValues.RA.stdBuilder()
//            .itemInputs(GTUtility.getIntegratedCircuit(1))
//            .fluidOutputs(
//                Materials.Argon.getGas(8000),
//                WerkstoffLoader.Neon.getFluidOrGas(4000),
//                Materials.Helium.getGas(2000),
//                WerkstoffLoader.Krypton.getFluidOrGas(1000),
//                WerkstoffLoader.Xenon.getFluidOrGas(500))
//            .noOptimize()
//            .eut(0)
//            .duration(20 * 160)
//            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //一阶富集 IV
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(1),CHIPTIER1.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*4),
                WerkstoffLoader.Neon.getFluidOrGas(4000*4),
                Materials.Helium.getGas(2000*4),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*4),
                WerkstoffLoader.Xenon.getFluidOrGas(500*4))
            .eut(RECIPE_EV)
            .duration(20 * 90)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //二阶富集 ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(2),CHIPTIER2.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*8),
                WerkstoffLoader.Neon.getFluidOrGas(4000*8),
                Materials.Helium.getGas(2000*8),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*8),
                WerkstoffLoader.Xenon.getFluidOrGas(500*32))
            .eut(RECIPE_LuV)
            .duration(20 * 90)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 UHV
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.getIntegratedCircuit(3),CHIPTIER3.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*32),
                WerkstoffLoader.Neon.getFluidOrGas(4000*32),
                Materials.Helium.getGas(2000*32),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*32),
                WerkstoffLoader.Xenon.getFluidOrGas(500*128))
            .eut(RECIPE_ZPM)
            .duration(20 * 90)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 下届空气
        GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(4),CHIPTIER3.get(0), Materials.Netherrack.getDust(16))
                .fluidInputs(Materials.Ice.getSolid(1000L))
                .fluidOutputs(Materials.NetherSemiFluid.getFluid(160000))
                .eut(RECIPE_ZPM)
                .duration(20 * 5)
                .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 Og
        GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.getIntegratedCircuit(4), GregtechItemList.Compressed_Fusion_Reactor.get(0),CHIPTIER3.get(0))
                .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(64000))
                .eut(RECIPE_ZPM)
                .duration(20 * 150)
                .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //预培养维生细菌液
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Calcium.getDust(12),
                PROTEIN_BLOCK.get(8),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 8, 2),
                Materials.Sugar.getDust(64))
            .fluidInputs(Materials.Glycerol.getFluid(32000),
                Materials.Ethanol.getFluid(16000))
            .fluidOutputs(ThTMaterial.PreculturedBacterialSolution.getFluidOrGas(1000))
            .eut(RECIPE_EV)
            .duration(20 *40)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //预培养维生冷藏细菌液处理
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem(GalaxySpace.ID, "item.UnknowCrystal", 4),
                Materials.Osmiridium.getDust(1),
                PROTEIN_BLOCK.get(16))
            .itemOutputs(GTUtility.copyAmountUnsafe(320,ItemList.Circuit_Chip_Stemcell.get(64)))
            .fluidInputs(ThTMaterial.FreezedPreculturedBacterialSolution.getFluidOrGas(100),
                Materials.GrowthMediumSterilized.getFluid(800))
            .fluidOutputs(ThTMaterial.RawBioSludge.getFluidOrGas(1000))
            .eut(RECIPE_LuV)
            .duration(20 * 115)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //待处理浓缩菌泥
        GTValues.RA.stdBuilder()
            .itemOutputs(Materials.Calcium.getDust(1),
                Materials.Salt.getDust(1),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "GTNHBioItems", 8, 0),
                GTModHandler.getModItem(GalaxySpace.ID, "item.UnknowCrystal", 4))
            .outputChances(10000,10000,10000,890)
            .fluidInputs(ThTMaterial.RawBioSludge.getFluidOrGas(1000),
                Materials.Glyceryl.getFluid(150),
                Materials.Ethanol.getFluid(850))
            .fluidOutputs(getFluidStack("bacterialsludge", 4500))
            .eut(RECIPE_HV)
            .duration(20 * 35)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //黄铁矿富集出硒
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Carbon.getDust(4),
                Materials.Pyrite.getDust(128))
            .itemOutputs(MaterialsElements.getInstance().SELENIUM.getDust(42))//硒粉
            .fluidInputs(GTModHandler.getDistilledWater(24000),
                Materials.HydricSulfide.getGas(12000))
            .fluidOutputs(Materials.Water.getFluid(24000))
            .eut(RECIPE_EV)
            .duration(20 * 24)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //硼砂处理
        GTValues.RA.stdBuilder()
                .itemInputs(Materials.SodiumHydroxide.getDust(64),Materials.SodiumHydroxide.getDust(64),Materials.SodiumHydroxide.getDust(64),
                        Materials.SodiumCarbonate.getDust(64),Materials.SodiumCarbonate.getDust(32))
                .itemOutputs(GTUtility.copyAmountUnsafe(24,Materials.Salt.getDust(1)),
                        GTUtility.copyAmountUnsafe(736,Materials.Borax.getDust(64)))
                .fluidInputs(Materials.SaltWater.getFluid(128000), new FluidStack(GTPPFluids.Kerosene, 6000))
                .eut(RECIPE_IV)
                .duration(20 * 64)
                .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //光刻胶
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialsElements.getInstance().IODINE.getDust(1))
            .fluidInputs(Materials.Oxygen.getGas(2000),
                Materials.Chlorine.getGas(1000),
                Materials.Ethylene.getGas(1000),
                Materials.Benzene.getFluid(1000))
            .fluidOutputs(ThTMaterial.Photoresist.getFluidOrGas(1000))
            .eut(RECIPE_IV)
            .duration(20 * 40)
            .addTo(ThTRecipeMap.GeneralChemicalFactory);

        //光刻胶预处理五种晶圆
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer.get(1))
            .fluidInputs(ThTMaterial.Photoresist.getFluidOrGas(200))
            .itemOutputs(PWAFER.get(1))
            .eut(RECIPE_LV)
            .duration(20 * 25)
            .addTo(RecipeMaps.laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer2.get(1))
            .fluidInputs(ThTMaterial.Photoresist.getFluidOrGas(200))
            .itemOutputs(PWAFER.get(1))
            .eut(RECIPE_MV)
            .duration(20 * 25)
            .addTo(RecipeMaps.laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer3.get(1))
            .fluidInputs(ThTMaterial.Photoresist.getFluidOrGas(200))
            .itemOutputs(PWAFER.get(1))
            .eut(RECIPE_EV)
            .duration(20 * 25)
            .addTo(RecipeMaps.laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer4.get(1))
            .fluidInputs(ThTMaterial.Photoresist.getFluidOrGas(200))
            .itemOutputs(PWAFER.get(1))
            .eut(RECIPE_IV)
            .duration(20 * 25)
            .addTo(RecipeMaps.laserEngraverRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Circuit_Silicon_Wafer5.get(1))
            .fluidInputs(ThTMaterial.Photoresist.getFluidOrGas(200))
            .itemOutputs(PWAFER.get(1))
            .eut(RECIPE_LuV)
            .duration(20 * 25)
            .addTo(RecipeMaps.laserEngraverRecipes);
    }
}
//GTValues.RA.stdBuilder()
//            .noOptimize()
//            .eut(RECIPE_)
//            .duration()
//            .addTo(RecipeMaps.);
