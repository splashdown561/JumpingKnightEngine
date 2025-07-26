package scenes;

import java.awt.Font;

import org.newdawn.slick.TrueTypeFont;

import static org.lwjgl.opengl.GL11.*;

public class FontRenderer {
	private static TrueTypeFont font;

	public static void setFont(TrueTypeFont f) {
		font = f;
	}

	public static void drawString(String text, float x, float y) {
		if (font == null) {
			System.err.println("Font is not initialized!");
			return;
		}

		glEnable(GL_TEXTURE_2D); // importante para texto
		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA); // suaviza contornos

		glColor3f(1f, 1f, 1f); // color del texto (blanco)
		font.drawString(x, y, text);

		glDisable(GL_TEXTURE_2D);
	}

}
