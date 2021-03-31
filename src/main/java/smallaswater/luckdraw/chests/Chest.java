package smallaswater.luckdraw.chests;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.BlockChest;
import cn.nukkit.item.Item;
import cn.nukkit.level.*;
import cn.nukkit.level.particle.DestroyBlockParticle;
import cn.nukkit.level.particle.HugeExplodeSeedParticle;
import cn.nukkit.level.particle.SmokeParticle;
import cn.nukkit.math.Vector3;
import cn.nukkit.network.protocol.*;
import cn.nukkit.scheduler.Task;
import cn.nukkit.utils.Config;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.events.PlayerOpenChestEvent;
import smallaswater.luckdraw.items.BaseItems;
import smallaswater.luckdraw.items.ItemSkin;
import smallaswater.luckdraw.utils.CustomItem;
import smallaswater.luckdraw.utils.Tools;

import java.io.File;
import java.util.*;

/**
 * 抽奖箱
 * @author 若水
 */
public class Chest {

    private String shownName;

    private int count;

    private LinkedList<BaseItems> defaultItem = new LinkedList<>();

    private String name;

    private Key key;

    private boolean canBroadCast;

    private LinkedList<BaseItems> items;

    private String skinName;

    private boolean canUse;

    private double scale = 1.0D;



    private LinkedList<Position> positions = new LinkedList<>();

    public static Chest getInstance(String name){
        if(LuckDraw.getInstance().chests.containsKey(name)){
            return LuckDraw.getInstance().chests.get(name);
        }
        return null;
    }

    public Chest(String name,String showName,Key key,  LinkedList<BaseItems> items){
        this.shownName = showName;
        this.name = name;
        this.key = key;
        this.items = items;
    }

    public String getShownName() {
        return shownName;
    }

    public String getName() {
        return name;
    }

    public Key getKey() {
        return key;
    }

    public LinkedList<BaseItems> getItems() {
        return items;
    }

    public void setPositions(LinkedList<Position> positions) {
        this.positions = positions;
    }

    public LinkedList<Position> getPositions() {
        return positions;
    }



    public boolean removeItem(BaseItems item){
        return items.remove(item);
    }

    public boolean addItem(BaseItems item){
        return items.add(item);
    }

    public boolean canUseKey(Key key){
        if(this.key.equals(key)){
            return this.key.getCount() <= key.getCount();
        }
        return false;
    }

    public static Chest getChest(String name,Config config){
        Key key = Key.getInstance(config.getString("使用key"));
        if(key != null){
            key = key.clone();
            key.setCount(config.getInt("消耗数量"));
            List<String> items = config.getStringList("奖励物品");
            LinkedList<BaseItems> items1 = new LinkedList<>();
            for(String text:items){
                BaseItems items2 = CustomItem.toBaseItem(text);
                if(items2 != null){
                    items1.add(items2);
                }
            }
            if(items1.size() > 0){
                Chest chest = new Chest(
                        name,
                        config.getString("名称"),
                        key,
                        items1
                );
                chest.setCanBroadCast(config.getBoolean("开启宝箱是否全服公告",false));
                if(config.getStringList("抽奖箱坐标").size() > 0){
                    LinkedList<Position> positions = new LinkedList<>();
                    for(String pos:config.getStringList("抽奖箱坐标")){
                        Position position = Tools.getPositionByString(pos);
                        if(position != null){
                            positions.add(position);
                        }
                    }
                    chest.setPositions(positions);
                }
                chest.setCount(config.getInt("抽取个数",1));
                List<String> list = config.getStringList("固定奖励");
                if(list.size() > 0){
                    LinkedList<BaseItems> defaultItems = new LinkedList<>();
                    for(String i:list){
                        BaseItems items2 = CustomItem.toBaseItem(i);
                        if(items2 != null){
                            defaultItems.add(items2);
                        }

                    }
                    chest.setDefaultItem(defaultItems);
                }
                chest.setCanUse(config.getBoolean("是否使用模型",true));
                chest.setSkinName(config.getString("模型名称","skin1"));
                chest.setScale(config.getDouble("模型大小",1.0));
                return chest;
            }
        }
        return null;
    }

    public void setScale(double scale) {
        this.scale = scale;
    }

    public double getScale() {
        return scale;
    }

