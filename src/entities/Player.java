package entities;

import org.lwjgl.input.Keyboard;
import org.lwjgl.util.vector.Vector2f;

import tools.animator.*;
import tools.Time;
import tools.physics.PhysicsObject;

public class Player {
	private Vector2f pos = new Vector2f(0, 0);
	private boolean flipX = false;
	private final Animator animator = new Animator();
	public PhysicsObject body;
	private final float jumpForce = 450.0f;
	private static final float COYOTE_TIME = 0.25f;
	private static final float JUMP_BUFFER_TIME = 0.1f;

	private float coyoteTimer = 0f;
	private float jumpBufferTimer = 0f;

	private boolean isDashing = false;
	private float dashTimer = 0f;
	private float dashCooldown = 0f;

	private final float DASH_DURATION = 0.15f;
	private final float DASH_SPEED = 600f;
	private final float DASH_COOLDOWN_TIME = 0.8f;

	
	public Player() {
		body = new PhysicsObject(100, 100, 26, 38, 1);
		initAnimations();
	}

	public void initAnimations() {
		animator.addAnimation("IDLE", new Animation("idle", 13, 19, 4, 150));
		animator.addAnimation("WALK", new Animation("walk", 14, 18, 4, 100));
	}

	public void move() {
		float walkSpeed = 200.0f;
		boolean walking = false;

		// Actualizar timers
		if (dashTimer > 0f) dashTimer -= Time.deltaTime;
		if (dashCooldown > 0f) dashCooldown -= Time.deltaTime;

		// Comenzar el dash (con tecla Z, por ejemplo)
		if (!isDashing && dashCooldown <= 0f && Keyboard.isKeyDown(Keyboard.KEY_X)) {
			isDashing = true;
			dashTimer = DASH_DURATION;
			dashCooldown = DASH_COOLDOWN_TIME;

			// Dirección según flipX
			if (flipX) body.vx = -DASH_SPEED;
			else body.vx = DASH_SPEED;

			animator.play("DASH"); // si tienes animación
			return; // No ejecutar movimiento normal mientras dasheas
		}

		// Si está dasheando, mantener velocidad
		if (isDashing) {
			// Dash termina
			if (dashTimer <= 0f) {
				isDashing = false;
			}
			return;
		}

		// Movimiento normal
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
			flipX = true;
			body.vx = -walkSpeed;
			walking = true;
		} else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
			flipX = false;
			body.vx = walkSpeed;
			walking = true;
		} else {
			body.vx = 0;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && flipX == false) {
			body.vx = walkSpeed * 2;
		}
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && flipX == true) {
			body.vx = -walkSpeed * 2;
		}
		
		// Saltar
		if (Keyboard.isKeyDown(Keyboard.KEY_C)
				&& (body.isOnGround || coyoteTimer > 0f)) {
			body.vy = -jumpForce;
			body.isOnGround = false;
			coyoteTimer = 0f;
		}
		
		// Animaciones
		if (walking) animator.play("WALK");
		else animator.play("IDLE");
	}


	public void update() {
		if (body.isOnGround) {
			coyoteTimer = COYOTE_TIME;
		} else {
			coyoteTimer = Math.max(0, coyoteTimer - Time.deltaTime);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_C)) {
			jumpBufferTimer = JUMP_BUFFER_TIME;
		} else {
			jumpBufferTimer = Math.max(0, jumpBufferTimer - Time.deltaTime);
		}

		if (jumpBufferTimer > 0 && (body.isOnGround || coyoteTimer > 0)) {
			doJump();
			jumpBufferTimer = 0;
			coyoteTimer = 0;
		}
		
		move();

		animator.update();
		pos.x = body.x;
		pos.y = body.y;
	}

	private void doJump() {
		body.vy = -jumpForce;
		body.isOnGround = false;
	}

	public void render(float width, float height) {
		animator.render(pos.x, pos.y, width, height, flipX);
	}

	// Métodos para partículas
	public boolean isWalking() {
		return Keyboard.isKeyDown(Keyboard.KEY_LEFT) || Keyboard.isKeyDown(Keyboard.KEY_RIGHT);
	}

	public boolean isOnGround() {
		return body.isOnGround;
	}

	public Vector2f getFeetPosition() {
		return new Vector2f(body.x + body.width / 2.5f, body.y + body.height);
	}
}
