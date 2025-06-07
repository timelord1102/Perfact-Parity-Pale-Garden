package com.perfectparitypg.mixin;

import net.minecraft.server.packs.OverlayMetadataSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(OverlayMetadataSection.class)
public class PackOverlayMixin {
    @Inject(method = "overlaysForVersion", at = @At("RETURN"), cancellable = true)
    private void allowOverlays(int version, CallbackInfoReturnable<List<String>> cir) {
        OverlayMetadataSection self = (OverlayMetadataSection) (Object) this;

        List<String> compatibleOverlays = self.overlays().stream()
                .filter(entry -> entry.format().maxInclusive() <= 64) // ← only entries whose "formats" field includes 64
                .map(OverlayMetadataSection.OverlayEntry::overlay)
                .distinct()
                .toList();

        cir.setReturnValue(compatibleOverlays);
    }
}
