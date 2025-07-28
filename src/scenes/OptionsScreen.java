package scenes;

import java.util.ArrayList;
import java.util.List;

import graphics.Window;
import static org.lwjgl.opengl.GL11.*;

public class OptionsScreen extends Screen {

    private List<String> resolutions;
    private int selected = 0;

    private Button left;
    private Button right;
    private Button apply;
    private Button back;

    public OptionsScreen() {
        // Resoluciones disponibles
        resolutions = new ArrayList<String>();
        resolutions.add("256x144");                   // 144p
        resolutions.add("426x240");                   // 240p
        resolutions.add("640x360");                   // 360p
        resolutions.add("854x480");					  // 480p
        resolutions.add("1280x720");                  // 720p
        resolutions.add("1920x1080");                 // 1080p
        resolutions.add("2560x1440");                 // 1440p
        resolutions.add("3840x2160");                 // 4K
        resolutions.add("7680x4320");                 // 8K

        // Crear botones con posiciones temporales (se ajustarán en render)
        left  = new Button(0,0,30,30,"<",  new Runnable(){ public void run(){
            if(selected>0) selected--; }});
        right = new Button(0,0,30,30,">",  new Runnable(){ public void run(){
            if(selected<resolutions.size()-1) selected++; }});
        apply = new Button(0,0,80,30,"Apply", new Runnable(){ public void run(){
            applyResolution(); }});
        back  = new Button(0,0,80,30,"Salir", new Runnable(){ public void run(){
            Game.setCurrentScreen(new MainMenuScreen()); }});
    }

    @Override
    public void render() {
        int w = Window.getWidth();
        int h = Window.getHeight();

        // Posiciones dinámicas centradas
        int centerX = w / 2;
        int centerY = h / 2;

        // Actualizar posición de botones cada frame
        left.setPosition(centerX - 120, centerY - 20);
        right.setPosition(centerX + 90,  centerY - 20);
        apply.setPosition(centerX - 100, centerY + 50);
        back.setPosition(centerX + 20,   centerY + 50);

        // Dibujar texto
        glEnable(GL_TEXTURE_2D);
        FontRenderer.drawCentered("Opciones de Resolución", centerX, centerY - 60);
        FontRenderer.drawCentered(resolutions.get(selected), centerX, centerY - 10);
        glDisable(GL_TEXTURE_2D);

        // Dibujar botones
        left.render();
        right.render();
        apply.render();
        back.render();
    }

    @Override
    public void update() {
        // No hace falta de momento
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        left.mouseClicked(x, y);
        right.mouseClicked(x, y);
        apply.mouseClicked(x, y);
        back.mouseClicked(x, y);
    }

    private void applyResolution() {
        String[] res = resolutions.get(selected).split("x");
        int newWidth  = Integer.parseInt(res[0]);
        int newHeight = Integer.parseInt(res[1]);
        try {
            org.lwjgl.opengl.Display.setDisplayMode(
                new org.lwjgl.opengl.DisplayMode(newWidth, newHeight)
            );
            Window.updateViewport(); // Reconfigura glOrtho y viewport
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
