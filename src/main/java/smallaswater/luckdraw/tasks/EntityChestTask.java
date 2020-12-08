package smallaswater.luckdraw.tasks;

import cn.nukkit.Server;
import cn.nukkit.block.BlockAir;
import cn.nukkit.block.BlockChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.data.Skin;
import cn.nukkit.level.Level;
import cn.nukkit.level.Position;
import cn.nukkit.scheduler.PluginTask;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.ChestEntity;
import smallaswater.luckdraw.utils.Tools;

import java.util.LinkedHashMap;
import java.util.LinkedList;

/**
 * @author SmallasWater
 */
public class EntityChestTask extends PluginTask<LuckDraw> {
    public EntityChestTask(LuckDraw owner) {
        super(owner);
    }

    @Override
    public void onRun(int i) {
        for (Chest chest : LuckDraw.getInstance().chests.values()) {
            if (chest == null){
                continue;
            }
            for (Position position : chest.getPositions()) {
                if(position == null){
                    continue;
                }
                if (chest.isCanUse()) {
                    if (position.getLevelBlock().getId() != 0) {
                        position.getLevel().setBlock(position, new BlockAir());
                    }
                    String skinName = chest.getSkinName();
                    try {
                        if (!LuckDraw.getInstance().entitys.containsKey(position)) {
                            if (LuckDraw.getInstance().chestSkins.containsKey(skinName)) {
                                Skin skin = LuckDraw.getInstance().chestSkins.get(skinName);
                                ChestEntity entity = new ChestEntity(position.getChunk(), Tools.getTag(position, skin), position);
                                skin.setTrusted(true);
                                entity.setSkin(skin);
                                entity.spawnToAll();
                                entity.setScale((float) chest.getScale());
                                LuckDraw.getInstance().entitys.put(position, entity.getId());
                            }
                        } else{
                            long eid = LuckDraw.getInstance().entitys.get(position);
                            Entity entity = position.level.getEntity(eid);
                            if (entity != null) {
                                if (entity instanceof ChestEntity) {
                                    if (((ChestEntity) entity).getSkin() == null) {
                                        entity.kill();
                                    }
                                }
                            }
                        }
                    }catch (Exception ignore){
                    }
                }else{
                    if(position.getLevelBlock().getId() == 0){
                        position.getLevel().setBlock(position,new BlockChest());
                    }
                }
            }
        }
        try {
            LinkedList<Position> positions = new LinkedList<>(LuckDraw.getInstance().entitys.keySet());
            for (Position position : positions) {
                if(position == null){
                    return;
                }
                Chest chest = LuckDraw.getInstance().getChestByPosition(position);
                if (chest == null) {
                    long entity = LuckDraw.getInstance().entitys.get(position);
                    Entity entity1 = position.level.getEntity(entity);
                    if (entity1 != null) {
                        entity1.kill();
                    }
                    LuckDraw.getInstance().entitys.remove(position);
                } else {
                    long entity = LuckDraw.getInstance().entitys.get(position);
                    Entity entity1 = position.level.getEntity(entity);
                    if (entity1 == null) {
                        LuckDraw.getInstance().entitys.remove(position);
                    }else{
                        if(!chest.isCanUse()){
                            LuckDraw.getInstance().entitys.remove(position);
                            entity1.kill();
                        }
                    }
                }
            }
        } catch (Exception e) {
            for (Level level : Server.getInstance().getLevels().values()) {
                for (Entity entity : level.getEntities()) {
                    if(entity != null) {
                        if (entity instanceof ChestEntity) {
                            entity.kill();
                        }
                    }
                }
            }
            LuckDraw.getInstance().entitys = new LinkedHashMap<>();
        }


    }
}
