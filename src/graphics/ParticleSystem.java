package graphics;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import org.lwjgl.util.vector.Vector2f;

public class ParticleSystem {
	private List<Particle> particles = new ArrayList<Particle>();

	private Random random = new Random();

	public void emit(Vector2f origin, int amount, Particle.Shape shape, float life, float size, float r, float g, float b, boolean useTexture, int textureId) {
		for (int i = 0; i < amount; i++) {
			Vector2f velocity = new Vector2f(
				(random.nextFloat() - 0.5f) * 200f,
				(random.nextFloat() - 0.5f) * 200f
			);
			Particle p = new Particle(origin, velocity, life, size);
			p.shape = shape;
			p.r = r;
			p.g = g;
			p.b = b;
			p.useTexture = useTexture;
			p.textureId = textureId;
			particles.add(p);
		}
	}


	public void update(float delta) {
		Iterator<Particle> it = particles.iterator();
		while (it.hasNext()) {
			Particle p = it.next();
			if (p.life <= 0) {
				it.remove();
			}
		}

		for (Particle p : particles) {
			p.update(delta);
		}
	}

	public void render() {
		for (Particle p : particles) {
			p.render();
		}
	}
}
