package graphics;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import java.awt.Font;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.PixelFormat;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.newdawn.slick.TrueTypeFont;

import scenes.FontRenderer;
import scenes.Game;
import scenes.MainMenuScreen;
import scenes.Screen;

public class Window {

	private int windowWidth;
	private int windowHeight;

	private static Screen lastScreen;
	
	public void create_window(int width, int height) {
		this.windowWidth = width;
		this.windowHeight = height;

		// 1) Crear la ventana
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create(new PixelFormat().withDepthBits(24).withSamples(4));
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(1);
		}
		System.out.println("Window created successfully");

		// 2) Fuente global
		Font awtFont = new Font("Arial", Font.PLAIN, 18);
		TrueTypeFont ttf = new TrueTypeFont(awtFont, false);
		if (ttf == null) {
			System.err.println("Error: No se pudo inicializar la fuente");
			System.exit(1);
		}
		System.out.println("Font initialized successfully.");
		FontRenderer.setFont(ttf);

		// 3) Configuración OpenGL
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, -1, 1);
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();

		glEnable(GL_BLEND);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		glClearColor(0.0f, 0.5f, 1.0f, 1.0f);
		
		glEnable(GL_MULTISAMPLE);

		// 4) Iniciar en el menú principal
		Game.setCurrentScreen(new MainMenuScreen());

		int fps = 0;
		long fpsTimer = System.currentTimeMillis();
		int lastFPS = 0;

		// 5) Bucle principal
		while (!Display.isCloseRequested()) {
			glClear(GL_COLOR_BUFFER_BIT);
			glLoadIdentity();


			// 5.2) Eventos del mouse
			while (Mouse.next()) {
				if (Mouse.getEventButtonState() && Mouse.getEventButton() == 0) {
					int mx = Mouse.getEventX();
					int screenH = Display.getDisplayMode().getHeight();
					int my = screenH - Mouse.getEventY();
					Screen overlay = Game.getOverlay();
					if (overlay != null) {
						overlay.mouseClicked(mx, my, 0);
					} else {
						Screen current = Game.getCurrentScreen();
						if (current != null)
							current.mouseClicked(mx, my, 0);
					}
				}
			}

			// 5.3) Actualizar y renderizar
			Screen current = Game.getCurrentScreen();
			Screen overlay = Game.getOverlay();

			if (current != null) {
				current.update();
				current.render();
			}

			if (overlay != null) {
				overlay.update();
				overlay.render();
			}

			// 5.4) Mostrar FPS
			fps++;
			if (System.currentTimeMillis() - fpsTimer > 1000) {
				lastFPS = fps;
				fps = 0;
				fpsTimer += 1000;
			}
			glEnable(GL_TEXTURE_2D);
			glColor3f(1f, 1f, 1f);
			FontRenderer.drawString("FPS: " + lastFPS, 10, 10);
			glDisable(GL_TEXTURE_2D);

			// 5.5) Sincronizar y actualizar
			Display.update();
			Display.sync(60);
		}

		Display.destroy();
	}
	
	// Métodos utilitarios
	public static int getWidth() {
		return Display.getDisplayMode().getWidth();
	}

	public static int getHeight() {
		return Display.getDisplayMode().getHeight();
	}

	public static Screen getLastScreen() {
		return lastScreen;
	}
	
	public static void updateViewport() {
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

		glViewport(0, 0, width, height); // Ajusta la vista
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, -1, 1); // Coordenadas ortográficas
		glMatrixMode(GL_MODELVIEW);
		glLoadIdentity();
	}

	
}
