package tools.animator;

import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.IOException;
import java.io.InputStream;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;

public class Animation {

	private Texture texture;
	private int frameWidth, frameHeight;
	private int totalFrames;
	private int currentFrame = 0;
	private long lastUpdateTime = 0;
	private long frameDuration = 100;

	public Animation(String imgName, int frameWidth, int frameHeight, int totalFrames, long frameDuration) {
		this.frameWidth = frameWidth;
		this.frameHeight = frameHeight;
		this.totalFrames = totalFrames;
		this.frameDuration = frameDuration;
		loadTexture(imgName);
	}

	private void loadTexture(String imgName) {
		try {
			InputStream in = getClass().getResourceAsStream("/" + imgName + ".png");
			if (in == null) {
				System.err.println("No se encontró la imagen: /" + imgName + ".png");
				return;
			}

			glEnable(GL_TEXTURE_2D);
			glEnable(GL_BLEND);
			glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

			texture = TextureLoader.getTexture("PNG", in);

			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
			glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

		} catch (IOException e) {
			System.err.println("Error al cargar animación:");
			e.printStackTrace();
		}
	}

	public void update() {
		long currentTime = System.currentTimeMillis();
		if (currentTime - lastUpdateTime >= frameDuration) {
			currentFrame = (currentFrame + 1) % totalFrames;
			lastUpdateTime = currentTime;
		}
	}

	public void render(float x, float y, float width, float height, boolean flipX) {
		if (texture == null) return;

		int framesPerRow = texture.getTextureWidth() / frameWidth;
		int xOffset = (currentFrame % framesPerRow) * frameWidth;
		int yOffset = (currentFrame / framesPerRow) * frameHeight;

		float u0 = xOffset / (float) texture.getTextureWidth();
		float v0 = yOffset / (float) texture.getTextureHeight();
		float u1 = (xOffset + frameWidth) / (float) texture.getTextureWidth();
		float v1 = (yOffset + frameHeight) / (float) texture.getTextureHeight();

		if (flipX) {
			float temp = u0;
			u0 = u1;
			u1 = temp;
		}

		texture.bind();

		glBegin(GL_QUADS);
		glTexCoord2f(u0, v0); glVertex2f(x, y);
		glTexCoord2f(u1, v0); glVertex2f(x + width, y);
		glTexCoord2f(u1, v1); glVertex2f(x + width, y + height);
		glTexCoord2f(u0, v1); glVertex2f(x, y + height);
		glEnd();
	}
}
