package smallaswater.luckdraw.commands.admin;

import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.SubCommand;

public class ReloadSubCommand extends SubCommand {
    public ReloadSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "重载";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"reload"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        LuckDraw.getInstance().reloadConfig();
        LuckDraw.getInstance().initKeys();
        LuckDraw.getInstance().initSkin();
        LuckDraw.getInstance().initChest();

        sender.sendMessage("重新读取完成");
        return true;
    }

    @Override
    public String getHelp() {
        return  "§a/lch reload §7重新读取配置";
    }
}
