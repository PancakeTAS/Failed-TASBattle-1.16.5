package de.pfannekuchen.tasbattle.gui;

import java.util.function.Supplier;

import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class QueueScreen extends Screen {

	public static String queue = "";
	public static String[] kits = new String[0];
	public static String[] maps = new String[0];
	
	/* Fake World List without entries to draw background */
	WorldListWidget widget;
	
	protected QueueScreen() {
		super(LiteralText.EMPTY);
	}
	
	@Override
	protected void init() {
		widget = new WorldListWidget(null, client, width, height, 16, height - 24, 20, null, null) {
			@Override
			public void filter(Supplier<String> supplier, boolean load) {
				
			}
		};
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		widget.render(matrices, mouseX, mouseY, delta); // Render Background
		
		// Draw Text
		drawCenteredString(matrices, client.textRenderer, queue + " Queue", width / 2, 4, 0xFFFFFFFF);
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
