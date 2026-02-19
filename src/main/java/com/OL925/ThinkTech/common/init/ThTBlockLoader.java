package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.block.ThTCasings0;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

public class ThTBlockLoader {

    public static Block thtCasings0 = new ThTCasings0();

    public static void init()
    {
        // 继续复用你原本的 ThTList 三个枚举项，但改成 ItemStack 指向不同 meta
        ThTList.controllerTier1.set(new ItemStack(thtCasings0, 1, 0));
        ThTList.controllerTier2.set(new ItemStack(thtCasings0, 1, 1));
        ThTList.controllerTier3.set(new ItemStack(thtCasings0, 1, 2));

    }
}
