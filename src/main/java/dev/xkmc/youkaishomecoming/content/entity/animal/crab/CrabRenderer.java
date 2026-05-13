package dev.xkmc.youkaishomecoming.content.entity.animal.crab;

import dev.xkmc.youkaishomecoming.init.GensokyoLegacy;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.ItemInHandLayer;
import net.minecraft.resources.ResourceLocation;

public class CrabRenderer extends MobRenderer<CrabEntity, CrabModel> {

	public static final ResourceLocation NORMAL = GensokyoLegacy.loc("textures/entities/crab/crab.png");
	public static final ResourceLocation MUD = GensokyoLegacy.loc("textures/entities/crab/mud_crab.png");

	public CrabRenderer(EntityRendererProvider.Context ctx) {
		super(ctx, new CrabModel(ctx.bakeLayer(CrabModel.LAYER_LOCATION)), 0f);
		addLayer(new ItemInHandLayer<>(this, ctx.getItemInHandRenderer()));
	}

	@Override
	public ResourceLocation getTextureLocation(CrabEntity e) {
		return switch (e.prop.getVariant()) {
			case MUD -> MUD;
			default -> NORMAL;
		};
	}

}
