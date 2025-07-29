package tools.sounds;

import java.io.*;
import java.util.*;
import org.lwjgl.LWJGLException;
import org.lwjgl.openal.AL;
import org.lwjgl.openal.AL10;
import org.lwjgl.util.WaveData;

public class Sound {

	private static List<Integer> buffers = new ArrayList<>();

	public static void init() {
		try {
			AL.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
		}
	}

	public static void setListenerData() {
		AL10.alListener3f(AL10.AL_POSITION, 0, 0, 0);
		AL10.alListener3f(AL10.AL_VELOCITY, 0, 0, 0);
	}

	/**
	 * Carga un sonido desde el sistema de archivos o desde el classpath.
	 * @param path puede ser una ruta como "res/mvp.wav" o "/res/mvp.wav" si está en el classpath
	 */
	public static int loadSound(String path) {
	    InputStream in = null;

	    // 1. Intenta cargar desde archivo externo
	    File file = new File(path);
	    if (file.exists()) {
	        System.out.println("Archivo encontrado en el sistema de archivos: " + path);
	        try {
	            in = new FileInputStream(file);
	        } catch (FileNotFoundException e) {
	            throw new RuntimeException("Archivo no encontrado: " + path, e);
	        }
	    } else {
	        // 2. Intenta cargar desde el classpath
	        in = Sound.class.getResourceAsStream(path.startsWith("/") ? path : "/" + path);
	        if (in == null) {
	            throw new RuntimeException("No se pudo encontrar el archivo de sonido: " + path);
	        }
	    }

	    try {
	        WaveData waveFile = WaveData.create(in);
	        if (waveFile == null)
	            throw new RuntimeException("No se pudo cargar el archivo WAV: " + path);

	        int buffer = AL10.alGenBuffers();
	        buffers.add(buffer);

	        AL10.alBufferData(buffer, waveFile.format, waveFile.data, waveFile.samplerate);
	        waveFile.dispose();
	        return buffer;
	    } catch (Exception e) {
	        throw new RuntimeException("Error al cargar el sonido: " + path, e);
	    } finally {
	        try {
	            if (in != null) in.close();
	        } catch (IOException ignored) {}
	    }
	}


	public static void cleanUp() {
		for (int buffer : buffers) {
			AL10.alDeleteBuffers(buffer);
		}
		AL.destroy();
	}
}
