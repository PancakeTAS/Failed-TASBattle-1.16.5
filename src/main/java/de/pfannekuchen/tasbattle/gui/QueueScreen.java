package de.pfannekuchen.tasbattle.gui;

import java.awt.Color;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Supplier;

import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.world.WorldListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

public class QueueScreen extends Screen {

	/** Settings for the Queue */
	public static String queue = "FFA";
	public static String[] kits = new String[] {"Crystals", "Rainbow", "Oldschool", "Tactical", "Test"};
	public static String[] maps = new String[] {"2b2t", "The Nile", "Beauty to Ashes", "Rave Map", "Void", "Ship", "Minigolf", "Snowy"};
	public static String[] players = new String[] {"Chmm3 [Offline]", "Pancake"};
	public static int selectedKit = 0;
	public static int selectedMap = 0;
	
	/* Offset of the Kits button */
	public static int offsetKit;
	/* Offset of the Maps button */
	public static int offsetMaps;
	
	/* Fake World List without entries to draw background */
	WorldListWidget widget;
	
	/* Move by X */
	int distanceM;
	/* Move by X */
	int distanceK;
	
	@Override
	public void mouseMoved(double mouseX, double mouseY) {
		distanceM = 0;
		if (mouseY > 25 && mouseY < 55) {
			if (mouseX > (width - 50)) distanceM = (int) -(mouseX - (width - 50));
			else if (mouseX < 50) distanceM = (int) (50 - mouseX);
		}
		distanceK = 0;
		if (mouseY > 58 && mouseY < 88) {
			if (mouseX > (width - 50)) distanceK = (int) -(mouseX - (width - 50));
			else if (mouseX < 50) distanceK = (int) (50 - mouseX);
		}
 	}
	
	protected QueueScreen() {
		super(LiteralText.EMPTY);
		final Timer timer = new Timer();
		timer.scheduleAtFixedRate(new TimerTask() {
			
			@Override
			public void run() {
				if ((buttons.get(offsetKit + kits.length - 1).x + (distanceM / 5)) < 420 - 110) return;
				if (buttons.get(offsetKit).x + (distanceM / 5) > 20) return;
				for (int i = 0; i < kits.length; i++) {
					buttons.get(offsetKit + i).x += (distanceM / 5); // Move all buttons
				}
				
				if ((buttons.get(offsetMaps + maps.length - 1).x + (distanceK / 5)) < 420 - 110) return;
				if (buttons.get(offsetMaps).x + (distanceK / 5) > 20) return;
				for (int i = 0; i < maps.length; i++) {
					buttons.get(offsetMaps + i).x += (distanceK / 5); // Move all buttons
				}
				if (client.currentScreen != QueueScreen.this) {
					timer.cancel();
				}
			}
		}, 16L, 16L);
	}
	
	@Override
	protected void init() {
		widget = new WorldListWidget(null, client, width, height, 16, height - 24, 20, null, null) {
			@Override
			public void filter(Supplier<String> supplier, boolean load) {
				
			}
		};
		
		// Add Buttons for the Kit Selection
		int x = -96; // TODO: Implement Scrolling offset
		final AtomicInteger index = new AtomicInteger(0);
		offsetKit = buttons.size();
		for (String kit : kits) {
			final int buttonId = index.getAndIncrement();
			addButton(new ButtonWidget(x+=104, 28, 98, 20, new LiteralText(kit), s -> {
				selectedKit = buttonId;
			}) {
				
				int id = buttonId;
				
				@Override
				public boolean isHovered() {
					return id == selectedKit;
				}
			});
		}
		index.set(0);
		x = -96;
		offsetMaps = buttons.size();
		for (String map : maps) {
			final int buttonId = index.getAndIncrement();
			addButton(new ButtonWidget(x+=104, 62, 98, 20, new LiteralText(map), s -> {
				selectedMap = buttonId;
			}) {
				
				int id = buttonId;
				
				@Override
				public boolean isHovered() {
					return id == selectedMap;
				}
			});
		}
	}

	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		widget.render(matrices, mouseX, mouseY, delta); // Render Background
		
		// Draw Text
		drawCenteredString(matrices, client.textRenderer, queue + " Queue", width / 2, 4, 0xFFFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, "<", 1, 33, 0xFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, ">", width - client.textRenderer.getWidth(">") - 1, 33, 0xFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, "<", 1, 67, 0xFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, ">", width - client.textRenderer.getWidth(">") - 1, 67, 0xFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, "Select a Kit: ", 1, 18, 0xFFFFFF);
		drawStringWithShadow(matrices, client.textRenderer, "Select a Map: ", 1, 51, 0xFFFFFF);
		
		// Draw Outline of Player List
		DrawableHelper.fill(matrices, 4, 85, width / 7 * 2 + 1, height - 34, Color.lightGray.getRGB());
		DrawableHelper.fill(matrices, 5, 86, width / 7 * 2, height - 35, Color.black.getRGB());
		
		// Draw Player List
		for (int i = 0; i < players.length; i++) {
			drawStringWithShadow(matrices, client.textRenderer, players[i], 6, 87 + (i * 11), 0xFFFFFF);
		}
		
		super.render(matrices, mouseX, mouseY, delta);
	}
	
}
