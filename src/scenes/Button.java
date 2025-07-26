package scenes;

import static org.lwjgl.opengl.GL11.*;

public class Button {
    private int x, y, width, height;
    private String label;
    private Runnable action;

    public Button(int x, int y, int width, int height, String label, Runnable action) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.label = label;
        this.action = action;
    }

    public void render() {
        // Fondo
        glDisable(GL_TEXTURE_2D);
        glColor3f(0.2f, 0.2f, 0.2f);
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();

        // Borde
        glLineWidth(2f);
        glColor3f(0.1f, 0.1f, 0.1f);
        glBegin(GL_LINE_LOOP);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();

        // Texto
        glEnable(GL_TEXTURE_2D);
        glColor3f(1f, 1f, 1f);
        FontRenderer.drawString(label, x + 10, y + 8);
        glDisable(GL_TEXTURE_2D);
    }

    public void mouseClicked(int mx, int my) {
        if (mx >= x && mx <= x + width && my >= y && my <= y + height) {
            action.run();
        }
    }
}
