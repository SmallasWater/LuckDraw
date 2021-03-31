package smallaswater.luckdraw.items;

import cn.nukkit.Player;
import cn.nukkit.entity.Entity;
import cn.nukkit.level.Location;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.nbt.tag.CompoundTag;
import cn.nukkit.network.protocol.SetEntityDataPacket;

/**
 * @author SmallasWater
 */
public class FloatTextEntity extends Entity {

    public FloatTextEntity(FullChunk chunk,CompoundTag nbt,String title) {
        super(chunk, nbt);
        this.setNameTag(title);
        this.setNameTagVisible(true);
        this.setNameTagAlwaysVisible(true);
    }

    @Override
    public int getNetworkId() {
        return 64;
    }

    @Override
    protected void initEntity() {
        super.initEntity();
        setImmobile(true);
        getDataProperties().putLong(0, 65536L);
    }


//
//    public FloatTextEntity(Location location, String title) {
//        super(location, title);
//    }

//    public void toUpData(){
//        if(level != null) {
//            SetEntityDataPacket packet = new SetEntityDataPacket();
//            packet.eid = this.entityId;
//            packet.metadata = this.metadata;
//            this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), packet);
//        }
//    }


    public void setTitle(String title) {
        this.setNameTag(title);
    }

//    @Override
//    public int getNetworkId() {
//        return 64;
//    }
}
