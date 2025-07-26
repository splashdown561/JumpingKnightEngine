package scenes;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.opengl.Display;

import scenes.Button;
import scenes.FontRenderer;

public class MainMenuScreen extends Screen {

    private List<Button> buttons = new ArrayList<Button>();

    public MainMenuScreen() {
        buttons.add(new Button(100, 150, 200, 30, "Singleplayer", new Runnable() {
            @Override
            public void run() {
                Game.initGame();
            }
        }));

        buttons.add(new Button(100, 200, 200, 30, "Options", new Runnable() {
            @Override
            public void run() {
                Game.setCurrentScreen(new OptionsScreen());
            }
        }));

        buttons.add(new Button(100, 250, 200, 30, "Exit", new Runnable() {
            @Override
            public void run() {
                Display.destroy();
                System.exit(0);
            }
        }));
    }

    @Override
    public void render() {
        // Fondo
        glClear(GL_COLOR_BUFFER_BIT);
        glLoadIdentity();

        // Título
        glColor3f(1f, 1f, 1f);
        FontRenderer.drawString("My LWJGL Game", 120, 80);

        // Botones
        for (Button b : buttons) {
            b.render();
        }
    }

    @Override
    public void update() {
        // nada por ahora
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        for (Button b : buttons) {
            b.mouseClicked(x, y);
        }
    }
}
