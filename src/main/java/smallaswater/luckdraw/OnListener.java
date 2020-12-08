package smallaswater.luckdraw;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.block.Block;
import cn.nukkit.block.BlockChest;
import cn.nukkit.entity.Entity;
import cn.nukkit.entity.item.EntityItem;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.block.BlockBreakEvent;
import cn.nukkit.event.block.BlockPlaceEvent;
import cn.nukkit.event.entity.EntityDamageByEntityEvent;
import cn.nukkit.event.entity.EntityDamageEvent;
import cn.nukkit.event.entity.ItemSpawnEvent;
import cn.nukkit.event.player.*;
import cn.nukkit.event.server.DataPacketReceiveEvent;
import cn.nukkit.item.Item;
import cn.nukkit.level.Position;
import cn.nukkit.network.protocol.ModalFormResponsePacket;
import cn.nukkit.network.protocol.RemoveEntityPacket;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.ChestEntity;
import smallaswater.luckdraw.commands.addons.LuckEntity;
import smallaswater.luckdraw.events.PlayerSkinChangeEvent;
import smallaswater.luckdraw.items.BaseItems;
import smallaswater.luckdraw.players.PlayerFile;
import smallaswater.luckdraw.players.entitys.BaseLuckEntity;
import smallaswater.luckdraw.tasks.ChestTask;
import smallaswater.luckdraw.utils.CustomItem;
import smallaswater.luckdraw.utils.ItemIDSunName;
import smallaswater.luckdraw.utils.Tools;

import java.util.LinkedHashMap;
import java.util.LinkedList;


/**
 * @author 若水
 */
public class OnListener implements Listener {

    private LinkedList<Player> removes = new LinkedList<>();


    @EventHandler
    public void onPlayerSkinChange(PlayerSkinChangeEvent event){
        Player player = event.getPlayer();
        Tools.addParticle(player);
    }

    @EventHandler(ignoreCancelled = true)
    public void onPlayerInteractEvent(PlayerInteractEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();

        if(block instanceof BlockChest){
            Chest chest = LuckDraw.getInstance().getChestByPosition(block.getLocation());
            if(chest != null){
                event.setCancelled();
                if(LuckDraw.getInstance().rmode.contains(player)){
                    Tools.sendRemoveItemGUI(player,chest);
                    LuckDraw.getInstance().clickChest.put(player.getName(),chest);
                    return;
                }
                if(LuckDraw.getInstance().mode.contains(player)){
                    Tools.sendAddItemGUI(player);
                    LuckDraw.getInstance().clickChest.put(player.getName(),chest);
                    return;
                }
                if(player.isSneaking() && player.getInventory().getItemInHand().getId() != 0){
                    Tools.showGUI(chest,player);
                }else{
                    chest.use(player, (BlockChest) block);
                }

            }
        }
    }

    @EventHandler(ignoreCancelled = true)
    public void onClickChest(EntityDamageEvent event){
        Entity entity = event.getEntity();
        ChestEntity e;
        if(entity instanceof ChestEntity){
            e = (ChestEntity) entity;
            event.setCancelled();
            if(event instanceof EntityDamageByEntityEvent){
                Entity players = ((EntityDamageByEntityEvent) event).getDamager();
                event.setCancelled();
                Position position = e.getChestPos();
                Chest chest = LuckDraw.getInstance().getChestByPosition(position);
                if (chest != null) {
                    if (players instanceof Player) {
                        if (((Player) players).isOp()) {
                            if(LuckDraw.getInstance().rmode.contains(players)){
                                Tools.sendRemoveItemGUI((Player) players,chest);
                                LuckDraw.getInstance().clickChest.put(players.getName(),chest);
                                return;
                            }
                            if(LuckDraw.getInstance().mode.contains(players)){
                                Tools.sendAddItemGUI((Player) players);
                                LuckDraw.getInstance().clickChest.put(players.getName(),chest);
                                return;
                            }
                            if (players.isSneaking()) {
                                if(((Player) players).getInventory().getItemInHand().getId() == 0){
                                    if (!removes.contains(players)) {
                                        ((Player) players).sendMessage("§l§e[抽奖箱] §e潜行空手再次点击移除箱子");
                                        removes.add((Player) players);
                                    } else {
                                        long id = entity.getId();
                                        if (chest.destroy(position)) {
                                            e.kill();
                                            RemoveEntityPacket pk = new RemoveEntityPacket();
                                            pk.eid = id;
                                            Server.broadcastPacket(Server.getInstance().getOnlinePlayers().values(),pk);
                                            ((Player) players).sendMessage("§l§e[抽奖箱] §a拆除成功 ..");
                                        } else {
                                            ((Player) players).sendMessage("§l§e[抽奖箱] §c拆除失败 ..");
                                        }
                                    }
                                }else{
                                    Tools.showGUI(chest, (Player) players);
                                }
                            } else {
                                removes.remove(players);
                                chest.use((Player) players,e);
                            }
                        } else {
                            chest.use((Player) players, e);
                        }
                    }
                }
            }
        }

    }


