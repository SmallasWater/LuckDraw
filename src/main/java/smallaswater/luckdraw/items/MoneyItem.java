package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import cn.nukkit.item.Item;
import me.onebone.economyapi.EconomyAPI;

/**
 * @author SmallasWater
 */
public class MoneyItem extends BaseItems {

    private final int money;
    public MoneyItem(String name,int money) {
        super(name);
        this.money = money;
    }

    @Override
    public void setSkin(ItemSkin skin) {
        if(skin == null){
            this.skin = new ItemSkin(new Item(175, 0));
        }else{
            this.skin = skin;
        }
    }

    public int getMoney() {
        return money;
    }

    @Override
    public void givePlayer(Player player) {
        super.givePlayer(player);
        EconomyAPI.getInstance().addMoney(player,money);
    }

    @Override
    public boolean isMoneyItem() {
        return true;
    }

    @Override
    public String toString() {
        String s = money+"&"+name+"@money";
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
