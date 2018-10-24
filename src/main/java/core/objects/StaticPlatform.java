package core.objects;

import processing.core.PApplet;
import processing.core.PVector;

import java.awt.*;

import static core.util.Constants.PLAYER_SZ;

public class StaticPlatform implements Collidable {

    protected Rectangle rect;
    protected PVector pos;
    protected Color color;

    public StaticPlatform(PVector pos, float w, float h, Color c)
    {
        color = c;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    public Rectangle getRect() { return rect; }

    public void display(PApplet p, long cycle)
    {
        update(cycle);
        p.fill(color.getRGB());
        p.noStroke();
        p.rect(pos.x, pos.y, rect.width, rect.height);
    }

    public void update(long cycle) {

    }

    @Override
    public void handle(Player p) {
        if (p.pos.y + PLAYER_SZ < rect.y + 10) {
            p.ground = rect.y - PLAYER_SZ;
            p.pos.y = rect.y - PLAYER_SZ + 1;
        }
    }
}
