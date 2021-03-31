package de.pfannekuchen.tasbattle.gui;

import java.awt.Color;

import de.pfannekuchen.tasbattle.networking.Client;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LobbyScreen extends Screen {

	public LobbyScreen() {
		super(LiteralText.EMPTY);
	}
	
	@Override
	protected void init() {
		// Add Clientside Buttons
		addButton(new ButtonWidget(5, height - 45, 204, 20, new LiteralText("<< Disconnect"), (b) -> {
			client.openScreen(new NewTitleScreen());
		}));
		
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		renderBackground(matrices);
		
		// Draw Outline of Player List
		DrawableHelper.fill(matrices, 4, 24, width / 3 * 2 + 1, height - 49, Color.lightGray.getRGB());
		DrawableHelper.fill(matrices, 5, 25, width / 3 * 2, height - 50, Color.black.getRGB());
		
		// Draw Connected Players from the List
		int playerlistY = 15;
		boolean drawGray = false;
		for (String player : Client.connectedPlayers) {
			drawStringWithShadow(matrices, textRenderer, player, 7, playerlistY += 12, ((drawGray = !drawGray) ? Color.LIGHT_GRAY.getRGB() : 0xFFFFFFFF));
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
	@Override
	public void renderBackground(MatrixStack matrices) {
		
		super.renderBackground(matrices);
	}
	
}
