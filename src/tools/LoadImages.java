package tools;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.*;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

import java.io.InputStream;
import java.io.IOException;

public class LoadImages {

    private Texture texture;  // Textura general (spritesheet)
    private int frameWidth, frameHeight;  // Tamaño de cada frame
    private int totalFrames;  // Número total de frames
    private int currentFrame = 0;  // Frame actual
    private long lastUpdateTime = 0;  // Tiempo de la última actualización
    private long frameDuration = 100;  // Duración de cada frame en milisegundos
    
    public Texture getTexture() {
		return texture;
	}


	// Cargar la imagen de la hoja de sprites
    public boolean load_image(String imgName, int frameWidth, int frameHeight, int totalFrames) {
    	try {
    		InputStream in = getClass().getResourceAsStream("/" + imgName + ".png");

    		if (in == null) {
    			System.err.println(" No se encontró la imagen: /" + imgName + ".png");
    			return false;
    		}

    		// Habilitar blending y texturas
    		glEnable(GL_TEXTURE_2D);
    		glEnable(GL_BLEND);
    		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

    		// Cargar la textura
    		texture = TextureLoader.getTexture("PNG", in);

    		// Enlazar la textura antes de modificar parámetros
    		texture.bind();

    		// Filtro NEAREST (sin interpolación, pixel-perfect)
    		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
    		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

    		//  Evitar que al acceder a frames se filtren bordes de otros
    		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
    		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);

    		// Guardar info del spritesheet
    		this.frameWidth = frameWidth;
    		this.frameHeight = frameHeight;
    		this.totalFrames = totalFrames;

    		System.out.println(" Imagen cargada: " + texture.getImageWidth() + "x" + texture.getImageHeight());
    		return true;

    	} catch (IOException e) {
    		System.err.println(" Error al cargar la imagen:");
    		e.printStackTrace();
    		return false;
    	}
    }


    public void load_image_without_animation(String imgName) {
        try {
            InputStream in = getClass().getResourceAsStream("/" + imgName + ".png");

            if (in == null) {
                System.err.println("No se encontró la imagen: /" + imgName + ".png");
            } else {
                texture = TextureLoader.getTexture("PNG", in);
            	texture.setTextureFilter(GL_NEAREST);
                System.out.println("Imagen cargada: " + texture.getImageWidth() + "x" + texture.getImageHeight());
            }
        } catch (IOException e) {
            System.err.println("Error al cargar la imagen:");
            e.printStackTrace();
        }
    }

    // Actualizar el frame actual de la animación
    public void updateAnimation() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdateTime >= frameDuration) {
            currentFrame = (currentFrame + 1) % totalFrames;  // Cambiar al siguiente frame
            lastUpdateTime = currentTime;
        }
    }

    public void render_image_without_animation(float x, float y, float width, float height) {
        if (texture != null) {
            texture.bind();

            float imageWidth = texture.getImageWidth();
            float imageHeight = texture.getImageHeight();

            float u0 = 0;
            float v0 = 0;
            float u1 = imageWidth / texture.getTextureWidth(); // Normaliza a 0-1
            float v1 = imageHeight / texture.getTextureHeight(); // Normaliza a 0-1


            glBegin(GL_QUADS);
            glTexCoord2f(u0, v0); glVertex2f(x, y);
            glTexCoord2f(u1, v0); glVertex2f(x + width, y);
            glTexCoord2f(u1, v1); glVertex2f(x + width, y + height);
            glTexCoord2f(u0, v1); glVertex2f(x, y + height);
            glEnd();
        }
    }


    // Dibujar el frame actual en la posición (x, y)
    public void render_image(float x, float y, float width, float height, boolean flipX) {
    	if (texture == null) return;

    	// Calcular las coordenadas del frame actual en la textura
    	int framesPerRow = texture.getImageWidth() / frameWidth;
    	int xOffset = (currentFrame % framesPerRow) * frameWidth;
    	int yOffset = (currentFrame / framesPerRow) * frameHeight;

    	// Usar getImageWidth y getImageHeight para evitar errores de interpolación
    	float u0 = xOffset / (float) texture.getTextureWidth();
    	float v0 = yOffset / (float) texture.getTextureHeight();
    	float u1 = (xOffset + frameWidth) / (float) texture.getTextureWidth();
    	float v1 = (yOffset + frameHeight) / (float) texture.getTextureHeight();

    	if (flipX) {
    		float temp = u0;
    		u0 = u1;
    		u1 = temp;
    	}

    	
    	texture.bind();

    	glBegin(GL_QUADS);
    		glTexCoord2f(u0, v0); glVertex2f(x, y);
    		glTexCoord2f(u1, v0); glVertex2f(x + width, y);
    		glTexCoord2f(u1, v1); glVertex2f(x + width, y + height);
    		glTexCoord2f(u0, v1); glVertex2f(x, y + height);
    	glEnd();
    }


}