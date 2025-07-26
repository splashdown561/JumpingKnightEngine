package scenes;

import org.lwjgl.util.vector.Vector2f;

import tools.Time;
import tools.physics.PhysicsEngine;
import tools.physics.PhysicsObject;
import entities.Camera;
import entities.Player;
import graphics.ParticleSystem;
import graphics.Particle;
import static org.lwjgl.opengl.GL11.*;

public class GameScene extends Screen {

	private Player player;
	private Camera camera;
	private PhysicsEngine engine;
	private PhysicsObject ground;
	private PhysicsObject ground2;
	private PhysicsObject ground3;
	private ParticleSystem particleSystem;
	
	public static final int SCREEN_WIDTH = 854;
	public static final int SCREEN_HEIGHT = 480;

	public GameScene(Player player) {
		this.player = player;
		engine = new PhysicsEngine();
		camera = new Camera();

		particleSystem = new ParticleSystem();
		
		// Suelo
		ground = new PhysicsObject(0, 480 - 60, 854, 60, 100000);
		ground.isStatic = true;
		ground.isOnGround = true;
		
		ground2 = new PhysicsObject(900, 360, 200, 30, 100000); // Se verá más arriba en la pantalla
		ground2.isStatic = true;
		ground2.isOnGround = true;

		ground3 = new PhysicsObject(1200, 360, 20, 30, 100000); // Se verá más arriba en la pantalla
		ground3.isStatic = true;
		ground3.isOnGround = true;
		
		// Añadir objetos al motor de físicas
		engine.addObject(player.body); // asegurarte que player.body esté creado en Player
		engine.addObject(ground);
		engine.addObject(ground2);
		engine.addObject(ground3);
	}


	@Override
	public void render() {
		
		camera.apply();
		
		glDisable(GL_TEXTURE_2D);
	    glColor3f(0.2f, 0.2f, 0.2f);
		ground.render();
		ground2.render();
		ground3.render();
		particleSystem.render();
		glEnable(GL_TEXTURE_2D);
		glColor3f(1.0F, 1.0F, 1.0F);
		player.render(28, 38);
	}

	@Override
	public void update() {
		Time.update();
	    engine.update(Time.deltaTime);
		player.update();
		
		if (Math.abs(player.body.vx) > 5f && player.body.isOnGround) {
			Vector2f footPosition = new Vector2f(player.body.x + player.body.width / 2f,
			                                     player.body.y + player.body.height);
			particleSystem.emit(footPosition, 2, Particle.Shape.CIRCLE, 0.5f, 4f,
			                    0.4f, 0.4f, 0.4f, false, 0); // humo gris
		}

		particleSystem.update(Time.deltaTime);
		camera.update(player.body.x, player.body.y);
		
	}

	@Override
	public void mouseClicked(int x, int y, int button) {
		// Si quieres manejar clics aquí
	}
}
