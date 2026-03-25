package com.benbenlaw.cloche.screen.cloche;

import com.benbenlaw.cloche.Cloche;
import com.benbenlaw.core.Core;
import com.benbenlaw.core.screen.util.DurationTooltip;
import com.benbenlaw.core.screen.util.TooltipArea;
import com.benbenlaw.core.util.MouseUtil;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class ClocheScreen extends AbstractContainerScreen<ClocheMenu> {
    private static final Identifier TEXTURE = Cloche.identifier("textures/gui/cloche.png");
    private static final Identifier PROGRESS_ARROW = Core.identifier("progress_arrow");

    public ClocheScreen(ClocheMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float a) {
        super.extractBackground(guiGraphics, mouseX, mouseY, a);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, x, y, 0, 0, imageWidth, imageHeight, 256, 256);

        if (menu.isCrafting()) {
            guiGraphics.blitSprite(RenderPipelines.GUI_TEXTURED, PROGRESS_ARROW, 24, 16, 0, 0, x + 49, y + 25, menu.getScaledProgress() + 1, 16);
        }
    }

    @Override
    public void extractRenderState(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTick) {
        super.extractRenderState(guiGraphics, mouseX, mouseY, partialTick);

        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        renderSlotTooltips(guiGraphics, mouseX, mouseY, x, y);
        DurationTooltip.renderDurationTooltip(guiGraphics, mouseX, mouseY, x, y, 155, 5, menu.data.get(0), menu.data.get(1));
    }

    private void renderSlotTooltips(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, int x, int y) {
        List<TooltipArea> tooltipAreas = new ArrayList<>();

        tooltipAreas.add(new TooltipArea(8, 17, 16, 16, "tooltip.cloche.seed_slot"));
        tooltipAreas.add(new TooltipArea(8, 35, 16, 16, "tooltip.cloche.soil_slot"));
        tooltipAreas.add(new TooltipArea(35, 53, 16, 16, "tooltip.cloche.upgrade_slot"));
        tooltipAreas.add(new TooltipArea(53, 53, 16, 16, "tooltip.cloche.upgrade_slot"));
        tooltipAreas.add(new TooltipArea(71, 53, 16, 16, "tooltip.cloche.upgrade_slot"));

        if (menu.numberOfCatalysts == 0) {
            tooltipAreas.add(new TooltipArea(8, 53, 16, 16, "tooltip.cloche.catalyst_slot_no_catalysts"));
        } else {
            tooltipAreas.add(new TooltipArea(8, 53, 16, 16, "tooltip.cloche.catalyst_slot"));
        }

        for (TooltipArea area : tooltipAreas) {
            if (MouseUtil.isMouseAboveArea(mouseX, mouseY, x, y, area.offsetX, area.offsetY, area.width, area.height)) {
                if (this.menu.getCarried().isEmpty() && this.hoveredSlot != null && !this.hoveredSlot.hasItem()) {
                    Component text = Component.translatable(area.translationKey);
                    FormattedCharSequence sequence = text.getVisualOrderText();
                    List<ClientTooltipComponent> tooltipLines = List.of(ClientTooltipComponent.create(sequence));
                    guiGraphics.tooltip(this.font, tooltipLines, mouseX, mouseY, DefaultTooltipPositioner.INSTANCE, null );
                }
            }
        }
    }


}
