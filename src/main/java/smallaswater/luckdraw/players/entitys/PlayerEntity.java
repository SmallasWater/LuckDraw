package smallaswater.luckdraw.players.entitys;

import smallaswater.luckdraw.utils.Tools;

import java.util.Date;

/**
 * @author SmallasWater
 */
public class PlayerEntity extends BaseLuckEntity{


    public PlayerEntity(String name, int time,String date) {
        super(name, time, Tools.getDate(date));
    }

    public PlayerEntity(String name, int time) {
        super(name, time, new Date());
    }

    @Override
    public int getType() {
        return 1;
    }

    @Override
    public boolean isEntity() {
        return true;
    }
}
