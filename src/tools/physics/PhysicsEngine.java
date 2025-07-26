package tools.physics;

import java.util.ArrayList;
import java.util.List;

public class PhysicsEngine {
    private final List<PhysicsObject> objects = new ArrayList<PhysicsObject>();

    public void addObject(PhysicsObject obj) {
        objects.add(obj);
    }

    public List<PhysicsObject> getObjects() {
        return objects;
    }

    public void update(float dt) {
        // Aplicar gravedad positiva en Y (hacia abajo)
        for (PhysicsObject obj : objects) {
            if (!obj.isStatic) obj.applyForce(0, obj.mass * 1000f);
        }

        // Actualizar movimiento
        for (PhysicsObject obj : objects) {
            obj.update(dt);
        }

        // Colisiones
        for (int i = 0; i < objects.size(); i++) {
            for (int j = i + 1; j < objects.size(); j++) {
                PhysicsObject a = objects.get(i);
                PhysicsObject b = objects.get(j);
                if (checkCollisionAABB(a, b)) {
                    resolveCollisionAABB(a, b);
                }
            }
        }
    }

    private boolean checkCollisionAABB(PhysicsObject a, PhysicsObject b) {
        return a.x < b.x + b.width && a.x + a.width > b.x
            && a.y < b.y + b.height && a.y + a.height > b.y;
    }

    private void resolveCollisionAABB(PhysicsObject a, PhysicsObject b) {
        if (a.isStatic && b.isStatic) return;

        float overlapX = Math.min(a.x + a.width - b.x, b.x + b.width - a.x);
        float overlapY = Math.min(a.y + a.height - b.y, b.y + b.height - a.y);

        // Colisión vertical
        if (overlapY < overlapX) {
            // Determinar dirección y resolver
            if (!a.isStatic) {
                if (a.y < b.y) {
                    a.y -= overlapY;
                    a.vy = 0;
                    a.isOnGround = true;
                } else {
                    a.y += overlapY;
                    a.vy = 0;
                }
            }
            if (!b.isStatic) {
                if (b.y < a.y) {
                    b.y -= overlapY;
                    b.vy = 0;
                    b.isOnGround = true;
                } else {
                    b.y += overlapY;
                    b.vy = 0;
                }
            }
        } else {
            // Colisión horizontal
            if (!a.isStatic) {
                if (a.x < b.x) {
                    a.x -= overlapX;
                } else {
                    a.x += overlapX;
                }
                a.vx = 0;
            }
            if (!b.isStatic) {
                if (b.x < a.x) {
                    b.x -= overlapX;
                } else {
                    b.x += overlapX;
                }
                b.vx = 0;
            }
        }
    }
}