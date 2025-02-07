package com.OL925.ThinkTech.common.init;

import com.OL925.ThinkTech.common.item.Briquette;
import com.OL925.ThinkTech.common.item.HalfBriquette;
import com.OL925.ThinkTech.common.item.MethaneClathrate;
import com.OL925.ThinkTech.common.item.superOreo;

public class ThTItemLoader {

    public static void init() {
        ThTList.BRIQUETTE.set(new Briquette());
        ThTList.HALF_BRIQUETTE.set(new HalfBriquette());
        ThTList.METHANE_CLATHRATE.set(new MethaneClathrate());
        ThTList.SUPEROREO.set(new superOreo());

    }
}
