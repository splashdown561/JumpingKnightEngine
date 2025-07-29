package tools.sounds;

import org.lwjgl.openal.AL10;

public class Source {

	private int sourceId;

	public Source() {
		sourceId = AL10.alGenSources();
	}

	public void play(int buffer) {
		stop();
		AL10.alSourcei(sourceId, AL10.AL_BUFFER, buffer);
		AL10.alSourcePlay(sourceId);
	}

	public void stop() {
		AL10.alSourceStop(sourceId);
	}

	public void delete() {
		stop();
		AL10.alDeleteSources(sourceId);
	}
}
