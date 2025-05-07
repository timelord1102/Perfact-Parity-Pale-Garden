package com.perfectparitypg.entity.monster;

import com.perfectparitypg.entity.creaking.Creaking;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.HierarchicalModel;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import org.jetbrains.annotations.NotNull;

import java.util.List;

@Environment(EnvType.CLIENT)
public class CreakingModel<T extends Creaking> extends HierarchicalModel<T> {
    public static final List<ModelPart> NO_PARTS = List.of();
    private final ModelPart root;
    private final ModelPart head;
    private final List<ModelPart> headParts;

    public CreakingModel(ModelPart modelPart) {
        super(RenderType::entityCutoutNoCull);
        root = modelPart.getChild("root");
        ModelPart modelPart3 = root.getChild("upper_body");
        this.head = modelPart3.getChild("head");
        this.headParts = List.of(this.head);
    }

    private static MeshDefinition createMesh() {
        MeshDefinition meshDefinition = new MeshDefinition();
        PartDefinition partDefinition = meshDefinition.getRoot();
        PartDefinition partDefinition2 = partDefinition.addOrReplaceChild("root", CubeListBuilder.create(), PartPose.offset(0.0F, 24.0F, 0.0F));
        PartDefinition partDefinition3 = partDefinition2.addOrReplaceChild("upper_body", CubeListBuilder.create(), PartPose.offset(-1.0F, -19.0F, 0.0F));
        partDefinition3.addOrReplaceChild("head", CubeListBuilder.create().texOffs(0, 0).addBox(-3.0F, -10.0F, -3.0F, 6.0F, 10.0F, 6.0F).texOffs(28, 31).addBox(-3.0F, -13.0F, -3.0F, 6.0F, 3.0F, 6.0F).texOffs(12, 40).addBox(3.0F, -13.0F, 0.0F, 9.0F, 14.0F, 0.0F).texOffs(34, 12).addBox(-12.0F, -14.0F, 0.0F, 9.0F, 14.0F, 0.0F), PartPose.offset(-3.0F, -11.0F, 0.0F));
        partDefinition3.addOrReplaceChild("body", CubeListBuilder.create().texOffs(0, 16).addBox(0.0F, -3.0F, -3.0F, 6.0F, 13.0F, 5.0F).texOffs(24, 0).addBox(-6.0F, -4.0F, -3.0F, 6.0F, 7.0F, 5.0F), PartPose.offset(0.0F, -7.0F, 1.0F));
        partDefinition3.addOrReplaceChild("right_arm", CubeListBuilder.create().texOffs(22, 13).addBox(-2.0F, -1.5F, -1.5F, 3.0F, 21.0F, 3.0F).texOffs(46, 0).addBox(-2.0F, 19.5F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(-7.0F, -9.5F, 1.5F));
        partDefinition3.addOrReplaceChild("left_arm", CubeListBuilder.create().texOffs(30, 40).addBox(0.0F, -1.0F, -1.5F, 3.0F, 16.0F, 3.0F).texOffs(52, 12).addBox(0.0F, -5.0F, -1.5F, 3.0F, 4.0F, 3.0F).texOffs(52, 19).addBox(0.0F, 15.0F, -1.5F, 3.0F, 4.0F, 3.0F), PartPose.offset(6.0F, -9.0F, 0.5F));
        partDefinition2.addOrReplaceChild("left_leg", CubeListBuilder.create().texOffs(42, 40).addBox(-1.5F, 0.0F, -1.5F, 3.0F, 16.0F, 3.0F).texOffs(45, 55).addBox(-1.5F, 15.7F, -4.5F, 5.0F, 0.0F, 9.0F), PartPose.offset(1.5F, -16.0F, 0.5F));
        partDefinition2.addOrReplaceChild("right_leg", CubeListBuilder.create().texOffs(0, 34).addBox(-3.0F, -1.5F, -1.5F, 3.0F, 19.0F, 3.0F).texOffs(45, 46).addBox(-5.0F, 17.2F, -4.5F, 5.0F, 0.0F, 9.0F).texOffs(12, 34).addBox(-3.0F, -4.5F, -1.5F, 3.0F, 3.0F, 3.0F), PartPose.offset(-1.0F, -17.5F, 0.5F));
        return meshDefinition;
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshDefinition = createMesh();
        return LayerDefinition.create(meshDefinition, 64, 64);
    }

    @Override
    public void setupAnim(Creaking creaking, float f, float g, float h, float i, float j) {
        this.root().getAllParts().forEach(ModelPart::resetPose);
        this.animateHeadLookTarget(i, j);
        if (creaking.canMove()) {
            this.animateWalk(CreakingAnimation.CREAKING_WALK, creaking.walkAnimation.position(), creaking.walkAnimation.speed(), 1.0F, 1.0F);
        }

        this.animate(creaking.attackAnimationState, CreakingAnimation.CREAKING_ATTACK, creaking.tickCount);
        this.animate(creaking.invulnerabilityAnimationState, CreakingAnimation.CREAKING_INVULNERABLE, creaking.tickCount);
        this.animate(creaking.deathAnimationState, CreakingAnimation.CREAKING_DEATH, creaking.tickCount);
    }

    private void animateHeadLookTarget(float f, float g) {
        this.head.xRot = g * ((float)Math.PI / 180F);
        this.head.yRot = f * ((float)Math.PI / 180F);
    }

    public List<ModelPart> getHeadModelParts(Creaking creaking) {
        return !creaking.hasGlowingEyes() ? NO_PARTS : this.headParts;
    }

    @Override
    public @NotNull ModelPart root() {
        return this.root;
    }
}