package smallaswater.luckdraw.utils;

import cn.nukkit.item.Item;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.scheduler.AsyncTask;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.items.*;


public class CustomItem {

    private static final String SPLIT_1 = "@";
    private static final String SPLIT_3 = "&";
    private static final String SPLIT_2 = ":";
    private static final String NBT = "nbt";
    private static final String NOT = "not";
    private static final String ITEM = "item";
    private static final String MONEY = "money";
    private static final String CMD = "cmd";
    private static final String ADDCMD = "addcmd";
    //264:0:1&钻石 * 1@item&100&<skin></skin> <item></item><entity></entity>&[sound=[]]
    //id&名称@nbt
    public static BaseItems toBaseItem(String str){
        BaseItems items = null;
        int count = 1;
        if(str.split(SPLIT_1).length > 1){
            String[] names = str.split(SPLIT_1)[0].split(SPLIT_3);
            String type = str.split(SPLIT_1)[1];
            if(type.split(SPLIT_3).length > 1){
                try{
                    count = Integer.parseInt(type.split(SPLIT_3)[1]);
                }catch (Exception ignored){}
                type = type.split(SPLIT_3)[0];
            }
            switch (type){
                case NBT:
                    Item item = toItem(names[0]);
                    if(item != null){
                        items = new DefaultItem(names[1],item);
                    }
                    break;
                case ITEM:
                    items = new DefaultItem(names[1],toStringItem(names[0]));
                    break;
                case MONEY:
                    try {
                        items = new MoneyItem(names[1],Integer.parseInt(names[0]));
                    }catch (Exception e){
                        return null;
                    }
                    break;
                case CMD:
                    items = new CommandItem(names[1],names[0]);
                    break;
                case ADDCMD:
                    items = new AddonCommandItem(names[1],names[0]);
                    break;
                default:break;
            }
        }
        ItemSound sound = ItemSound.toSound(str.trim());
        ItemMessage message = ItemMessage.toMessage(str.trim());
        if(items != null){
            if(sound != null){
                items.setSounds(sound);
            }
            if(message != null){
                items.setMsg(message);
            }
            items.setSkin(ItemSkin.getSkinByString(str));
            items.setCount(count);
        }


        return items;

    }

    //id:damage:count:nbt
    private static Item toItem(String str){
        String configString = LuckDraw.getNbtItem(str);
        if(!"".equals(configString)){
            String[] strings = configString.split(SPLIT_2);
            Item item = new Item(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
            item.setCount(Integer.parseInt(strings[2]));
            if(!NOT.equals(strings[3])){
                byte[] bytes = Tools.hexStringToBytes(strings[3]);
                if(bytes != null) {
                    CompoundTag tag = Item.parseCompoundTag(bytes);
                    item.setNamedTag(tag);
                }
            }
            return item;
        }
        return null;
    }

    public static Item toStringItem(String i){
        String[] items = i.split(SPLIT_2);
        if(items.length > 1){
            if(items.length > 2){
                Item item = Item.get(Integer.parseInt(items[0]),Integer.parseInt(items[1]));
                item.setCount(Integer.parseInt(items[2]));
                return item;
            }
            return Item.get(Integer.parseInt(items[0]),Integer.parseInt(items[1]));
        }
        return new Item(0,0);
    }

    public static String toStringSkinItem(Item item){
        String s;
        if(!item.hasCompoundTag()){
            s = item.getId()+":"+item.getDamage()+":"+item.getCount();
        }else{
            s = LuckDraw.getInstance().getNameByTag(CustomItem.toStringItem(item));
        }
        return s;

    }

    public static String toStringItem(Item item){
        String tag = "not";
        if(item.hasCompoundTag()){
            tag = Tools.bytesToHexString(item.getCompoundTag());
        }
        return item.getId()+":"+item.getDamage()+":"+item.getCount()+":"+tag;

    }



}
