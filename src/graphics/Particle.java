package graphics;

import static org.lwjgl.opengl.GL11.*;
import org.lwjgl.util.vector.Vector2f;

public class Particle {
	public Vector2f position;
	public Vector2f velocity;
	public float life;
	public float maxLife;
	public float size;
	public float r, g, b, a;
	public Shape shape = Shape.QUAD;
	public boolean useTexture = false;
	public int textureId = -1; // (opcional si usas textures)

	public enum Shape {
		QUAD,
		CIRCLE,
		TRIANGLE,
		HEXAGON,
		PENTAGON
	}

	public Particle(Vector2f position, Vector2f velocity, float life, float size) {
		this.position = new Vector2f(position);
		this.velocity = velocity;
		this.life = this.maxLife = life;
		this.size = size;
		this.r = 1f;
		this.g = 1f;
		this.b = 1f;
		this.a = 1f;
	}

	public void update(float delta) {
		if (life > 0) {
			position.x += velocity.x * delta;
			position.y += velocity.y * delta;
			life -= delta;
			a = Math.max(0f, life / maxLife); // se desvanece suavemente
		}
	}

	public void render() {
		if (life <= 0) return;

		glColor4f(r, g, b, a);

		if (useTexture && textureId != -1) {
			glEnable(GL_TEXTURE_2D);
			glBindTexture(GL_TEXTURE_2D, textureId);
		} else {
			glDisable(GL_TEXTURE_2D);
		}

		switch (shape) {
			case QUAD:
				drawQuad();
				break;
			case CIRCLE:
				drawCircle();
				break;
			case TRIANGLE:
				drawTriangle();
				break;
			case HEXAGON:
				drawHexagon(20.0f);
				break;
			case PENTAGON:
				drawPentagon(20.0f);
				break;
		}
	}

	private void drawQuad() {
		glBegin(GL_QUADS);
		glTexCoord2f(0, 0); glVertex2f(position.x - size, position.y - size);
		glTexCoord2f(1, 0); glVertex2f(position.x + size, position.y - size);
		glTexCoord2f(1, 1); glVertex2f(position.x + size, position.y + size);
		glTexCoord2f(0, 1); glVertex2f(position.x - size, position.y + size);
		glEnd();
	}

	private void drawHexagon(float radius) {
		glBegin(GL_POLYGON);
		for (int i = 0; i < 6; i++) {
			float angle = (float)(2 * Math.PI * i / 6); // 6 lados
			float x = position.x + (float)Math.cos(angle) * radius;
			float y = position.y + (float)Math.sin(angle) * radius;
			glVertex2f(x, y);
		}
		glEnd();
	}
	
	private void drawPentagon(float radius) {
		glBegin(GL_POLYGON);
		for (int i = 0; i < 5; i++) {
			float angle = (float)(2 * Math.PI * i / 5); // 5 lados
			float x = position.x + (float)Math.cos(angle) * radius;
			float y = position.y + (float)Math.sin(angle) * radius;
			glVertex2f(x, y);
		}
		glEnd();
	}
	
	private void drawCircle() {
		glBegin(GL_TRIANGLE_FAN);
		glVertex2f(position.x, position.y);
		for (int i = 0; i <= 20; i++) {
			double angle = 2 * Math.PI * i / 20;
			glVertex2f(position.x + (float)Math.cos(angle) * size, position.y + (float)Math.sin(angle) * size);
		}
		glEnd();
	}

	private void drawTriangle() {
		glBegin(GL_TRIANGLES);
		glVertex2f(position.x, position.y - size);
		glVertex2f(position.x - size, position.y + size);
		glVertex2f(position.x + size, position.y + size);
		glEnd();
	}
}