    @EventHandler
    public void onPlaceBlockEvent(BlockPlaceEvent event){
        Player player = event.getPlayer();
        if(LuckDraw.getInstance().placeChest.containsKey(player)){
            Chest chest = LuckDraw.getInstance().placeChest.get(player);
            Block block = event.getBlock();
            if(block instanceof BlockChest){
                if(!chest.isCanUse()){
                    if(chest.place(block.getLocation())){
                        player.sendMessage("§l§e[抽奖箱] §a放置成功 .. 输/luck end 结束放置");
                    }else{
                        player.sendMessage("§l§e[抽奖箱] §c放置失败 ..");
                    }
                }else{
                    event.setCancelled();
                    if(chest.place((Tools.getLocationByPlayer(player,block.getLocation())))){
                        player.sendMessage("§l§e[抽奖箱] §a放置成功 .. 输/luck end 结束放置");
                        player.sendMessage("§l§aTips: 潜行空手双击 可拆除");
                    }else{
                        player.sendMessage("§l§e[抽奖箱] §c放置失败 ..");
                    }

                }

            }
        }
    }
    @EventHandler
    public void onBreakBlockEvent(BlockBreakEvent event){
        Player player = event.getPlayer();
        Block block = event.getBlock();
        Chest chest = LuckDraw.getInstance().getChestByPosition(block.getLocation());
        if(chest != null){
            if(player.isOp()){
                if(chest.destroy(block.getLocation())){
                    player.sendMessage("§l§e[抽奖箱] §a拆除成功 ..");
                }else{
                    player.sendMessage("§l§e[抽奖箱] §c拆除失败 ..");
                }
            }else{
                event.setCancelled();
            }

        }
    }
    @EventHandler
    public void onDamage(EntityDamageEvent event){
        Entity entity = event.getEntity();
        if(entity instanceof Player){
            if(entity.namedTag.contains("createentity")){
                if(event.getCause() != EntityDamageEvent.DamageCause.SUICIDE && event.getCause() != EntityDamageEvent.DamageCause.VOID){
                    event.setCancelled();
                }
            }
        }else{

            if(event instanceof EntityDamageByEntityEvent){
                Entity dam = ((EntityDamageByEntityEvent) event).getDamager();
                if(entity instanceof LuckEntity){
                    Player player = Server.getInstance().getPlayer(entity.namedTag.getString("monsterName"));
                    if(player != null){
                        double deltaX = entity.x - dam.x;
                        double deltaZ = entity.z - dam.z;
                        player.knockBack(dam,0,deltaX,deltaZ,((EntityDamageByEntityEvent) event).getKnockBack());
                        ((LuckEntity) entity).knockBack(dam,0,deltaX,deltaZ,((EntityDamageByEntityEvent) event).getKnockBack());
                    }
                }
                if(entity instanceof LuckEntity){
                    if(dam.namedTag.contains("createentity") && entity.namedTag.contains("monsterName")){
                        if(dam.namedTag.getLong("createentity") == entity.getId()
                                && entity.namedTag.getString("monsterName").equals(dam.getName())){
                            event.setCancelled();
                        }
                    }
                }

            }
        }
    }



