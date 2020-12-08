package smallaswater.luckdraw.commands.block;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.commands.SubCommand;

public class PlaceChestSubCommand extends SubCommand {

    public PlaceChestSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "place";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"放置"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(args.length > 1){
                String chestName = args[1];
                Chest chest = Chest.getInstance(chestName);
                if(chest != null){
                    LuckDraw.getInstance().placeChest.put((Player) sender,chest);
                    sender.sendMessage("§a请放置箱子 记录抽奖箱坐标");
                }else{
                    sender.sendMessage("§c不存在"+chestName+"抽奖箱");
                }
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§a/lch place <抽奖箱名称>§7放置抽奖箱";
    }
}
