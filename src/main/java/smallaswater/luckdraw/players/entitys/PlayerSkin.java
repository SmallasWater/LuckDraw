package smallaswater.luckdraw.players.entitys;

import smallaswater.luckdraw.utils.Tools;

import java.util.Date;

/**
 * @author SmallasWater
 */
public class PlayerSkin extends BaseLuckEntity{

    public PlayerSkin(String name,int time,String date){
        super(name,time,Tools.getDate(date));
    }

    @Override
    public int getType() {
        return 2;
    }

    public PlayerSkin(String name, int time) {
        super(name, time, new Date());
    }

    @Override
    public boolean isSkin() {
        return true;
    }
}
