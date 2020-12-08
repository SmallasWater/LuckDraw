package smallaswater.luckdraw.commands.addons;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;
import smallaswater.luckdraw.utils.Tools;




public class CreateEntitySubCommand extends SubCommand {


    public CreateEntitySubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "变身";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ce"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String entityName = args[1];
            String playerName = args[2];
            Player player = Server.getInstance().getPlayer(playerName);
            if(player != null){
                if(LuckDraw.getInstance().chestSkins.containsKey(entityName)){
                    Tools.setPlayerSkin(player,LuckDraw.getInstance().chestSkins.get(entityName));
                    sender.sendMessage("§a>>§e成功将§b"+player.getName()+"§e皮肤设置为§d"+entityName);
                }else{
                    if(Tools.setEntityPlayer(player,entityName)){
                        sender.sendMessage("§a>>§e成功将§b"+player.getName()+"§e变成§3"+entityName);
                    }
                }

            }else {
                sender.sendMessage("§c玩家"+playerName+"不在线");
            }

        }

        return false;
    }

    @Override
    public String getHelp() {
        return "§7拓展指令 §a/luckadd ce <生物名/皮肤名> <玩家> 将玩家变成一个实体/设置玩家皮肤";
    }
}

