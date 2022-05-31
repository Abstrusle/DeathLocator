package com.puradox.deathlocator.mixin;

import com.puradox.deathlocator.DeathLocator;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftClient.class)
public abstract class TickMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    public void tick(CallbackInfo ci) {
        if(DeathLocator.despawnTicksRemaining > 0 && DeathLocator.isPlayerInDeathRange) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                DeathLocator.despawnTicksRemaining--;
            }
                else if (!(MinecraftClient.getInstance().currentScreen.shouldPause() && MinecraftClient.getInstance().isIntegratedServerRunning())) {
                DeathLocator.despawnTicksRemaining--;
            }
        }
    }
}
