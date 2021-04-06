package de.pfannekuchen.tasbattle.gui;

import java.awt.Color;
import java.util.function.Supplier;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class LobbyScreen extends Screen {
	
	public String showInformation = null;
	/* Fake World List without entries to draw background */
	WorldListWidget widget;
	
	public LobbyScreen() {
		super(LiteralText.EMPTY);
	}
	
	private boolean isMouseOver(int x, int y, double mouseX, double mouseY) {
		return mouseX >= (double) x && mouseY >= (double) y && mouseX < (double) (x + this.width) && mouseY < (double) (y + this.height);
	}
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		if (isMouseOver(buttons.get(0).x, buttons.get(0).y, mouseX, mouseY)) {
			if (!buttons.get(0).isFocused()) buttons.get(0).changeFocus(true);
			showInformation = "ffa";
		}
		super.mouseMoved(mouseX, mouseY);
	}
	
	@Override
	protected void init() {
		int offsetX = width / 7 * 2 + 5;
		int size = (width / 7 * 5) - 6;
		addButton(new ButtonWidget(offsetX + (size / 4), height - 54, size / 2, 20, new LiteralText("Join FFA"), (b) -> { // TODO: .lang File
			client.openScreen(new QueueScreen());
		}));
		// Add Clientside Buttons
		addButton(new ButtonWidget(5, height - 22, 150, 20, new LiteralText("<< Disconnect"), (b) -> {
			client.openScreen(new NewTitleScreen());
		}));
		widget = new WorldListWidget(null, client, width, height, 16, height - 24, 20, null, null) {
			@Override
			public void filter(Supplier<String> supplier, boolean load) {
				
			}
		};
		super.init();
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		widget.render(matrices, mouseX, mouseY, delta); // Render Background
		
		// Draw Outline of Player List
		DrawableHelper.fill(matrices, 4, 24, width / 7 * 2 + 1, height - 34, Color.lightGray.getRGB());
		DrawableHelper.fill(matrices, 5, 25, width / 7 * 2, height - 35, Color.black.getRGB());
		
		
		// Draw Text
		drawCenteredString(matrices, client.textRenderer, "TAS Battle Server Hub", width / 2, 4, 0xFFFFFFFF);
		
		// Draw Game Information
		int offsetX = width / 7 * 2 + 4;
		if (showInformation != null) {
			switch (showInformation) {
				case "ffa":
					drawCenteredString(matrices, textRenderer, "There is a game running/Currently there are", offsetX + ((width - offsetX) / 2), 80, 0xFFFFFF);
					drawCenteredString(matrices, textRenderer, "with x players/x players in the queue", offsetX + ((width -offsetX) / 2), 90, 0xFFFFFF);
					break;
			}
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
