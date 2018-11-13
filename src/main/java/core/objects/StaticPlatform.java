package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

import static core.util.Constants.PLAYER_SZ;

public class StaticPlatform implements Collidable
{
    Type type = Type.STATIC_PLATFORM;
    protected final Rectangle rect;
    protected final PVector pos;
    protected final Color color;

    public StaticPlatform(PVector pos, float w, float h, Color c)
    {
        color = c;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    public Rectangle getRect()
    {
        return rect;
    }

    @Override
    public PVector getPos()
    {
        return pos;
    }

    @Override
    public Type type() { return type; }

    public void display(PApplet p, long cycle)
    {
        update(cycle);
        p.fill(color.getRGB());
        p.noStroke();
        p.rect(pos.x, pos.y, this.rect.width, this.rect.height);
    }

    public void update(long cycle)
    {

    }

    @Override
    public void handle(Collidable p)
    {
        if (p instanceof Player) {
            Player tmp = (Player) p;
            if (tmp.pos.y + PLAYER_SZ < rect.y + 10) {
                tmp.ground = rect.y - PLAYER_SZ;
                tmp.pos.y = rect.y - PLAYER_SZ + 1;
            }
            p = tmp;
        }
    }

    @Override
    public int compareTo(Collidable o) { return type.compareTo(o.type()); }
}
