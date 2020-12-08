package smallaswater.luckdraw.items;

import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;

import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.utils.CustomItem;
import smallaswater.luckdraw.utils.Tools;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author SmallasWater
 */
public class ItemSkin {

    public static final int ITEM = 0;
    public static final int SKIN = 1;
    public static final int ENTITY = 2;

    private int type;

    public Item item = null;

    public Skin skin = null;

    public String entity = null;

    public int getType() {
        return type;
    }

    ItemSkin(Item item){
        this.item = item;
        this.type = ITEM;
    }

    private ItemSkin(Skin skin){
        this.skin = skin;
        this.type = SKIN;
    }

    ItemSkin(String entity){
        this.entity = entity;
        this.type = ENTITY;
    }

    public static ItemSkin getSkinByString(String str){
        int type = -1;
        String s = "";
        String itemString = "<item>(.*?)</item>";
        String string = Tools.getSubUtilSimple(str, itemString);
        if(!"".equals(string)){
            type = ITEM;
            s = string;

        }else{
            String skinString = "<skin>(.*?)</skin>";
            string = Tools.getSubUtilSimple(str, skinString);
            if(!"".equals(string)){
                type = SKIN;
                s = string;

            }else{
                String entityString = "<entity>(.*?)</entity>";
                string = Tools.getSubUtilSimple(str, entityString);
                if(!"".equals(string)){
                    type = ENTITY;
                    s = string;
                }
            }
        }
        ItemSkin skin = null;
        switch (type){
            case ITEM:
                skin = new ItemSkin(CustomItem.toStringItem(s));
                break;
            case SKIN:
                if(LuckDraw.getInstance().chestSkins.containsKey(s)){
                    skin = new ItemSkin(LuckDraw.getInstance().chestSkins.get(s));
                }
                break;
            case ENTITY:
                skin = new ItemSkin(s);
                default:break;
        }
        if(skin != null){
            skin.type = type;
        }
        return skin;
    }



    @Override
    public String toString() {
        switch (type){
            case 0:
                return "<item>"+ CustomItem.toStringSkinItem(item) +"</item>";
            case 1:
                return "<skin>"+skin.getSkinId()+"</skin>";
            case 2:
                return "<entity>"+entity+"</entity>";
            default:break;

        }
        return "";
    }
}
