package com.perfectparitypg.entity.monster;

import com.perfectparitypg.entity.CreakingEyesLayer;
import com.perfectparitypg.entity.ModModelLayers;
import com.perfectparitypg.entity.creaking.Creaking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class CreakingRenderer<T extends Creaking> extends MobRenderer<Creaking, CreakingModel<Creaking>> {
    private static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking.png");
    private static final ResourceLocation EYES_TEXTURE_LOCATION = ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png");

    public CreakingRenderer(EntityRendererProvider.Context context) {
        super(context, new CreakingModel(context.bakeLayer(ModModelLayers.CREAKING)), 0.6F);
        this.addLayer(new CreakingEyesLayer(this));
    }


    public ResourceLocation getTextureLocation(Creaking creaking) {
        return TEXTURE_LOCATION;
    }
}
