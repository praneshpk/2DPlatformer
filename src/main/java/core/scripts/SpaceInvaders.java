package core.scripts;

import core.Main;
import core.network.Client;
import core.objects.*;
import core.util.time.LocalTime;
import processing.core.PApplet;
import processing.core.PShape;
import processing.core.PVector;

import java.awt.*;
import java.util.*;

public class SpaceInvaders extends Main
{
    static core.Server server;
    static HashMap<String, HashMap> gameData;
    boolean shot = false, sw = false;
    float t = 2000;
    Random r = new Random();
    int lives = 3;

    protected void renderObjects()
    {
        background(0);
        long time = client.time().getTime();
        if(time % 1600 < 100) {
            for (Collidable p : client.platforms()) {
                if(((LinkedList)gameData.get("enemy").get("id")).contains(p.getId())) {
                    if(!sw) {
                        p.getPos().y += 20;
                        ((MovingPlatform) p).reset();
                    }
                }
            }
            sw = true;
        } else if(time % 2000 < 200) {
            sw = false;
        }

        for (Collidable p : client.platforms()) {
            if(((LinkedList)gameData.get("enemy").get("id")).contains(p.getId())) {
                int iter = (int) (Math.ceil(time/100.0))*100;
                LinkedList<PShape> imgSheet = (LinkedList) gameData.get("enemy").get("img");
                p.display(this, imgSheet.get((iter/100)%imgSheet.size()), iter);
            } else if(((LinkedList)gameData.get("bullet").get("id")).contains(p.getId())) {
                LocalTime t = (LocalTime) ((HashMap) gameData.get("bullet").get("time")).get(p.getId());
                p.display(this, t.getTime());
            } else {
                p.display(this, client.time().getTime());
            }
        }
        for(Player p: client.users().values()) {
            if(((LinkedList)gameData.get("player").get("id")).contains(p.getId())) {
                LinkedList<PShape> imgSheet = (LinkedList) gameData.get("player").get("img");
                p.display(this, imgSheet.getFirst(), client.time().getTic()/TIC);
            } else {
                p.display(this, client.time().getTic()/TIC);
            }
        }
    }

    public void setup()
    {
        smooth();
        noStroke();
        for(HashMap<String, LinkedList> image : gameData.values())
            for (Object p : image.get("path"))
                image.get("img").add(loadShape((String) p));
    }

    @Override
    protected void updateObjects() {
        if((r.nextInt(client.platforms().size())) == 1) {
            MovingPlatform c = new MovingPlatform(new PVector(r.nextInt(WIDTH -100) + 100, 0),
                    8, 8, new PVector(0, -2000), 24, new Color(0xBFFFFF));
            client.platforms().add(c);
            ((LinkedList)gameData.get("bullet").get("id")).add(c.getId());
            LocalTime t = new LocalTime(client.time(), 1);
            t.reset();
            ((HashMap)gameData.get("bullet").get("time")).put(c.getId(), t);
        }
        for(Collidable c : client.platforms()) {
            if(((LinkedList)gameData.get("bullet").get("id")).contains(c.getId())) {
                if(c.getPos().y < 0)
                    shot = false;
                if(c.getPos().y < 0 || c.getPos().y > HEIGHT) {
                    client.platforms().remove(c);
                    ((HashMap)gameData.get("bullet").get("time")).remove(c.getId());

                    client.send(event_type.INPUT, true, client.platforms());
                    break;
                }
            }
            Collidable col = collision(c, client.platforms());
            if(col != null) {
                if(((LinkedList)gameData.get("bullet").get("id")).contains(col.getId())) {
                    if(((MovingPlatform)col).getDir().y > 0 &&
                            ((LinkedList)gameData.get("enemy").get("id")).contains(c.getId())) {
                        client.platforms().remove(c);
                        client.platforms().remove(col);
                        ((HashMap)gameData.get("bullet").get("time")).remove(c.getId());
                        client.send(event_type.INPUT, true, client.platforms());
                        shot = false;
                        break;
                    }
                    if(((LinkedList)gameData.get("barrier").get("id")).contains(c.getId())) {
                        c.setColor(c.getColor().darker());
                        client.platforms().remove(col);
                        ((HashMap)gameData.get("bullet").get("time")).remove(c.getId());
                        if(c.getColor().getGreen() < 40) {
                            client.platforms().remove(c);
                        }
                        client.send(event_type.INPUT, true, client.platforms());
                        if(((MovingPlatform)col).getDir().y > 0)
                            shot = false;
                        break;
                    }
                }
            }
        }
        for(Player p : client.users().values()) {
            Collidable col = collision(p, client.platforms());
            if(col != null) {
                if(((LinkedList)gameData.get("bullet").get("id")).contains(col.getId()))
                    client.platforms().remove(col);
                lives --;
                client.send(event_type.DEATH, false, p, client.users());
                if(lives < 1) {
                    noLoop();
                    delay(1000);
                    textSize(32);
                    text("GAME OVER!", WIDTH / 2 - 100, HEIGHT - 250);
                    client.close();
                }
            }
        }
    }

