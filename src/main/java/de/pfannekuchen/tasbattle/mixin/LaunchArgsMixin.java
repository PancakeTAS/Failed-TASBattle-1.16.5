package de.pfannekuchen.tasbattle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.authlib.properties.PropertyMap;

import net.minecraft.client.RunArgs;
import net.minecraft.client.RunArgs.AutoConnect;
import net.minecraft.client.RunArgs.Game;
import net.minecraft.client.RunArgs.Network;
import net.minecraft.client.main.Main;
import net.minecraft.client.util.Session;

/**
 * 
 * Removes autoconnect and login from the launch arguments.
 * 
 * @author Pancake
 */
@Mixin(Main.class)
public class LaunchArgsMixin {

	/** Replace RunArgs with new ones that don't have any Session and Auto-Connect. */
	@ModifyArg(method = "main", index = 0, at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;<init>(Lnet/minecraft/client/RunArgs;)V"))
	private static RunArgs modifyRunArgs(RunArgs original) {
		return new RunArgs(new Network(new Session(original.network.session.getUsername(), original.network.session.getUuid(), "", ""), new PropertyMap(), new PropertyMap(), original.network.netProxy), 
				original.windowSettings, 
				original.directories,
				new Game(false, "1.16.5-TASBattle", "release", true, false), 
				new AutoConnect(null, 0));
	}
	
}
