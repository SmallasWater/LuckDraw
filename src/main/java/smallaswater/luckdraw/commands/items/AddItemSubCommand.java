package smallaswater.luckdraw.commands.items;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;

public class AddItemSubCommand extends SubCommand {

    public AddItemSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp() && sender.isPlayer();
    }

    @Override
    public String getName() {
        return "添加";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"addItem","add"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(!LuckDraw.getInstance().mode.contains(sender)){
                sender.sendMessage("§a>> §e您已经进入添加物品模式 手持物品点击箱子即可添加\n再次执行/luck add可退出");
                LuckDraw.getInstance().mode.add((Player) sender);
            }else{
                sender.sendMessage("§a>> §e您已退出添加物品模式");
                LuckDraw.getInstance().mode.remove(sender);
            }
        }
        return true;
    }

    @Override
    public String getHelp() {
        return "§a/lch add §7进入添加物品模式";
    }
}