    @EventHandler
    public void getUI(DataPacketReceiveEvent event){
        String data;
        ModalFormResponsePacket ui;
        Player player = event.getPlayer();
        if((event.getPacket() instanceof ModalFormResponsePacket)){
            ui = (ModalFormResponsePacket)event.getPacket();
            data = ui.data.trim();
            int fromId = ui.formId;
            if(fromId == 0x55AAB211){
                if("null".equals(data)) {
                    return;
                }
                Object[] date = Tools.decodeData(data);
                if(date == null){
                    return;
                }
                if(LuckDraw.getInstance().clickChest.containsKey(player.getName())){
                    Chest chest = LuckDraw.getInstance().clickChest.get(player.getName());
                    Item item = player.getInventory().getItemInHand();
                    String items = date[0].toString();
                    String type = BaseItems.types.get((int)((double) date[1]));
                    String custom = date[2].toString();
                    int count = 1;
                    try{
                        count = Integer.parseInt(date[3]+"");
                    }catch (Exception e){
                        player.sendMessage("§a>>§c 错误数量 已设置为 1");
                    }
                    if("@nbt".equals(type)){
                        String text = CustomItem.toStringItem(item);
                        LuckDraw.getInstance().saveNbt(items,text);
                    }

                    if(chest.addItem(CustomItem.toBaseItem(items+"&"+custom+type+"&"+count))){
                        player.sendMessage("§a>> §e添加成功~~");
                        chest.saveAll();
                        LuckDraw.getInstance().clickChest.remove(player.getName());
                    }else{
                        player.sendMessage("§a>> §c添加失败..");
                    }
                }
            }
            if(fromId == 0x55AAB212){
                if(!"null".equals(data)){
                    try {
                        if(LuckDraw.getInstance().clickChest.containsKey(player.getName())){
                            Chest chest = LuckDraw.getInstance().clickChest.get(player.getName());
                            int date = Integer.parseInt(data);
                            if(date < chest.getDefaultItem().size()){
                                chest.removeDefaultItem(chest.getDefaultItem().get(date));
                            }else{
                                chest.removeItem(chest.getItems().get(date - chest.getDefaultItem().size()));
                            }
                            player.sendMessage("§a>> §a移除成功");
                            chest.saveAll();
                            LuckDraw.getInstance().clickChest.remove(player.getName());
                        }
                    }catch (Exception ignored){}
                }
            }
            if(fromId == 0x55AAB213) {
                if (!"null".equals(data)) {
                    PlayerFile file = PlayerFile.getPlayerFile(player.getName());
                    if(file.getEntities().size() == Integer.parseInt(data)){
                        if(player.namedTag.contains("createentity")) {
                            player.sendMessage("§a您的变身解除了");
                            LuckEntity.removeEntitySkin(player);
                        }
                        return;
                    }
                    BaseLuckEntity entity = file.getEntities().get(Integer.parseInt(data));

                    if (LuckDraw.getInstance().playerClickType.contains(player)) {
                        LuckDraw.getInstance().playerClick.put(player.getName(),entity);
                        Tools.sendInputGUI(player);
                        LuckDraw.getInstance().playerClickType.remove(player);
                        return;
                    }
                    if(entity.isSkin()){
                        if(LuckDraw.getInstance().chestSkins.containsKey(entity.getName())){
                            Tools.setPlayerSkin(player,LuckDraw.getInstance().chestSkins.get(entity.getName()));
                            player.sendMessage("§a>>§e更换成功 重进消失!!");
                        }else{
                            player.sendMessage("§a>>§c哎呀..名为 "+entity.getName()+"的皮肤没有啦..快去找服主更换吧");
                        }
                    }
                    if(entity.isEntity()){
                        if(Tools.setEntityPlayer(player,entity.getName())){
                            player.sendMessage("§a>>§e成功伪装为 "+entity.getName()+"§e的生物啦..快去吓吓别人吧\np.s. 退出游戏 死亡失效 可通过GUI关闭(/pla use)");
                        }
                    }
                }
            }
            if(fromId == 0x55AAB214){
                if (!"null".equals(data)) {
                    Object[] date = Tools.decodeData(data);
                    if(date == null){
                        return;
                    }
                    String name = date[0].toString();
                    if(LuckDraw.getInstance().playerClick.containsKey(player.getName())){
                        BaseLuckEntity entity = LuckDraw.getInstance().playerClick.get(player.getName());
                        player.sendMessage("§a>>已将 "+("".equals(entity.getShowName())?entity.getName():entity.getShowName())+"重命名为"+name);
                        entity.setShowName(name);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        Player player = event.getPlayer();
        if(LuckDraw.getInstance().playerFiles.containsKey(player.getName())) {
            PlayerFile file = PlayerFile.getPlayerFile(player.getName());
            file.save();
            LuckEntity.removeEntitySkin(player);
        }
    }



    @EventHandler
    public void onDeath(PlayerDeathEvent event){
        Player player = event.getEntity();
        if(player.namedTag.contains("createentity")){
            player.sendMessage("§b变身解除");
            LuckEntity.removeEntitySkin(player);
        }

    }

    @EventHandler
    public void onItemDrop(ItemSpawnEvent event){
        if(LuckDraw.getInstance().getConfig().getBoolean("掉落物是否显示名称",true)){
            EntityItem item = event.getEntity();
            String name = LuckDraw.getInstance().getConfig().getString("自定义显示内容","%name% §ex §a%count%");
            name = name.replace("%name%", ItemIDSunName.getIDByName(item.getItem()))
                    .replace("%count%",item.getItem().getCount()+"");
            item.setNameTagAlwaysVisible(true);
            item.setNameTag(name);

        }
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent event){
        Player player = event.getPlayer();
        PlayerFile file = PlayerFile.getPlayerFile(player.getName());
        file.changeTime();
        if(!LuckDraw.getInstance().texts.containsKey(player)){
            LuckDraw.getInstance().texts.put(player,new LinkedHashMap<>());
        }


        Server.getInstance().getScheduler().scheduleRepeatingTask(new ChestTask(LuckDraw.getInstance(),player),20);
    }

}
