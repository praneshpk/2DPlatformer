package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

public class MovingPlatform extends StaticPlatform implements Collidable {
    private PVector dir;
    private long start = System.currentTimeMillis();
    private float damping = .005f;

    public MovingPlatform(PApplet p, PVector pos, float w, float h, Color c, PVector dir)
    {
        super(p,pos,w,h,c);
        this.dir = dir;
    }

    public void update()
    {
        long elapsed = (System.currentTimeMillis() - start)/1000;
        if(elapsed < 3) {
            pos.x -= pos.x * dir.x * damping;
            pos.y -= pos.y * dir.y * damping;
        }
        else if(elapsed >= 3 && elapsed < 6) {
            pos.x += pos.x * dir.x * damping;
            pos.y += pos.y * dir.y * damping;
        }
        else {
            start = System.currentTimeMillis();
        }
        System.out.println("time: " + elapsed + " pos: " + pos);

    }
}
