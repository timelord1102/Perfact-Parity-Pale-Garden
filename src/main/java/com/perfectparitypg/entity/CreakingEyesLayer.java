package com.perfectparitypg.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.perfectparitypg.entity.creaking.Creaking;
import com.perfectparitypg.entity.monster.CreakingModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class CreakingEyesLayer<T extends Creaking>
        extends RenderLayer<T, CreakingModel<T>>{

    private static final RenderType CREAKING_EYES = RenderType.eyes(ResourceLocation.withDefaultNamespace("textures/entity/creaking/creaking_eyes.png"));

    public CreakingEyesLayer(RenderLayerParent<T, CreakingModel<T>> renderLayerParent) {
        super(renderLayerParent);
    }


    @Override
    public void render(PoseStack poseStack,
                       MultiBufferSource buffer,
                       int packedLight,
                       T creaking,
                       float limbSwing, float limbSwingAmount,
                       float partialTicks,
                       float ageInTicks,
                       float netHeadYaw, float headPitch)
    {
        /* <‑‑ ONLY if the mob is currently “active”  */
        if (!creaking.isActive()) return;

        VertexConsumer vc = buffer.getBuffer(CREAKING_EYES);
        this.getParentModel()
                .renderToBuffer(poseStack, vc, packedLight,
                        OverlayTexture.NO_OVERLAY);
    }
}
