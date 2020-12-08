package smallaswater.luckdraw.utils;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityHuman;
import cn.nukkit.entity.data.EntityMetadata;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.entity.item.EntityFirework;
import cn.nukkit.form.element.ElementButton;
import cn.nukkit.form.element.ElementButtonImageData;
import cn.nukkit.form.element.ElementDropdown;
import cn.nukkit.form.element.ElementInput;
import cn.nukkit.form.window.FormWindowCustom;
import cn.nukkit.form.window.FormWindowSimple;
import cn.nukkit.item.Item;
import cn.nukkit.item.ItemFirework;
import cn.nukkit.item.ItemSign;
import cn.nukkit.level.*;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.NBTIO;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.DoubleTag;
import cn.nukkit.nbt.tag.FloatTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.network.protocol.*;

import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.DyeColor;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.ChestEntity;
import smallaswater.luckdraw.commands.addons.LuckEntity;
import smallaswater.luckdraw.events.PlayerSkinChangeEvent;
import smallaswater.luckdraw.items.BaseItems;
import smallaswater.luckdraw.players.PlayerFile;
import smallaswater.luckdraw.players.entitys.BaseLuckEntity;
import smallaswater.luckdraw.tasks.ChunkEntitySkinTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * @author SmallasWater
 */
public class Tools {
    static byte[] hexStringToBytes(String hexString) {
        if (hexString == null || "".equals(hexString)) {
            return null;
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }


    /** 放烟花*/
    public static void spawnFirework(Position player) {

        Level level = player.getLevel();
        ItemFirework item = new ItemFirework();
        CompoundTag tag = new CompoundTag();
        Random random = new Random();
        CompoundTag ex = new CompoundTag();
        ex.putByteArray("FireworkColor",new byte[]{
                (byte) DyeColor.values()[random.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].getDyeData()
        });
        ex.putByteArray("FireworkFade",new byte[0]);
        ex.putBoolean("FireworkFlicker",random.nextBoolean());
        ex.putBoolean("FireworkTrail",random.nextBoolean());
        ex.putByte("FireworkType",ItemFirework.FireworkExplosion.ExplosionType.values()
                [random.nextInt(ItemFirework.FireworkExplosion.ExplosionType.values().length)].ordinal());
        tag.putCompound("Fireworks",(new CompoundTag("Fireworks")).putList(new ListTag<CompoundTag>("Explosions").add(ex)).putByte("Flight",1));
        item.setNamedTag(tag);
        CompoundTag nbt = new CompoundTag();
        nbt.putList(new ListTag<DoubleTag>("Pos")
                .add(new DoubleTag("",player.x+0.5D))
                .add(new DoubleTag("",player.y+0.5D))
                .add(new DoubleTag("",player.z+0.5D))
        );
        nbt.putList(new ListTag<DoubleTag>("Motion")
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
                .add(new DoubleTag("",0.0D))
        );
        nbt.putList(new ListTag<FloatTag>("Rotation")
                .add(new FloatTag("",0.0F))
                .add(new FloatTag("",0.0F))

        );
        nbt.putCompound("FireworkItem", NBTIO.putItemHelper(item));
        EntityFirework entity = new EntityFirework(level.getChunk((int)player.x >> 4, (int)player.z >> 4), nbt);
        entity.spawnToAll();
    }
    public static Position getPositionByString(String pos){
        String[] posList = pos.split(":");
        if(posList.length > 3){
            String level = posList[3];
            Level level1 = Server.getInstance().getLevelByName(level);
            if(level1 != null){
                return new Position(Integer.parseInt(posList[0])
                            ,Integer.parseInt(posList[1])
                            ,Integer.parseInt(posList[2]),level1);

            }

        }
        return null;
    }

    //转换Date为String
    public static String getDateToString(Date date){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        return lsdStrFormat.format(date);
    }


    //转换String为Date
    public static Date getDate(String format){
        SimpleDateFormat lsdStrFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return lsdStrFormat.parse(format);
        }catch (ParseException e){
            return null;
        }
    }

    // 获取相差天数
    public static int getOutDay(Date date)
    {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        long time1 = cal.getTimeInMillis();
        cal.setTime(new Date());
        long time2 = cal.getTimeInMillis();
        long betweenDays = (time2-time1)/(1000*3600*24);
        return Integer.parseInt(String.valueOf(betweenDays));
    }


    public static void setEntitySkin(EntityHuman human,Skin skin){
        PlayerSkinPacket data = new PlayerSkinPacket();
        data.skin = skin;
        data.newSkinName = skin.getSkinId();
        data.oldSkinName = human.getSkin().getSkinId();
        data.uuid = human.getUniqueId();
        human.setSkin(skin);
        Server.getInstance().getOnlinePlayers().values().forEach(player -> player.dataPacket(data));
    }

