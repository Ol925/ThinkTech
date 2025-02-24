package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.item.*;

public class ThTItemLoader {

    public static void init() {
        ThTList.BRIQUETTE.set(new Briquette());
        ThTList.HALF_BRIQUETTE.set(new HalfBriquette());
        ThTList.METHANE_CLATHRATE.set(new MethaneClathrate());
        ThTList.SUPEROREO.set(new superOreo());
        ThTList.CRYSTALLINESUBSTRATE.set(new crystallineSubstrate());
    }
}
