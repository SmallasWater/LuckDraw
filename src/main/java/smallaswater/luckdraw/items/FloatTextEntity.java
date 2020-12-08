package smallaswater.luckdraw.items;

import cn.nukkit.level.Location;
import cn.nukkit.level.particle.FloatingTextParticle;
import cn.nukkit.network.protocol.SetEntityDataPacket;

/**
 * @author SmallasWater
 */
public class FloatTextEntity extends FloatingTextParticle {
    public FloatTextEntity(Location location, String title) {
        super(location, title);
    }

    public void toUpData(){
        if(level != null) {
            SetEntityDataPacket packet = new SetEntityDataPacket();
            packet.eid = this.entityId;
            packet.metadata = this.metadata;
            this.level.addChunkPacket(this.getChunkX(), this.getChunkZ(), packet);
        }
    }

}
