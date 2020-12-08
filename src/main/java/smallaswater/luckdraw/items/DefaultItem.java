package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.utils.CustomItem;
import smallaswater.luckdraw.utils.Tools;

public class DefaultItem extends BaseItems {

    private Item item;

    public DefaultItem(String name,Item item) {
        super(name);
        this.item = item;
    }

    public Item getItem() {
        return item;
    }

    @Override
    public boolean isDefaultItem() {
        return true;
    }

    @Override
    public void givePlayer(Player player) {
        super.givePlayer(player);
        player.getInventory().addItem(getItem());
    }

    @Override
    public void setSkin(ItemSkin skin) {
        if(skin == null){
            this.skin = new ItemSkin(item);
        }else{
            this.skin = skin;
        }
    }

    @Override
    public String toString() {
        String s;
        if(!item.hasCompoundTag()){
            s = item.getId()+":"+item.getDamage()+":"+item.getCount()+"&"+name+"@item";
        }else{
            s = LuckDraw.getInstance().getNameByTag(CustomItem.toStringItem(item))+"&"+name+"@nbt";
        }
        if(getCount() > 0){
            s += "&"+getCount();
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
}
