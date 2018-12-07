package core.objects;

import processing.core.PVector;

import java.awt.*;
import java.util.UUID;

public class DeathZone extends Collidable
{
    public DeathZone(PVector pos, float w, float h)
    {
        id = UUID.randomUUID();
        type = Type.DEATH_ZONE;
        this.pos = pos;
        rect = new Rectangle((int) pos.x, (int) pos.y, (int) w, (int) h);
    }

    @Override
    public void update(float cycle) { }

    @Override
    public void handle(Collidable p)
    {
        if (p instanceof Player) {
            Player tmp = (Player) p;
            if(tmp.spawnPts != null)
                tmp.pos = tmp.spawnPts[(int)(p.pos.y + p.pos.x) % tmp.spawnPts.length].sub(0, PLAYER_SZ);
            tmp.rect = new Rectangle((int) tmp.pos.x, (int) tmp.pos.y, PLAYER_SZ, PLAYER_SZ);
            tmp.dir = 1;
            tmp.left = 0;
            tmp.right = 0;
            tmp.up = 0;
            tmp.velocity = new PVector(0, 0);
            p = tmp;
        }
    }
}
