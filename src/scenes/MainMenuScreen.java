package scenes;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;

import graphics.Window;
import scenes.FontRenderer;

public class MainMenuScreen extends Screen {

    private List<Button> buttons = new ArrayList<Button>();
    private final int buttonWidth  = 200;
    private final int buttonHeight = 30;
    private final int spacing      = 20;  // espacio vertical entre botones

    public MainMenuScreen() {
        // Creamos los botones con posici�n (0,0), se ajustar�n en render()
        buttons.add(new Button(0, 0, buttonWidth, buttonHeight, "Singleplayer", new Runnable() {
            @Override public void run() {
                Game.initGame();
            }
        }));

        buttons.add(new Button(0, 0, buttonWidth, buttonHeight, "Options", new Runnable() {
            @Override public void run() {
                Game.setCurrentScreen(new OptionsScreen());
            }
        }));

        buttons.add(new Button(0, 0, buttonWidth, buttonHeight, "Exit", new Runnable() {
            @Override public void run() {
                Display.destroy();
                System.exit(0);
            }
        }));
    }

    @Override
    public void render() {
        // Limpiar fondo
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        int w = Window.getWidth();
        int h = Window.getHeight();
        int centerX = w / 2;
        int centerY = h / 2;

        // T�tulo centrado en la parte superior
        glEnable(GL_TEXTURE_2D);
        FontRenderer.drawCentered("My LWJGL Game", centerX, centerY - 150);
        glDisable(GL_TEXTURE_2D);

        // Calcular posici�n inicial del primer bot�n para centrar todo el bloque
        int totalHeight = buttons.size() * buttonHeight + (buttons.size() - 1) * spacing;
        int startY = centerY - totalHeight / 2;

        // Reposicionar y dibujar cada bot�n
        for (int i = 0; i < buttons.size(); i++) {
            Button b = buttons.get(i);
            int x = centerX - buttonWidth / 2;
            int y = startY + i * (buttonHeight + spacing);
            b.setPosition(x, y);
            b.render();
        }
    }

    @Override
    public void update() {
        // No se necesita l�gica extra por ahora
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        for (Button b : buttons) {
            b.mouseClicked(x, y);
        }
    }
}
