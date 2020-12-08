package smallaswater.luckdraw.commands;

import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.players.PlayerSettingSubCommand;
import smallaswater.luckdraw.commands.players.PlayerUseSubCommand;


/**
 * @author 若水
 */
public class PlayerLuckCommand extends LuckCommand {
    public PlayerLuckCommand(LuckDraw owner) {
        super("pla", owner);
        this.setDescription("玩家伪装");
        this.setPermission("luckdraw.pla");
        this.loadSubCommand(new PlayerSettingSubCommand(getPlugin()));
        this.loadSubCommand(new PlayerUseSubCommand(getPlugin()));

    }

    private void loadSubCommand(SubCommand cmd) {
        commands.add(cmd);
        int commandId = (commands.size()) - 1;
        SubCommand.put(cmd.getName().toLowerCase(), commandId);
        for (String alias : cmd.getAliases()) {
            SubCommand.put(alias.toLowerCase(), commandId);
        }
    }

    @Override
    public boolean execute(CommandSender sender, String s, String[] args) {
        if(args.length == 0){
            sender.sendMessage(getHelp());
        }else{
            String subCommand = args[0].toLowerCase();
            if (SubCommand.containsKey(subCommand)) {
                SubCommand command = commands.get(SubCommand.get(subCommand));
                boolean canUse = command.canUse(sender);
                if (canUse) {
                    return command.execute(sender, args);
                } else {
                    return false;
                }
            } else {
                return this.sendHelp(sender, args);
            }
        }
        return true;
    }

    private boolean sendHelp(CommandSender sender, String[] args) {
        if ("help".equals(args[0])) {
            sender.sendMessage("§a§l >> §eHelp for PlayerLuck§a<<");
            sender.sendMessage(getHelp());
            for(SubCommand subCommand:commands){
                if(subCommand.canUse(sender)){
                    sender.sendMessage(subCommand.getHelp());
                }
            }
            sender.sendMessage("§a§l >> §eHelp for PlayerLuck §a<<");
        }
        return true;
    }


    @Override
    public String getHelp() {
        return "§a/pla §7help";
    }
}
