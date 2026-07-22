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
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.WerkstoffMaterialPool;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

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

        //立方氧化锆
        GTValues.RA.stdBuilder()
                .itemInputs(WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 10))
                .fluidInputs(Materials.Oxygen.getGas(1000 * 20))
                .itemOutputs(WerkstoffLoader.CubicZirconia.get(OrePrefixes.gem, 30))
                .eut(RECIPE_HV)
                .duration(20 * 40)
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

        //焦炉砖
        GTValues.RA.stdBuilder()
                .itemInputs(
                        Materials.Clay.getDust(16),
                        GTModHandler.getModItem("minecraft", "sand", 32, 1) // Red Sand meta=1
                )
                .itemOutputs(ItemList.CokeOvenCasing.get(64))
                .eut(0)
                .duration(20 * 20)
                .addTo(ThTRecipeMap.Kiln);

        //焦炉砖
        GTValues.RA.stdBuilder()
                .itemInputs(
                        Materials.Clay.getDust(16),
                        GTModHandler.getModItem("minecraft", "sand", 32, 0) // Sand meta=0
                )
                .itemOutputs(ItemList.CokeOvenCasing.get(64))
                .eut(0)
                .duration(20 * 20)
                .addTo(ThTRecipeMap.Kiln);

        // 焦黑砖
        GTValues.RA.stdBuilder()
                .itemInputs(
                        GTModHandler.getModItem("minecraft", "sand", 16, 0),
                        GTModHandler.getModItem("minecraft", "gravel", 16, 0)
                )
                .itemOutputs(GTModHandler.getModItem("TConstruct", "Smeltery", 16, 2))
                .eut(0)
                .duration(20 * 16)
                .addTo(ThTRecipeMap.Kiln);

        // 焦黑砖（红沙版本）
        GTValues.RA.stdBuilder()
                .itemInputs(
                        GTModHandler.getModItem("minecraft", "sand", 16, 1),
                        GTModHandler.getModItem("minecraft", "gravel", 16, 0)
                )
                .itemOutputs(GTModHandler.getModItem("TConstruct", "Smeltery", 16, 2))
                .eut(0)
                .duration(20 * 16)
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
            .itemInputs(CHIPTIER1.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*4),
                WerkstoffLoader.Neon.getFluidOrGas(4000*4),
                Materials.Helium.getGas(2000*4),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*4),
                WerkstoffLoader.Xenon.getFluidOrGas(500*4))
            .eut(RECIPE_EV)
            .duration(20 * 90)
            .circuit(1)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //二阶富集 ZPM
        GTValues.RA.stdBuilder()
            .itemInputs(CHIPTIER2.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*8),
                WerkstoffLoader.Neon.getFluidOrGas(4000*8),
                Materials.Helium.getGas(2000*8),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*8),
                WerkstoffLoader.Xenon.getFluidOrGas(500*32))
            .eut(RECIPE_LuV)
            .duration(20 * 90)
            .circuit(2)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 UHV
        GTValues.RA.stdBuilder()
            .itemInputs(CHIPTIER3.get(0))
            .fluidOutputs(
                Materials.Argon.getGas(8000*32),
                WerkstoffLoader.Neon.getFluidOrGas(4000*32),
                Materials.Helium.getGas(2000*32),
                WerkstoffLoader.Krypton.getFluidOrGas(1000*32),
                WerkstoffLoader.Xenon.getFluidOrGas(500*128))
            .eut(RECIPE_ZPM)
            .duration(20 * 90)
            .circuit(3)
            .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 下届空气
        GTValues.RA.stdBuilder()
                .itemInputs(CHIPTIER3.get(0), Materials.Netherrack.getDust(16))
                .fluidInputs(Materials.Ice.getSolid(1000L))
                .fluidOutputs(Materials.NetherSemiFluid.getFluid(160000))
                .eut(RECIPE_ZPM)
                .duration(20 * 5)
                .circuit(4)
                .addTo(ThTRecipeMap.NobleGasEnrichmentSystem);

        //三阶富集 Og
        GTValues.RA.stdBuilder()
                .itemInputs(GregtechItemList.Compressed_Fusion_Reactor.get(0), CHIPTIER3.get(0))
                .fluidOutputs(WerkstoffLoader.Oganesson.getFluidOrGas(64000))
                .eut(RECIPE_ZPM)
                .duration(20 * 150)
                .circuit(4)
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

        //钻井
        //岩浆
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.Lava.getFluid(24000))
                .eut(RECIPE_MV)
                .duration(20 * 5)
                .circuit(1)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);

        //天然气
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.NaturalGas.getGas(18000))
                .eut(RECIPE_MV)
                .duration(70)
                .circuit(2)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);
        //原油
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.OilMedium.getFluid(18000))
                .eut(RECIPE_HV)
                .duration(20 * 5)
                .circuit(3)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);
        //轻油
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.OilLight.getFluid(22000))
                .eut(RECIPE_HV)
                .duration(20 * 3)
                .circuit(4)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);
        //重油
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.OilHeavy.getFluid(14000))
                .eut(RECIPE_HV)
                .duration(20 * 7)
                .circuit(5)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);
        //极重油
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.OilExtraHeavy.getFluid(24000))
                .eut(RECIPE_IV)
                .duration(20 * 8)
                .circuit(6)
                .specialValue(1)
                .addTo(ThTRecipeMap.DrillingRig);
        //盐水
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.SaltWater.getFluid(16000))
                .eut(RECIPE_EV)
                .duration(20 * 4)
                .circuit(7)
                .specialValue(2)
                .addTo(ThTRecipeMap.DrillingRig);
        //蒸馏水
        GTValues.RA.stdBuilder()
                .fluidOutputs(GTModHandler.getDistilledWater(24000))
                .eut(RECIPE_HV)
                .duration(20 * 4)
                .circuit(8)
                .specialValue(2)
                .addTo(ThTRecipeMap.DrillingRig);
        //氯苯
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.Chlorobenzene.getFluid(20000))
                .eut(RECIPE_EV)
                .duration(20 * 8)
                .circuit(9)
                .specialValue(3)
                .addTo(ThTRecipeMap.DrillingRig);
        //硫酸
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.SulfuricAcid.getFluid(18000))
                .eut(RECIPE_EV)
                .duration(20 * 10)
                .circuit(10)
                .specialValue(3)
                .addTo(ThTRecipeMap.DrillingRig);
        //不明液体
        GTValues.RA.stdBuilder()
                .fluidOutputs(FluidRegistry.getFluidStack("unknowwater", 12000))
                .eut(RECIPE_LuV)
                .duration(20 * 10)
                .circuit(11)
                .specialValue(5)
                .addTo(ThTRecipeMap.DrillingRig);
        //熔融铁
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.Iron.getMolten(12000))
                .eut(RECIPE_LuV)
                .duration(20 * 16)
                .circuit(12)
                .specialValue(4)
                .addTo(ThTRecipeMap.DrillingRig);
        //熔融铅
        GTValues.RA.stdBuilder()
                .fluidOutputs(Materials.Lead.getMolten(14000))
                .eut(RECIPE_LuV)
                .duration(20 * 16)
                .circuit(11)
                .specialValue(4)
                .addTo(ThTRecipeMap.DrillingRig);
        //末影粘浆
        GTValues.RA.stdBuilder()
                .fluidOutputs(FluidRegistry.getFluidStack("endergoo", 12000))
                .eut(RECIPE_EV)
                .duration(20 * 8)
                .circuit(14)
                .specialValue(3)
                .addTo(ThTRecipeMap.DrillingRig);

        // ==================== 坩埚配方 ====================

        // 朱砂 → 液态汞
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cinnabar.getDust(1))
            .fluidOutputs(Materials.Mercury.getFluid(1000))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 熔融铁/铁粉 + 氧气 → 液态钢
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iron.getMolten(144), Materials.Oxygen.getGas(1000))
            .fluidOutputs(Materials.Steel.getMolten(144))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(1))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .fluidOutputs(Materials.Steel.getMolten(144))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 铁矿石
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnetite.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BandedIron.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BrownLimonite.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.YellowLimonite.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Pyrite.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 孔雀石
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Malachite.getDust(1))
            .fluidOutputs(Materials.Copper.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 黄铜矿
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Chalcopyrite.getDust(2))
            .fluidOutputs(Materials.Copper.getMolten(144), Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 熔融铁/铁粉
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iron.getMolten(144))
            .itemInputs(Materials.Coal.getDust(1))
            .fluidOutputs(Materials.CastIron.getMolten(144))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(1), Materials.Coal.getDust(1))
            .fluidOutputs(Materials.CastIron.getMolten(144))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
                .fluidInputs(Materials.Iron.getMolten(144))
                .itemInputs(Materials.Carbon.getDust(1))
                .fluidOutputs(Materials.CastIron.getMolten(144))
                .eut(30).duration(50).circuit(2)
                .specialValue(1)
                .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
                .itemInputs(Materials.Iron.getDust(1), Materials.Carbon.getDust(1))
                .fluidOutputs(Materials.CastIron.getMolten(144))
                .eut(30).duration(100).circuit(2)
                .specialValue(1)
                .addTo(ThTRecipeMap.Crucible);

        // 闪锌矿
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Sphalerite.getDust(1))
            .itemOutputs(Materials.Gallium.getDust(1))
            .outputChances(800)
            .fluidOutputs(Materials.Zinc.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 黝铜矿
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tetrahedrite.getDust(8))
            .fluidOutputs(Materials.Copper.getMolten(432), Materials.Iron.getMolten(144),
                Materials.Antimony.getMolten(144))
            .eut(30).duration(150).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 辉锑矿
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Stibnite.getDust(1))
            .fluidOutputs(Materials.Antimony.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 锡石矿/锡石矿砂
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Cassiterite.getDust(1))
            .fluidOutputs(Materials.Tin.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.CassiteriteSand.getDust(1))
            .fluidOutputs(Materials.Tin.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 锡粉/金粉
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tin.getDust(1))
            .fluidOutputs(Materials.Tin.getMolten(144))
            .eut(16).duration(50).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Gold.getDust(1))
            .fluidOutputs(Materials.Gold.getMolten(144))
            .eut(16).duration(50).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 镍黄铁矿
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Pentlandite.getDust(17))
            .fluidOutputs(Materials.Nickel.getMolten(1296))
            .eut(30).duration(200).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 花岗岩矿砂/玄武岩矿砂
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.GraniticMineralSand.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BasalticMineralSand.getDust(1))
            .fluidOutputs(Materials.Iron.getMolten(144))
            .eut(30).duration(100).circuit(1)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // ==================== 合金配方 ====================

        // 红色合金 RedAlloy
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(1), Materials.Redstone.getDust(4))
            .fluidOutputs(Materials.RedAlloy.getMolten(144))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Copper.getMolten(144))
            .itemInputs(Materials.Redstone.getDust(4))
            .fluidOutputs(Materials.RedAlloy.getMolten(144))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 红石合金 RedstoneAlloy
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Silicon.getDust(1),Materials.Carbon.getDust(1),Materials.Redstone.getDust(1))
            .fluidOutputs(Materials.RedstoneAlloy.getMolten(432))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 青铜 Bronze
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tin.getDust(1), Materials.Copper.getDust(3))
            .fluidOutputs(Materials.Bronze.getMolten(576))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tin.getMolten(144), Materials.Copper.getMolten(432))
            .fluidOutputs(Materials.Bronze.getMolten(576))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 白铜
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(1), Materials.Nickel.getDust(1))
            .fluidOutputs(Materials.Cupronickel.getMolten(288))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Copper.getMolten(144), Materials.Nickel.getMolten(144))
            .fluidOutputs(Materials.Cupronickel.getMolten(288))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 殷钢
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(2), Materials.Nickel.getDust(1))
            .fluidOutputs(Materials.Invar.getMolten(432))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iron.getMolten(288), Materials.Nickel.getMolten(144))
            .fluidOutputs(Materials.Invar.getMolten(432))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 琥珀金
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Silver.getDust(1), Materials.Gold.getDust(1))
            .fluidOutputs(Materials.Electrum.getMolten(288))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Silver.getMolten(144), Materials.Gold.getMolten(144))
            .fluidOutputs(Materials.Electrum.getMolten(288))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 焊锡
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tin.getDust(9), Materials.Antimony.getDust(1))
            .fluidOutputs(Materials.SolderingAlloy.getMolten(1440))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Tin.getMolten(1296), Materials.Antimony.getMolten(144))
            .fluidOutputs(Materials.SolderingAlloy.getMolten(1440))
            .eut(30).duration(50).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 钴黄铜 CobaltBrass: 7黄铜 + 1钴 + 1锡 → 9×144 = 1296L
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Brass.getDust(7), Materials.Cobalt.getDust(1), Materials.Tin.getDust(1))
            .fluidOutputs(Materials.CobaltBrass.getMolten(1296))
            .eut(30).duration(100).circuit(3)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 镁铝合金 Magnalium: 1镁 + 2铝 → 3×144 = 432L
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Magnesium.getDust(1), Materials.Aluminium.getDust(2))
            .fluidOutputs(Materials.Magnalium.getMolten(432))
            .eut(30).duration(100).circuit(2)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 退火铜 AnnealedCopper: 1铜 + 1000L 氧气
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(1))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .fluidOutputs(Materials.AnnealedCopper.getMolten(144))
            .eut(30).duration(100).circuit(11)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // Potin: 2铅 + 1锡 + 2青铜 → 5×144 = 720L
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Lead.getDust(2), Materials.Tin.getDust(1), Materials.Bronze.getDust(2))
            .fluidOutputs(MaterialsAlloy.POTIN.getFluidStack(720))
            .eut(30).duration(100).circuit(13)
            .specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 玄钢
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Steel.getMolten(144))
            .itemInputs(Materials.Coal.getDust(4), Materials.Silicon.getDust(1), Materials.Obsidian.getDust(3))
            .fluidOutputs(Materials.DarkSteel.getMolten(1296))
            .eut(60).duration(200).circuit(14)
            .specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 魂金
        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getModItem("minecraft", "soul_sand", 1, 0),
                Materials.Gold.getDust(1), Materials.Ash.getDust(1))
            .fluidOutputs(Materials.Soularium.getMolten(432))
            .eut(60).duration(200).circuit(2)
            .specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 熔融铝
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminiumoxide.getDust(10), Materials.Cryolite.getDust(5))
            .fluidOutputs(Materials.Aluminium.getMolten(576))
            .eut(60).duration(200)
            .specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // ==================== 高级配方 v7 (specialValue 2-5) ====================

        // ---- specialValue(2) ----

        // 导电铁+黑钢+金 → 充能合金
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.ConductiveIron.getDust(1), Materials.BlackSteel.getDust(1),
                Materials.Gold.getDust(1))
            .fluidOutputs(Materials.EnergeticAlloy.getMolten(432))
            .eut(30).duration(300).circuit(3).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 末影之眼+铬+充能合金 → 充能银合金
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.EnderEye.getDust(1), Materials.Chrome.getDust(1),
                Materials.EnergeticAlloy.getDust(1))
            .fluidOutputs(Materials.VibrantAlloy.getMolten(432))
            .eut(30).duration(300).circuit(3).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 钽铁矿 → 锰 + 钽
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tantalite.getDust(9))
            .fluidOutputs(Materials.Manganese.getMolten(144), Materials.Tantalum.getMolten(288))
            .eut(30).duration(300).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 钽粉 → 熔融钽
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tantalum.getDust(1))
            .fluidOutputs(Materials.Tantalum.getMolten(144))
            .eut(30).duration(300).specialValue(2).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 红石合金+铁+银 → 导电铁
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.RedstoneAlloy.getDust(1), Materials.Iron.getDust(1),
                Materials.Silver.getDust(1))
            .fluidOutputs(Materials.ConductiveIron.getMolten(432))
            .eut(30).duration(300).circuit(3).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 铜+琥珀金 → 黑铜
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(3), Materials.Electrum.getDust(2))
            .fluidOutputs(Materials.BlackBronze.getMolten(720))
            .eut(30).duration(300).circuit(2).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 黑铜+银+钢 → 黑钢
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.BlackBronze.getDust(1), Materials.Silver.getDust(1),
                Materials.Steel.getDust(3))
            .fluidOutputs(Materials.BlackSteel.getMolten(720))
            .eut(30).duration(300).circuit(3).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // ---- specialValue(3) ----

        // 铝粉 → 熔融铝
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Aluminium.getDust(1))
            .fluidOutputs(Materials.Aluminium.getMolten(144))
            .eut(30).duration(400).specialValue(3).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 熔融铁+铝+铬 → 坎塔尔合金
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iron.getMolten(144), Materials.Aluminium.getMolten(144),
                Materials.Chrome.getMolten(144))
            .fluidOutputs(Materials.Kanthal.getMolten(432))
            .eut(30).duration(400).circuit(3).specialValue(3)
            .addTo(ThTRecipeMap.Crucible);

        // 铬粉 → 熔融铬
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Chrome.getDust(1))
            .fluidOutputs(Materials.Chrome.getMolten(144))
            .eut(30).duration(400).specialValue(3).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 铂金属粉 → 熔融铂
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Platinum.getDust(9))
            .fluidOutputs(Materials.Platinum.getMolten(144))
            .eut(30).duration(400).specialValue(3).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 银+蓝石 → 蓝色合金
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Silver.getDust(1), Materials.Electrotine.getDust(4))
            .fluidOutputs(Materials.BlueAlloy.getMolten(144))
            .eut(30).duration(400).circuit(2).specialValue(3)
            .addTo(ThTRecipeMap.Crucible);

        // ---- specialValue(4) ----

        // 熔融镍+铬 → 镍铬合金
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Nickel.getMolten(576), Materials.Chrome.getMolten(144))
            .fluidOutputs(Materials.Nichrome.getMolten(144))
            .eut(30).duration(500).circuit(2).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // 铁+镍+锰+铬 → 不锈钢
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iron.getDust(6), Materials.Nickel.getDust(1),
                Materials.Manganese.getDust(1), Materials.Chrome.getDust(1))
            .fluidOutputs(Materials.StainlessSteel.getMolten(1296))
            .eut(30).duration(500).circuit(6).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // ---- specialValue(5) ----

        // 钛粉 → 熔融钛
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Titanium.getDust(1))
            .fluidOutputs(Materials.Titanium.getMolten(144))
            .eut(30).duration(600).specialValue(5).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 金红石+碳 → 钛
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Rutile.getDust(2), Materials.Carbon.getDust(2))
            .fluidOutputs(Materials.Titanium.getMolten(144))
            .eut(30).duration(600).circuit(2).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        // 钨粉 → 熔融钨
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tungsten.getDust(1))
            .fluidOutputs(Materials.Tungsten.getMolten(144))
            .eut(30).duration(600).specialValue(5).circuit(1)
            .addTo(ThTRecipeMap.Crucible);

        // 钨+钢 → 钨钢
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Tungsten.getDust(1), Materials.Steel.getDust(1))
            .fluidOutputs(Materials.TungstenSteel.getMolten(288))
            .eut(30).duration(600).circuit(2).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        // ==================== v8 高级合金配方 ====================

        // ---- specialValue(2) 新增 ----

        // 红钢（100秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(4), Materials.Silver.getDust(4),
                Materials.Bismuth.getDust(1), Materials.Zinc.getDust(1),
                Materials.BlackSteel.getDust(20), Materials.Steel.getDust(10))
            .fluidOutputs(Materials.RedSteel.getMolten(5760))
            .eut(30).duration(2000).circuit(15).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 蓝钢（100秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(19), Materials.Gold.getDust(16),
                Materials.Zinc.getDust(5), Materials.BlackSteel.getDust(80),
                Materials.Steel.getDust(40))
            .fluidOutputs(Materials.BlueSteel.getMolten(23040))
            .eut(30).duration(2000).circuit(16).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // 钒钢（15秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Vanadium.getDust(1), Materials.Chrome.getDust(1),
                Materials.Steel.getDust(7))
            .fluidOutputs(Materials.VanadiumSteel.getMolten(1296))
            .eut(30).duration(300).circuit(3).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // ---- specialValue(4) 新增 ----

        // 高速钢-G（50秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.TungstenSteel.getDust(5), Materials.Chrome.getDust(1),
                Materials.Molybdenum.getDust(2), Materials.Vanadium.getDust(1))
            .fluidOutputs(Materials.HSSG.getMolten(1296))
            .eut(30).duration(1000).circuit(4).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // 高速钢-E（60秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HSSG.getDust(6), Materials.Cobalt.getDust(1),
                Materials.Manganese.getDust(1), Materials.Silicon.getDust(1))
            .fluidOutputs(Materials.HSSE.getMolten(1296))
            .eut(30).duration(1200).circuit(4).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // 高速钢-S（60秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HSSG.getDust(6), Materials.Iridium.getDust(2),
                Materials.Osmium.getDust(1))
            .fluidOutputs(Materials.HSSS.getMolten(1296))
            .eut(30).duration(1200).circuit(3).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // 黄铜（10秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(3), Materials.Zinc.getDust(1))
            .fluidOutputs(Materials.Brass.getMolten(576))
            .eut(30).duration(200).circuit(2).specialValue(1)
            .addTo(ThTRecipeMap.Crucible);

        // 玫瑰金（10秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Copper.getDust(1), Materials.Gold.getDust(4))
            .fluidOutputs(Materials.RoseGold.getMolten(720))
            .eut(30).duration(200).circuit(2).specialValue(2)
            .addTo(ThTRecipeMap.Crucible);

        // TPV合金（10秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Titanium.getDust(3), Materials.Platinum.getDust(3),
                Materials.Vanadium.getDust(1))
            .fluidOutputs(Materials.TPV.getMolten(1008))
            .eut(30).duration(200).circuit(3).specialValue(4)
            .addTo(ThTRecipeMap.Crucible);

        // ---- specialValue(5) 新增 ----

        // PGM 熔化（6个配方，20秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Platinum.getDust(1))
            .fluidOutputs(Materials.Platinum.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Rhodium.get(OrePrefixes.dust, 1))
            .fluidOutputs(WerkstoffLoader.Rhodium.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Palladium.getDust(1))
            .fluidOutputs(Materials.Palladium.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.Ruthenium.get(OrePrefixes.dust, 1))
            .fluidOutputs(WerkstoffLoader.Ruthenium.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Osmium.getDust(1))
            .fluidOutputs(Materials.Osmium.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Iridium.getDust(1))
            .fluidOutputs(Materials.Iridium.getMolten(144))
            .eut(30).duration(400).circuit(1).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        // 镀铑钯（40秒）
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Palladium.getMolten(432), WerkstoffLoader.Rhodium.getMolten(144))
            .fluidOutputs(WerkstoffLoader.RhodiumPlatedPalladium.getMolten(576))
            .eut(30).duration(800).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        // 铱锇合金（40秒）
        GTValues.RA.stdBuilder()
            .fluidInputs(Materials.Iridium.getMolten(432), Materials.Osmium.getMolten(144))
            .fluidOutputs(Materials.Osmiridium.getMolten(576))
            .eut(30).duration(800).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);

        // 铌钛合金（40秒）
        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Niobium.getDust(1), Materials.Titanium.getDust(1))
            .fluidOutputs(Materials.NiobiumTitanium.getMolten(288))
            .eut(30).duration(800).circuit(2).specialValue(5)
            .addTo(ThTRecipeMap.Crucible);
    }
}
