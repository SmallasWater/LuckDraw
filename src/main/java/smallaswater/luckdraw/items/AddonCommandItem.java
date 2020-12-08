package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import smallaswater.luckdraw.utils.Tools;

/**
 * @author 若水
 */
public class AddonCommandItem extends BaseItems {

    private String entity;
    public AddonCommandItem(String name,String entity) {
        super(name);
        this.entity = entity;
    }

    @Override
    public String toString() {
        String s;
        if(getCount() > 0){
            s =  entity+"&"+name+"@addcmd&"+getCount();
        }else{
            s =  entity+"&"+name+"@addcmd";
        }
        s += "&"+skin.toString();
        if(sounds != null){
            s += "&"+sounds.toString();
        }
        if(msg != null){
            s += "&"+msg.toString();
        }
        return s;

    }

    @Override
    public boolean isAddItem() {
        return true;
    }

    public String getEntity() {
        return entity;
    }

    @Override
    public boolean isCommandItem() {
        return false;
    }

    @Override
    public void givePlayer(Player player) {
        super.givePlayer(player);
        Tools.setEntityPlayer(player,entity);

    }

    @Override
    public void setSkin(ItemSkin skin) {
        if(skin == null){
            this.skin = new ItemSkin(entity);
        }else{
            this.skin = skin;
        }
    }
}
