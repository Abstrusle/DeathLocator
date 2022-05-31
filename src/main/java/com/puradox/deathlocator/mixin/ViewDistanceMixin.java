package com.puradox.deathlocator.mixin;

import com.puradox.deathlocator.GameOptionsAccess;
import net.minecraft.client.option.GameOptions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(GameOptions.class)
public abstract class ViewDistanceMixin implements GameOptionsAccess {
    @Shadow
    int serverViewDistance;
    public int getServerViewDistance(){return serverViewDistance;}
}
