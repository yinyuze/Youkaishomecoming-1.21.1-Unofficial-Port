package dev.xkmc.youkaishomecoming.content.pot.base;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.utility.TextUtils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Base screen for kettle / moka / other pot-like GUIs.
 * <p>
 * The vanilla recipe-book widget is intentionally omitted here: {@link BasePotMenu} no longer
 * extends {@code RecipeBookMenu} (that caused REI's exclusion-zone code to NPE), and the widget
 * itself was largely redundant with JEI/REI/EMI.
 */
public abstract class BasePotScreen<T extends BasePotMenu<R>, R extends BasePotRecipe> extends AbstractContainerScreen<T> {

	public BasePotScreen(T screenContainer, Inventory inv, Component titleIn) {
		super(screenContainer, inv, titleIn);
	}

	public abstract Rectangle getHeatIcon();

	public abstract Rectangle getProgressArrow();

	public abstract ResourceLocation getBackgroundTexture();

	@Override
	public void init() {
		super.init();
		titleLabelX = 8;
	}

	@Override
	public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
		super.render(gui, mouseX, mouseY, partialTicks);
		renderMealDisplayTooltip(gui, mouseX, mouseY);
		renderHeatIndicatorTooltip(gui, mouseX, mouseY);
	}

	private void renderHeatIndicatorTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (isHovering(getHeatIcon().x, getHeatIcon().y, getHeatIcon().width, getHeatIcon().height, mouseX, mouseY)) {
			String key = "container.cooking_pot." + (menu.isHeated() ? "heated" : "not_heated");
			gui.renderTooltip(font, TextUtils.getTranslation(key), mouseX, mouseY);
		}
	}

	protected void renderMealDisplayTooltip(GuiGraphics gui, int mouseX, int mouseY) {
		if (minecraft != null && minecraft.player != null && menu.getCarried().isEmpty() && hoveredSlot != null && hoveredSlot.hasItem()) {
			if (hoveredSlot.index == BasePotBlockEntity.MEAL_DISPLAY_SLOT) {
				List<Component> tooltip = new ArrayList<>();
				ItemStack mealStack = hoveredSlot.getItem();
				tooltip.add(((MutableComponent) mealStack.getItem().getDescription()).withStyle(mealStack.getRarity().getStyleModifier()));
				ItemStack containerStack = menu.blockEntity.getContainer();
				String container = !containerStack.isEmpty() ? containerStack.getItem().getDescription().getString() : "";
				tooltip.add(TextUtils.getTranslation("container.cooking_pot.served_on", container).withStyle(ChatFormatting.GRAY));
				gui.renderComponentTooltip(font, tooltip, mouseX, mouseY);
			} else {
				gui.renderTooltip(font, hoveredSlot.getItem(), mouseX, mouseY);
			}
		}
	}

	@Override
	protected void renderLabels(GuiGraphics gui, int mouseX, int mouseY) {
		gui.drawString(this.font, this.title, this.titleLabelX, this.titleLabelY, 4210752, false);
		gui.drawString(font, playerInventoryTitle, 8, imageHeight - 96 + 2, 4210752, false);
	}

	@Override
	protected void renderBg(GuiGraphics gui, float partialTicks, int mouseX, int mouseY) {
		RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
		if (minecraft != null) {
			gui.blit(getBackgroundTexture(), leftPos, topPos, 0, 0, imageWidth, imageHeight);
			if (menu.isHeated()) {
				gui.blit(getBackgroundTexture(), leftPos + getHeatIcon().x, topPos + getHeatIcon().y, 176, 0, getHeatIcon().width, getHeatIcon().height);
			}
			int l = (int) (menu.getCookProgressionScaled() * getProgressArrow().width);
			gui.blit(getBackgroundTexture(), leftPos + getProgressArrow().x, topPos + getProgressArrow().y, 176, getHeatIcon().height, l + 1, getProgressArrow().height);
		}
	}
}
