package smallaswater.luckdraw.tasks;


import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.item.Item;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import cn.nukkit.scheduler.PluginTask;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.items.FloatTextEntity;

import java.util.LinkedHashMap;


/**
 * @author 显示标题
 */
public class ChestTask extends PluginTask<LuckDraw> {
    private Player player;
    public ChestTask(LuckDraw draw,Player player){
        super(draw);
        this.player = player;
    }
    @Override
    public void onRun(int i) {
        if(player.isOnline()){
            if(!LuckDraw.getInstance().texts.containsKey(player)){
                LuckDraw.getInstance().texts.put(player,new LinkedHashMap<>());
            }

            LinkedHashMap<Position, FloatTextEntity> texts = LuckDraw.getInstance().texts.get(player);
            for (Chest chest : LuckDraw.getInstance().chests.values()) {
                if (player.isOnline()) {
                    for (Position position : chest.getPositions()) {
                        FloatTextEntity particle;
                        Position news = position.add(0.5, 1.5, 0.5);
                        if (position.level.getFolderName().equals(player.getLevel().getFolderName())) {
                            if (!texts.containsKey(news)) {
                                particle = new FloatTextEntity(Location.fromObject(news,news.getLevel()).getChunk(),Entity.getDefaultNBT(news), getText(chest));
//                                position.level.addParticle(particle,player);
                                particle.spawnTo(player);
                                texts.put(news, particle);
                            } else {
                                particle = texts.get(news);
                                particle.setTitle(getText(chest));
//                                particle.toUpData();
                            }
                        } else {
                            if (texts.containsKey(news)) {
                                particle = texts.get(news);
                                RemoveEntityPacket pk = new RemoveEntityPacket();
                                pk.eid = particle.getId();
                                player.dataPacket(pk);
                                texts.remove(news);
                            }
                        }

                    }
                }

            }

        }else{
            this.cancel();
        }
        if(LuckDraw.getInstance().texts.containsKey(player)){
            try {
                LinkedHashMap<Position, FloatTextEntity> texts = LuckDraw.getInstance().texts.get(player);
                if(texts.size() > 0 ){
                    for(Position position:texts.keySet()){
                        if(LuckDraw.getInstance().getChestByPosition(Location.fromObject(position.add(-0.5,-1.5,-0.5),position.level)) == null){
                            RemoveEntityPacket pk = new RemoveEntityPacket();
                            pk.eid = texts.get(position).getId();
                            player.dataPacket(pk);
                            texts.remove(position);
                        }
                    }
                }
            }catch (Exception ignored){}
        }
    }
    private String getText(Chest chest){
        String text = chest.getShownName();
        Item item = chest.getKey(player,chest.getKey());
        int count = 0;
        if(item != null){
            count = item.getCount();
        }
        text = text.replace("%count%"
                ,count+"");
        return text;
    }
}
