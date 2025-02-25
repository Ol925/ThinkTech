package com.OL925.ThinkTech.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

public enum ThTList {

    // Item
    BRIQUETTE,
    HALF_BRIQUETTE,
    METHANE_CLATHRATE,
    SUPEROREO,
    CRYSTALLINESUBSTRATE,

    CHIPTIER1,
    CHIPTIER2,
    CHIPTIER3,
    CHIPTIER4,

    // Machine
    ExplosiveGenerator,
    CzochralskiSingleCrystalFurnace,

    TestItem;

    private boolean mHasNotBeenSet;

    private ItemStack sStack;

    ThTList() {
        mHasNotBeenSet = true;
    }

    public ThTList set(Item aItem) {
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        sStack = GTUtility.copyAmountUnsafe(1, aStack);
        return this;
    }

    public ThTList set(ItemStack aStack) {
        if (aStack != null) {
            sStack = GTUtility.copyAmountUnsafe(1, aStack);
        }
        return this;
    }

    public Item getItem() {
        if (GTUtility.isStackInvalid(sStack)) return null;
        return sStack.getItem();
    }

    public ItemStack get(int aAmount, Object... temp) {
        // if invalid, return a replacements
        if (GTUtility.isStackInvalid(sStack)) {
            return GTUtility.copyAmountUnsafe(aAmount, TestItem.get(1));
        }
        return GTUtility.copyAmountUnsafe(aAmount, sStack);
    }

    public boolean hasBeenSet() {
        return !mHasNotBeenSet;
    }
}
