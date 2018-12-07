function update()
{
    if(event != null) {
        switch (event.type()) {
            case COLLISION:
                var eusers = event.data().get(USERS).values();
                eusers.forEach(function(e) {
                    client.users().get(e.getId()).setColor(e.getCollide().getColor());
                });
            case DEATH:
            case START_REC:
            case STOP_REC:
            case INPUT:
            case SPAWN:
            case LEAVE:
                break;
        }
    }
}