    public Collidable collision(Collidable c, Collection<Collidable> obj) {
        for(Collidable o : obj)
            if(o != c && o.getRect().intersects(c.getRect()))
                return o;
        return null;
    }

    public void draw()
    {
        renderObjects();
        client.handleEvent(client.receive());
        updateObjects();
        for(int i = 0; i < lives; i++ )
            shape((PShape)((LinkedList)gameData.get("player").get("img")).peek(), 20 + i*50, 20, 25, 25);

    }

    public void keyPressed()
    {
        Player player = client.users().get(client.id());
        switch (keyCode) {
            case LEFT:
                player.setLeft(-1);
                player.setDir(1);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case RIGHT:
                player.setRight(1);
                player.setDir(-1);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case 32:
                if(!shot) {
                    MovingPlatform c = new MovingPlatform(new PVector(player.getPos().x + PLAYER_SZ / 2  - 4, HEIGHT - PLAYER_SZ - 25),
                            8, 8, new PVector(0, 2000), 25, new Color(0xBFFFFF));
                    client.platforms().add(c);
                    ((LinkedList)gameData.get("bullet").get("id")).add(c.getId());
                    LocalTime t = new LocalTime(client.time(), 1);
                    t.reset();
                    ((HashMap)gameData.get("bullet").get("time")).put(c.getId(), t);
                    shot = true;
                }
                break;
        }
    }

    public void keyReleased()
    {
        Player player = client.users().get(client.id());
        switch (keyCode) {
            case LEFT:
                player.setLeft(0);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
            case RIGHT:
                player.setRight(0);
                client.send(client.event_type.INPUT, true, player, client.users());
                break;
        }
    }

    public static void main(String[] args) {
        gameData = new HashMap<>();
        gameData.put("player", new HashMap<>());
        gameData.put("enemy", new HashMap<>());
        gameData.put("bullet", new HashMap<>());
        gameData.put("barrier", new HashMap<>());

        for (HashMap vals : gameData.values()) {
            vals.put("id", new LinkedList<>());
            vals.put("path", new LinkedList<>());
            vals.put("img", new LinkedList<>());
            vals.put("time", new HashMap<>());
        }
        String root = System.getProperty("user.dir") + "/scripts/space-invaders/assets/";
        ((LinkedList)gameData.get("player").get("path")).add(root + "tank.svg");
        ((LinkedList)gameData.get("enemy").get("path")).add(root + "invader.svg");
        ((LinkedList)gameData.get("enemy").get("path")).add(root + "invader2.svg");

        LinkedList<Collidable> collidables = new LinkedList<>();
        for (int y = 0; y < 6; y++) {
            for (int x = 0; x < 14; x++) {
                Collidable c = new MovingPlatform(new PVector(x * 50, y * 50),
                        50, 50, new PVector(-25 * TIC, 0),
                        8, new Color(0xFFFFFF));
                collidables.add(c);
                ((LinkedList)gameData.get("enemy").get("id")).add(c.getId());
            }
        }

        for (int i = 3; i > 0; i--) {
            Collidable c = new StaticPlatform(
                    new PVector((WIDTH / 3) * i - 150, server.HEIGHT - 150),
                    50, 50, new Color(0, 255, 0));
            collidables.add(c);
            ((LinkedList)gameData.get("barrier").get("id")).add(c.getId());
        }

        server = new core.Server(collidables);

        new Thread(() -> {
            try {
                server.listen();
            } catch (Exception e) {
                server = null;
            }
        }).start();

        client = new Client(server.HOSTNAME, server.PORT);
        client.start();
        client.users().get(client.id()).setColor(Color.GREEN);
        ((LinkedList)gameData.get("player").get("id")).add(client.id());

        PApplet.main("core.scripts.SpaceInvaders", args);

    }
}
