package smallaswater.luckdraw;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.nbt.tag.ListTag;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.ChestEntity;
import smallaswater.luckdraw.chests.Key;
import smallaswater.luckdraw.commands.AddonsLuckCommand;
import smallaswater.luckdraw.commands.LuckCommand;
import smallaswater.luckdraw.commands.PlayerLuckCommand;
import smallaswater.luckdraw.commands.addons.LuckEntity;
import smallaswater.luckdraw.items.FloatTextEntity;
import smallaswater.luckdraw.players.PlayerFile;
import smallaswater.luckdraw.players.entitys.BaseLuckEntity;
import smallaswater.luckdraw.tasks.ChunkEntitySkinTask;
import smallaswater.luckdraw.tasks.EntityChestTask;

import smallaswater.luckdraw.utils.Tools;
import updata.AutoData;


import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;



/**
 * @author 若水
 */
public class LuckDraw extends PluginBase {

    private static LuckDraw instance;

    private static Config langauge;

    public LinkedHashMap<String,Chest> chests = new LinkedHashMap<>();

    public LinkedHashMap<String,Chest> clickChest = new LinkedHashMap<>();

    public LinkedList<Player> mode = new LinkedList<>();

    public LinkedList<Player> rmode = new LinkedList<>();

    public LinkedHashMap<Player,Chest> placeChest = new LinkedHashMap<>();

    public LinkedHashMap<String,Key> keys = new LinkedHashMap<>();

//    public LinkedHashMap<Player,FloatTextEntity> cacheEntity = new LinkedHashMap<>();

    public LinkedHashMap<Player,LinkedHashMap<Position, FloatTextEntity>> texts = new LinkedHashMap<>();

    public LinkedList<Position> lock = new LinkedList<>();

    public LinkedHashMap<Position, Long> entitys = new LinkedHashMap<>();

    public LinkedHashMap<String, Skin> chestSkins = new LinkedHashMap<>();

    public LinkedHashMap<String, PlayerFile> playerFiles = new LinkedHashMap<>();

    public LinkedHashMap<String, BaseLuckEntity> playerClick = new LinkedHashMap<>();

    public LinkedList<Player> playerClickType = new LinkedList<>();

    @Override
    public void onEnable() {
        instance = this;
        if(Server.getInstance().getPluginManager().getPlugin("AutoUpData") != null){
            if(AutoData.defaultUpData(this,getFile(),"SmallasWater","LuckDraw")){
                return;
            }
        }
        this.saveDefaultConfig();
        this.reloadConfig();
        initKeys();
        Entity.registerEntity("luckentity", LuckEntity.class);
        Entity.registerEntity("chestEntity", ChestEntity.class);
        if(!new File(this.getDataFolder()+"/language.yml").exists()){
            this.saveResource("language.yml");
        }
        langauge = new Config(new File(this.getDataFolder()+"/language.yml"),Config.YAML);
        if(!new File(this.getDataFolder()+"/Chests").exists()){
            if(!new File(this.getDataFolder()+"/Chests").mkdirs()){
                this.getLogger().info("创建文件夹 Chests  失败");
            }
        }
        if(!new File(this.getDataFolder()+"/Players").exists()){
            if(!new File(this.getDataFolder()+"/Players").mkdirs()){
                this.getLogger().info("创建文件夹 Players  失败");
            }
        }
        initPlayer();
        getLogger().info("开始加载模型");
        initSkin();
        if(!new File(this.getDataFolder()+"/nbtitem.yml").exists()){
           this.saveResource("nbtitem.yml");
        }
        initChest();
        this.getServer().getCommandMap().register("LuckDraw",new LuckCommand(this));
        this.getServer().getCommandMap().register("LuckDrawAddons",new AddonsLuckCommand(this));
        this.getServer().getCommandMap().register("LuckDrawAddonsPlayer",new PlayerLuckCommand(this));
        this.getServer().getPluginManager().registerEvents(new OnListener(),this);
        this.getServer().getScheduler().scheduleRepeatingTask(new EntityChestTask(this),20);
        this.getServer().getScheduler().scheduleRepeatingTask(new ChunkEntitySkinTask(this),10);
    }

    public void initSkin(){
        if(!new File(this.getDataFolder()+"/Skins").exists()){
            loadSkin();
        }
        File[] files = new File(this.getDataFolder()+"/Skins").listFiles();
        if(files != null && files.length > 0){
            for(File file:files){
                String skinName = file.getName();
                if(new File(this.getDataFolder()+"/Skins/"+skinName+"/skin.png").exists()){
                    Skin skin = new Skin();
                    BufferedImage skindata = null;
                    try {
                        skindata = ImageIO.read(new File(this.getDataFolder()+"/Skins/"+skinName+"/skin.png"));
                    } catch (IOException var19) {
                        System.out.println("不存在模型");
                    }

                    if (skindata != null) {
                        skin.setSkinData(skindata);
                        skin.setSkinId(skinName);

                    }
                    //如果是4D皮肤
                    if(new File(this.getDataFolder()+"/Skins/"+skinName+"/skin.json").exists()){
                        Map<String, Object> skinJson = (new Config(this.getDataFolder()+"/Skins/"+skinName+"/skin.json", Config.JSON)).getAll();
                        String geometryName = null;
                        for (Map.Entry<String, Object> entry1: skinJson.entrySet()){
                            if(geometryName == null){
                                geometryName = entry1.getKey();
                            }
                        }
                        skin.setGeometryName(geometryName);
                        skin.setGeometryData(Tools.readFile(new File(this.getDataFolder()+"/Skins/"+skinName+"/skin.json")));
                    }
                    this.getLogger().info(skinName+"模型读取完成");
                    chestSkins.put(skinName,skin);
                }else{

                    this.getLogger().info("无法读取模型: "+skinName+"错误的皮肤名称格式 请将皮肤文件命名为 skin.png");
                }
            }
        }

    }

