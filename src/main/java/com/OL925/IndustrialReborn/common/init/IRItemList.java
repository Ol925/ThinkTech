package com.OL925.IndustrialReborn.common.init;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

public enum IRItemList {

    BRIQUETTE,
    HALF_BRIQUETTE,
    METHANE_CLATHRATE,
    SUPEROREO,

    TestItem;

    private boolean mHasNotBeenSet;

    private ItemStack sStack;

    IRItemList() {
        mHasNotBeenSet = true;
    }

    public IRItemList set(Item aItem) {
        if (aItem == null) return this;
        ItemStack aStack = new ItemStack(aItem, 1, 0);
        sStack = GTUtility.copyAmountUnsafe(1, aStack);
        return this;
    }

    public IRItemList set(ItemStack aStack) {
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
