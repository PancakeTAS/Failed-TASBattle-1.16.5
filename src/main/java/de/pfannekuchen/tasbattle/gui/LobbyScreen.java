package de.pfannekuchen.tasbattle.gui;

import java.awt.Color;

import de.pfannekuchen.tasbattle.networking.Client;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LobbyScreen extends Screen {

	public LobbyScreen() {
		super(LiteralText.EMPTY);
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		
		// Draw Outline of Player List
		DrawableHelper.fill(matrices, 4, 24, width / 3 * 2 + 1, height - 49, Color.lightGray.getRGB());
		DrawableHelper.fill(matrices, 5, 25, width / 3 * 2, height - 50, Color.black.getRGB());
		
		// Draw Connected Players from the List
		int playerlistY = 26;
		boolean drawGray = false;
		if (Client.connectedPlayers.isEmpty()) drawCenteredString(matrices, textRenderer, "Looks like you are alone.. :(", width / 3 / 2, height / 2 + 25, 0xFFFFFF);
		else {
			for (String player : Client.connectedPlayers) {
				drawStringWithShadow(matrices, textRenderer, player, 26, playerlistY += 12, ((drawGray = !drawGray) ? Color.lightGray.getRGB() : 0xFFFFFFFF));
			}
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
