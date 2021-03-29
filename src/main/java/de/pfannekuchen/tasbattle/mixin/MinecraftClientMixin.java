package de.pfannekuchen.tasbattle.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

import com.mojang.authlib.minecraft.OfflineSocialInteractions;
import com.mojang.authlib.minecraft.SocialInteractionsService;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;

import de.pfannekuchen.tasbattle.gui.NewTitleScreen;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	/**
	 * Remove ugly 'Failed to verify authentication' Message, because it is going to show every single time.
	 * @author Pancake
	 */
	@Overwrite
	private SocialInteractionsService method_31382(YggdrasilAuthenticationService yggdrasilAuthenticationService, RunArgs runArgs) {
		/* Original Code: 
		 try { return yggdrasilAuthenticationService.createSocialInteractionsService(runArgs.network.session.getAccessToken()); } catch (AuthenticationException var4) {
		 	LOGGER.error((String) "Failed to verify authentication", (Throwable) var4); return new OfflineSocialInteractions(); } */
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

}
