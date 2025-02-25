package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.item.*;

public class ThTItemLoader {

    public static void init() {
        ThTList.BRIQUETTE.set(new Briquette());
        ThTList.HALF_BRIQUETTE.set(new HalfBriquette());
        ThTList.METHANE_CLATHRATE.set(new MethaneClathrate());
        ThTList.SUPEROREO.set(new superOreo());
        ThTList.CRYSTALLINESUBSTRATE.set(new crystallineSubstrate());
        ThTList.CHIPTIER1.set(new HighComputingCowerChipTier1());
        ThTList.CHIPTIER2.set(new HighComputingCowerChipTier2());
        ThTList.CHIPTIER3.set(new HighComputingCowerChipTier3());
        ThTList.CHIPTIER4.set(new HighComputingCowerChipTier4());
    }
}
