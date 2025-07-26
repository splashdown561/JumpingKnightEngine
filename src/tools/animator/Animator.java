package tools.animator;

import java.util.HashMap;
import java.util.Map;

public class Animator {

	private Map<String, Animation> animations = new HashMap<String, Animation>();
	private Animation currentAnimation;
	private String currentName = "";

	public void addAnimation(String name, Animation anim) {
		animations.put(name, anim);
		if (currentAnimation == null) {
			currentAnimation = anim;
			currentName = name;
		}
	}

	public void play(String name) {
		if (!name.equals(currentName)) {
			Animation next = animations.get(name);
			if (next != null) {
				currentAnimation = next;
				currentName = name;
			}
		}
	}

	public void update() {
		if (currentAnimation != null) {
			currentAnimation.update();
		}
	}

	public void render(float x, float y, float width, float height, boolean flipX) {
		if (currentAnimation != null) {
			currentAnimation.render(x, y, width, height, flipX);
		}
	}
}
