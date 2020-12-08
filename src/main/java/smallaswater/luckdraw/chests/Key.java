package smallaswater.luckdraw.chests;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.utils.Config;
import smallaswater.luckdraw.LuckDraw;

/**
 * @author 若水
 */
public class Key implements Cloneable{

    public static final String TAG = "LuckKey";
    public static final String SHOW_NAME = "LuckKeyName";

    private String showName;

    private String name;

    private int count;

    private Item keyItem;


    public Key(String name,String showName,int count,Item item){
        this.name = name;
        this.showName = showName;
        this.count = count;
        this.keyItem = item;
    }

    public static Key getInstance(String name){
        if(LuckDraw.getInstance().keys.containsKey(name)){
            return LuckDraw.getInstance().keys.get(name);
        }
        return null;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getKey() {
        return name;
    }

    public String getShowName() {
        return showName;
    }

    public int getCount() {
        return count;
    }

    public Item getKeyItem() {
        return keyItem;
    }

    public static boolean isKey(Item item){
        if(item.hasCompoundTag()){
            CompoundTag tag = item.getNamedTag();
            return !"".equals(tag.getString(TAG));
        }
        return false;
    }

    @Override
    public Key clone() {
        try {
            return (Key) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }



    public static Key getKeyByItem(Item item){
        if(isKey(item)){
            CompoundTag tag = item.getNamedTag();
            return new Key(tag.getString(TAG),tag.getString(SHOW_NAME),item.getCount(),item);
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Key){
            return getKey().equals(((Key) obj).getKey());
        }
        return false;
    }
}
