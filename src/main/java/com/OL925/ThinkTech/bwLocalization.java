package com.OL925.ThinkTech;

import static com.OL925.ThinkTech.common.Material.ThTMaterial.*;
import static gregtech.api.util.GTLanguageManager.addStringLocalization;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Map;

import bartworks.system.material.Werkstoff;

/**
 * ThinkTech Werkstoff 本地化桥接。
 * <p>
 * 背景：Werkstoff 构造器通过 sAfterGTPreload 回调注入
 * {@code Material.<varNameLowered> → 缩写}，覆盖了 .lang 文件中的翻译。
 * 由于 ThinkTech 依赖 GT5U（required-before），GT5U 先加载，
 * ThinkTech 本类在 preInit 中后执行，将翻译"反击"回去。
 * <p>
 * 原理：Werkstoff 只覆盖 {@code Material.*} 键，不碰 {@code fluid.*} 和
 * {@code item.*} 键。因此本类从 ThTMaterial.TRANSLATION_REF 获取每个
 * Werkstoff 对应的 lang 翻译键，用 translateToLocalFormatted 读出 .lang
 * 文件的原始翻译，再写入 Werkstoff 实际使用的 {@code Material.*} 键。
 */
public class bwLocalization {

    public void loader() {
        // 第一步：从 .lang 的 fluid.*/item.* 键读出翻译 → 写入 Material.* 键
        for (Map.Entry<Werkstoff, String> entry : TRANSLATION_REF.entrySet()) {
            Werkstoff w = entry.getKey();
            String materialKey = w.getLocalizedNameKey();
            String translation = translateToLocalFormatted(entry.getValue());
            addStringLocalization(materialKey, translation);
        }

        // 第二步：熔融流体走 fluid.molten.* 体系，此时 Material.* 已写入正确翻译
        for (Werkstoff w : THT_MOLTEN_WERKSTOFFS) {
            String moltenKey = "fluid.molten." + w.getVarName().toLowerCase();
            addStringLocalization(moltenKey, translateToLocalFormatted(w.getLocalizedNameKey()));
        }
    }
}
