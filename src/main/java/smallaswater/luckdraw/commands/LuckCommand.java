package smallaswater.luckdraw.commands;

import cn.nukkit.Player;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.PluginCommand;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.Key;
import smallaswater.luckdraw.commands.admin.ReloadSubCommand;
import smallaswater.luckdraw.commands.block.BreakChestSubCommand;
import smallaswater.luckdraw.commands.block.CreateChestSubCommand;
import smallaswater.luckdraw.commands.block.EndChestSubCommand;
import smallaswater.luckdraw.commands.block.PlaceChestSubCommand;
import smallaswater.luckdraw.commands.items.AddItemSubCommand;
import smallaswater.luckdraw.commands.items.RemoveItemSubCommand;
import smallaswater.luckdraw.commands.items.SaveItemSubCommand;
import smallaswater.luckdraw.commands.key.GiveKeySubCommand;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 若水
 */
public class LuckCommand extends PluginCommand<LuckDraw> {

    final List<SubCommand> commands = new ArrayList<>();
    final ConcurrentHashMap<String, Integer> SubCommand = new ConcurrentHashMap<>();
    LuckCommand(String name,LuckDraw owner){
        super(name, owner);

    }

    public LuckCommand(LuckDraw owner) {
        this("luck",owner);
        this.setAliases(new String[]{"抽奖箱","lch"});
        this.setDescription("抽奖箱主命令");
        this.loadSubCommand(new EndChestSubCommand(getPlugin()));
        this.loadSubCommand(new PlaceChestSubCommand(getPlugin()));
        this.loadSubCommand(new CreateChestSubCommand(getPlugin()));
        this.loadSubCommand(new GiveKeySubCommand(getPlugin()));
        this.loadSubCommand(new SaveItemSubCommand(getPlugin()));
        this.loadSubCommand(new ReloadSubCommand(getPlugin()));
        this.loadSubCommand(new BreakChestSubCommand(getPlugin()));
        this.loadSubCommand(new AddItemSubCommand(getPlugin()));
        this.loadSubCommand(new RemoveItemSubCommand(getPlugin()));
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
        if(!sender.hasPermission("luckdraw.lch")){
            return false;
        }
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
            sender.sendMessage("§a§l >> §eHelp for LuckDraw§a<<");
            sender.sendMessage(getHelp());
            for(SubCommand subCommand:commands){
                if(subCommand.canUse(sender)){
                    sender.sendMessage(subCommand.getHelp());
                }
            }
            sender.sendMessage("§a§l >> §eHelp for LuckDraw §a<<");
        }
        return true;
    }

    public String getHelp() {
        return "§a/lch §7help";
    }


}
