package com.perfectparitypg;

import com.perfectparitypg.entity.ModModelLayers;
import com.perfectparitypg.entity.monster.CreakingModel;
import com.perfectparitypg.particle.ModParticles;
import com.perfectparitypg.particle.TrailParticle;
import com.perfectparitypg.world.entity.ModBoats;
import com.perfectparitypg.world.level.block.ModBlocks;
import com.perfectparitypg.world.level.block.ModWoodTypes;
import com.terraformersmc.terraform.boat.api.client.TerraformBoatClientHelper;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;

public class PerfectParityPGClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        BlockRenderLayerMap.INSTANCE.putBlocks(RenderType.cutout(), ModBlocks.RESIN_CLUMP, ModBlocks.POTTED_PALE_OAK_SAPLING,
                ModBlocks.PALE_OAK_SAPLING, ModBlocks.PALE_OAK_LEAVES);


        TerraformBoatClientHelper.registerModelLayers(ModBoats.PALE_OAK_BOAT_ID, false);

        Sheets.getHangingSignMaterial(ModWoodTypes.PALE_OAK);

        EntityModelLayerRegistry.registerModelLayer(
                ModModelLayers.CREAKING,
                CreakingModel::createBodyLayer
        );
        ParticleFactoryRegistry.getInstance().register(ModParticles.TRAIL, TrailParticle.Provider::new);
    }
}