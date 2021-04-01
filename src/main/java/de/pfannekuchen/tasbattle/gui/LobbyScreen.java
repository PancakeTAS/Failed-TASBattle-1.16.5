package de.pfannekuchen.tasbattle.gui;

import java.awt.Color;
import java.util.function.Supplier;

import de.pfannekuchen.tasbattle.networking.Client;
import de.pfannekuchen.tasbattleserver.packets.JoinQueuePacket;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LobbyScreen extends Screen {

	public String showInformation = null;
	
	public LobbyScreen() {
		super(LiteralText.EMPTY);
	}
	
	private boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
		return mouseX >= (double) x && mouseY >= (double) y && mouseX < (double) (x + this.width) && mouseY < (double) (y + this.height);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		if (isMouseOver(buttons.get(0).x, buttons.get(0).y, mouseX, mouseY)) {
			buttons.get(0).changeFocus(true);
			showInformation = "ffa";
		}
		if (isMouseOver(buttons.get(1).x, buttons.get(1).y, mouseX, mouseY)) {
			//buttons.get(1).changeFocus(true);
			showInformation = "skywars";
		}
		if (isMouseOver(buttons.get(2).x, buttons.get(2).y, mouseX, mouseY)) {
			//buttons.get(2).changeFocus(true);
			showInformation = "bedwars";
		}
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	protected void init() {
		int offsetX = width / 7 * 2 + 4;
		int size = (width / 7 * 5 + 4) / 3 - 6;
		addButton(new ButtonWidget(offsetX + 1, height - 54, size, 20, new LiteralText("Queue FFA"), (b) -> {
			try {
				Client.sendPacket(new JoinQueuePacket("ffa"));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}));
		addButton(new ButtonWidget(offsetX + size + 4, height - 54, size, 20, new LiteralText("Queue Skywars"), (b) -> {
			
		})).active = false;
		addButton(new ButtonWidget(offsetX + size + size + 7, height - 54, size, 20, new LiteralText("Queue Bedwars"), (b) -> {
					
		})).active = false;
		// Add Clientside Buttons
		addButton(new ButtonWidget(5, height - 22, 150, 20, new LiteralText("<< Disconnect"), (b) -> {
			try {
				Client.disconnect();
			} catch (Exception e) {
				e.printStackTrace();
			}
			client.openScreen(new NewTitleScreen());
		}));
		widget = new WorldListWidget(null, client, width, height, 16, height - 24, 20, null, null) {
			@Override
			public void filter(Supplier<String> supplier, boolean load) {
			}
		};
		super.init();
	}
	
	
	WorldListWidget widget;
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		widget.render(matrices, mouseX, mouseY, delta); // Render Background
		
		// Draw Outline of Player List
		DrawableHelper.fill(matrices, 4, 24, width / 7 * 2 + 1, height - 34, Color.lightGray.getRGB());
		DrawableHelper.fill(matrices, 5, 25, width / 7 * 2, height - 35, Color.black.getRGB());
		
		// Draw Connected Players from the List
		int playerlistY = 15;
		boolean drawGray = false;
		for (String player : Client.connectedPlayers.players) {
			drawStringWithShadow(matrices, textRenderer, player, 7, playerlistY += 12, ((drawGray = !drawGray) ? Color.LIGHT_GRAY.getRGB() : 0xFFFFFFFF));
		}
		
		// Draw Text
		drawCenteredString(matrices, client.textRenderer, "TAS Battle Server Hub", width / 2, 4, 0xFFFFFFFF);
		
		// Draw Game Information
		
		int offsetX = width / 7 * 2 + 4;
		if (showInformation != null) {
			switch (showInformation) {
			case "ffa":
				drawStringWithShadow(matrices, textRenderer, "Current Map: " + Client.settings.ffaMap, offsetX, 30, 0xFFFFFF);
				drawStringWithShadow(matrices, textRenderer, "Current Tickrate: " + Client.settings.ffaTickrate, offsetX, 40, 0xFFFFFF);
				drawCenteredString(matrices, textRenderer, (Client.connectedPlayers.queueFFA > 99) ? "There is a game running" : "Currently there are", offsetX + ((width - offsetX) / 2), 80, 0xFFFFFF);
				drawCenteredString(matrices, textRenderer, (Client.connectedPlayers.queueFFA > 99) ? "with x players".replaceFirst("x", (Client.connectedPlayers.queueFFA - 100) + "") : "x players in the queue".replaceFirst("x", Client.connectedPlayers.queueFFA + ""), offsetX + ((width -offsetX) / 2), 90, 0xFFFFFF);
				
				 if (Client.shouldFFA) drawCenteredString(matrices, textRenderer, "\u00A76FFA Game is starting in x seconds".replaceFirst("x", Client.ffaWhen + ""), offsetX + ((width -offsetX) / 2), 150, 0xFFFFFF);
				break;
			default:
				drawCenteredString(matrices, textRenderer, "Looks like this Gamemode", offsetX + ((width - offsetX) / 2), 80, 0xFFFFFF);
				drawCenteredString(matrices, textRenderer, "isn't available yet. :(", offsetX + ((width -offsetX) / 2), 90, 0xFFFFFF);
				break;
			}
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
