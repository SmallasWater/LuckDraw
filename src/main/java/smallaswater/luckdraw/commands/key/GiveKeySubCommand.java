package smallaswater.luckdraw.commands.key;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.item.Item;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Key;
import smallaswater.luckdraw.commands.SubCommand;

/**
 * @author SmallasWater
 */
public class GiveKeySubCommand extends SubCommand {

    public GiveKeySubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "giveKey";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"gk"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 3){
            String keyName = args[2];
            String playerName = args[1];
            int count = Integer.parseInt(args[3]);
            Player player = Server.getInstance().getPlayer(playerName);
            if(player != null){
                Key key = Key.getInstance(keyName);
                if(key != null){
                    key = key.clone();
                    Item item = key.getKeyItem();
                    item.setCount(count);
                    player.getInventory().addItem(item);
                    sender.sendMessage("§a给予"+keyName+"成功");
                }else{
                    sender.sendMessage("§c钥匙"+keyName+"不存在");
                }
            }else{
                sender.sendMessage("§c玩家"+playerName+"不在线");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        StringBuilder builder = new StringBuilder();
        for(Key key:LuckDraw.getInstance().keys.values()){
            builder.append("key: ").append(key.getKey()).append(" >>").append(key.getShowName()).append("\n");
        }
        return "§a/lch gk <玩家> <key> <数量>§7给予玩家开箱钥匙\n当前服务器拥有: \n"+builder.toString();
    }
}
