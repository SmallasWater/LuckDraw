package smallaswater.luckdraw.chests;

import cn.nukkit.entity.EntityHuman;
import cn.nukkit.level.Location;
import cn.nukkit.level.Position;
import cn.nukkit.level.format.FullChunk;
import cn.nukkit.nbt.tag.CompoundTag;

/**
 * @author SmallasWater
 */
public class ChestEntity extends EntityHuman {


    private Position chestPos;


    public ChestEntity(FullChunk chunk, CompoundTag nbt, Position position) {
        super(chunk, nbt);
        this.x += 0.5;
        this.z += 0.5;
        this.chestPos = position;


    }

    public ChestEntity(FullChunk chunk, CompoundTag nbt){
        super(chunk, nbt);

    }


    @Override
    protected void initEntity() {
        super.initEntity();
        this.setMaxHealth(20);
        this.setHealth(20.0F);

    }



    public Position getChestPos() {
        return chestPos;
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
