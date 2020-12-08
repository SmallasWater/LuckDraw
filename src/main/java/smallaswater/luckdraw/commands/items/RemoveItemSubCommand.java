package smallaswater.luckdraw.commands.items;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;

public class RemoveItemSubCommand extends SubCommand {
    public RemoveItemSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "移除";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"ritem"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(!LuckDraw.getInstance().rmode.contains(sender)){
                sender.sendMessage("§a>> §e您已经进入移除物品模式 点击箱子即可移除\n再次执行/luck 移除可退出");
                LuckDraw.getInstance().rmode.add((Player) sender);
            }else{
                sender.sendMessage("§a>> §e您已退出移除物品模式");
                LuckDraw.getInstance().rmode.remove(sender);
            }
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/lch ritem §7进入移除物品模式";
    }
}
