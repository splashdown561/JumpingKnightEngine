package scenes;

import org.lwjgl.util.vector.Vector2f;
import static org.lwjgl.opengl.GL11.*;

import tools.Time;
import tools.physics.PhysicsEngine;
import tools.physics.PhysicsObject;
import entities.Camera;
import entities.Player;
import graphics.ParticleSystem;
import graphics.Particle;
import graphics.Window;  //  Importa tu Window con updateViewport()

public class GameScene extends Screen {

    private Player player;
    private Camera camera;
    private PhysicsEngine engine;
    private PhysicsObject ground, ground2, ground3;
    private ParticleSystem particleSystem;

    public GameScene(Player player) {
        this.player = player;
        engine = new PhysicsEngine();
        camera = new Camera();
        camera.setMode(Camera.Mode.DYNAMIC);

        particleSystem = new ParticleSystem();

        // Suelos en coordenadas fijas de “mundo” 854×480:
        ground  = new PhysicsObject(0,         480 - 60, 854, 60, 100000);
        ground2 = new PhysicsObject(900,       360,      200, 30, 100000);
        ground3 = new PhysicsObject(1200,      360,       20, 30, 100000);
        ground .isStatic = true; ground .isOnGround = true;
        ground2.isStatic = true; ground2.isOnGround = true;
        ground3.isStatic = true; ground3.isOnGround = true;

        engine.addObject(player.body);
        engine.addObject(ground);
        engine.addObject(ground2);
        engine.addObject(ground3);
    }

    @Override
    public void render() {
        // 1) Ajustar viewport al tamaño actual de la ventana
        Window.updateViewport();
        
        camera.follow(player.body.x, player.body.y);

        // 2) Aplicar la proyección/cámara (mantén tu glOrtho fijo de 854×480)
        camera.apply();

        // 3) Dibujar todo en espacio “mundo”
        glDisable(GL_TEXTURE_2D);
        glColor3f(0.2f, 0.2f, 0.2f);
        ground.render();
        ground.renderDebug(0.5f, 0.0f, 0.5f);
        glColor3f(0.2f, 0.2f, 0.2f);
        ground2.render();
        ground3.render();
        particleSystem.render();
        player.body.renderDebug(1.0f, 0.0f, 0.0f);

        glEnable(GL_TEXTURE_2D);
        glColor3f(1.0f, 1.0f, 1.0f);
        player.render(28, 38);
    }

    @Override
    public void update() {
        Time.update();
        engine.update(Time.deltaTime);
        player.update();

        if (Math.abs(player.body.vx) > 5f && player.body.isOnGround) {
            Vector2f foot = new Vector2f(
                player.body.x + player.body.width  / 2f,
                player.body.y + player.body.height
            );
            particleSystem.emit(
                foot, 2, Particle.Shape.CIRCLE,
                0.5f, 4f,
                0.8f, 0.8f, 0.8f,
                false, 0
            );
        }

        particleSystem.update(Time.deltaTime);
        camera.update(Time.deltaTime);
    }

    @Override
    public void mouseClicked(int x, int y, int button) {
        // …
    }
}
