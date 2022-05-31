package com.puradox.deathlocator;

import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Option;
import net.minecraft.server.dedicated.MinecraftDedicatedServer;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeathLocator implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger("deathlocator");
	private static String activeDeath = "";
	private static int textColour = 0xFFFFFFFF;
	private static int deathX;
	private static int deathChunkX;
	private static int deathY;
	private static int deathZ;
	private static int deathChunkZ;
	private static RegistryKey deathDimension;

	public static int despawnTicksRemaining = 0;
	public static boolean isPlayerInDeathRange;
	public static boolean isPlayerNearItems = false;

	private static ClientPlayerEntity thisPlayer;
	private static GameOptions options;

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
	}

	public static void setActiveDeath(int x, int y, int z, RegistryKey dimension) {
		deathX = x;
		deathY = y;
		deathZ = z;

		deathDimension = dimension;

		deathChunkX = deathX/16;
		deathChunkZ = deathZ/16;

		activeDeath = "Last Death: " + deathX + ", " + deathZ + ", " + deathZ;
	}

	public static void setTextColour(int colour) {
		textColour = colour;
	}

	public static void setDespawnTicksRemaining(int ticks) {
		despawnTicksRemaining = ticks;
	}

	public static String getUpdatedActiveDeath() {
		if (despawnTicksRemaining==0 || isPlayerNearItems) {activeDeath = "";}
		return activeDeath;
	}

	public static int getUpdatedTextColour() {
		updatePlayerNearness();
		if (isPlayerInDeathRange) {
			if (despawnTicksRemaining<1200) {setTextColour(0xFFFF0000);}
			else {setTextColour(0xFFFFFFFF);}
		}
		else {setTextColour(0xFF3F3F00);}
		return(textColour);
	}

	public static void updatePlayerNearness() {
		thisPlayer = MinecraftClient.getInstance().player;
		options = MinecraftClient.getInstance().options;
		int serverViewDistance = ((GameOptionsAccess)options).getServerViewDistance(); //Usually 10 for servers. Be sure client render distance is set to this amount.
		int currentChunkX = (int)(((ClientPlayerEntityAccess)thisPlayer).getLastX())/16;
		int currentChunkZ = (int)(((ClientPlayerEntityAccess)thisPlayer).getLastZ())/16;
		isPlayerNearItems = thisPlayer.world.getRegistryKey().equals(deathDimension) && currentChunkX == deathChunkX && currentChunkZ == deathChunkZ && !thisPlayer.isDead();
		if (thisPlayer.world.getRegistryKey().equals(deathDimension) && Math.abs(currentChunkX-deathChunkX)<serverViewDistance && Math.abs(currentChunkZ-deathChunkZ)<serverViewDistance) {
			isPlayerInDeathRange = true;
			return;
		}
		isPlayerInDeathRange=false;
	}
	public String getActiveDeath(){return new String(activeDeath);}
	public int getTextColour(){return(textColour);}

	public int getDeathX(){return(deathX);}
	public int getDeathChunkX(){return(deathChunkX);}
	public int getDeathY(){return(deathY);}
	public int getDeathZ(){return(deathZ);}
	public int getDeathChunkZ(){return(deathChunkZ);}
}
