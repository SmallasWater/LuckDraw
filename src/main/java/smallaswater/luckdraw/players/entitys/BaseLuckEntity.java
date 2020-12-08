package smallaswater.luckdraw.players.entitys;

import smallaswater.luckdraw.utils.Tools;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BaseLuckEntity {

    private String name;

    private String showName = "";

    private int time;

    private Date startTime;


    BaseLuckEntity(String name, int time,Date startTime){
        this.name = name;
        this.time = time;
        this.startTime = startTime;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getShowName() {
        return showName;
    }

    public int getType(){
        return 0;
    }

    public String getName() {
        return name;
    }

    public int getTime() {
        return time;
    }

    public BaseLuckEntity setName(String name) {
        this.name = name;
        return this;
    }

    public BaseLuckEntity setTime(int time) {
        this.time = time;
        return this;
    }

    public boolean isSkin(){
        return false;
    }

    public Date getStartTime() {
        return startTime;
    }



    public boolean isEntity(){
        return false;
    }

    public LinkedHashMap<String,Object> getSaveConfig(){
        LinkedHashMap<String,Object> config = new LinkedHashMap<>();
        config.put("name",name);
        config.put("time",time);
        config.put("type",getType());
        config.put("showName",showName);
        config.put("startTime", Tools.getDateToString(startTime));
        return config;
    }

    public static BaseLuckEntity toBaseLuckEntity(Map map){
        if(map.containsKey("name")
                && map.containsKey("time")
                && map.containsKey("type") && map.containsKey("startTime")){
            if((int) map.get("type") == 1){
                PlayerEntity entity =  new PlayerEntity(map.get("name").toString(),(int)map.get("time"),map.get("startTime").toString());
                if(map.containsKey("showName")){
                    entity.setShowName(map.get("showName").toString());
                }
                return entity;
            }else if((int) map.get("type") == 2){
                PlayerSkin skin = new PlayerSkin(map.get("name").toString(),(int)map.get("time"),map.get("startTime").toString());
                if(map.containsKey("showName")){
                    skin.setShowName(map.get("showName").toString());
                }

                return skin;
            }
        }
        return null;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof BaseLuckEntity){
            return ((BaseLuckEntity) obj).getName().equals(getName())
                    && ( getType() == ((BaseLuckEntity) obj).getType());
        }
        return false;
    }
}
