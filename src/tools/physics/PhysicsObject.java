package tools.physics;

import static org.lwjgl.opengl.GL11.*;

public class PhysicsObject {
    public float x, y;
    public float width, height;
    public float vx, vy;
    public float ax, ay;
    public float mass;
    public boolean isStatic = false;
    public boolean isOnGround = false;

    public PhysicsObject(float x, float y, float width, float height, float mass) {
        this.x = x; this.y = y;
        this.width = width; this.height = height;
        this.mass = mass;
        this.vx = this.vy = this.ax = this.ay = 0;
    }

    public void applyForce(float fx, float fy) {
        if (isStatic) return;
        ax += fx / mass;
        ay += fy / mass;
    }

    public void update(float dt) {
        if (isStatic) return;
        // Integración
        vx += ax * dt;
        vy += ay * dt;
        x += vx * dt;
        y += vy * dt;
        // Reset
        ax = ay = 0;
        // Reset isOnGround cada frame
        isOnGround = false;
    }

    public void renderDebug() {
    	glBegin(GL_LINE_LOOP);
    		glVertex2f(x, y);
    		glVertex2f(x + width, y);
    		glVertex2f(x + width, y + height);
    		glVertex2f(x, y + height);
    	glEnd();
    }

    
    public void render() {
        glBegin(GL_QUADS);
        glVertex2f(x, y);
        glVertex2f(x + width, y);
        glVertex2f(x + width, y + height);
        glVertex2f(x, y + height);
        glEnd();
    }
}
