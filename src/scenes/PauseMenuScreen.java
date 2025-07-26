package scenes;

import graphics.Window;

public class PauseMenuScreen extends Screen {

	@Override
	public void render() {
		FontRenderer.drawString("PAUSED", Window.getWidth() / 2, 150);
		FontRenderer.drawString("Press ESC to resume", Window.getWidth() / 2, 200);
	}

	@Override
	public void update() {
		if (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_ESCAPE)) {
			Game.setCurrentScreen(Window.getLastScreen()); // volver a la escena anterior
			while (org.lwjgl.input.Keyboard.isKeyDown(org.lwjgl.input.Keyboard.KEY_ESCAPE)) {
				org.lwjgl.opengl.Display.update(); // evitar rebote
			}
		}
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		// Menú opcional con botones de continuar, salir, etc.
	}
}
