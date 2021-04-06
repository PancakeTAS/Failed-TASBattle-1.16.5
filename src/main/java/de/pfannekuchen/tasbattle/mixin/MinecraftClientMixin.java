package de.pfannekuchen.tasbattle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import de.pfannekuchen.tasbattle.gui.NewTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.world.ClientWorld;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	private ClientWorld world;

	/**
	 * Remove ugly 'Failed to verify authentication' Message, because it is going to show every single time.
	 * @author Pancake
	 */
	@Overwrite
	private SocialInteractionsService method_31382(YggdrasilAuthenticationService yggdrasilAuthenticationService, RunArgs runArgs) {
		// try { yggdrasilAuthenticationService.createSocialInteractionsService(runArgs.network.session.getAccessToken());} catch (Exception var4) {} 
		return new OfflineSocialInteractions(); // Always return this, because it is always gonna happen.
	}
	
	/**
	 * Redirect TitleScreen to NewTitleScreen
	 * @author Pancake
	 */
	@ModifyArg(method = "<init>", at = @At(value = "INVOKE", target = "openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", ordinal = 1), index = 0)
	public Screen redirectOpenScreen(Screen original) {
		return new NewTitleScreen();
	}
	

	/** Redirects the Main Menu to the NewTitleScreen */
	@ModifyVariable(at = @At("HEAD"), method = "openScreen")
	public Screen redirectMainMenu(Screen screen) {
		return (screen == null) ? ((world == null) ? new NewTitleScreen() : screen) : ((screen instanceof TitleScreen) ? new NewTitleScreen() : screen);
	}

}
