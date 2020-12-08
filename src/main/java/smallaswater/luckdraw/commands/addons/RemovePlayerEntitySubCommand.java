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

public class RemovePlayerEntitySubCommand extends SubCommand {

    public RemovePlayerEntitySubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "移除变身";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"rmce"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 3){
            String entityName = args[2];
            String playerName = args[3];
            Player player = Server.getInstance().getPlayer(playerName);
            PlayerFile file = PlayerFile.getPlayerFile(playerName);
            if(player != null){
                file = PlayerFile.getPlayerFile(player.getName());
            }
            if(!LuckDraw.getInstance().playerFiles.containsKey(playerName)){
                sender.sendMessage("§c玩家 "+playerName+"不存在...");
                return true;
            }
            switch (args[1]){
                case "s":
                    if (!file.getEntities().contains(new PlayerSkin(entityName, -1))) {
                        sender.sendMessage("§c玩家 "+playerName+"不存在"+entityName+"皮肤");
                    }else{
                        file.removeEntity(new PlayerSkin(entityName,-1));
                    }
                    break;
                case "e":
                    if (!file.getEntities().contains(new PlayerEntity(entityName, -1))) {
                        sender.sendMessage("§c玩家 "+playerName+"不存在"+entityName+"生物");
                    }else{
                        file.removeEntity(new PlayerEntity(entityName,-1));
                    }
                    break;
                default:break;
            }
        }
        return false;

    }

    @Override
    public String getHelp() {
        return "§7拓展指令 §a/luckadd rmce <s/e> <生物名/皮肤名> <玩家> 移除玩家一个可变身皮肤/生物";
    }
}
