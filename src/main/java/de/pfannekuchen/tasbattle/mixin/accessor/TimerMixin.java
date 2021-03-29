package de.pfannekuchen.tasbattle.mixin.accessor;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderTickCounter;

/**
 * 
 * Gives Public Access to renderTickCounter to change the Tickrate later.
 * 
 * @author Pancake
 */
@Mixin(MinecraftClient.class)
public interface TimerMixin {

	@Accessor("renderTickCounter")
	public RenderTickCounter timer();
	
}
