package smallaswater.luckdraw.events;

import cn.nukkit.Player;
import cn.nukkit.event.Cancellable;
import cn.nukkit.event.HandlerList;
import cn.nukkit.event.player.PlayerEvent;
import cn.nukkit.level.Position;
import smallaswater.luckdraw.chests.Chest;

/**
 * @author 开箱事件
 */
public class PlayerOpenChestEvent extends PlayerEvent {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    public static HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    private Chest chest;

    private Position position;

    public PlayerOpenChestEvent(Player player, Chest chest, Position position){
        this.player = player;
        this.chest = chest;
        this.position = position;
    }

    public Position getPosition() {
        return position;
    }

    public Chest getChest() {
        return chest;
    }
}
