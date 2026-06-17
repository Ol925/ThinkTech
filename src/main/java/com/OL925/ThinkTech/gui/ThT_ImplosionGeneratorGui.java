package com.OL925.ThinkTech.gui;

import com.OL925.ThinkTech.common.MTE.ThT_ImplosionGenerator;
import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.api.widget.IWidget;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.LongSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ListWidget;
import com.gtnewhorizons.modularui.api.NumberFormatMUI;

import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;

public class ThT_ImplosionGeneratorGui extends MTEMultiBlockBaseGui<ThT_ImplosionGenerator> {

    private static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    public ThT_ImplosionGeneratorGui(ThT_ImplosionGenerator multiblock) {
        super(multiblock);
    }

    @Override
    protected ListWidget<IWidget, ?> createTerminalTextWidget(PanelSyncManager syncManager, ModularPanel parent) {
        LongSyncValue displayGeneratingSyncer = new LongSyncValue(
            ThT_ImplosionGenerator::getDisplayGenerating,
            ThT_ImplosionGenerator::setDisplayGenerating);
        syncManager.syncValue("displayGenerating", displayGeneratingSyncer);

        return super.createTerminalTextWidget(syncManager, parent)
            .child(
                IKey.dynamic(() -> "Last EU generating(eu/tick):"
                    + numberFormat.format(displayGeneratingSyncer.getValue()))
                    .color(Color.WHITE.main)
                    .asWidget()
                    .textAlign(Alignment.CenterLeft)
                    .setEnabledIf(w -> multiblock.getErrorDisplayID() == 0)
                    .marginBottom(2)
                    .fullWidth());
    }
}
