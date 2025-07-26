package scenes;

import entities.Player;

public class Game {
	private static Screen currentScreen;
	private static GameScene gameScene;
	private static Screen overlayScreen;

	public static void setOverlay(Screen overlay) {
		overlayScreen = overlay;
	}

	public static Screen getOverlay() {
		return overlayScreen;
	}

	public static void clearOverlay() {
		overlayScreen = null;
	}

	public static void pause() {
		// Aquí puedes usar tu propia clase de menú de pausa
		setOverlay(new PauseMenuScreen());
	}

	public static void resume() {
		clearOverlay();
	}

	
	public static void initGame() {
		gameScene = new GameScene(new Player());
		setCurrentScreen(gameScene);
	}

	public static GameScene getGameScene() {
		return gameScene;
	}

	public static void setCurrentScreen(Screen screen) {
		currentScreen = screen;
	}

	public static Screen getCurrentScreen() {
		return currentScreen;
	}
}
