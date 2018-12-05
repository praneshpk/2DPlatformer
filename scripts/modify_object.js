function update()
{
    platforms.forEach(function(e) {
        if(e.getType() == MOVING_PLATFORM)
            e.setColor(Math.floor(Math.random() * 255),Math.floor(Math.random() * 255),Math.floor(Math.random() * 255));
    });
}