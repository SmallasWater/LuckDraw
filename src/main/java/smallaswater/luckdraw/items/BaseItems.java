package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.level.Sound;
import smallaswater.luckdraw.LuckDraw;

import java.util.LinkedList;

abstract public class BaseItems {
    protected String name;

    private int count;

    public ItemSkin skin;

    public ItemSound sounds;

    public ItemMessage msg = null;


    public static LinkedList<String> types = new LinkedList<String>(){
        {
            add("@item");
            add("@nbt");
            add("@cmd");
            add("@money");
        }
    };


    public BaseItems(String name){
        this(name,1);
    }

    public BaseItems(String name,int count){
        this.name = name;
        this.count = count;
    }

    public void setSkin(ItemSkin skin) {
        this.skin = skin;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    public ItemSound getSounds() {
        return sounds;
    }

    public ItemMessage getMsg() {
        return msg;
    }

    public void setMsg(ItemMessage msg) {
        this.msg = msg;
    }

    public void setSounds(ItemSound sounds) {
        this.sounds = sounds;
    }

    public String getName() {
        return name;
    }

    public boolean isCommandItem(){
        return false;
    }

    public boolean isDefaultItem(){
        return false;
    }

    public boolean isAddItem(){
        return false;
    }



    public ItemSkin getSkin() {
        return skin;
    }

    public void givePlayer(Player player){
        if(msg != null){
            if(!"".equals(msg.getMsg())){
                player.sendMessage(msg.getMsg().replace("{name}",getName()).replace("{player}",player.getName()));
            }
            if(!"".equals(msg.getBroad())){
                Server.getInstance().broadcastMessage(msg.getBroad().replace("{name}",getName()).replace("{player}",player.getName()));
            }
        }else{
            player.sendMessage(LuckDraw.getLanguage("player.get.item").replace("{name}",getName()));
        }

    }

    public boolean isMoneyItem(){
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseItems){
            return ((BaseItems) obj).getName().equals(name);
        }
        return false;
    }
}
