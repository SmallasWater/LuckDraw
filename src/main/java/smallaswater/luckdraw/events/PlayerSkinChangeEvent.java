package smallaswater.luckdraw.events;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;

/**
 * @author  SmallasWater
 */
public class PlayerSkinChangeEvent extends PlayerEvent implements Cancellable {

    private Skin skin;

    private Entity entity;
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public PlayerSkinChangeEvent(Player player,Skin skin){
        this.player = player;
        this.skin = skin;
    }
    public PlayerSkinChangeEvent(Player player,Entity entity){
        this.player = player;
        this.entity = entity;
    }

    public Entity getEntity() {
        return entity;
    }

    public Skin getSkin() {
        return skin;
    }
}

