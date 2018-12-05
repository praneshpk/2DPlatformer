function update()
{
    var id = networkClient.id();
    var player = networkClient.users().get(id);
    player.setColor(255,255,255);
    images.put(PLAYER, rootpath + "/scripts/space-invaders/assets/tank.svg");

    print(player);
}