package smallaswater.luckdraw.tasks;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.scheduler.PluginTask;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.addons.LuckEntity;
import smallaswater.luckdraw.utils.Tools;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class ChunkEntitySkinTask extends PluginTask<LuckDraw> {

    public static LinkedList<Player> players = new LinkedList<>();
    public static LinkedHashMap<String, LuckEntity> entityHashMap = new LinkedHashMap<>();
    public ChunkEntitySkinTask(LuckDraw owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for(Player player: Server.getInstance().getOnlinePlayers().values()){
            if(entityHashMap.containsKey(player.getName())){
                LuckEntity entity1 = entityHashMap.get(player.getName());
                if(player.namedTag.contains("createentity")){
                    if(player.level.getEntity(entity1.getId()) == null){
                        LuckEntity.removeEntitySkin(player,true);
                        Tools.setEntityPlayer(player,entity1.getEntityName(),false);
                    }
                    Server.getInstance().getOnlinePlayers().values().forEach(player1 -> {
                        if(!player1.getName().equals(player.getName())){
                            player1.hidePlayer(player);
                            if(!players.contains(player1)){
                                entity1.spawnTo(player1);
                                players.add(player1);
                            }

                        }
                    });
                }
            }

        }
    }
}
