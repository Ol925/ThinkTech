package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.item.Briquette;
import com.OL925.ThinkTech.common.item.HalfBriquette;
import com.OL925.ThinkTech.common.item.MethaneClathrate;

public class IRItemLoader {

    public static void init() {
        IRItemList.BRIQUETTE.set(new Briquette());
        IRItemList.HALF_BRIQUETTE.set(new HalfBriquette());
        IRItemList.METHANE_CLATHRATE.set(new MethaneClathrate());
    }
}
