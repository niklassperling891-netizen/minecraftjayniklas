package de.breedglow.mixin;

import de.breedglow.BreedGlowClient;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixin {

    /** Laesst Tiere (Client-seitig) leuchten, solange die Mod aktiv ist. */
    @Inject(method = "isCurrentlyGlowing", at = @At("HEAD"), cancellable = true)
    private void breedglow$forceGlow(CallbackInfoReturnable<Boolean> cir) {
        if (BreedGlowClient.shouldHighlight((Entity) (Object) this)) {
            cir.setReturnValue(true);
        }
    }

    /** Faerbt den Umriss: gruen = zuechtbar, rot = nicht zuechtbar. */
    @Inject(method = "getTeamColor", at = @At("HEAD"), cancellable = true)
    private void breedglow$color(CallbackInfoReturnable<Integer> cir) {
        Entity self = (Entity) (Object) this;
        if (BreedGlowClient.shouldHighlight(self)) {
            cir.setReturnValue(BreedGlowClient.colorFor(self));
        }
    }
}
