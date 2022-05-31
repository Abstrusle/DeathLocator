package com.puradox.deathlocator;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.server.command.CommandManager;

public class DeathLocatorClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {

        ClientCommandManager.DISPATCHER.register(ClientCommandManager.literal("cleardeath").executes(context -> {
                    DeathLocator.setDespawnTicksRemaining(0);
                    return 1;
                }));
        HudRenderCallback.EVENT.register((matrixStack, tickDelta) -> {

            TextRenderer deathCoordsRenderer = MinecraftClient.getInstance().textRenderer;
            deathCoordsRenderer.draw(matrixStack, DeathLocator.getUpdatedActiveDeath(), (float) (MinecraftClient.getInstance().getWindow().getScaledHeight()*0.01), (float) (MinecraftClient.getInstance().getWindow().getScaledHeight()*0.9), DeathLocator.getUpdatedTextColour());
        });
    }
}
