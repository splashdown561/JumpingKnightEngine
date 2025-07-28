package entities;

import static org.lwjgl.opengl.GL11.*;

public class Camera {
    /** Dimensiones fijas de tu mundo en unidades de juego */
    public static final int WORLD_WIDTH  = 854;
    public static final int WORLD_HEIGHT = 480;

    public enum CameraType {
        SCREEN_BASED,  // Paginación horizontal por ancho de mundo
        DYNAMIC        // Cámara libre centrada en el jugador
    }

    private float currentX = 0, currentY = 0;
    private float targetX  = 0, targetY  = 0;
    private final float speed = 0.1f;
    private CameraType type = CameraType.SCREEN_BASED;

    public void setType(CameraType type) {
        this.type = type;
    }

    public CameraType getType() {
        return type;
    }

    /**
     * 1) En SCREEN_BASED: pagina en bloques de WORLD_WIDTH
     *    y deja Y siempre en 0 (no sube/baja).
     * 2) En DYNAMIC: centra en el jugador usando WORLD_WIDTH/HEIGHT.
     */
    public void update(float playerX, float playerY) {
        if (type == CameraType.SCREEN_BASED) {
            // Paging horizontal por ancho de mundo
            if (playerX < targetX) {
                targetX -= WORLD_WIDTH;
            }
            if (playerX >= targetX + WORLD_WIDTH) {
                targetX += WORLD_WIDTH;
            }
            // Fijar vertical en 0
            targetY = 0;

        } else {
            // Centrado dinámico: mitad de ancho y alto de mundo
            targetX = playerX - WORLD_WIDTH  / 2f;
            targetY = playerY - WORLD_HEIGHT / 2f;
        }

        // Interpolación suave
        currentX += (targetX - currentX) * speed;
        currentY += (targetY - currentY) * speed;
    }

    /**
     * Ajusta la matriz de proyección para mostrar siempre un rectángulo
     * WORLD_WIDTH×WORLD_HEIGHT en “unidades de mundo”.
     */
    public void apply() {
        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();
        glOrtho(
            currentX,
            currentX + WORLD_WIDTH,
            currentY + WORLD_HEIGHT,
            currentY,
            -1, 1
        );
        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }
}
