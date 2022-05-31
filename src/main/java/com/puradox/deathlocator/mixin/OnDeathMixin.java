package com.puradox.deathlocator.mixin;

import com.mojang.authlib.GameProfile;
import com.puradox.deathlocator.ClientPlayerEntityAccess;
import com.puradox.deathlocator.DeathLocator;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.hud.DebugHud;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class OnDeathMixin extends AbstractClientPlayerEntity implements ClientPlayerEntityAccess {

	@Shadow private double lastX;
	@Shadow private double lastBaseY;
	@Shadow private double lastZ;

	@Override
	public double getLastX() {return lastX;}
	@Override
	public double getLastZ() {return lastZ;}

	public OnDeathMixin(ClientWorld world, GameProfile profile) {
		super(world, profile);
	}

	@Inject(at = @At("TAIL"), method = "updateHealth")
	protected void updateHealth(float health, CallbackInfo ci) {
		if (this.getHealth() <= 0) {
			DeathLocator.setDespawnTicksRemaining(6000);
			DeathLocator.setActiveDeath((int)lastX, (int)lastBaseY, (int)lastZ, this.world.getRegistryKey());
		}
	}
}
