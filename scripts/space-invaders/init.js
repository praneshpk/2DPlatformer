function update()
{
    var id = networkClient.id();
    var player = networkClient.users.get(id);
    player.setColor(50,0,13);
    print(player);
}