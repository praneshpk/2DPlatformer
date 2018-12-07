package core.objects;

import processing.core.PVector;

import java.awt.*;
import java.util.UUID;

public class StaticPlatform extends Collidable
{
    public StaticPlatform(PVector pos, float w, float h, Color c)
    {
        id = UUID.randomUUID();
        type = Type.STATIC_PLATFORM;
        color = c;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    @Override
    public void update(float cycle)
    {
    }

    @Override
    public void handle(Collidable p)
    {
        if (p instanceof Player) {
            Player tmp = (Player) p;
            if (tmp.pos.y + PLAYER_SZ < rect.y + 10) {
                tmp.setGround(rect.y - PLAYER_SZ);
                tmp.pos.y = rect.y - PLAYER_SZ + 1;
            }
            p = tmp;
        }
    }
}
