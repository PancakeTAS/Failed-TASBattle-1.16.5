package de.pfannekuchen.tasbattle.gui;

import java.util.Iterator;

import org.jline.utils.OSUtils;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;

import de.pfannekuchen.tasbattle.TASBattleClient;
import net.minecraft.client.gui.RotatingCubeMapRenderer;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.gui.screen.options.OptionsScreen;
import net.minecraft.client.gui.widget.AbstractButtonWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.math.MathHelper;

/**
 * This is the new title screen, it has new buttons for the new Server
 * 
 * @author Pancake
 */
public class NewTitleScreen extends Screen {

	private static final Identifier PANORAMA_OVERLAY = new Identifier("textures/gui/title/background/panorama_overlay.png");
	private static final Identifier MINECRAFT_TITLE_TEXTURE = new Identifier("textures/gui/title/minecraft.png");
	
	private final RotatingCubeMapRenderer backgroundRenderer;
	private long backgroundFadeStart;

	public NewTitleScreen() {
		super(LiteralText.EMPTY);
		this.backgroundRenderer = new RotatingCubeMapRenderer(TitleScreen.PANORAMA_CUBE_MAP);
	}

	@Override
	protected void init() {
		/* Add Normal Title Screen Buttons */
		addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 48 + 72 + 12, 98, 20, new TranslatableText("menu.options"), (buttonWidget) -> {
			this.client.openScreen(new OptionsScreen(this, this.client.options));
		}));
		addButton(new ButtonWidget(this.width / 2 + 2, this.height / 4 + 48 + 72 + 12, 98, 20, new TranslatableText("menu.quit"), (buttonWidget) -> {
			this.client.scheduleStop();
		}));
		
		/* Add Custom Buttons */
		addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 48, 200, 20, new TranslatableText("Play TAS Challenges Offline"), (buttonWidget) -> { // TODO: Put this into a .lang file
			
		}, (buttonWidget, matrixStack, i, j) -> {
			if (buttonWidget.isMouseOver(i, j)) this.renderOrderedTooltip(matrixStack, this.client.textRenderer.wrapLines(new TranslatableText("Sorry, this feature isn't implemented yet."), Math.max(this.width / 2 - 43, 170)), i, j);
		})).active = false; // TODO: Implement Singleplayer Training Maps
		addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 48 + 24, 200, 20, new TranslatableText("Join TAS Battle Server"), (buttonWidget) -> {
			
		}, (buttonWidget, matrixStack, i, j) -> {
			if (buttonWidget.isMouseOver(i, j) && !buttonWidget.active) this.renderOrderedTooltip(matrixStack, this.client.textRenderer.wrapLines(new TranslatableText("It looks like we couldn't connect you to the Internet!"), Math.max(this.width / 2 - 43, 170)), i, j);
		})).active = TASBattleClient.isConnected;
		addButton(new ButtonWidget(this.width / 2 - 100, this.height / 4 + 48 + 48, 200, 20, new TranslatableText("Speed up Videos"), (buttonWidget) -> {
			client.openScreen(new VideoUpspeederScreen());
		}, (buttonWidget, matrixStack, i, j) -> {
			if (buttonWidget.isMouseOver(i, j)) this.renderOrderedTooltip(matrixStack, this.client.textRenderer.wrapLines(new TranslatableText(OSUtils.IS_WINDOWS ? "The Video Upspeeder is a small Mod that can speed up your Videos without the need of any Editing Software! \u00A74Download might trigger Anti-Virus" : "This feature is only available for Windows Users"), Math.max(this.width / 2 - 43, 170)), i, j);
		})).active = OSUtils.IS_WINDOWS;
	}
	
	@Override
	public void render(MatrixStack matrices, int mouseX, int mouseY, float delta) {
		if (this.backgroundFadeStart == 0L) {
			this.backgroundFadeStart = Util.getMeasuringTimeMs();
		}
		float f = (Util.getMeasuringTimeMs() - this.backgroundFadeStart) / 1000.0F;
		fill(matrices, 0, 0, this.width, this.height, -1);
		this.backgroundRenderer.render(delta, MathHelper.clamp(f, 0.0F, 1.0F));
		int j = this.width / 2 - 137;
		this.client.getTextureManager().bindTexture(PANORAMA_OVERLAY);
		RenderSystem.enableBlend();
		RenderSystem.blendFunc(GlStateManager.SrcFactor.SRC_ALPHA, GlStateManager.DstFactor.ONE_MINUS_SRC_ALPHA);
		RenderSystem.color4f(1.0F, 1.0F, 1.0F, MathHelper.ceil(MathHelper.clamp(f, 0.0F, 1.0F)));
		drawTexture(matrices, 0, 0, this.width, this.height, 0.0F, 0.0F, 16, 128, 16, 128);
		float g = MathHelper.clamp(f - 1.0F, 0.0F, 1.0F);
		int l = MathHelper.ceil(g * 255.0F) << 24;
		if ((l & -67108864) != 0) {
			this.client.getTextureManager().bindTexture(MINECRAFT_TITLE_TEXTURE);
			RenderSystem.color4f(1.0F, 1.0F, 1.0F, g);
			this.method_29343(j, 30, (integer, integer2) -> {
				this.drawTexture(matrices, integer + 0, integer2, 0, 0, 155, 44);
				this.drawTexture(matrices, integer + 155, integer2, 0, 45, 155, 44);
			});

			drawStringWithShadow(matrices, this.textRenderer, "TAS Battle 1.16.5 Client", 2, this.height - 10, 16777215 | l);

			Iterator<AbstractButtonWidget> var12 = this.buttons.iterator();

			while (var12.hasNext()) {
				AbstractButtonWidget abstractButtonWidget = (AbstractButtonWidget) var12.next();
				abstractButtonWidget.setAlpha(g);
			}

			super.render(matrices, mouseX, mouseY, delta);
		}

	}

}
