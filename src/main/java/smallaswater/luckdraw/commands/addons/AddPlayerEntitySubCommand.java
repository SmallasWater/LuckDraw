package smallaswater.luckdraw.commands.addons;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;
import smallaswater.luckdraw.players.PlayerFile;
import smallaswater.luckdraw.players.entitys.PlayerEntity;
import smallaswater.luckdraw.players.entitys.PlayerSkin;
import smallaswater.luckdraw.utils.Tools;

public class AddPlayerEntitySubCommand extends SubCommand {

    public AddPlayerEntitySubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "添加变身";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"addce"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String entityName = args[1];
            String playerName = args[2];
            int time = -1;
            if(args.length > 3){
                try {
                    time = Integer.parseInt(args[3]);
                }catch (Exception ignored){}
            }
            Player player = Server.getInstance().getPlayer(playerName);
            if(player != null){
                PlayerFile file = PlayerFile.getPlayerFile(player.getName());
                if(LuckDraw.getInstance().chestSkins.containsKey(entityName)){
                    file.addEntity(new PlayerSkin(entityName,time));
                    sender.sendMessage("§a>>§e成功给予玩家§b"+player.getName()+"§e"+(time!=-1?time+"天":"永久")+"的时装§d"+entityName);
                }else if(Tools.isContainEntity(entityName)){
                    file.addEntity(new PlayerEntity(entityName,time));
                    sender.sendMessage("§a>>§e成功给予玩家§b"+player.getName()+"§e"+(time!=-1?time+"天":"永久")+"的变身§d"+entityName);
                }else{
                    sender.sendMessage("§c实体 "+entityName+"不存在....");
                }
            }else {
                sender.sendMessage("§c玩家"+playerName+"不在线");
            }

        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§7拓展指令 §a/luckadd addce <生物名/皮肤名> <玩家> <时间(-1永久)> 给玩家一个可变身皮肤/生物";
    }
}