    private void loadSkin(){
        if(!new File(this.getDataFolder()+"/Skins/skin1").exists()){
            if(!new File(this.getDataFolder()+"/Skins/skin1").mkdirs()){
                this.getLogger().info("载入 skin1 失败");

            }
        }
        this.saveResource("chestSkin/skin1/skin.json","/Skins/skin1/skin.json",false);
        this.saveResource("chestSkin/skin1/skin.png","/Skins/skin1/skin.png",false);
        this.getLogger().info("成功载入 skin1 模型");
    }




    public void initPlayer(){
        if(getPlayers().size() > 0){
            for (String name:getPlayers()){
                PlayerFile.getPlayerFile(name);
            }
        }
    }


   public void initKeys(){

       List<Map> keyList = getConfig().getMapList("keys");
       for(Map map:keyList){
           Item item = Item.fromString(map.get("id").toString());
           CompoundTag tag = new CompoundTag();
           if((boolean)map.get("enchant")){
               tag.putList(new ListTag<>("ench"));
           }
           tag.putString(Key.TAG,map.get("key").toString());
           tag.putString(Key.SHOW_NAME,map.get("name").toString());
           item.setNamedTag(tag);
           item.setCustomName(map.get("name").toString());
           item.setLore(map.get("lore").toString().split("\\n"));
           keys.put(map.get("key").toString(),new Key(map.get("key").toString(),map.get("name").toString(),1,item));
           this.getLogger().info("成功加载"+map.get("key").toString()+"抽奖箱钥匙");
       }
   }


   /**
    * 初始化抽奖箱
    * */
   public void initChest(){
        for (String name:getChests()){
            Config config = new Config(getChestFile(name));
            Chest chest = Chest.getChest(name,config);
            if(chest != null){
                chests.put(name,chest);
                this.getLogger().info("成功加载"+chest.getName()+"抽奖箱");
            }
        }
   }



   public List getBanEntitys(){
       return getConfig().getList("禁用变身生物",new LinkedList<String>(){
           {
               add("Phantom");
               add("EnderDragon");
           }
       });
   }

   private Config getNbtItemConfig(){
        return new Config(this.getDataFolder()+"/nbtitem.yml",Config.YAML);
   }

    public static String getNbtItem(String string){
        return LuckDraw.getInstance().getNbtItemConfig().getString(string,"");
    }

    public void saveNbt(String name,String text){
       Config config = getNbtItemConfig();
       config.set(name,text);
       config.save();
    }

    private LinkedList<String> getDefaultFileNames(String fileName){
        LinkedList<String> fileNames = new LinkedList<>();
        File[] files = new File(this.getDataFolder()+"/"+fileName).listFiles();
        if(files != null) {
            for (File file1 : files) {
                if (file1.isFile()) {
                    String names = file1.getName().substring(0, file1.getName().lastIndexOf("."));
                    fileNames.add(names);
                }
            }

        }
        return fileNames;
    }

    private LinkedList<String> getPlayers(){
       return getDefaultFileNames("Players");
    }

    private LinkedList<String> getChests(){
       return getDefaultFileNames("Chests");
    }

    public static LuckDraw getInstance() {
        return instance;
    }

    public String getNameByTag(String tag){
       Config config = getNbtItemConfig();
       LinkedHashMap<String,Object> stringObjectMap = (LinkedHashMap<String, Object>) config.getAll();
       for(String name:stringObjectMap.keySet()){
           String tags = (String) stringObjectMap.get(name);
           if(tags.equals(tag)){

               return name;
           }
       }
       String name = new Random().nextInt(2566)+"";
       stringObjectMap.put(name,tag);
       config.setAll(stringObjectMap);
       config.save();
       return name;

    }

    public File getChestFile(String name){
        return new File(this.getDataFolder()+"/Chests/"+name+".yml");
    }

    public Chest getChestByPosition(Position position){
        for(Chest chest:chests.values()){
            Chest chest1 = chest.getChestByPosition(position);
            if(chest1 != null){
                return chest1;
            }
        }
        return null;
    }

    @Override
    public void onDisable() {
       for(Chest chest:chests.values()){
           chest.saveAll();
       }
       for(PlayerFile file:playerFiles.values()){
           file.save();
       }
       entitys = new LinkedHashMap<>();

       placeChest = new LinkedHashMap<>();
       keys = new LinkedHashMap<>();
       texts = new LinkedHashMap<>();
       lock = new LinkedList<>();
       chests = new LinkedHashMap<>();
    }



    public static String getLanguage(String s){
       return langauge.getString(s, "");
    }


}
