package tools;

public class Time {
	public static float deltaTime = 0f;
	public static float time = 0f;

	private static long lastTime = System.nanoTime();

	public static void update() {
		long now = System.nanoTime();
		deltaTime = (now - lastTime) / 1000000000f; // nota el sufijo 'f'
		time += deltaTime;
		lastTime = now;
	}
}
