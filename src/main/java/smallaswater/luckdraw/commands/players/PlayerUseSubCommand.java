package smallaswater.luckdraw.commands.players;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;
import smallaswater.luckdraw.utils.Tools;

public class PlayerUseSubCommand extends SubCommand {

    public PlayerUseSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "使用";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"use"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            Tools.sendChangeSkinGUI((Player) sender);
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§7§a/pla use 唤醒使用GUI";
    }
}
