package smallaswater.luckdraw.players;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.utils.Config;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.addons.LuckEntity;
import smallaswater.luckdraw.players.entitys.BaseLuckEntity;
import smallaswater.luckdraw.tasks.ChunkEntitySkinTask;
import smallaswater.luckdraw.utils.Tools;

import java.io.File;
import java.util.*;

/**
 * @author 若水
 */
public class PlayerFile {

    private String playerName;

    private LinkedList<BaseLuckEntity> entities;

    public PlayerFile(String playerName,BaseLuckEntity... entities){
        this.playerName = playerName;
        this.entities = new LinkedList<>(Arrays.asList(entities));
    }

    public LinkedList<BaseLuckEntity> getEntities() {
        return entities;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void addEntity(BaseLuckEntity entity){
        if(this.entities.contains(entity)){
            BaseLuckEntity e = entities.get(entities.indexOf(entity));
            if(entity.getTime() == -1){
                e.setTime(-1);
            }else if(e.getTime() != -1) {
                e.setTime(e.getTime() + entity.getTime());
            }
        }else{
            this.entities.add(entity);
        }
    }




    public void setEntity(BaseLuckEntity entity){
        if(this.entities.contains(entity)){
            BaseLuckEntity e = entities.get(entities.indexOf(entity));
            if(e.getTime() != -1) {
                e.setTime(entity.getTime());
            }
        }else{
            this.entities.add(entity);
        }
    }



    public void removeEntity(BaseLuckEntity entity){
        entities.remove(entity);
    }

    public void changeTime(){
        try {
            for (BaseLuckEntity entity : getEntities()) {
                if (entity.getTime() != -1) {
                    if (entity.getTime() <= Tools.getOutDay(entity.getStartTime())) {
                        removeEntity(entity.setTime(Tools.getOutDay(entity.getStartTime())));
                    }
                }
            }
        }catch (Exception ignore){}
    }


    public static PlayerFile getPlayerFile(String playerName){
        if(!LuckDraw.getInstance().playerFiles.containsKey(playerName)){
            if(!new File(LuckDraw.getInstance().getDataFolder()+"/Players/"+playerName+".yml").exists()){
                LuckDraw.getInstance().saveResource("playerEntitys.yml","/Players/"+playerName+".yml",false);
            }
            Config config = new Config(LuckDraw.getInstance().getDataFolder()+"/Players/"+playerName+".yml",Config.YAML);
            List<Map> mapList = config.getMapList("拥有实体变身");
            LinkedList<BaseLuckEntity> entities = new LinkedList<>();
            if(mapList.size() > 0){
                for(Map map:mapList){
                    entities.add(BaseLuckEntity.toBaseLuckEntity(map));
                }
            }
            LuckDraw.getInstance().playerFiles.put(playerName,
                    new PlayerFile(playerName,entities.toArray(new BaseLuckEntity[0])));
        }
        return LuckDraw.getInstance().playerFiles.get(playerName);
    }

    public void save(){
        Config config = new Config(LuckDraw.getInstance().getDataFolder()+"/Players/"+playerName+".yml",Config.YAML);
        LinkedList<LinkedHashMap<String,Object>> maps = new LinkedList<>();
        for(BaseLuckEntity entity:getEntities()){
            maps.add(entity.getSaveConfig());
        }
        config.set("拥有实体变身",maps);
        config.save();
    }
}
