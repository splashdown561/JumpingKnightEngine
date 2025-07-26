package entities;

import static org.lwjgl.opengl.GL11.*;
import scenes.GameScene;

public class Camera {
	private float currentX = 0;
	private float currentY = 0;
	private float targetX = 0;
	private float targetY = 0;
	private final float speed = 1f; // cuanto mayor, más rápida la transición

	public void update(float playerX, float playerY) {
		// Detectar si el jugador cruzó un borde de pantalla
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

		// Interpolación suave hacia el objetivo
		currentX += (targetX - currentX) * 0.1f;
		currentY += (targetY - currentY) * 0.1f;
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


