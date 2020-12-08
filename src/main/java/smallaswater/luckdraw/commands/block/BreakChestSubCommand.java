package smallaswater.luckdraw.commands.block;

import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.Key;
import smallaswater.luckdraw.commands.SubCommand;

public class BreakChestSubCommand extends SubCommand {

    public BreakChestSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "拆除";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"remove","r"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 1){
            String chestName = args[1];
            Chest chest = Chest.getInstance(chestName);
            if(chest != null){
               if(chest.kill()){
                   sender.sendMessage("§d抽奖箱 "+chestName+"拆除成功");
               }else{
                   sender.sendMessage("§c抽奖箱 "+chestName+"拆除失败");
               }
            }else{
                sender.sendMessage("§c抽奖箱 "+chestName+"不存在");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return  "§a/lch r <抽奖箱名称> §7拆除抽奖箱";
    }
}
