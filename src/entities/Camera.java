package entities;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.opengl.Display;

public class Camera {

	public enum Mode {
		DYNAMIC, SCREEN_BY_SCREEN
	}

	private float currentX = 0;
	private float currentY = 0;
	private float targetX = 0;
	private float targetY = 0;
	private final float speed = 1.5f; // velocidad de interpolación

	public static final int WORLD_WIDTH = 854;
	public static final int WORLD_HEIGHT = 480;

	private Mode mode = Mode.DYNAMIC;

	public void setMode(Mode newMode) {
		this.mode = newMode;
	}

	public void follow(float playerX, float playerY) {
		if (mode == Mode.DYNAMIC) {
			targetX = playerX - WORLD_WIDTH / 2f;
			targetY = playerY - WORLD_HEIGHT / 2f;
		} else if (mode == Mode.SCREEN_BY_SCREEN) {
			int screenX = (int)(playerX / WORLD_WIDTH);
			int screenY = (int)(playerY / WORLD_HEIGHT);
			targetX = screenX * WORLD_WIDTH;
			targetY = screenY * WORLD_HEIGHT;
		}
	}

	public void update(float delta) {
		currentX += (targetX - currentX) * speed * delta;
		currentY += (targetY - currentY) * speed * delta;
	}

	public void apply() {
		// Calcula escala y centrado para mantener proporción 16:9 (letterboxing)
		float screenWidth = Display.getDisplayMode().getWidth();
		float screenHeight = Display.getDisplayMode().getHeight();

		float scaleX = screenWidth / (float)WORLD_WIDTH;
		float scaleY = screenHeight / (float)WORLD_HEIGHT;
		float scale = Math.min(scaleX, scaleY);

		int viewportWidth = (int)(WORLD_WIDTH * scale);
		int viewportHeight = (int)(WORLD_HEIGHT * scale);

		int offsetX = (int)((screenWidth - viewportWidth) / 2f);
		int offsetY = (int)((screenHeight - viewportHeight) / 2f);

		glViewport(offsetX, offsetY, viewportWidth, viewportHeight);

		// Proyección ortográfica fija para mantener el mismo tamaño del mundo
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(currentX, currentX + WORLD_WIDTH, currentY + WORLD_HEIGHT, currentY, 1, -1);

		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
}
