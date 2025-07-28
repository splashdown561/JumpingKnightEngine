package entities;

import static org.lwjgl.opengl.GL11.*;
import scenes.GameScene;

public class Camera {
	public enum CameraType {
		SCREEN_BASED,
		DYNAMIC
	}

	private float currentX = 0;
	private float currentY = 0;
	private float targetX = 0;
	private float targetY = 0;
	private final float speed = 0.1f; // velocidad de interpolación
	private CameraType type = CameraType.SCREEN_BASED; // tipo de cámara actual

	public void setType(CameraType type) {
		this.type = type;
	}

	public CameraType getType() {
		return type;
	}

	public void update(float playerX, float playerY) {
		switch (type) {
			case SCREEN_BASED:
				if (playerX < targetX) {
					targetX -= GameScene.SCREEN_WIDTH;
				}
				if (playerX >= targetX + GameScene.SCREEN_WIDTH) {
					targetX += GameScene.SCREEN_WIDTH;
				}
				if (playerY < targetY) {
					targetY -= GameScene.SCREEN_HEIGHT;
				}
				if (playerY >= targetY + GameScene.SCREEN_HEIGHT) {
					targetY += GameScene.SCREEN_HEIGHT;
				}
				break;

			case DYNAMIC:
				// Centra la cámara alrededor del jugador
				targetX = playerX - GameScene.SCREEN_WIDTH / 2f;
				targetY = playerY - GameScene.SCREEN_HEIGHT / 2f;
				break;
		}

		// Interpolación suave
		currentX += (targetX - currentX) * speed;
		currentY += (targetY - currentY) * speed;
	}

	public void apply() {
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(currentX, currentX + GameScene.SCREEN_WIDTH,
		        currentY + GameScene.SCREEN_HEIGHT, currentY,
		        -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}
}
