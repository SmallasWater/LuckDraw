package smallaswater.luckdraw.commands.addons;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.EntityLiving;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.math.Vector3;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.*;
import smallaswater.luckdraw.tasks.ChunkEntitySkinTask;
import smallaswater.luckdraw.utils.Tools;

import java.util.LinkedList;


/**
 * @author 若水
 *
 */
public class LuckEntity extends EntityLiving {

    private Player player;
    private int network;

    private String entityName;


    public LuckEntity(FullChunk chunk, CompoundTag nbt, int network, Player player) {
        super(chunk, nbt);
        this.player = player;
        this.network = network;
        this.health = player.getHealth();
        this.setMaxHealth(player.getMaxHealth());
    }

    public void setEntityName(String entityName) {
        this.entityName = entityName;
    }

    public String getEntityName() {
        return entityName;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
        this.setHealth(20.0F);
    }



    @Override
    public int getNetworkId() {
        return this.network;
    }

    @Override
    public void setHealth(float health) {
        super.setHealth(health);
    }

    @Override
    public void setMaxHealth(int maxHealth) {
        super.setMaxHealth(maxHealth);
    }

    @Override
    public boolean isClosed() {
        boolean b = super.isClosed();
        if (b) {
            this.setKill();
        }

        return b;
    }



    @Override
    public boolean onUpdate(int currentTick) {
        boolean b = super.onUpdate(currentTick);
        if (!this.isClosed()) {
            if (this.isAlive()) {
                if (!this.player.isOnline()) {
                    this.player.setHealth(0.0F);
                    this.setKill();
                    return false;
                }

                this.player.setHealth(this.getHealth());
                setNameTag(player.getNameTag());
                MobArmorEquipmentPacket pks = new MobArmorEquipmentPacket();
                pks.eid = getId();
                pks.slots = player.getInventory().getArmorContents();
                MobEquipmentPacket pk = new MobEquipmentPacket();
                pk.eid = getId();
                pk.item = player.getInventory().getItemInHand();
                pk.inventorySlot = 0;
                RemoveEntityPacket pka = new RemoveEntityPacket();
                pka.eid = getId();
                player.dataPacket(pka);
                for (Player p : Server.getInstance().getOnlinePlayers().values()) {
                    if(!player.getName().equals(p.getName())){
                        p.dataPacket(pk);
                        p.dataPacket(pks);
                    }
                }
                this.x = player.x;
                this.y = player.y;
                this.z = player.z;

                this.addMotion(this.player.motionX, this.player.motionY, this.player.motionZ);
                this.updateMovement();
                this.setPositionAndRotation(new Vector3(this.player.x, this.player.y, this.player.z), this.player.yaw, this.player.pitch);
                this.updateMovement();
                if (!this.player.getLevel().getFolderName().equals(this.level.getFolderName())) {
                    this.close();
                    ChunkEntitySkinTask.players.clear();
                }
            } else {
                this.player.setHealth(0.0F);
                b = false;
                this.setKill();
            }

            return b;
        } else {
            this.setKill();
            return false;
        }
    }

    public static void removeEntitySkin(Player player) {
        removeEntitySkin(player,false);

    }
    public static void removeEntitySkin(Player player,boolean hide) {
        if(player != null) {
            CompoundTag tag = player.namedTag;
            if (tag != null) {
                if (tag.contains("createentity")) {
                    Server.getInstance().getOnlinePlayers().values().forEach(player1 -> {
                        if (!player1.getName().equals(player.getName())) {
                            if(!hide) {
                                player1.showPlayer(player);
                            }
                            if (player.isOnline()) {
                                RemoveEntityPacket pk = new RemoveEntityPacket();
                                pk.eid = tag.getLong("createentity");
                                player.dataPacket(pk);
                            }
                        }
                    });

                    player.namedTag = tag;
                    Server.getInstance().getOnlinePlayers().values().forEach(player1 -> player1.showPlayer(player));
                    LuckEntity entity1 = ChunkEntitySkinTask.entityHashMap.get(player.getName());
                    entity1.close();
                    ChunkEntitySkinTask.entityHashMap.remove(player.getName());
                    tag.remove("createentity");
                    Tools.addParticle(player);
                }
            }

            ChunkEntitySkinTask.players = new LinkedList<>();

        }

    }

    public void setKill() {
        CompoundTag tag = player.namedTag;
        tag.remove("createentity");
        player.namedTag = tag;
        if(player.isOnline()){
            player.sendMessage("§a变身解除");
        }
        ChunkEntitySkinTask.players = new LinkedList<>();
        Server.getInstance().getOnlinePlayers().values().forEach(player1 -> {
            player1.showPlayer(player);
        });
        this.close();
    }

}