    public static CompoundTag getTag(Position player, Skin skin){
        CompoundTag tag = Entity.getDefaultNBT(new Vector3(player.x,player.y,player.z));
        tag.putCompound("Skin",new CompoundTag()
                .putByteArray("Data", skin.getSkinData().data)
                .putString("ModelId", skin.getSkinId()));
        return tag;
    }



    public static String positionToString(Position position){
        return (int)position.x+":"+(int)position.y+":"+(int)position.z+":"+position.getLevel().getName();
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static String bytesToHexString(byte[] src){
        StringBuilder stringBuilder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        for (byte aSrc : src) {
            int v = aSrc & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static AddEntityPacket getEntity(Position position, String name,String showName,long eid){
        Entity base = Entity.createEntity(name,position);
        AddEntityPacket pk = new AddEntityPacket();
        pk.type = base.getNetworkId();
        pk.entityUniqueId = eid;
        pk.entityRuntimeId = eid;
        pk.x = (float)(position.x + 0.5D);
        pk.y = (float)(position.y + 0.75D);
        pk.z = (float)(position.z + 0.5D);
        pk.speedX = 0.0F;
        pk.speedY = 0.0F;
        pk.speedZ = 0.0F;
        pk.speedX = 0;
        pk.speedY = 0;
        pk.speedZ = 0;
        pk.yaw = 0;
        pk.pitch = 0;
        long flags = 114688L;
        base.close();
        pk.metadata = (new EntityMetadata())
                .putLong(0, flags)
                .putString(4, showName)
                .putLong(37, -1L)
                .putFloat(38, 0.25F);
        return pk;
    }


    public static ChestEntity getEntitySkin(Position position, Skin skin,String showName,long eid){
        ChestEntity entity = new ChestEntity(position.getChunk(), Tools.getTag(position,skin),position.getLocation()){
            @Override
            public long getId() {
                return eid;
            }

            @Override
            public float getWidth() {
                return 0.2F;
            }

            @Override
            public float getHeight() {
                return 0.2F;
            }
        };

        entity.setSkin(skin);
        entity.x = position.x + 0.5D;
        entity.y = position.y + 0.75D;
        entity.z = position.z + 0.5D;
        entity.setNameTagAlwaysVisible(true);
        entity.setNameTag(showName);
        entity.setScale(0.25F);
        entity.setSkin(skin);
        skin.setTrusted(true);
//        Tools.setEntitySkin(entity,skin);
        return entity;

    }



    public static void showGUI(Chest chest,Player player){
        FormWindowSimple simple = new FormWindowSimple(chest.getName()+"--奖池详情","");
        StringBuilder text = new StringBuilder();
        StringBuilder builder = new StringBuilder();
        if(chest.getDefaultItem().size() > 0){
            builder.append("§e必定获得: §r\n");
            for (BaseItems items:chest.getDefaultItem()){
                builder.append(items.getName()).append("§r\n");
            }
            text.append(builder);
        }
        builder = new StringBuilder("§a奖池: §r\n");
        double maxCount  = 0;
        for (BaseItems items:chest.getItems()){
            maxCount += items.getCount();
        }
        double d = 0;
        if(maxCount > 0){
            d = (1 / maxCount);
        }
        for(BaseItems items:chest.getItems()){
            builder.append(items.getName()).append("    §b概率: §d").append(String.format("%.2f",d * items.getCount() * 100)).append("％§r\n");
        }
        text.append(builder);
        simple.setContent(text.toString());
        simple.addButton(new ElementButton("我知道了"));
        player.showFormWindow(simple,0x551AA);
    }


    public static AddItemEntityPacket getEntityTag(Position position, Item item,String showName,long eid){
        AddItemEntityPacket pk = new AddItemEntityPacket();
        pk.item = item;
        pk.entityRuntimeId = eid;
        pk.entityUniqueId = eid;
        pk.x = (float)(position.x + 0.5D);
        pk.y = (float)(position.y + 0.75D);
        pk.z = (float)(position.z + 0.5D);
        pk.speedX = 0.0F;
        pk.speedY = 0.0F;
        pk.speedZ = 0.0F;
        pk.metadata = new EntityMetadata()
                .putBoolean(Entity.DATA_FLAG_CAN_SHOW_NAMETAG,true)
                .putString(Entity.DATA_NAMETAG,showName)
                .putBoolean(80,true)
                .putBoolean(Entity.DATA_FLAG_IMMOBILE,true)
                .putLong(Entity.DATA_LEAD_HOLDER_EID, -1)
                .putFloat(Entity.DATA_SCALE, 1f)
                .putBoolean(Entity.DATA_FLAG_GRAVITY,false);

        return pk;
    }



    public static boolean isContainEntity(String entityName){
        Entity base = Entity.createEntity(entityName,Server.getInstance().getDefaultLevel().getSafeSpawn().add(0,-2));
        if(base == null){
            return false;
        }else{
            base.close();
        }
        return true;
    }

    public static boolean setEntityPlayer(Player player,String entityName,boolean message){
        if(LuckDraw.getInstance().getBanEntitys().contains(entityName)){
            player.sendMessage(TextFormat.RED+"实体 "+entityName+"禁止使用");
            return false;
        }
        if("close".equalsIgnoreCase(entityName)){
            if(player.namedTag.contains("createentity")) {
                Tools.addParticle(player);
                LuckEntity.removeEntitySkin(player);
            }
            return false;
        }
        Entity base = Entity.createEntity(entityName,player);
        if(base == null){
            return false;
        }
        if(player.namedTag.contains("createentity") && !player.namedTag.getString("createentity").equalsIgnoreCase(entityName)){
            LuckEntity.removeEntitySkin(player,true);
        }

        LuckEntity entity = new LuckEntity(player.chunk,Entity.getDefaultNBT(player),base.getNetworkId(),player){
            @Override
            public float getWidth() {
                return base.getWidth();
            }

            @Override
            public float getHeight() {
                return base.getHeight();
            }
        };
        entity.setEntityName(entityName);
        base.close();
        PlayerSkinChangeEvent event = new PlayerSkinChangeEvent(player,entity);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return false;
        }
        final LuckEntity entity1 = (LuckEntity) event.getEntity();
        entity1.setMaxHealth(player.getMaxHealth());
        entity1.setHealth(player.getHealth());
        player.namedTag.putLong("createentity",entity1.getId());
        if(message) {
            player.sendMessage("§e你被变成" + entityName + "啦");
        }
        entity1.namedTag.putString("monsterName",player.getName());
        entity1.setScale(player.getScale());
        ChunkEntitySkinTask.entityHashMap.put(player.getName(),entity1);
        entity1.setNameTagAlwaysVisible();
        return true;


    }

    public static boolean setEntityPlayer(Player player, String entityName){
        return setEntityPlayer(player,entityName,true);
    }

    public static String readFile(File file){
        String content = "";
        try{
            content = Utils.readFile(file);
        }catch (IOException e){
            e.printStackTrace();
        }
        return content;
    }

    public static void addParticle(Player player){
        player.getLevel().addParticleEffect(player.getPosition().add(0.5), ParticleEffect.EXPLOSION_DEATH);
        player.getLevel().addParticleEffect(player.getPosition().add(0,0,0.5), ParticleEffect.EXPLOSION_DEATH);
        player.getLevel().addParticleEffect(player.getPosition(), ParticleEffect.EXPLOSION_DEATH);
        player.getLevel().addParticleEffect(player.getPosition().add(0,0,-0.5), ParticleEffect.EXPLOSION_DEATH);
        player.getLevel().addParticleEffect(player.getPosition().add(-0.5), ParticleEffect.EXPLOSION_DEATH);
    }
    public static String getSubUtilSimple(String soap, String rgex){
        Pattern pattern = Pattern.compile(rgex);
        Matcher m = pattern.matcher(soap);
        if(m.find()){
            return m.group(1);
        }
        return "";
    }


    public static Sound getSoundByName(String name){
        Sound sound;
        try {
            sound =  Sound.valueOf(name.toUpperCase().replace(".","_"));
            return sound;
        }catch (IllegalArgumentException e){
            return null;
        }
    }

    public static Object[] decodeData(String data){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(data,(new TypeToken<Object[]>() {
        }) .getType());
    }

    public static void sendInputGUI(Player player){
        FormWindowCustom custom = new FormWindowCustom("重命名");
        custom.addElement(new ElementInput("请输入名称.."));
        player.showFormWindow(custom,0x55AAB214);
    }

    public static void sendChangeSkinGUI(Player player){
        PlayerFile file = PlayerFile.getPlayerFile(player.getName());
        file.changeTime();
        FormWindowSimple simple = new FormWindowSimple("变身系统","");
        for(BaseLuckEntity entity:file.getEntities()){
            simple.addButton(new ElementButton("§e>>§a"+("".equals(entity.getShowName())?entity.getName()+"§7(初始名)"
                    :entity.getShowName())+"§e<<§a\n§b时效: "+(entity.getTime() == -1?"§d永久":"§a"
                    +(entity.getTime() - Tools.getOutDay(entity.getStartTime()))+" §7天")
                    ,new ElementButtonImageData("path","textures/ui/MashupIcon")));
        }
        if(simple.getButtons().size() == 0){
            simple.setContent("当前没有任何变身...");
        }else{
            if(player.namedTag.contains("createentity")) {
                simple.addButton(new ElementButton("还原当前变身"));
            }
        }
        player.showFormWindow(simple,0x55AAB213);
    }

    private static void sendAddItemGUI(Player player,String name,int type,String customString){
        FormWindowCustom custom = new FormWindowCustom("添加物品");
        custom.addElement(new ElementInput("请输入你要添加的奖励","例如: "+name,name));
        LinkedList<String> linkedList = new LinkedList<String>(){
            {
                add("普通物品 (@item)");
                add("nbt物品 (@nbt)");
                add("指令 (@cmd)");
                add("金钱 (@money)");
            }
        };
        custom.addElement(new ElementDropdown("请选择奖励类型",linkedList,type));
        custom.addElement(new ElementInput("请输入自定义名称","例如 "+customString,customString));
        custom.addElement(new ElementInput("请输入奖励数量 (默认为1)","例如: 1","1"));
        player.showFormWindow(custom,0x55AAB211);
    }

    public static void sendRemoveItemGUI(Player player,Chest chest){
        int id = 0x55AAB212;
        FormWindowSimple simple = new FormWindowSimple("移除物品--点击按键删除","");
        for(BaseItems i:chest.getDefaultItem()){
            simple.addButton(new ElementButton(i.getName()+" (固定)"));
        }
        for(BaseItems i:chest.getItems()){
            simple.addButton(new ElementButton(i.getName()+" (奖励)"));
        }
        player.showFormWindow(simple,id);

    }

    public static void sendAddItemGUI(Player player){
        Item item = player.getInventory().getItemInHand();
        String name = item.getId()+":"+item.getDamage()+":"+item.getCount();
        String custom = ItemIDSunName.getIDByName(item)+" * "+item.getCount();
        int type = 0;
        if(item.hasCompoundTag()){
            name =  new Random().nextInt(25565)+"";
            type = 1;
            if(item.hasCustomName()){
                custom = item.getCustomName();
            }
        }else{
            if(item instanceof ItemSign){
                name = "me 这是一个指令示范";
                type = 2;
                custom = "指令奖励";
            }
            if(item.getId() == 175){
                name = "100";
                type = 3;
                custom = "金币 * 100";
            }
        }
        sendAddItemGUI(player,name,type,custom);
    }

    private static Skin getPlayerSkinBySkin(Player player, Skin skin){
        Skin skin1 = player.getSkin();
        LinkedHashMap<String,Object> hashMap = decodeSkin(skin1);
        LinkedHashMap<String,Object> hashMap2 = decodeSkin(skin);
        for(String s:hashMap2.keySet()){
            hashMap.put(s,hashMap2.get(s));
        }
        skin1.setGeometryData(toJson(hashMap));
        return skin1;
    }

    private static LinkedHashMap<String,Object> decodeSkin(Skin skin){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.fromJson(skin.getGeometryData(), (new TypeToken<LinkedHashMap<String, Object>>() {
        }).getType());
    }

    private static String toJson(LinkedHashMap<String, Object> map){
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        return gson.toJson(map);
    }


    public static void setPlayerSkin(Player player,Skin skin){
        PlayerSkinChangeEvent event = new PlayerSkinChangeEvent(player,skin);
        Server.getInstance().getPluginManager().callEvent(event);
        if(event.isCancelled()){
            return;
        }
        PlayerSkinPacket pk = new PlayerSkinPacket();
        Skin oldSkin = player.getSkin();
        pk.skin = event.getSkin();
        pk.newSkinName = skin.getSkinId();
        pk.oldSkinName = oldSkin.getSkinId();
        pk.uuid = player.getUniqueId();
        player.dataPacket(pk);
        player.setSkin(skin);
    }

    //面向玩家
    public static Location getLocationByPlayer(Player player,Location location){
        return Location.fromObject(new Vector3(location.x,location.y,location.z),location.level,0.0);
    }


    /**
     * 反射获取 Entity
     * */
    public static Entity getEntity(String name){
        try {
            Class<?> entityClass = Class.forName("cn.nukkit.entity.Entity");
            Field field = entityClass.getDeclaredField("knownEntities");
            field.setAccessible(true);
            Object obj = field.get(entityClass.newInstance());
            if(obj instanceof Map){
                if(((Map) obj).containsKey(name)){
                    Object o = ((Map) obj).get(name);
                    if(o instanceof Entity){
                        return (Entity) o;
                    }
                }
            }

        } catch (ClassNotFoundException | NoSuchFieldException | IllegalAccessException | InstantiationException e) {
            e.printStackTrace();
        }
        return null;
    }

}
