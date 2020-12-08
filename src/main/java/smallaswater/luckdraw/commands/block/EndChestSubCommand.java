package smallaswater.luckdraw.commands.block;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.commands.SubCommand;

public class EndChestSubCommand extends SubCommand {

    public EndChestSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "end";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"结束"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(LuckDraw.getInstance().placeChest.containsKey(sender)){
                LuckDraw.getInstance().placeChest.remove(sender);
                sender.sendMessage("§b 放置结束");
            }else{
                sender.sendMessage("§c 你未开始放置");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/lch end §7结束放置";
    }
}