    public void setSkinName(String skinName) {
        this.skinName = skinName;
    }

    public String getSkinName() {
        return skinName;
    }

    public boolean isCanUse() {
        return canUse;
    }

    public void setCanUse(boolean canUse) {
        this.canUse = canUse;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public void setDefaultItem(LinkedList<BaseItems> defaultItem) {
        this.defaultItem = defaultItem;
    }

    public int getCount() {
        return count;
    }

    public LinkedList<BaseItems> getDefaultItem() {
        return defaultItem;
    }

    public boolean removeDefaultItem(BaseItems items){
        return defaultItem.remove(items);
    }

    public boolean addDefaultItem(BaseItems items){
        return defaultItem.add(items);
    }

    public boolean isCanBroadCast() {
        return canBroadCast;
    }

    public void setCanBroadCast(boolean canBroadCast) {
        this.canBroadCast = canBroadCast;
    }

    public static boolean create(String name, Key key){
        if(!LuckDraw.getInstance().getChestFile(name).exists()){
            LuckDraw.getInstance().saveResource("chest.yml","/Chests/"+name+".yml",false);
            Config config = new Config(LuckDraw.getInstance().getChestFile(name),Config.YAML);
            String names = config.getString("名称")
                    .replace("%name%",name)
                    .replace("%key%",key.getShowName());
            config.set("名称",names);
            config.set("使用key",key.getKey());
            config.set("消耗数量",key.getCount());
            config.save();
            Chest chest = getChest(name,config);
            LuckDraw.getInstance().chests.put(name,chest);
            return true;
        }
        return false;
    }



    /**
     * 放置
     * */
    public boolean place(Location position){
        if(!positions.contains(position)){
            positions.add(position);
            save();
            return true;
        }
        return false;
    }

    public boolean canUse(Player player){
        Item item = getKey(player,key);
        if(item != null) {
            Key key = Key.getKeyByItem(item);
            if (key != null) {
                return canUseKey(key);
            }
        }
        return false;
    }

    /**
     * 获取随机物品
     * */
    private BaseItems getRoundBaseItem(){
        LinkedList<BaseItems> items = new LinkedList<>();
        for(BaseItems items1:this.items){
            for(int a = 0;a < items1.getCount();a++){
                items.add(items1);
            }
        }
        if(items.size() > 0){
            Collections.shuffle(items);
            return items.get(new Random().nextInt(items.size()));
        }
        return null;

    }

    private void openChest(Player player,ChestEntity position){
        PlayerOpenChestEvent event = new PlayerOpenChestEvent(player,this,position);
        Server.getInstance().getPluginManager().callEvent(event);
        Level level = position.getLevel();
        if (level != null) {
            level.addSound(position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTOPEN);
        }

        while (position.pitch < 260){
            position.pitch += 10;

        }
        Tools.spawnFirework(position.getChestPos());
        addItem(player,position.getChestPos(),position);

    }


    private void openChest(Player player,BlockChest position){
        PlayerOpenChestEvent event = new PlayerOpenChestEvent(player,this,position);
        Server.getInstance().getPluginManager().callEvent(event);
        BlockEventPacket pk = new BlockEventPacket();
        pk.x = (int) position.getX();
        pk.y = (int) position.getY();
        pk.z = (int) position.getZ();
        pk.case1 = 1;
        pk.case2 = 2;
        Level level = position.getLevel();
        if (level != null) {
            level.addSound(position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTOPEN);
            level.addChunkPacket((int) position.getX() >> 4, (int) position.getZ() >> 4, pk);
            Tools.spawnFirework(position);
            addItem(player,position,null);
        }
    }

    private void addItem(Player player,Position position,ChestEntity chest){
        LinkedHashMap<Object,BaseItems> entityPackets = new LinkedHashMap<>();
        long eid;
        if(defaultItem.size() > 0){
            for(BaseItems items1:defaultItem){
                eid = (long) ((int) position.x + new Random().nextDouble() + (int) position.z + new Random().nextDouble()) + new Random().nextLong();
                addEntitys(position, entityPackets, eid, items1);

            }
        }

        for(int i = 0;i < getCount();i++){
            eid =  (long) ((int) position.x + new Random().nextDouble() + (int) position.z + new Random().nextDouble()) + new Random().nextLong();
            BaseItems items = getRoundBaseItem();
            if(items != null){
                addEntitys(position, entityPackets, eid, items);
            }
        }
        if (entityPackets.size() > 0) {
            entityPackets.keySet().forEach(entityItem -> {
                if(entityItem instanceof DataPacket){
                    Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), (DataPacket) entityItem);
                }
                if(entityItem instanceof ChestEntity){
                    ((ChestEntity) entityItem).spawnToAll();
                }
                BaseItems chest1 = entityPackets.get(entityItem);
                if(chest1.getSounds()!= null){
                    chest1.getSounds().getSounds().forEach(sound -> player.getLevel().addSound(player,sound));
                }
            });
            entityPackets.values().forEach(baseItem-> baseItem.givePlayer(player));
            if(isCanBroadCast()){
                Server.getInstance().broadcastMessage(LuckDraw.getLanguage("openchest.broadcast")
                        .replace("%p",player.getName()).replace("{name}",name));
            }


            Server.getInstance().getScheduler().scheduleDelayedTask(new Task() {
                @Override
                public void onRun(int i) {
                    entityPackets.keySet().forEach(dataPacket -> {
                        if(dataPacket instanceof AddEntityPacket){
                            removePacket(((AddEntityPacket) dataPacket).entityUniqueId,position);
                        }
                        if(dataPacket instanceof AddItemEntityPacket){
                            removePacket(((AddItemEntityPacket) dataPacket).entityUniqueId,position);
                        }
                        if(dataPacket instanceof ChestEntity){
                            ((ChestEntity) dataPacket).kill();
                        }
                    });

                    if(chest == null){
                        BlockEventPacket pk = new BlockEventPacket();
                        pk.x = (int) position.getX();
                        pk.y = (int) position.getY();
                        pk.z = (int) position.getZ();
                        pk.case1 = 1;
                        pk.case2 = 0;
                        Level level = position.getLevel();
                        if (level != null) {
                            level.addSound(position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTCLOSED);
                            level.addChunkPacket((int) position.getX() >> 4, (int) position.getZ() >> 4, pk);
                        }
                        LuckDraw.getInstance().lock.remove(position);

                    }else{
                        Level level = position.getLevel();

                        if (level != null) {
                            while (chest.pitch > 0){
                                chest.pitch -= 10;
                            }
                            level.addSound(position.add(0.5, 0.5, 0.5), Sound.RANDOM_CHESTCLOSED);
                        }
                        LuckDraw.getInstance().lock.remove(chest.getChestPos());
                    }


                }
            }, 60);
        }else{
            player.sendMessage(LuckDraw.getLanguage("chest.notitem").replace("{name}",name));
        }
    }

    private void addEntitys(Position position, LinkedHashMap<Object, BaseItems> entityPackets, long eid, BaseItems items1) {
        ItemSkin pk = items1.getSkin();
        switch (pk.getType()){
            case ItemSkin.ITEM:
                entityPackets.put(Tools.getEntityTag(position,pk.item,items1.getName(),eid),items1);
                break;
            case ItemSkin.ENTITY:
                entityPackets.put(Tools.getEntity(position,pk.entity,items1.getName(),eid),items1);
                break;
            case ItemSkin.SKIN:
                entityPackets.put(Tools.getEntitySkin(position,pk.skin,items1.getName(),eid),items1);
                break;
            default:break;
        }
    }



    public void use(Player player,ChestEntity entity){
        if(LuckDraw.getInstance().lock.contains(entity.getChestPos())){
            player.sendMessage(LuckDraw.getLanguage("click.frist"));
        }else{
            if(canUse(player)){
                Item key = getKey(player,this.key);
                LuckDraw.getInstance().lock.add(entity.getChestPos());
                key.setCount(this.key.getCount());
                player.getInventory().removeItem(key);
                openChest(player,  entity);
                return;
            }else{
                player.sendMessage(LuckDraw.getLanguage("player.notkey").replace("{key}",this.key.getShowName()));
            }
        }
        player.knockBack(player,0,(player.x - entity.x),(player.z - entity.z),0.8);
        player.level.addParticle(new HugeExplodeSeedParticle(entity.getLocation()));
        player.level.addSound(new Vector3(entity.x, entity.y, entity.z), Sound.RANDOM_EXPLODE);

    }


    public void use(Player player, BlockChest chest){
        if(LuckDraw.getInstance().lock.contains(chest.getLocation())){
            player.sendMessage(LuckDraw.getLanguage("click.frist"));
        }else{
            if(canUse(player)){
                Item key = getKey(player,this.key);
                LuckDraw.getInstance().lock.add(chest.getLocation());
                key.setCount(this.key.getCount());
                player.getInventory().removeItem(key);
                openChest(player,  chest);
                return;
            }else{
                player.sendMessage(LuckDraw.getLanguage("player.notkey").replace("{key}",this.key.getShowName()));
            }
        }
        player.knockBack(player,0,(player.x - chest.getLocation().x),(player.z - chest.getLocation().z),0.8);
        player.level.addParticle(new HugeExplodeSeedParticle(chest.getLocation()));
        player.level.addSound(new Vector3(chest.getLocation().x, chest.getLocation().y, chest.getLocation().z), Sound.RANDOM_EXPLODE);

    }



    public Item getKey(Player player,Key key){
        key = key.clone();
        Item items = key.getKeyItem();
        int c = 0;
        for(Item item:player.getInventory().getContents().values()){
            Key playerKey = Key.getKeyByItem(item);
            if(playerKey != null){
                if(playerKey.equals(key)){
                    c += item.getCount();
                }
            }
        }
        items.setCount(c);
        return items;
    }


    public Chest getChestByPosition(Position position){
        for(Position position1:positions){
            if(position1.level.getFolderName().equals(position.level.getFolderName())){
                if(position.x == position1.x && position.y == position1.y && position.z == position1.z){
                    return this;
                }
            }
        }
        return null;
    }

    private void save(){
        Config config = new Config(LuckDraw.getInstance().getChestFile(name),Config.YAML);
        LinkedList<String> position = new LinkedList<>();
        if(positions.size() > 0){
            for(Position position1:positions){
                position.add(Tools.positionToString(position1));
            }
        }
        config.set("抽奖箱坐标",position);
        config.save();

    }


    public void saveAll(){
        Config config = new Config(LuckDraw.getInstance().getChestFile(name),Config.YAML);
        config.set("名称",shownName);
        config.set("使用key",key.getKey());
        config.set("消耗数量",key.getCount());
        config.set("是否使用模型",canUse);
        config.set("模型名称",skinName);
        config.set("模型大小",Double.parseDouble(String.format("%.2f",scale)));
        config.set("开启宝箱是否全服公告",canBroadCast);
        LinkedList<String> items = new LinkedList<>();
        for(BaseItems baseItems:getDefaultItem()){
            items.add(baseItems.toString());
        }
        config.set("固定奖励",items);
        config.set("抽取个数",getCount());
        items = new LinkedList<>();
        for(BaseItems baseItems:getItems()){
            items.add(baseItems.toString());
        }
        LinkedList<String> position = new LinkedList<>();
        if(positions.size() > 0){
            for(Position position1:positions){
                position.add(Tools.positionToString(position1));
            }
        }
        config.set("奖励物品",items);
        config.set("抽奖箱坐标",position);
        config.save();
    }


    /**
     * 破坏
     * */
    public boolean destroy(Position position){
        if(positions.contains(position)){
            positions.remove(position);
            save();
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Chest){
            return ((Chest) obj).getName().equals(name);
        }
        return false;
    }

    /**
     * 拆除
     * */
    public boolean kill(){
        LuckDraw.getInstance().chests.remove(name);
        File file = LuckDraw.getInstance().getChestFile(name);
        for(Position position:positions){
            if(position.getLevelBlock() instanceof BlockChest){
                position.getLevel().setBlock(position,new BlockChest());
                position.getLevel().addParticle(new DestroyBlockParticle(position,new BlockChest()));
            }

        }
        if(file.exists()){
            return file.delete();
        }
        return false;
    }


    private void removePacket(long eid,Position position){
        for (int a = 0; a < 5; a++) {
            position.getLevel().addParticle(new SmokeParticle(new Vector3(position.x + 0.5
                    , position.y + 1,
                    position.z + 0.5)));
        }
        RemoveEntityPacket pk1 = new RemoveEntityPacket();
        pk1.eid = eid;
        Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(), pk1);
    }
}
