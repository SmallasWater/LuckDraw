package smallaswater.luckdraw.commands.players;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;
import smallaswater.luckdraw.utils.Tools;

/**
 * @author SmallasWater
 */
public class PlayerSettingSubCommand extends SubCommand {
    public PlayerSettingSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isPlayer();
    }

    @Override
    public String getName() {
        return "重命名";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"rn"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player){
            if(!LuckDraw.getInstance().playerClickType.contains(((Player) sender).getPlayer())){
                LuckDraw.getInstance().playerClickType.add((Player) sender);
            }
            Tools.sendChangeSkinGUI((Player) sender);
            return true;
        }
        return false;
    }

    @Override
    public String getHelp() {
        return "§7§a/pla rn 唤醒重命名GUI";
    }
}
