package com.OL925.IndustrialReborn.common.init;

import com.OL925.IndustrialReborn.common.item.Briquette;
import com.OL925.IndustrialReborn.common.item.HalfBriquette;
import com.OL925.IndustrialReborn.common.item.MaybeOreo;
import com.OL925.IndustrialReborn.common.item.MethaneClathrate;

public class IRItemLoader {

    public static void init() {
        IRItemList.BRIQUETTE.set(new Briquette());
        IRItemList.HALF_BRIQUETTE.set(new HalfBriquette());
        IRItemList.METHANE_CLATHRATE.set(new MethaneClathrate());
        IRItemList.SUPEROREO.set(new MaybeOreo());
    }
}
