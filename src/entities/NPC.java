package entities;

import org.lwjgl.util.vector.Vector2f;

import tools.animator.Animator;
import tools.animator.Animation;
import tools.physics.PhysicsObject;

public class NPC {
	private Vector2f pos = new Vector2f(0, 0);
	private final Animator animator = new Animator();
	public PhysicsObject body;
	private boolean flipX = false;

	public NPC(float startX, float startY) {
		body = new PhysicsObject(startX, startY, 26, 38, 1);
		body.isStatic = true; // no se mueve por físicas (opcional)
		initAnimations();
	}

	private void initAnimations() {
		animator.addAnimation("IDLE", new Animation("idle", 13, 19, 4, 150));
		animator.play("IDLE");
	}

	public void update() {
		animator.update();
		pos.x = body.x;
		pos.y = body.y;
	}

	public void render(float width, float height) {
		animator.render(pos.x, pos.y, width, height, flipX);
	}
}
