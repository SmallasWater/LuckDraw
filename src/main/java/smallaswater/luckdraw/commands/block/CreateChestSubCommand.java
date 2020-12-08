package smallaswater.luckdraw.commands.block;

import cn.nukkit.command.CommandSender;
import smallaswater.luckdraw.LuckDraw;
import smallaswater.luckdraw.chests.Chest;
import smallaswater.luckdraw.chests.Key;
import smallaswater.luckdraw.commands.SubCommand;

public class CreateChestSubCommand extends SubCommand {
    public CreateChestSubCommand(LuckDraw plugin) {
        super(plugin);
    }

    @Override
    public boolean canUse(CommandSender sender) {
        return sender.isOp();
    }

    @Override
    public String getName() {
        return "创建";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"create","c"};
    }

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length > 2){
            String chestName = args[1];
            String key = args[2];
            if(Chest.getInstance(chestName) == null){
                Key key1 = Key.getInstance(key);
                if(key1 != null){
                    if(Chest.create(chestName,key1)){
                        sender.sendMessage("§a 抽奖箱"+chestName+"创建成功");
                    }else{
                        sender.sendMessage("§c 抽奖箱"+chestName+"创建失败");
                    }
                }else{
                    sender.sendMessage("§c钥匙"+key+"不存在");
                }

            }else{
                sender.sendMessage("§c抽奖箱 "+chestName+"已经存在啦");
            }
        }
        return false;
    }

    @Override
    public String getHelp() {
        return  "§a/lch c <抽奖箱名称> <key>§7创建抽奖箱";
    }
}
