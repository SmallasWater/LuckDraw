package smallaswater.luckdraw.commands;

import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.commands.addons.AddPlayerEntitySubCommand;
import smallaswater.luckdraw.commands.addons.CreateEntitySubCommand;
import smallaswater.luckdraw.commands.addons.RemovePlayerEntitySubCommand;


public class AddonsLuckCommand extends LuckCommand {

    public AddonsLuckCommand(LuckDraw owner) {
        super("luckadd",owner);
        this.setAliases(new String[]{"lca"});
        this.setDescription("抽奖箱拓展");
        this.loadSubCommand(new CreateEntitySubCommand(getPlugin()));
        this.loadSubCommand(new AddPlayerEntitySubCommand(getPlugin()));
        this.loadSubCommand(new RemovePlayerEntitySubCommand(getPlugin()));

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
            sender.sendMessage("§a§l >> §eHelp for LuckDrawAdd§a<<");
            sender.sendMessage(getHelp());
            for(SubCommand subCommand:commands){
                if(subCommand.canUse(sender)){
                    sender.sendMessage(subCommand.getHelp());
                }
            }
            sender.sendMessage("§a§l >> §eHelp for LuckDrawAdd §a<<");
        }
        return true;
    }


    @Override
    public String getHelp() {
        return "§a/lca §7help";
    }
}
