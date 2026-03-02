package com.starstag.skyforgemining.screen;

import com.starstag.skyforgemining.SkyforgeMining;
import com.starstag.skyforgemining.menu.MasteryInfuserMenu;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;

public class MasteryInfuserScreen extends AbstractContainerScreen<MasteryInfuserMenu> {

    private static final ResourceLocation TEXTURE =
            new ResourceLocation(SkyforgeMining.MODID, "textures/gui/mastery_infuser.png");

    public MasteryInfuserScreen(MasteryInfuserMenu menu, Inventory inv, Component title) {
        super(menu, inv, title);
        this.imageWidth = 176;
        this.imageHeight = 166; // changed from 186
    }

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        renderBackground(graphics);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}